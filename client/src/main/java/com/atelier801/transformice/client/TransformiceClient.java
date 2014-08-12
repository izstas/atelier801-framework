package com.atelier801.transformice.client;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
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
import com.atelier801.transformice.client.proto.data.*;
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

        logger.info("Attempting to log in as {}", username);
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

    @Override
    public void sendPrivateMessage(String recipient, String message) {
        checkNotNull(recipient, "recipient");
        checkNotNull(message, "message");
        checkState(state == State.LOGGED_IN, "Illegal state: %s", state);

        channel.writeAndFlush(new OPPrivateMessage(recipient, message.replace("<", "&lt;")));
    }


    /* TRIBE */
    final TribeImpl tribe = new TribeImpl();

    @Override
    public Tribe tribe() {
        return tribe;
    }

    final class TribeImpl implements Tribe {
        private int id = -1;
        private String name;
        private String greeting;
        private int houseMap;
        Pool<Integer, TribeRankImpl, DTribeRank> ranks =
                new Pool<>(id -> new TribeRankImpl(TransformiceClient.this, id), DTribeRank::getId);
        Pool<Integer, TribeMemberImpl, DTribeMember> members =
                new Pool<>(id -> new TribeMemberImpl(TransformiceClient.this, id), DTribeMember::getId);

        private int channelId = -1;

        @Override
        public String getName() {
            checkState(state == State.LOGGED_IN, "Illegal state: %s", state);
            checkState(id != -1, "Not in tribe");

            return name;
        }

        @Override
        public String getGreeting() {
            checkState(state == State.LOGGED_IN, "Illegal state: %s", state);
            checkState(id != -1, "Not in tribe");

            return greeting;
        }

        @Override
        public int getHouseMap() {
            checkState(state == State.LOGGED_IN, "Illegal state: %s", state);
            checkState(id != -1, "Not in tribe");

            return houseMap;
        }

        @Override
        @SuppressWarnings("unchecked")
        public Collection<TribeRank> getRanks() {
            checkState(state == State.LOGGED_IN, "Illegal state: %s", state);
            checkState(id != -1, "Not in tribe");

            return (Collection) ranks.valid();
        }

        @Override
        @SuppressWarnings("unchecked")
        public Collection<TribeMember> getMembers() {
            checkState(state == State.LOGGED_IN, "Illegal state: %s", state);
            checkState(id != -1, "Not in tribe");

            return (Collection) members.valid();
        }

        @Override
        public void sendMessage(String message) {
            checkNotNull(message, "message");
            checkState(state == State.LOGGED_IN, "Illegal state: %s", state);
            checkState(channelId != -1, "Not in tribe channel");

            channel.writeAndFlush(new OPChannelMessage(channelId, message.replace("<", "&lt;")));
        }

        @Override
        public Observable<RoomChangeEvent> enterHouse() {
            checkState(state == State.LOGGED_IN, "Illegal state: %s", state);
            checkState(id != -1, "Not in tribe");

            channel.writeAndFlush(new OPTribeHouse());
            return observable.ofType(RoomChangeEvent.class).filter(e -> e.getRoom().charAt(1) == '\3').take(1);
        }

        Observable<TribeMemberRankChangeEvent> changeMemberRank(TribeMemberImpl member, TribeRankImpl rank) {
            checkNotNull(member, "member");
            checkArgument(members.valid().contains(member), "member is not valid");
            checkNotNull(rank, "rank");
            checkArgument(ranks.valid().contains(rank), "rank is not valid");
            checkState(state == State.LOGGED_IN, "Illegal state: %s", state);
            checkState(id != -1, "Not in tribe");

            channel.writeAndFlush(new OPTribeMemberRank(member.getId(), rank.getId()));
            return observable.ofType(TribeMemberRankChangeEvent.class).filter(e -> e.getMember() == member).take(1);
        }
    }


    /* ROOM */
    private final RoomImpl room = new RoomImpl();

    @Override
    public Room room() {
        return room;
    }

    private final class RoomImpl implements Room {
        private String name;

        @Override
        public String getName() {
            checkState(state == State.LOGGED_IN, "Illegal state: %s", state);

            return name;
        }

        @Override
        public void sendMessage(String message) {
            checkNotNull(message, "message");
            checkState(satelliteState == SatelliteState.CONNECTED, "Illegal satellite state: %s", satelliteState);

            satelliteChannel.writeAndFlush(new OPRoomMessage(message.replace("<", "&lt;")));
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

            logger.info("Successfully logged in as {}", p.getMouseName());
            triggerNext(new StateChangeEvent(state = State.LOGGED_IN));
            triggerNext(new LoginSuccessEvent(p.getMouseName()));
        });

        putPacketHandler(IPLoginFailure.class, p -> {
            LoginFailureEvent.Reason r;
            switch (p.getReason()) {
                case 1:
                    r = LoginFailureEvent.Reason.INVALID;
                    break;
                case 2:
                    r = LoginFailureEvent.Reason.ALREADY_CONNECTED;
                    break;
                default:
                    r = LoginFailureEvent.Reason.UNKNOWN;
            }

            logger.info("Failed to log in: {} (#{})", r, p.getReason());
            triggerNext(new StateChangeEvent(state = State.CONNECTED));
            triggerNext(new LoginFailureEvent(r));
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
                triggerNext(new TribeMessageEvent(tribe,
                        tribe.members.valid().stream()
                                .filter(m -> m.getName().equalsIgnoreCase(p.getSender())).findAny().orElse(null),
                        TransformiceUtil.normalizeMouseName(p.getSender()),
                        Community.valueOf(p.getSenderCommunity()), p.getMessage().replace("&lt;", "<")));
            }
        });

        putPacketHandler(IPPrivateMessage.class, p -> {
            triggerNext(new PrivateMessageEvent(msg -> sendPrivateMessage(p.getSender(), msg),
                    TransformiceUtil.normalizeMouseName(p.getSender()), Community.valueOf(p.getSenderCommunity()),
                    p.getMessage().replace("&lt;", "<")));
        });

        putPacketHandler(IPTribe.class, p -> {
            tribe.id = p.getId();
            tribe.name = p.getName();
            tribe.greeting = p.getGreeting();
            tribe.houseMap = p.getHouseMap();
            tribe.ranks.replaceAll(p.getRanks());

            channel.writeAndFlush(new OPTribeMembersRequest());
        });

        putPacketHandler(IPTribeMembers.class, p -> {
            tribe.members.replaceAll(p.getMembers());

            triggerNext(new TribeChangeEvent());
        });

        Consumer<DTribeMember> tribeMemberConnectHandler = d -> {
            TribeMemberImpl member = tribe.members.replace(d);

            triggerNext(new TribeMemberConnectEvent(member, d.getLocations().get(0).toLocation().getGame()));
        };

        putPacketHandler(IPTribeMemberConnect.class, p -> tribeMemberConnectHandler.accept(p.getMember()));
        putPacketHandler(IPTribeMemberConnectBatch.class, p -> p.getMembers().forEach(tribeMemberConnectHandler));

        BiConsumer<Integer, Integer> tribeMemberDisconnectHandler = (id, gId) -> {
            TribeMemberImpl member = tribe.members.get(id);
            if (member != null) {
                Location.Game g = Location.Game.valueOf(gId);
                member.removeLocation(g);

                triggerNext(new TribeMemberDisconnectEvent(member, g));
            }
        };

        putPacketHandler(IPTribeMemberDisconnect.class,
                p -> tribeMemberDisconnectHandler.accept(p.getId(), p.getGame()));
        putPacketHandler(IPTribeMemberDisconnectBatch.class,
                p -> p.getIds().forEach(id -> tribeMemberDisconnectHandler.accept(id, p.getGame())));

        putPacketHandler(IPTribeMemberJoin.class, p -> {
            TribeMemberImpl member = tribe.members.replace(p.getMember());

            triggerNext(new TribeMemberJoinEvent(member));
        });

        putPacketHandler(IPTribeMemberLeave.class, p -> {
            TribeMemberImpl member = tribe.members.get(p.getId());
            tribe.members.invalidate(p.getId());

            triggerNext(new TribeMemberLeaveEvent(member));
        });

        putPacketHandler(IPTribeMemberKick.class, p -> {
            TribeMemberImpl member = tribe.members.get(p.getId());
            tribe.members.invalidate(p.getId());

            triggerNext(new TribeMemberKickEvent(member,
                    tribe.members.valid().stream()
                            .filter(m -> m.getName().equalsIgnoreCase(p.getKicker())).findAny().orElse(null)));
        });

        putPacketHandler(IPTribeMemberRank.class, p -> {
            TribeMemberImpl member = tribe.members.get(p.getId());
            if (member != null) {
                TribeRankImpl r = tribe.ranks.get(p.getRankId());
                member.setRank(r);

                triggerNext(new TribeMemberRankChangeEvent(member, r));
            }
        });

        putPacketHandler(IPTribeMemberLocation.class, p -> {
            TribeMemberImpl member = tribe.members.get(p.getId());
            if (member != null) {
                Location l = p.getLocation().toLocation();
                member.replaceLocation(l);

                triggerNext(new TribeMemberLocationChangeEvent(member, l));
            }
        });

        putPacketHandler(IPRoom.class, p -> {
            triggerNext(new RoomChangeEvent(room.name = p.getRoom()));
        });

        putPacketHandler(IPRoomMessage.class, p -> {
            triggerNext(new RoomMessageEvent(room, p.getSender(),
                    Community.valueOf(p.getSenderCommunity()), p.getMessage().replace("&lt;", "<")));
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
            ctx.writeAndFlush(new OPClient());

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
