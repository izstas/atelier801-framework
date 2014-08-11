package com.atelier801.transformice.client;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Function;
import com.google.common.collect.MapMaker;

final class Pool<K, V extends Pooled<D>, D> {
    private final Map<K, V> pooled = new MapMaker().weakValues().makeMap();
    private final Collection<V> valid = new CopyOnWriteArraySet<>();
    private final Function<K, V> valueFromKey;
    private final Function<D, K> keyFromData;

    Pool(Function<K, V> valueFromKey, Function<D, K> keyFromData) {
        this.valueFromKey = valueFromKey;
        this.keyFromData = keyFromData;
    }

    Collection<V> valid() {
        return Collections.unmodifiableCollection(valid);
    }

    V get(K key) {
        return pooled.computeIfAbsent(key, valueFromKey);
    }

    V getValid(K key) {
        V object = pooled.get(key);
        return object != null && valid.contains(object) ? object : null;
    }

    V replace(D data) {
        V object = pooled.computeIfAbsent(keyFromData.apply(data), valueFromKey);
        object.update(data);
        valid.add(object);
        return object;
    }

    Collection<V> replaceAll(Collection<D> data) {
        valid.clear();
        data.forEach(this::replace);
        return valid();
    }

    void invalidate(K key) {
        valid.remove(pooled.get(key));
    }
}
