package com.atelier801.transformice.client;

import java.util.function.Function;

public interface ProtoData {
    int getVersion();
    String getKey();

    Function<Integer, Integer> getLoginKeyFunction();
    Function<String, Integer> getTribulleLabelResolver();
}
