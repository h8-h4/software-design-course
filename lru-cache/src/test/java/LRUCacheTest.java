import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class LRUCacheTest {
    @Test
    public void testNegativeCapacity() {
        assertThrows(IllegalArgumentException.class, () -> createCache(-1));
    }

    @Test
    public void testCorrectnessSimple() {
        Cache<Integer, Integer> cache = createCache(1);
        assertEquals(1, cache.capacity());

        assertNull(cache.get(2423));
        assertNull(cache.get(-1234));
        assertEquals(0, cache.size());

        cache.put(1, 1);
        assertEquals(1, cache.size());
        assertEquals(1, cache.get(1));

        cache.put(1, 10);
        assertEquals(1, cache.size());
        assertEquals(10, cache.get(1));

        cache.put(2, 2);
        assertEquals(1, cache.size());
        assertEquals(2, cache.get(2));

        cache.put(3, 3);
        assertEquals(1, cache.size());
        assertEquals(3, cache.get(3));
    }


    @Test
    public void testEviction() {
        Cache<Integer, Integer> cache = createCache(2);

        cache.put(1, 1);
        cache.put(2, 2);

        assertEquals(1, cache.get(1));
        assertEquals(2, cache.get(2));

        cache.put(3, 3);
        assertNull(cache.get(1));
        assertEquals(2, cache.get(2));
        assertEquals(3, cache.get(3));
        assertEquals(2, cache.size());


        cache.get(2);
        cache.put(4, 4);
        assertNull(cache.get(3));
        assertEquals(2, cache.get(2));
        assertEquals(4, cache.get(4));
        assertEquals(2, cache.size());
    }


    @Test
    public void randomTest() {
        Cache<Integer, Integer> cache = createCache(10);
        LinkedHashMapLRUCache<Integer, Integer> expectedCache = new LinkedHashMapLRUCache<>(10);

        Random rng = new Random();
        IntStream.generate(() -> rng.nextInt(10))
                .limit(10000)
                .forEach(val -> {
                    expectedCache.put(val, val);
                    cache.put(val, val);
                    assertEquals(expectedCache.get(val), cache.get(val));
                });
    }


    private static Cache<Integer, Integer> createCache(int capacity) {
        return new LRUCache<>(capacity);
    }
}
