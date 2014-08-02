package com.atelier801.transformice.client.proto.packet.in;

import com.google.common.base.Objects;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;
import com.atelier801.transformice.client.proto.packet.out.OPChannelMessage;

// Valid for 1.180
@InboundTribullePacket.Label("ET_ResultatMessageCanal")
public final class IPChannelMessageResult extends InboundReferencingTribullePacket<OPChannelMessage> {
    private final int result;

    public IPChannelMessageResult(TransformiceByteBuf in) {
        referenceId = in.readInt();
        result = in.readByte();
    }

    public int getResult() {
        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("reference", reference)
                .add("result", result)
                .toString();
    }
}
