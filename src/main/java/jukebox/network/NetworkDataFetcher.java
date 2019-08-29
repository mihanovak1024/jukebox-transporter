package jukebox.network;

import java.util.concurrent.ExecutorService;

public interface NetworkDataFetcher<T> {

    void fetchDataAsync(NetworkDataCallback<T> callback, ExecutorService executorService);

    T fetchData();
}
