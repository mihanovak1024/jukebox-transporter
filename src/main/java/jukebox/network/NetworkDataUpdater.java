package jukebox.network;

public interface NetworkDataUpdater<T> {

    void updateData(T dataToUpdate);

    void saveSongDetailsToBacklog();
}
