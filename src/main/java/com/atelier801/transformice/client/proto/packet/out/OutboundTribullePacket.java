package com.atelier801.transformice.client.proto.packet.out;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

public interface OutboundTribullePacket {
    void write(TransformiceByteBuf out);


    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Label {
        String value();
    }
}
