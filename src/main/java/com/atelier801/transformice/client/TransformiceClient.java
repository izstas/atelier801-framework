package com.atelier801.transformice.client;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.Subscriber;

import com.atelier801.transformice.*;
import com.atelier801.transformice.client.proto.packet.in.*;
import com.atelier801.transformice.client.proto.packet.out.*;
import com.atelier801.transformice.event.*;

import static com.google.common.base.Preconditions.*;

public final class TransformiceClient implements Transformice {
    private static final Logger logger = LoggerFactory.getLogger(TransformiceClient.class);


    /* BASE STUFF */
    private ProtoData protoData;
    private EventLoopGroup eventLoopGroup;

    private Channel channel;
    private State state = State.CONNECTING;

    private Channel satelliteChannel;
    private SatelliteState satelliteState = SatelliteState.CLOSED;

    public TransformiceClient(InetSocketAddress address, ProtoData data) {
        checkNotNull(address, "address");
        checkNotNull(data, "data");

        protoData = data;
        eventLoopGroup = new NioEventLoopGroup();
        channel = new Bootstrap()
                .group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new TransformiceChannelInitializer(protoData,
                        new CommonChannelHandler(), new MainChannelHandler()))
                .connect(address)
                .channel();

        logger.info("Connecting to the main server at {}", address);
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public SatelliteState getSatelliteState() {
        return satelliteState;
    }

    @Override
    public void close() {
        checkState(state != State.CLOSING && state != State.CLOSED, "Illegal state: %s", state);

        satelliteClose(false);
        channel.close();

        logger.info("Closing connection to the main server");
        triggerNext(new StateChangeEvent(state = State.CLOSING));
    }

    private void satelliteClose(boolean abandon) {
        if (satelliteChannel != null && satelliteChannel.isOpen()) {
            if (abandon) {
                satelliteChannel.pipeline().remove(SatelliteChannelHandler.class);
                satelliteChannel.pipeline().remove(CommonChannelHandler.class);
                satelliteChannel.close();

                logger.info("Abandoning and closing connection to the satellite server at {}",
                        satelliteChannel.remoteAddress());
            }
            else {
                satelliteChannel.close();

                logger.info("Closing connection to the satellite server at {}",
                        satelliteChannel.remoteAddress());
                triggerNext(new SatelliteStateChangeEvent(satelliteState = SatelliteState.CLOSING));
            }
        }
    }


    /* PRE-LOGIN */
    @Override
    public void changeCommunity(Community community) {
        checkNotNull(community, "community");
        checkState(state == State.CONNECTED, "Illegal state: %s", state);

        channel.writeAndFlush(new OPCommunity(community.getId()));
    }

    @Override
    public Observable<LoginEvent> logIn(String username, Optional<String> password, Optional<String> room) {
        checkNotNull(username, "username");
        checkNotNull(password, "password");
        checkNotNull(room, "room");
        checkState(state == State.CONNECTED, "Illegal state: %s", state);

        channel.writeAndFlush(new OPLogin(username, password, room));
        triggerNext(new StateChangeEvent(state = State.LOGGING_IN));
        return observable.ofType(LoginEvent.class);
    }


    /* POST-LOGIN */
    private int clientMouseId;
    private String clientMouseName;

    @Override
    public String getClientMouseName() {
        checkState(state == State.LOGGED_IN, "Illegal state: %s", state);

        return clientMouseName;
    }


    /* TRIBE */
    private final TribeClient tribe = new TribeClient();

    @Override
    public Tribe tribe() {
        return tribe;
    }

    private final class TribeClient implements Tribe {
        private int channelId = -1;

        @Override
        public void sendMessage(String message) {
            checkNotNull(message, "message");
            checkState(state == State.LOGGED_IN, "Illegal state: %s", state);
            checkState(channelId != -1, "Tribe channel is not defined");

            channel.writeAndFlush(new OPChannelMessage(channelId, message));
        }
    }


    /* EVENTS */
    private final Collection<Subscriber<? super Event>> subscribers = new CopyOnWriteArrayList<>();
    private final Observable<Event> observable = Observable.create((Observable.OnSubscribe<Event>) subscribers::add);

    @Override
    public Observable<Event> events() {
        return observable;
    }

    private void triggerNext(Event evt) {
        subscribers.removeIf(Subscriber::isUnsubscribed);
        subscribers.forEach(s -> s.onNext(evt));
    }

    private void triggerCompleted() {
        subscribers.removeIf(Subscriber::isUnsubscribed);
        subscribers.forEach(s -> s.onCompleted());
    }


    /* PACKET HANDLERS */
    private final Map<Class<?>, Consumer<?>> packetHandlers = new HashMap<>();

    private <T> void putPacketHandler(Class<T> clazz, Consumer<T> handler) {
        packetHandlers.put(clazz, handler);
    }

