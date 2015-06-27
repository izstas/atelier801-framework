package com.atelier801.transformice.client.proto.packet.in;

import lombok.*;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.252
@InboundPacket.Code(major = 4, minor = 4)
@Getter @ToString
public final class IPRoomMouseMove implements InboundPacket {
    private final int mouseId;
    private final boolean goingRight;
    private final boolean goingLeft;
    private final int posX;
    private final int posY;
    private final int velX;
    private final int velY;
    private final boolean jumping;
    private final int jumpingType;
    private final int usedPortal;
    private final int rot;
    private final int velRot;

    public IPRoomMouseMove(TransformiceByteBuf in) {
        mouseId = in.readInt();
        in.readInt();
        goingRight = in.readBoolean();
        goingLeft = in.readBoolean();
        posX = readScaled(in);
        posY = readScaled(in);
        velX = readScaled(in);
        velY = readScaled(in);
        jumping = in.readBoolean();
        jumpingType = in.readByte();
        usedPortal = in.readByte();

        if (in.isReadable()) {
            rot = readScaled(in);
            velRot = readScaled(in);
        }
        else {
            rot = 0;
            velRot = 0;
        }
    }

    private static int readScaled(TransformiceByteBuf in) {
        return (int) (in.readShort() / 10.0 * 3.0);
    }
}
