package com.atelier801.transformice.client.proto.packet.out;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

public interface OutboundLegacyPacket {
    void write(List<String> out);


    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Code {
        int major();
        int minor();
    }
}
