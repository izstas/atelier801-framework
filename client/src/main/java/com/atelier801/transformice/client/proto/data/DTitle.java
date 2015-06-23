package com.atelier801.transformice.client.proto.data;

import lombok.*;
import java.util.List;
import com.google.common.base.Splitter;

// Valid for 1.252
@Getter @ToString
public final class DTitle {
    private static final Splitter splitter = Splitter.on(',');

    private final int id;
    private final int level;

    public DTitle(String in) {
        this(splitter.splitToList(in));
    }

    public DTitle(List<String> in) {
        id = Integer.parseInt(in.get(0));
        level = Integer.parseInt(in.get(1));
    }
}
