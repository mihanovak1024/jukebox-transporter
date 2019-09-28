package jukebox.network;

import java.util.List;

public interface NetworkDataUpdater<T> {

    void updateData(T dataToUpdate);

    void updateBacklog(List<T> dataList);
}
