package jukebox.network;

public interface DataParser<T, V> {

    T parseData(V data) throws RuntimeException;

    V reverseParseData(T data);

}
