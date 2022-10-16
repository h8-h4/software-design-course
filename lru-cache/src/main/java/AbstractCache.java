import org.jetbrains.annotations.Nullable;

public abstract class AbstractCache<K, V> implements Cache<K, V> {
    protected final int capacity;

    public AbstractCache(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity should be positive.");
        }

        this.capacity = capacity;
    }

    @Override
    @Nullable
    public V get(K key) {
        int oldSize = size();
        assert isCorrectSize(oldSize);

        V result = doGet(key);

        assert oldSize == size();
        return result;
    }

    @Override
    public void put(K key, V value) {
        int oldSize = size();
        assert isCorrectSize(oldSize);

        doPut(key, value);

        int newSize = size();
        assert (newSize == oldSize || newSize == oldSize + 1)
                && isCorrectSize(newSize)
                && doGet(key) == value;
    }

    @Override
    public int size() {
        int oldSize = getSize();
        assert isCorrectSize(oldSize);

        int size = getSize();

        assert isCorrectSize(size) && size == oldSize;
        return size;
    }

    @Override
    public int capacity() {
        int capacity = this.capacity;
        assert capacity > 0;
        return capacity;
    }

    private boolean isCorrectSize(int size) {
        return size <= capacity && size >= 0;
    }

    @Nullable
    abstract V doGet(K key);

    abstract void doPut(K key, V value);
    abstract int getSize();
}
