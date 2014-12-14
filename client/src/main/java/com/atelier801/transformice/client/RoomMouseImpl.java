package com.atelier801.transformice.client;

import com.atelier801.transformice.RoomMouse;
import com.atelier801.transformice.client.proto.data.DRoomMouse;

final class RoomMouseImpl implements RoomMouse, Pooled<DRoomMouse> {
    final TransformiceClient transformice;
    final int id;
    private String name;
    private boolean dead;
    private int score;
    private boolean cheese;

    RoomMouseImpl(TransformiceClient transformice, int id) {
        this.transformice = transformice;
        this.id = id;
    }

    @Override
    public void update(DRoomMouse data) {
        name = data.getName();
        dead = data.isDead();
        score = data.getScore();
        cheese = data.hasCheese();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isDead() {
        return dead;
    }

    void setDead(boolean dead) {
        this.dead = dead;
    }

    @Override
    public int getScore() {
        return score;
    }

    void setScore(int score) {
        this.score = score;
    }

    @Override
    public boolean hasCheese() {
        return cheese;
    }

    void setCheese(boolean cheese) {
        this.cheese = cheese;
    }
}
