package com.atelier801.transformice.client.proto.data;

import lombok.*;
import java.util.List;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

// Valid for 1.252
@Getter @ToString
public final class DOutfit {
    private static final Splitter splitter = Splitter.on(';');
    private static final Splitter itemsSplitter = Splitter.on(',');

    private final int fur;
    private final List<DOutfitItem> items;

    public DOutfit(String in) {
        this(splitter.splitToList(in));
    }

    public DOutfit(List<String> in) {
        fur = Integer.parseInt(in.get(0));
        items = ImmutableList.copyOf(Iterables.transform(itemsSplitter.split(in.get(1)), DOutfitItem::new));
    }
}
