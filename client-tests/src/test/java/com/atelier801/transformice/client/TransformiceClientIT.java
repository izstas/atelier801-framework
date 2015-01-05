package com.atelier801.transformice.client;

import java.net.InetSocketAddress;
import java.util.Optional;
import rx.observables.ConnectableObservable;

import com.atelier801.transformice.*;
import com.atelier801.transformice.event.*;

import org.testng.annotations.*;
import static org.testng.Assert.*;

public class TransformiceClientIT {
    private String username;
    private String password;
    private String tribe;
    private String room;

    private IzstasProtoData data;
    private TransformiceClient tfm;
    private ConnectableObservable<Event> allEvents;

    private TribeRank testTribeRank1, testTribeRank2;
    private TribeMember testTribeMember0, testTribeMember1;
    private ChatChannel testChannel;
    private RoomMouse testRoomMouse;

    @BeforeClass
    public void setUp() throws Exception {
        String izstasProtoDataKey = System.getenv("TEST801_IZSTAS_PROTO_DATA_KEY");
        username = System.getenv("TEST801_USERNAME");
        password = System.getenv("TEST801_PASSWORD");
        tribe = System.getenv("TEST801_TRIBE");

        assertNotNull(izstasProtoDataKey, "izstasProtoDataKey");
        assertNotNull(username, "username");
        assertNotNull(password, "password");
        assertNotNull(tribe, "tribe");

        data = IzstasProtoData.fetch(izstasProtoDataKey);
    }

    @AfterClass
    public void tearDown() throws Exception {
        if (tfm != null && tfm.getState() != Transformice.State.CLOSING && tfm.getState() != Transformice.State.CLOSED) {
            tfm.close();
        }
    }

    @Test(timeOut = 5000)
    public void testConnect() throws Exception {
        tfm = new TransformiceClient(new InetSocketAddress(data.getIp(), 44444), data);
        allEvents = tfm.events().publish();
        allEvents.connect();

        StateChangeEvent stateChangeEvent = tfm.events().ofType(StateChangeEvent.class).toBlocking().first();
        assertEquals(stateChangeEvent.getState(), Transformice.State.CONNECTED, "stateChangeEvent.getState");
    }

    @Test(timeOut = 5000, dependsOnMethods = "testConnect")
    public void testLogIn() throws Exception {
        room = "*Room" + System.currentTimeMillis();

        LoginEvent loginEvent = tfm.logIn(username, Optional.of(password), Optional.of(room)).toBlocking().first();
        assertTrue(loginEvent.isSuccess(), "loginEvent.isSuccess");
        assertEquals(((LoginSuccessEvent) loginEvent).getMouseName(), username, "loginEvent.getMouseName");
    }

    @Test(timeOut = 5000, dependsOnMethods = "testLogIn", priority = -1)
    public void testInitialTribe() throws Exception {
        allEvents.ofType(TribeChangeEvent.class).toBlocking().first();
        assertEquals(tfm.tribe().getName(), tribe, "tribe.getName");
    }

    @Test(dependsOnMethods = "testInitialTribe")
    public void testInitialTribeRanks() throws Exception {
        testTribeRank1 = tfm.tribe().getRanks().stream().filter(r -> r.getName().equals("Rank1")).findAny().orElse(null);
        assertNotNull(testTribeRank1, "testTribeRank1");
        assertTrue(testTribeRank1.getPermissions().isEmpty(), "testTribeRank1.getPermissions.isEmpty");

        testTribeRank2 = tfm.tribe().getRanks().stream().filter(r -> r.getName().equals("Rank2")).findAny().orElse(null);
        assertNotNull(testTribeRank2, "testTribeRank2");
        assertEquals(testTribeRank2.getPermissions().size(), 1, "testTribeRank2.getPermissions.size");
        assertTrue(testTribeRank2.getPermissions().contains(TribePermission.PLAY_MUSIC), "testTribeRank2.getPermissions.contains");
        assertTrue(testTribeRank2.compareTo(testTribeRank1) > 0, "testTribeRank2 > testTribeRank1");
    }

    @Test(dependsOnMethods = "testInitialTribeRanks")
    public void testInitialTribeMembers() throws Exception {
        testTribeMember0 = tfm.tribe().getMembers().stream()
                .filter(m -> m.getName().equals(username)).findAny().orElse(null);
        assertNotNull(testTribeMember0, "testTribeMember0");

        testTribeMember1 = tfm.tribe().getMembers().stream()
                .filter(m -> m.getRank() == testTribeRank1 || m.getRank() == testTribeRank2).findAny().orElse(null);
        assertNotNull(testTribeMember1, "testTribeMember1");
    }

