package jukebox.network;

public interface NetworkDataCallback<T> {

    void onDataReceived(T data);

    void onFailure(String error);

}
