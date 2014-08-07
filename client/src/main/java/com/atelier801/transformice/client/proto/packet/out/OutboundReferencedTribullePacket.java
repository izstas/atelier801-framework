package com.atelier801.transformice.client.proto.packet.out;

import com.atelier801.transformice.client.proto.packet.TribulleContext;

public abstract class OutboundReferencedTribullePacket implements OutboundTribullePacket {
    protected int referenceId;

    public OutboundTribullePacket context(TribulleContext ctx) {
        referenceId = ctx.putReferencedPacket(this);
        return this;
    }
}
