package com.atelier801.transformice.client.proto.packet.in;

import io.netty.buffer.ByteBuf;
import io.netty.util.AbstractReferenceCounted;
import io.netty.util.ReferenceCounted;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.180
@InboundPacket.Code(major = 60, minor = 1)
public final class IPWrapperTribulle extends AbstractReferenceCounted implements InboundPacket {
    private final int code;
    private final ByteBuf body;

    public IPWrapperTribulle(TransformiceByteBuf in) {
        code = in.readShort();
        body = in.readSlice(in.readableBytes()).retain();
    }

    public int getCode() {
        return code;
    }

    public ByteBuf getBody() {
        return body;
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
