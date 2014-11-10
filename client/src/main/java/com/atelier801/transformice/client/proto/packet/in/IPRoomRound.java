package com.atelier801.transformice.client.proto.packet.in;

import com.google.common.base.MoreObjects;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.201
@InboundPacket.Code(major = 5, minor = 2)
public final class IPRoomRound implements InboundPacket {
    private final int mapId;
    private final int id;
    private final String mapXml;
    private final String mapAuthor;
    private final int mapCategory;

    public IPRoomRound(TransformiceByteBuf in) {
        mapId = in.readInt();
        in.readShort(); // Number of mice
        id = in.readByte();
        mapXml = in.readUTF();
        mapAuthor = in.readUTF();
        mapCategory = in.readByte();
    }

    public int getMapId() {
        return mapId;
    }

    public int getId() {
        return id;
    }

    public String getMapXml() {
        return mapXml;
    }

    public String getMapAuthor() {
        return mapAuthor;
    }

    public int getMapCategory() {
        return mapCategory;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("mapId", mapId)
                .add("id", id)
                .add("mapXml", mapXml)
                .add("mapAuthor", mapAuthor)
                .add("mapCategory", mapCategory)
                .toString();
    }
}
