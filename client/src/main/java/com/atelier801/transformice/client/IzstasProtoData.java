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
    private int loginKey;
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
            throw new RuntimeException("Unable to fetch the data", e);
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
    public Function<Integer, Integer> getLoginKeyFunction() {
        return code -> code ^ loginKey;
    }

    @Override
    public Function<String, Integer> getTribulleLabelResolver() {
        return tribulle::get;
    }
}