    @SuppressWarnings("unchecked")
    private <T> boolean callPacketHandler(Class<T> clazz, Object packet) {
        if (packetHandlers.containsKey(clazz)) {
            ((Consumer<T>) packetHandlers.get(clazz)).accept((T) packet);
            return true;
        }

        return false;
    }

    {
        putPacketHandler(IPLoginSuccess.class, p -> {
            clientMouseId = p.getMouseId();
            clientMouseName = p.getMouseName();

            triggerNext(new StateChangeEvent(state = State.LOGGED_IN));
            triggerNext(new LoginSuccessEvent(p.getMouseName()));
        });

        putPacketHandler(IPLoginFailure.class, p -> {
            triggerNext(new StateChangeEvent(state = State.CONNECTED));
            switch (p.getReason()) {
                case 1:
                    triggerNext(new LoginFailureEvent(LoginFailureEvent.Reason.INVALID));
                    break;
                case 2:
                    triggerNext(new LoginFailureEvent(LoginFailureEvent.Reason.ALREADY_CONNECTED));
                    break;
                default:
                    triggerNext(new LoginFailureEvent(LoginFailureEvent.Reason.UNKNOWN));
            }
        });

        putPacketHandler(IPSatellite.class, p -> {
            satelliteClose(true);

            InetSocketAddress satelliteAddress = new InetSocketAddress(p.getHost(),
                    ((InetSocketAddress) channel.remoteAddress()).getPort());
            satelliteChannel = new Bootstrap()
                    .group(eventLoopGroup)
                    .channel(channel.getClass())
                    .handler(new TransformiceChannelInitializer(protoData,
                            new CommonChannelHandler(), new SatelliteChannelHandler(p.getKey())))
                    .connect(satelliteAddress)
                    .channel();

            logger.info("Connecting to the satellite server at {}", satelliteAddress);
            triggerNext(new SatelliteStateChangeEvent(satelliteState = SatelliteState.CONNECTING));
        });

        putPacketHandler(IPChannelEnter.class, p -> {
            if (p.getName().charAt(0) == '~') {
                tribe.channelId = p.getId();
            }
        });

        putPacketHandler(IPChannelQuit.class, p -> {
            if (p.getId() == tribe.channelId) {
                tribe.channelId = -1;
            }
        });

        putPacketHandler(IPChannelMessage.class, p -> {
            if (p.getChannelId() == tribe.channelId) {
                triggerNext(new TribeMessageEvent(tribe, p.getSender(),
                        Community.valueOf(p.getSenderCommunity()), p.getMessage()));
            }
        });
    }


    /* CHANNEL HANDLERS */
    private final class CommonChannelHandler extends SimpleChannelInboundHandler<Object> {
        private Future<?> pingFuture;

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            pingFuture = ctx.executor().scheduleWithFixedDelay(() -> ctx.writeAndFlush(new OPPing()),
                    11, 11, TimeUnit.SECONDS);

            ctx.fireChannelActive();
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            pingFuture.cancel(false);

            ctx.fireChannelInactive();
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
            if (!callPacketHandler(msg.getClass(), msg)) {
                ctx.fireChannelRead(msg);
            }
        }
    }

    private final class MainChannelHandler extends SimpleChannelInboundHandler<IPConnect> {
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            ctx.writeAndFlush(new OPConnect(protoData.getVersion(), protoData.getKey()));

            ctx.fireChannelActive();
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            if (state == State.CLOSING) {
                logger.info("Connection to the main server has been closed");
            }
            else {
                logger.info("Connection to the main server has been closed UNEXPECTEDLY");
            }

            triggerNext(new StateChangeEvent(state = State.CLOSED));
            satelliteClose(false);
            eventLoopGroup.shutdownGracefully().addListener(f -> triggerCompleted());

            ctx.fireChannelInactive();
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, IPConnect msg) throws Exception {
            logger.info("Connection to the main server has been established");
            triggerNext(new StateChangeEvent(state = State.CONNECTED));
        }
    }

    private final class SatelliteChannelHandler extends SimpleChannelInboundHandler<IPSatelliteConnect> {
        private final int key;

        SatelliteChannelHandler(int key) {
            this.key = key;
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            ctx.writeAndFlush(new OPSatelliteConnect(key));

            ctx.fireChannelActive();
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            if (satelliteState == SatelliteState.CLOSING) {
                logger.info("Connection to the satellite server at {} has been closed",
                        ctx.channel().remoteAddress());
            }
            else {
                logger.info("Connection to the satellite server at {} has been closed UNEXPECTEDLY",
                        ctx.channel().remoteAddress());
            }

            triggerNext(new SatelliteStateChangeEvent(satelliteState = SatelliteState.CLOSED));

            ctx.fireChannelInactive();
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, IPSatelliteConnect msg) throws Exception {
            logger.info("Connection to the satellite server at {} has been established",
                    ctx.channel().remoteAddress());
            triggerNext(new SatelliteStateChangeEvent(satelliteState = SatelliteState.CONNECTED));
        }
    }
}
