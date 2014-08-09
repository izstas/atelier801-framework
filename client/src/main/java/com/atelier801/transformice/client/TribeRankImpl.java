package com.atelier801.transformice.client;

import com.atelier801.transformice.TribeRank;
import com.atelier801.transformice.client.proto.data.DTribeRank;

final class TribeRankImpl implements TribeRank, Pooled<DTribeRank> {
    private final TransformiceClient transformice;
    private final int id;
    private String name;
    private int position;

    TribeRankImpl(TransformiceClient transformice, int id) {
        this.transformice = transformice;
        this.id = id;
    }

    @Override
    public void update(DTribeRank data) {
        name = data.getName();
        position = data.getPosition();
    }

    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int compareTo(TribeRank other) {
        return Integer.compare(((TribeRankImpl) other).position, position);
    }
}
