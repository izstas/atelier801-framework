package com.atelier801.transformice.client;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.atelier801.transformice.Location;
import com.atelier801.transformice.TribeMember;
import com.atelier801.transformice.TribeRank;
import com.atelier801.transformice.client.proto.data.DLocation;
import com.atelier801.transformice.client.proto.data.DTribeMember;

final class TribeMemberImpl implements TribeMember, Pooled<DTribeMember> {
    private final TransformiceClient transformice;
    private final int id;
    private String name;
    private TribeRankImpl rank;
    private LocalDateTime joinTime;
    private LocalDateTime lastOnlineTime;
    private List<Location> locations;

    public TribeMemberImpl(TransformiceClient transformice, int id) {
        this.transformice = transformice;
        this.id = id;
    }

    @Override
    public void update(DTribeMember data) {
        name = data.getName();
        rank = transformice.tribe.ranks.get(data.getRankId());
        joinTime = LocalDateTime.ofEpochSecond(data.getJoinTime() * 60, 0, ZoneOffset.UTC);
        lastOnlineTime = LocalDateTime.ofEpochSecond(data.getLastOnlineTime() * 60, 0, ZoneOffset.UTC);
        locations = data.getLocations().stream().map(DLocation::toLocation).collect(Collectors.toList());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public TribeRank getRank() {
        return rank;
    }

    @Override
    public LocalDateTime getJoinTime() {
        return joinTime;
    }

    @Override
    public LocalDateTime getLastOnlineTime() {
        return lastOnlineTime;
    }

    @Override
    public List<Location> getLocations() {
        return Collections.unmodifiableList(locations);
    }

    void replaceLocation(Location location) {
        locations.removeIf(l -> l.getGame() == location.getGame());
        locations.add(location);
    }

    void removeLocation(Location.Game game) {
        locations.removeIf(l -> l.getGame() == game);
    }
}