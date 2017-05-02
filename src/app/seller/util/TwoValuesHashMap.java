package app.seller.util;

import javafx.util.Pair;

import java.util.HashMap;

public class TwoValuesHashMap<K, T, U> extends HashMap<K, Pair<T, U>> {

    public void put(K key, T value1, U value2) {
        Pair<T, U> value = new Pair<T, U>(value1, value2);
        this.put(key, value);
    }

    public T getFirstValue(K key) {
        return this.get(key).getKey();
    }

    public U getSecondValue(K key) {
        return this.get(key).getValue();
    }
}
