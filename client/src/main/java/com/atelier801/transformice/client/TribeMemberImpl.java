package com.atelier801.transformice.client;

import java.time.Instant;
import rx.Observable;

import com.atelier801.transformice.Location;
import com.atelier801.transformice.TransformiceUtil;
import com.atelier801.transformice.TribeMember;
import com.atelier801.transformice.TribeRank;
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
    private Location location;
    private boolean online;

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
        location = data.getLocation().toLocation();
        online = data.isOnline();
    }

    @Override
    public int getId() {
        return id;
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
    public Location getLocation() {
        if (!online) {
            throw new IllegalStateException("The tribe member is not online");
        }

        return location;
    }

    @Override
    public boolean isOnline() {
        return online;
    }

    void setOnline(boolean online) {
        this.online = online;
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
