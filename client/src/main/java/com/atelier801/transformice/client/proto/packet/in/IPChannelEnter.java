package com.atelier801.transformice.client.proto.packet.in;

import java.util.List;
import com.google.common.base.Objects;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.180
@InboundTribullePacket.Label("ET_SignaleRejointCanal")
public final class IPChannelEnter implements InboundTribullePacket {
    private final int id;
    private final String name;
    private final boolean closeable;
    private final List<Integer> memberIds;
    private final List<String> memberNames;

    public IPChannelEnter(TransformiceByteBuf in) {
        id = in.readInt();
        name = in.readUTF();
        closeable = in.readBoolean();
        memberIds = in.readList(in.readShort(), in::readInt);
        memberNames = in.readList(in.readShort(), in::readUTF);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isCloseable() {
        return closeable;
    }

    public List<Integer> getMemberIds() {
        return memberIds;
    }

    public List<String> getMemberNames() {
        return memberNames;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("closeable", closeable)
                .add("memberIds", memberIds)
                .add("memberNames", memberNames)
                .toString();
    }
}
