package com.atelier801.transformice.client;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Function;
import com.google.common.collect.MapMaker;

final class Pool<K, V extends Pooled<D>, D> {
    private final Map<K, V> pool = new MapMaker().weakValues().makeMap();
    private final Collection<V> objects = new CopyOnWriteArraySet<>();
    private final Function<K, V> valueFromKey;
    private final Function<D, K> keyFromData;

    Pool(Function<K, V> valueFromKey, Function<D, K> keyFromData) {
        this.valueFromKey = valueFromKey;
        this.keyFromData = keyFromData;
    }

    Collection<V> objects() {
        return Collections.unmodifiableCollection(objects);
    }

    V get(K key) {
        return pool.get(key);
    }

    void replace(D data) {
        V object = pool.computeIfAbsent(keyFromData.apply(data), valueFromKey);
        object.update(data);
        objects.add(object);
    }

    void replace(Collection<D> data) {
        objects.clear();
        data.forEach(this::replace);
    }

    void remove(K key) {
        objects.remove(pool.get(key));
    }
}
