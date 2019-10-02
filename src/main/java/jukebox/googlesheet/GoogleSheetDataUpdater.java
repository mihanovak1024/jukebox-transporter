package jukebox.googlesheet;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import jukebox.LocalProperties;
import jukebox.network.DataParser;
import jukebox.network.NetworkDataUpdater;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class GoogleSheetDataUpdater implements NetworkDataUpdater<GoogleSheetData> {

    private GoogleSheetConnector googleSheetConnector;
    private DataParser<GoogleSheetData, List> dataParser;
    private final LocalProperties localProperties;

    public GoogleSheetDataUpdater(GoogleSheetConnector googleSheetConnector, DataParser dataParser, LocalProperties localProperties) {
        this.googleSheetConnector = googleSheetConnector;
        this.dataParser = dataParser;
        this.localProperties = localProperties;
    }

    @Override
    public void updateData(GoogleSheetData googleSheetData) {
        try {
            updateGoogleSheetCellData(googleSheetData);
        } catch (IOException | GeneralSecurityException e) {
            // TODO: 2019-09-22 handle exception
            e.printStackTrace();
        }
    }

    @Override
    public void updateBacklog(List<GoogleSheetData> googleSheetDataList) {
        moveDownloadedEntriesToBacklog(googleSheetDataList);
    }

    private void updateGoogleSheetCellData(GoogleSheetData googleSheetData) throws IOException, GeneralSecurityException {
        Sheets googleSheetService = googleSheetConnector.createConnection();
        final String spreadsheetId = localProperties.getSpreadsheetId();
        final String spreadsheetRange = googleSheetConnector.getSpreadsheetRangeFormat(
                localProperties,
                googleSheetData.getIndex(),
                googleSheetData.getIndex()
        );

        List<List<Object>> valuesToUpdate = getValueListToUpdate(googleSheetData);

        ValueRange body = new ValueRange()
                .setValues(valuesToUpdate);

        UpdateValuesResponse result =
                googleSheetService.spreadsheets().values().update(spreadsheetId, spreadsheetRange, body)
                        .setValueInputOption("RAW")
                        .execute();
        System.out.printf("%d cells appended.", result.getUpdatedCells());
    }

    private List<List<Object>> getValueListToUpdate(GoogleSheetData googleSheetData) {
        // TODO: 2019-09-22 add proper implementation
        List singleGoogleSheetItemList = new ArrayList();
        List googleSheetParsedData = dataParser.reverseParseData(googleSheetData);

        singleGoogleSheetItemList.add(googleSheetParsedData);
        return singleGoogleSheetItemList;
    }

    private void moveDownloadedEntriesToBacklog(List<GoogleSheetData> googleSheetDataList) {
        List<GoogleSheetData> downloadedSongs = getDownloadedSongs(googleSheetDataList);
        // TODO: 2019-09-22 remove downloaded song rows
        // TODO: 2019-09-22 update backlog
    }

    private List<GoogleSheetData> getDownloadedSongs(List<GoogleSheetData> googleSheetDataList) {
        List<GoogleSheetData> downloadedSongs = new ArrayList<>();
        for (GoogleSheetData googleSheetData : googleSheetDataList) {
            if (googleSheetData.getGoogleSheetStatus() == GoogleSheetStatus.DOWNLOADED) {
                downloadedSongs.add(googleSheetData);
            }
        }
        return downloadedSongs;
    }
}
