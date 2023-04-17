package ru.otus.cachehw;


import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {
    private final WeakHashMap<K,V> cache = new WeakHashMap<>();
    private final List<HwListener<K, V>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        listeners.forEach(l->{
            try{
                l.notify(key, value, "put");
            }catch(Exception e){}
        });
    }

    @Override
    public void remove(K key) {
        var value = cache.remove(key);
        listeners.forEach(l->{
            try{
                l.notify(key, value, "remove");
            }catch(Exception e){}
        });
    }

    @Override
    public V get(K key) {
        var value = cache.get(key);
        listeners.forEach(l->{
            try{
                l.notify(key, value, "get");
            }catch(Exception e){}
        });
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        if (listeners.contains(listener))
            listeners.remove(listeners);
    }
}
