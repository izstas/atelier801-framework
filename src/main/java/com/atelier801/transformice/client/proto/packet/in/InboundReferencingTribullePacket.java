package com.atelier801.transformice.client.proto.packet.in;

import com.atelier801.transformice.client.proto.packet.TribulleContext;
import com.atelier801.transformice.client.proto.packet.out.OutboundReferencedTribullePacket;

public abstract class InboundReferencingTribullePacket<T extends OutboundReferencedTribullePacket>
        implements InboundTribullePacket {
    protected int referenceId;
    protected T reference;

    @SuppressWarnings("unchecked")
    public InboundTribullePacket context(TribulleContext ctx) {
        reference = (T) ctx.getReferencedPacket(referenceId);
        return this;
    }

    public T getReference() {
        return reference;
    }
}
