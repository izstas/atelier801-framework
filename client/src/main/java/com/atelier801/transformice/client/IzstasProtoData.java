package com.atelier801.transformice.client;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.function.Function;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.common.net.UrlEscapers;
import com.google.gson.Gson;

public final class IzstasProtoData implements ProtoData {
    private static final Gson gson = new Gson();

    private String ip;
    private int version;
    private String key;
    private Map<Integer, Integer> transforms;
    private int dynamicTransformInitial, dynamicTransformAdd, dynamicTransformModulo;
    private Map<String, Integer> tribulle;

    public static IzstasProtoData fetch(String key) {
        URL url;
        try {
            url = new URL("http://tmdevs.hnngghh.com/transformice.json?key=" +
                    UrlEscapers.urlFormParameterEscaper().escape(key));
        }
        catch (MalformedURLException e) {
            throw new AssertionError(e);
        }

        String json;
        try {
            json = Resources.toString(url, Charsets.UTF_8);
        }
        catch (IOException e) {
            throw new RuntimeException("unable to fetch data", e);
        }

        return gson.fromJson(json, IzstasProtoData.class);
    }


    public String getIp() {
        return ip;
    }

    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public Function<Integer, Integer> getPacketCodePreTransformer() {
        return transforms::get;
    }

    @Override
    public Function<Integer, Integer> getPacketCodeDynamicTransformer() {
        return new Function<Integer, Integer>() {
            private int mask = dynamicTransformInitial;

            @Override
            public Integer apply(Integer code) {
                mask = (mask + dynamicTransformAdd) % dynamicTransformModulo;
                return code ^ mask;
            }
        };
    }

    @Override
    public Function<String, Integer> getTribulleLabelResolver() {
        return tribulle::get;
    }
}
