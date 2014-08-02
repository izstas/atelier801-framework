package com.atelier801.transformice.client.proto.packet.out;

import io.netty.buffer.ByteBuf;
import io.netty.util.AbstractReferenceCounted;
import io.netty.util.ReferenceCounted;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.180
@OutboundPacket.Code(major = 60, minor = 1)
public final class OPWrapperTribulle extends AbstractReferenceCounted implements OutboundPacket {
    private final int code;
    private final ByteBuf body;

    public OPWrapperTribulle(int code, ByteBuf body) {
        this.code = code;
        this.body = body;
    }

    @Override
    public void write(TransformiceByteBuf out) {
        out.writeShort(code);
        out.writeBytes(body);
    }

    @Override
    public ReferenceCounted touch(Object hint) {
        return this;
    }

    @Override
    protected void deallocate() {
        body.release();
    }
}
