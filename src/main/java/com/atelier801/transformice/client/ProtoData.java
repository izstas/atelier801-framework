package com.atelier801.transformice.client;

import java.util.Map;

public interface ProtoData {
    int getVersion();
    String getKey();
    Map<Integer, Integer> getCodeTransforms();
    Map<String, Integer> getTribulleCodes();
}
