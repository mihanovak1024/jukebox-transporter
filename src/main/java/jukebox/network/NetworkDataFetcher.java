package jukebox.network;

import java.util.concurrent.ExecutorService;

public interface NetworkDataFetcher<V, T> {

    void fetchDataAsync(V requestData, NetworkDataCallback<T> callback, ExecutorService executorService);

    T fetchData(V requestData);
}
