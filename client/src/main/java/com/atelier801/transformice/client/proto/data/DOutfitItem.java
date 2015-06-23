package com.atelier801.transformice.client.proto.data;

import lombok.*;
import java.util.List;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

// Valid for 1.252
@Getter @ToString
public final class DOutfitItem {
    private static final Splitter splitter = Splitter.on('+');
    private static final Splitter colorsSplitter = Splitter.on(',');

    private final int item;
    private final List<Integer> colors;

    public DOutfitItem(String in) {
        this(splitter.splitToList(in));
    }

    public DOutfitItem(List<String> in) {
        item = Integer.parseInt(in.get(0));
        colors = in.size() > 1 ?
                ImmutableList.copyOf(Iterables.transform(colorsSplitter.split(in.get(1)), c -> Integer.valueOf(c, 16))) :
                ImmutableList.of();
    }
}
