package com.atelier801.transformice.client.proto.packet.out;

import java.util.Optional;
import java.util.Random;
import com.google.common.base.Charsets;
import com.google.common.base.Objects;
import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.180
@OutboundPacket.Code(major = 26, minor = 8)
public final class OPLogin implements OutboundPacket {
    private static final Random random = new Random();

    private final String username;
    private final Optional<String> password;
    private final Optional<String> room;

    public OPLogin(String username, Optional<String> password, Optional<String> room) {
        this.username = username;
        this.password = password;
        this.room = room;
    }

    @Override
    public void write(TransformiceByteBuf out) {
        out.writeByte(random.nextInt(100));
        out.writeUTF(username);
        out.writeUTF(password
                .map(s -> Hashing.sha256().hashString(s, Charsets.UTF_8).toString())
                .map(s -> Hashing.sha256().newHasher()
                        .putString(s, Charsets.US_ASCII)
                        .putBytes(new byte[] {-9, 26, -90, -34, -113, 23, 118, -88, 3, -99, 50, -72, -95, 86, -78, -87,
                                62, -35, 67, -99, -59, -35, -50, 86, -45, -73, -92, 5, 74, 13, 8, -80})
                        .hash().asBytes())
                .map(BaseEncoding.base64()::encode)
                .orElse("")); // SHAKikoo!
        out.writeByte(random.nextInt(100));
        out.writeByte(random.nextInt(100));
        out.writeUTF("http://www.transformice.com/Transformice.swf?d=" + (System.currentTimeMillis() - 5000));
        out.writeUTF(room.orElse("1"));
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("username", username)
                .add("password", password.isPresent() ? "Optional[**HIDDEN**]" : "Optional.empty")
                .add("room", room)
                .toString();
    }
}
