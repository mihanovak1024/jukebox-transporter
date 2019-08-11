package network;

public interface DataParser<T, V> {

    T parseData(V data);

}
