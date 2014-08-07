package com.atelier801.transformice.client.proto.packet;

import com.atelier801.transformice.client.proto.packet.out.OutboundReferencedTribullePacket;

public final class TribulleContext {
    private final Reference[] references = new Reference[256];
    private int nextReferenceId = 1;

    public int putReferencedPacket(OutboundReferencedTribullePacket packet) {
        int referenceId = nextReferenceId++;
        references[referenceId % references.length] = new Reference(referenceId, packet);
        return referenceId;
    }

    public OutboundReferencedTribullePacket getReferencedPacket(int referenceId) {
        Reference reference = references[referenceId % references.length];
        if (reference == null) {
            throw new IllegalStateException(String.format("reference #%d does not exist", referenceId));
        }
        if (reference.id != referenceId) {
            throw new IllegalStateException(String.format("reference #%d is no longer available", referenceId));
        }

        references[referenceId % references.length] = null;
        return reference.packet;
    }


    private static final class Reference {
        final int id;
        final OutboundReferencedTribullePacket packet;

        Reference(int id, OutboundReferencedTribullePacket packet) {
            this.id = id;
            this.packet = packet;
        }
    }
}
