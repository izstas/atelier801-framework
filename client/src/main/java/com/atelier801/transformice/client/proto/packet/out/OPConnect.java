package com.atelier801.transformice.client.proto.packet.out;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.google.common.base.MoreObjects;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.180
@OutboundPacket.Code(major = 28, minor = 1, transformable = false)
public final class OPConnect implements OutboundPacket {
    private static final Random random = new Random();

    private final int version;
    private final String key;

    public OPConnect(int version, String key) {
        this.version = version;
        this.key = key;
    }

    @Override
    public void write(TransformiceByteBuf out) {
        out.writeShort(version);
        out.writeUTF(key);
        out.writeUTF("PlugIn"); // Capabilities.playerType
        out.writeUTF("5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2107.2 " +
                "Safari/537.36-Netscape"); // Client's browser
        out.writeInt(10084); // Size of loader
        out.writeUTF("");
        out.writeUTF(Stream.generate(() -> String.valueOf("0123456789abcdef".charAt(random.nextInt(16)))).limit(64)
                .collect(Collectors.joining())); // SHA256 of client's fonts
        out.writeUTF("A=t&SA=t&SV=t&EV=t&MP3=t&AE=t&VE=t&ACC=t&PR=t&SP=f&SB=f&DEB=f&V=WIN 14,0,0,145&M=Google Pepper&" +
                "R=1366x768&COL=color&AR=1.0&OS=Windows 8.1&ARCH=x86&L=en&IME=t&PR32=t&PR64=t&PT=PlugIn&AVD=f&LFD=f&" +
                "WD=f&TLS=t&ML=5.1&DP=72"); // Capabilities.serverString
        out.writeInt(0); // Referrer ("Want some free cheese? Refer other players!" thing)
        out.writeInt(1000 + random.nextInt(4001)); // getTimer()
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("version", version)
                .add("key", key)
                .toString();
    }
}
