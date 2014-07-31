package com.atelier801.transformice.client.proto.packet.in;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface InboundPacket {
    /**
     * This is a marker interface.
     * Implementers must have a constructor which accepts a TransformiceByteBuf.
     */


    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Code {
        int major();
        int minor();
    }
}
