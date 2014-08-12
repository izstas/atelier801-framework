package com.atelier801.transformice.client;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.atelier801.transformice.TribePermission;
import com.atelier801.transformice.TribeRank;
import com.atelier801.transformice.client.proto.data.DTribeRank;

final class TribeRankImpl implements TribeRank, Pooled<DTribeRank> {
    final TransformiceClient transformice;
    final int id;
    private String name;
    private int position;
    private Set<TribePermission> permissions;

    TribeRankImpl(TransformiceClient transformice, int id) {
        this.transformice = transformice;
        this.id = id;
    }

    @Override
    public void update(DTribeRank data) {
        name = data.getName();
        position = data.getPosition();
        permissions = IntStream.range(0, data.getPermissions().size()).filter(data.getPermissions()::get)
                .mapToObj(TribePermission::valueOf).collect(Collectors.toSet());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Set<TribePermission> getPermissions() {
        return Collections.unmodifiableSet(permissions);
    }

    @Override
    public int compareTo(TribeRank other) {
        return Integer.compare(((TribeRankImpl) other).position, position);
    }
}
