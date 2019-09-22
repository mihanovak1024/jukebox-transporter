package jukebox.googlesheet;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import jukebox.LocalProperties;
import jukebox.network.NetworkDataUpdater;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class GoogleSheetDataUpdater implements NetworkDataUpdater<GoogleSheetData> {

    private GoogleSheetConnector googleSheetConnector;
    private final LocalProperties localProperties;

    public GoogleSheetDataUpdater(GoogleSheetConnector googleSheetConnector, LocalProperties localProperties) {
        this.googleSheetConnector = googleSheetConnector;
        this.localProperties = localProperties;
    }

    @Override
    public void updateData(GoogleSheetData googleSheetData) {
        try {
            updateGoogleSheetCellData(googleSheetData);
        } catch (IOException | GeneralSecurityException e) {
            // TODO: 2019-09-22 handle exception
        }
    }

    @Override
    public void saveSongDetailsToBacklog() {

    }

    private void updateGoogleSheetCellData(GoogleSheetData googleSheetData) throws IOException, GeneralSecurityException {
        Sheets googleSheetService = googleSheetConnector.createConnection();
        final String spreadsheetId = localProperties.getSpreadsheetId();
        final String range = localProperties.getSpreadsheetRange();

        List<List<Object>> valuesToUpdate = getValueListToUpdate(googleSheetData);

        ValueRange body = new ValueRange()
                .setValues(valuesToUpdate);
        // TODO: 2019-09-22 WIP
//        AppendValuesResponse result =
//                googleSheetService.spreadsheets().values().append(spreadsheetId, range, body)
//                        .setValueInputOption(valueInputOption)
//                        .execute();
//        System.out.printf("%d cells appended.", result.getUpdates().getUpdatedCells());
    }

    private List<List<Object>> getValueListToUpdate(GoogleSheetData googleSheetData) {
        // TODO: 2019-09-22 add proper implementation
        ArrayList arrayList = new ArrayList();
        arrayList.add(new ArrayList<>());
        return arrayList;
    }
}
