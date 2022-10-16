import org.jetbrains.annotations.Nullable;

import java.util.*;

public class LRUCache<K, V> extends AbstractCache<K, V> {
    private final Map<K, Node<K, V>> cache;
    private final Node<K, V> head;
    private final Node<K, V> tail;

    public LRUCache(int capacity) {
        super(capacity);

        this.cache = new HashMap<>();
        this.head = Node.dummy();
        this.tail = Node.dummy();

        head.next = tail;
        tail.prev = head;
    }

    @Override
    @Nullable V doGet(K key) {
        Node<K, V> node = cache.get(key);
        if (node == null) {
            return null;
        }

        removeNode(node);
        addNode(node.key, node.value);
        validateLRU(key);

        return node.value;
    }

    @Override
    void doPut(K key, V value) {
        Node<K, V> node = cache.get(key);
        if (node != null) {
            removeNode(node);
        }

        if (evictionNeeded()) {
            removeNode(tail.prev);
        }

        addNode(key, value);
        validateLRU(key);
    }

    @Override
    int getSize() {
        return cache.size();
    }

    private boolean evictionNeeded() {
        return cache.size() == capacity();
    }

    private void removeNode(Node<K, V> node) {
        Node<K, V> next = node.next;
        Node<K, V> prev = node.prev;

        cache.remove(node.key);

        next.prev = prev;
        prev.next = next;
    }

    private void addNode(K key, V value) {
        Node<K, V> afterNewNode = head.next;
        Node<K, V> newNode = new Node<>(key, value, head, afterNewNode);

        afterNewNode.prev = newNode;
        head.next = newNode;

        cache.put(key, newNode);
    }

    private void validateLRU(K key) {
        if (head.next.key != key) {
            throw new IllegalStateException("Recently used key should be at head");
        }
    }

    private static class Node<K, V> {
        private final K key;
        private final V value;
        private Node<K, V> prev;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> prev, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.prev = prev;
            this.next = next;
        }

        public  static <K, V> Node<K, V> dummy() {
            return new Node<>(null, null, null, null);
        }
    }
}
