public class WPair<K, V> {

    private K key;
    private V value;

    public WPair(K o1, V o2) {
        this.key = o1;
        this.value = o2;
    }

    public final K getKey() {
        return this.key;
    }

    public final V getVal() {
        return this.value;
    }

    public int compareTo(WPair p2) {
        return this.getKey().toString().compareTo(p2.getKey().toString());
    }

}
