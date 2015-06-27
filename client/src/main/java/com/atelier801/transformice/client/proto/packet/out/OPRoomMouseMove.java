package com.atelier801.transformice.client.proto.packet.out;

import lombok.*;

import com.atelier801.transformice.client.proto.TransformiceByteBuf;

// Valid for 1.252
@OutboundPacket.Code(major = 4, minor = 4)
@AllArgsConstructor @ToString
public final class OPRoomMouseMove implements OutboundPacket {
    private final int round;
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

    @Override
    public void write(TransformiceByteBuf out) {
        out.writeInt(round);
        out.writeBoolean(goingRight);
        out.writeBoolean(goingLeft);
        writeScaled(out, posX);
        writeScaled(out, posY);
        writeScaled(out, velX);
        writeScaled(out, velY);
        out.writeBoolean(jumping);
        out.writeByte(jumpingType);
        out.writeByte(usedPortal);

        if (rot != 0) {
            writeScaled(out, rot);
            writeScaled(out, velRot);
        }
    }

    private static void writeScaled(TransformiceByteBuf out, int value) {
        out.writeShort((int) (value / 3.0 * 10.0));
    }
}
