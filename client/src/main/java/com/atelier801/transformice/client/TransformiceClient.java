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
import io.netty.channel.ChannelOption;
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
        eventLoopGroup = new NioEventLoopGroup(1);
        channel = new Bootstrap()
                .group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .handler(new TransformiceChannelInitializer(protoData,
                        new CommonChannelHandler(), new MainChannelHandler()))
                .connect(address)
                .addListener(f -> {
                    if (!f.isSuccess()) {
                        logger.info("Unable to connect to the main server", f.cause());
                        emitNext(new StateChangeEvent(state = State.CLOSED));
                        emitCompleted();
                    }
                })
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
        emitNext(new StateChangeEvent(state = State.CLOSING));
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
                emitNext(new SatelliteStateChangeEvent(satelliteState = SatelliteState.CLOSING));
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
        emitNext(new StateChangeEvent(state = State.LOGGING_IN));
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
        checkArgument(!recipient.isEmpty(), "recipient is empty");
        checkNotNull(message, "message");
        checkArgument(!message.isEmpty(), "message is empty");
        checkState(state == State.LOGGED_IN, "Illegal state: %s", state);

        channel.writeAndFlush(new OPPrivateMessage(recipient, message.replace("&", "&amp;").replace("<", "&lt;")));
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
        final Pool<Integer, TribeRankImpl, DTribeRank> ranks =
                new Pool<>(id -> new TribeRankImpl(TransformiceClient.this, id), DTribeRank::getId);
        final Pool<Integer, TribeMemberImpl, DTribeMember> members =
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
            checkArgument(!message.isEmpty(), "message is empty");
            checkState(state == State.LOGGED_IN, "Illegal state: %s", state);
            checkState(channelId != -1, "Not in tribe channel");

            channel.writeAndFlush(new OPChannelMessage(channelId, message.replace("&", "&amp;").replace("<", "&lt;")));
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

            channel.writeAndFlush(new OPTribeMemberRank(member.id, rank.id));
            return observable.ofType(TribeMemberRankChangeEvent.class).filter(e -> e.getMember() == member).take(1);
        }

        Observable<TribeMemberKickEvent> kickMember(TribeMemberImpl member) {
            checkNotNull(member, "member");
            checkArgument(members.valid().contains(member), "member is not valid");
            checkState(state == State.LOGGED_IN, "Illegal state: %s", state);
            checkState(id != -1, "Not in tribe");

            channel.writeAndFlush(new OPTribeMemberKick(member.id));
            return observable.ofType(TribeMemberKickEvent.class).filter(e -> e.getMember() == member).take(1);
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
        final Pool<Integer, RoomMouseImpl, DRoomMouse> mice =
                new Pool<>(id -> new RoomMouseImpl(TransformiceClient.this, id), DRoomMouse::getId);

        @Override
        public String getName() {
            checkState(state == State.LOGGED_IN, "Illegal state: %s", state);

            return name;
        }

        @Override
        @SuppressWarnings("unchecked")
        public Collection<RoomMouse> getMice() {
            checkState(state == State.LOGGED_IN, "Illegal state: %s", state);

            return (Collection) mice.valid();
        }

        @Override
        public void sendMessage(String message) {
            checkNotNull(message, "message");
            checkArgument(!message.isEmpty(), "message is empty");
            checkState(satelliteState == SatelliteState.CONNECTED, "Illegal satellite state: %s", satelliteState);

            satelliteChannel.writeAndFlush(new OPRoomMessage(message.replace("&", "&amp;").replace("<", "&lt;")));
        }
    }


    /* EVENTS */
    private final Collection<Subscriber<? super Event>> subscribers = new CopyOnWriteArrayList<>();
    private final Observable<Event> observable = Observable.<Event>create(subscribers::add);

    @Override
    public Observable<Event> events() {
        return observable;
    }

    private void emitNext(Event evt) {
        subscribers.removeIf(Subscriber::isUnsubscribed);
        subscribers.forEach(s -> {
            try {
                s.onNext(evt);
            }
            catch (Exception e) {
                logger.warn("An unhandled exception has occurred while calling onNext on {}", s, e);
            }
        });
    }

    private void emitCompleted() {
        subscribers.removeIf(Subscriber::isUnsubscribed);
        subscribers.forEach(s -> {
            try {
                s.onCompleted();
            }
            catch (Exception e) {
                logger.warn("An unhandled exception has occurred while calling onCompleted on {}", s, e);
            }
        });
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
            emitNext(new StateChangeEvent(state = State.LOGGED_IN));
            emitNext(new LoginSuccessEvent(p.getMouseName()));
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
            emitNext(new StateChangeEvent(state = State.CONNECTED));
            emitNext(new LoginFailureEvent(r));
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
            emitNext(new SatelliteStateChangeEvent(satelliteState = SatelliteState.CONNECTING));
        });

        putPacketHandler(IPSatellitePing.class, p -> {
            satelliteChannel.writeAndFlush(new OPSatellitePing(p.getCounter()));
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
                emitNext(new TribeMessageEvent(tribe,
                        tribe.members.valid().stream()
                                .filter(m -> m.getName().equalsIgnoreCase(p.getSender())).findAny().orElse(null),
                        TransformiceUtil.normalizeMouseName(p.getSender()),
                        Community.valueOf(p.getSenderCommunity()), p.getMessage().replace("&lt;", "<").replace("&amp;", "&")));
            }
        });

        putPacketHandler(IPPrivateMessage.class, p -> {
            if (!p.isOutgoing()) {
                emitNext(new PrivateMessageEvent(msg -> sendPrivateMessage(p.getSender(), msg),
                        TransformiceUtil.normalizeMouseName(p.getSender()), Community.valueOf(p.getSenderCommunity()),
                        p.getMessage().replace("&lt;", "<").replace("&amp;", "&")));
            }
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

            emitNext(new TribeChangeEvent());
        });

        putPacketHandler(IPTribeGreeting.class, p -> {
            tribe.greeting = p.getGreeting();

            emitNext(new TribeGreetingChangeEvent(
                    tribe.members.valid().stream().filter(m -> m.getName().equalsIgnoreCase(p.getChanger()))
                            .findAny().get()));
        });

        putPacketHandler(IPTribeHouseMap.class, p -> {
            tribe.houseMap = p.getHouseMap();

            emitNext(new TribeHouseMapChangeEvent(
                    tribe.members.valid().stream().filter(m -> m.getName().equalsIgnoreCase(p.getChanger()))
                            .findAny().get()));
        });

        Consumer<DTribeMember> tribeMemberConnectHandler = d -> {
            // Workaround for what looks like a tribulle bug
            TribeMemberImpl member = tribe.members.getValid(d.getId());
            Collection<Location> locations = null;
            if (member != null) {
                locations = member.getLocations();
            }

            member = tribe.members.replace(d);
            if (locations != null) {
                locations.forEach(member::replaceLocation);
            }

            emitNext(new TribeMemberConnectEvent(member, d.getLocations().get(0).toLocation().getGame()));
        };

        putPacketHandler(IPTribeMemberConnect.class, p -> tribeMemberConnectHandler.accept(p.getMember()));
        putPacketHandler(IPTribeMemberConnectBatch.class, p -> p.getMembers().forEach(tribeMemberConnectHandler));

        BiConsumer<Integer, Integer> tribeMemberDisconnectHandler = (id, gameId) -> {
            TribeMemberImpl member = tribe.members.getValid(id);
            if (member != null) {
                Location.Game game = Location.Game.valueOf(gameId);
                if (member.removeLocation(game)) {
                    emitNext(new TribeMemberDisconnectEvent(member, game));
                }
            }
        };

        putPacketHandler(IPTribeMemberDisconnect.class,
                p -> tribeMemberDisconnectHandler.accept(p.getId(), p.getGame()));
        putPacketHandler(IPTribeMemberDisconnectBatch.class,
                p -> p.getIds().forEach(id -> tribeMemberDisconnectHandler.accept(id, p.getGame())));

        putPacketHandler(IPTribeMemberJoin.class, p -> {
            TribeMemberImpl member = tribe.members.replace(p.getMember());

            emitNext(new TribeMemberJoinEvent(member));
        });

        putPacketHandler(IPTribeMemberLeave.class, p -> {
            TribeMemberImpl member = tribe.members.getValid(p.getId());
            if (member != null) {
                tribe.members.invalidate(p.getId());

                emitNext(new TribeMemberLeaveEvent(member));
            }
        });

        putPacketHandler(IPTribeMemberKick.class, p -> {
            TribeMemberImpl member = tribe.members.getValid(p.getId());
            if (member != null) {
                tribe.members.invalidate(p.getId());

                emitNext(new TribeMemberKickEvent(member,
                        tribe.members.valid().stream().filter(m -> m.getName().equalsIgnoreCase(p.getKicker()))
                                .findAny().get()));
            }
        });

        putPacketHandler(IPTribeMemberRank.class, p -> {
            TribeMemberImpl member = tribe.members.getValid(p.getId());
            if (member != null) {
                TribeRankImpl rank = tribe.ranks.get(p.getRankId());
                member.setRank(rank);

                emitNext(new TribeMemberRankChangeEvent(member, rank));
            }
        });

        putPacketHandler(IPTribeMemberLocation.class, p -> {
            TribeMemberImpl member = tribe.members.getValid(p.getId());
            if (member != null) {
                Location location = p.getLocation().toLocation();
                member.replaceLocation(location);

                emitNext(new TribeMemberLocationChangeEvent(member, location));
            }
        });

        putPacketHandler(IPRoom.class, p -> {
            emitNext(new RoomChangeEvent(room.name = p.getRoom()));
        });

        putPacketHandler(IPRoomMice.class, p -> {
            room.mice.replaceAll(p.getMice());
        });

        putPacketHandler(IPRoomMouse.class, p -> {
            if (room.mice.getValid(p.getMouse().getId()) == null) {
                emitNext(new RoomMouseJoinEvent(room.mice.replace(p.getMouse())));
            }
            else { // I think this happens ONLY on respawn
                emitNext(new RoomMouseRespawnEvent(room.mice.replace(p.getMouse())));
            }
        });

        putPacketHandler(IPRoomMouseLeave.class, p -> {
            RoomMouseImpl mouse = room.mice.getValid(p.getMouseId());
            if (mouse != null) {
                room.mice.invalidate(p.getMouseId());

                emitNext(new RoomMouseLeaveEvent(mouse));
            }
        });

        putPacketHandler(IPRoomMessage.class, p -> {
            emitNext(new RoomMessageEvent(room, p.getSender(),
                    Community.valueOf(p.getSenderCommunity()), p.getMessage().replace("&lt;", "<").replace("&amp;", "&")));
        });
    }


    /* CHANNEL HANDLERS */
    private final class CommonChannelHandler extends SimpleChannelInboundHandler<Object> {
        private Future<?> keepAliveFuture;

        @Override
        public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
            keepAliveFuture = ctx.executor().scheduleWithFixedDelay(() -> {
                if (ctx.channel().isActive()) {
                    ctx.channel().writeAndFlush(new OPKeepAlive());
                }
            }, 11, 11, TimeUnit.SECONDS);
        }

        @Override
        public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
            keepAliveFuture.cancel(false);
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
            if (!callPacketHandler(msg.getClass(), msg)) {
                ctx.fireChannelRead(msg);
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            logger.warn("An unhandled exception has been caught", cause);
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

            emitNext(new StateChangeEvent(state = State.CLOSED));
            satelliteClose(false);
            eventLoopGroup.shutdownGracefully().addListener(f -> emitCompleted());

            ctx.fireChannelInactive();
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, IPConnect msg) throws Exception {
            ctx.writeAndFlush(new OPClient());

            logger.info("Connection to the main server has been established");
            emitNext(new StateChangeEvent(state = State.CONNECTED));
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

            emitNext(new SatelliteStateChangeEvent(satelliteState = SatelliteState.CLOSED));

            ctx.fireChannelInactive();
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, IPSatelliteConnect msg) throws Exception {
            logger.info("Connection to the satellite server at {} has been established",
                    ctx.channel().remoteAddress());
            emitNext(new SatelliteStateChangeEvent(satelliteState = SatelliteState.CONNECTED));
        }
    }
}
