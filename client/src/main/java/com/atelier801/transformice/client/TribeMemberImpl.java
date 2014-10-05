package com.atelier801.transformice.client;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import rx.Observable;

import com.atelier801.transformice.Location;
import com.atelier801.transformice.TransformiceUtil;
import com.atelier801.transformice.TribeMember;
import com.atelier801.transformice.TribeRank;
import com.atelier801.transformice.client.proto.data.DLocation;
import com.atelier801.transformice.client.proto.data.DTribeMember;
import com.atelier801.transformice.event.TribeMemberKickEvent;
import com.atelier801.transformice.event.TribeMemberRankChangeEvent;

final class TribeMemberImpl implements TribeMember, Pooled<DTribeMember> {
    final TransformiceClient transformice;
    final int id;
    private String name;
    private TribeRankImpl rank;
    private Instant joiningTime;
    private Instant lastConnectionTime;
    private List<Location> locations;

    TribeMemberImpl(TransformiceClient transformice, int id) {
        this.transformice = transformice;
        this.id = id;
    }

    @Override
    public void update(DTribeMember data) {
        name = TransformiceUtil.normalizeMouseName(data.getName());
        rank = transformice.tribe.ranks.get(data.getRankId());
        joiningTime = Instant.ofEpochSecond(data.getJoiningTime() * 60);
        lastConnectionTime = Instant.ofEpochSecond(data.getLastConnectionTime() * 60);
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

    void setRank(TribeRankImpl rank) {
        this.rank = rank;
    }

    @Override
    public Instant getJoiningTime() {
        return joiningTime;
    }

    @Override
    public Instant getLastConnectionTime() {
        return lastConnectionTime;
    }

    @Override
    public List<Location> getLocations() {
        return Collections.unmodifiableList(locations);
    }

    void replaceLocation(Location location) {
        locations.removeIf(l -> l.getGame() == location.getGame());
        locations.add(location);
    }

    boolean removeLocation(Location.Game game) {
        return locations.removeIf(l -> l.getGame() == game);
    }

    @Override
    public Observable<TribeMemberRankChangeEvent> changeRank(TribeRank rank) {
        return transformice.tribe.changeMemberRank(this, (TribeRankImpl) rank);
    }

    @Override
    public Observable<TribeMemberKickEvent> kick() {
        return transformice.tribe.kickMember(this);
    }
}
