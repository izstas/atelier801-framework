package com.atelier801.transformice.client.proto.packet.in;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.atelier801.transformice.client.proto.packet.TribulleContext;

public interface InboundTribullePacket {
    /*
     * This is a marker interface.
     * Implementers must have a constructor which accepts a TransformiceByteBuf.
     */

    default InboundTribullePacket context(TribulleContext ctx) {
        return this;
    }


    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Label {
        String value();
    }
}
