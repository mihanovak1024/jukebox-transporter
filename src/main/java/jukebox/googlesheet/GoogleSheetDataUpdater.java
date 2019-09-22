package jukebox.googlesheet;

import jukebox.network.NetworkDataUpdater;

public class GoogleSheetDataUpdater implements NetworkDataUpdater {

    private GoogleSheetConnector googleSheetConnector;

    public GoogleSheetDataUpdater(GoogleSheetConnector googleSheetConnector) {
        this.googleSheetConnector = googleSheetConnector;
    }

    @Override
    public void updateData() {

    }

    @Override
    public void saveSongDetailsToBacklog() {

    }
}
