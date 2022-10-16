import org.jetbrains.annotations.Nullable;

public interface Cache<K, V> {
    int size();

    int capacity();

    @Nullable
    V get(K key);

    void put(K key, V value);
}
