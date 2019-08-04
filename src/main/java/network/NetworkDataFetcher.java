package network;

public interface NetworkDataFetcher {

    <T> void fetchDataAsync(String url, NetworkDataCallback<T> callback);

    <T> T fetchData(String url);
}