    @Test(timeOut = 5000, dependsOnMethods = "testLogIn", priority = -1)
    public void testInitialRoom() throws Exception {
        allEvents.ofType(RoomChangeEvent.class).toBlocking().first();
        assertEquals(tfm.room().getName(), room, "room.getName");
    }

    @Test(dependsOnMethods = "testInitialRoom")
    public void testInitialRoomMice() throws Exception {
        assertEquals(tfm.room().getMice().size(), 1, "room.getMice.size");

        testRoomMouse = tfm.room().getMice().stream().filter(m -> m.getName().equals(username)).findAny().orElse(null);
        assertNotNull(testRoomMouse, "testRoomMouse");
    }

    @Test(timeOut = 5000, dependsOnMethods = "testLogIn")
    public void testSendPrivateMessage() throws Exception {
        String message = "Message" + System.currentTimeMillis();

        tfm.sendPrivateMessage(username, message);
        PrivateMessageEvent privateMessageEvent = tfm.events().ofType(PrivateMessageEvent.class)
                .filter(e -> e.getSenderName().equals(username)).toBlocking().first();
        assertEquals(privateMessageEvent.getMessage(), message, "privateMessageEvent.getMessage");
    }

    @Test(timeOut = 5000, dependsOnMethods = "testLogIn")
    public void testEnterChannel() throws Exception {
        String testChannelName = "Channel" + System.currentTimeMillis();

        testChannel = tfm.enterChannel(testChannelName).toBlocking().first().getChannel();
        assertEquals(testChannel.getName(), testChannelName, "testChannel.getName");
    }

    @Test(timeOut = 5000, dependsOnMethods = "testEnterChannel")
    public void testChannelSendMessage() throws Exception {
        String message = "Message" + System.currentTimeMillis();

        testChannel.sendMessage(message);
        ChannelMessageEvent channelMessageEvent = tfm.events().ofType(ChannelMessageEvent.class)
                .filter(e -> e.getChat() == testChannel).toBlocking().first();
        assertEquals(channelMessageEvent.getMessage(), message, "channelMessageEvent.getMessage");
    }

    @Test(timeOut = 5000, dependsOnMethods = "testEnterChannel", priority = 1)
    public void testChannelQuit() throws Exception {
        testChannel.quit().toBlocking().first();
    }

    @Test(timeOut = 5000, dependsOnMethods = "testInitialTribeMembers")
    public void testTribeSendMessage() throws Exception {
        String message = "Message" + System.currentTimeMillis();

        tfm.tribe().sendMessage(message);
        TribeMessageEvent tribeMessageEvent = tfm.events().ofType(TribeMessageEvent.class)
                .filter(e -> e.getSender() == testTribeMember0).toBlocking().first();
        assertEquals(tribeMessageEvent.getMessage(), message, "tribeMessageEvent.getMessage");
    }

    @Test(timeOut = 5000, dependsOnMethods = "testInitialTribe", priority = 1)
    public void testTribeEnterHouse() throws Exception {
        tfm.tribe().enterHouse().toBlocking().first();
        assertEquals(tfm.room().getName(), "*\u0003" + tribe, "room.getName");
    }

    @Test(timeOut = 5000, dependsOnMethods = "testInitialTribeMembers")
    public void testTribeMemberChangeRank() throws Exception {
        TribeRank newRank = testTribeMember1.getRank() == testTribeRank2 ? testTribeRank1 : testTribeRank2;

        testTribeMember1.changeRank(newRank).toBlocking().first();
        assertEquals(testTribeMember1.getRank(), newRank);
    }

    @Test(timeOut = 5000, dependsOnMethods = "testInitialRoomMice")
    public void testRoomSendMessage() throws Exception {
        String message = "Message" + System.currentTimeMillis();

        tfm.room().sendMessage(message);
        RoomMessageEvent roomMessageEvent = tfm.events().ofType(RoomMessageEvent.class)
                .filter(e -> e.getSender() == testRoomMouse).toBlocking().first();
        assertEquals(roomMessageEvent.getMessage(), message, "roomMessageEvent.getMessage");
    }
}
