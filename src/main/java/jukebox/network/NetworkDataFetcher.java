package jukebox.network;

import java.util.concurrent.ExecutorService;

public interface NetworkDataFetcher<T> {

    void fetchDataAsync(String url, NetworkDataCallback<T> callback, ExecutorService executorService);

    T fetchData(String url);
}
