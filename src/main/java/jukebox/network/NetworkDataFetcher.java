package jukebox.network;

public interface NetworkDataFetcher<T> {

    void fetchDataAsync(String url, NetworkDataCallback<T> callback);

    T fetchData(String url);
}
