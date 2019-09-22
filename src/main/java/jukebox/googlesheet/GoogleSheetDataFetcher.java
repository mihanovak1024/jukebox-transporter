package jukebox.googlesheet;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import jukebox.LocalProperties;
import jukebox.Util;
import jukebox.network.DataParser;
import jukebox.network.NetworkDataCallback;
import jukebox.network.NetworkDataFetcher;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class GoogleSheetDataFetcher implements NetworkDataFetcher<LocalProperties, List<GoogleSheetData>> {

    private DataParser<GoogleSheetData, List> dataParser;
    private GoogleSheetConnector googleSheetConnector;

    public GoogleSheetDataFetcher(DataParser<GoogleSheetData, List> dataParser, GoogleSheetConnector googleSheetConnector) {
        this.dataParser = dataParser;
        this.googleSheetConnector = googleSheetConnector;
    }

    public void fetchDataAsync(LocalProperties localProperties, NetworkDataCallback<List<GoogleSheetData>> callback, ExecutorService executorService) {
        executorService.execute(() -> {
            try {
                List<GoogleSheetData> googleSheetData = fetchGoogleSheetData(localProperties);
                callback.onDataReceived(googleSheetData);
            } catch (GeneralSecurityException | IOException e) {
                e.printStackTrace();
                callback.onFailure(e.getClass().getSimpleName());
            }
        });
    }

    public List<GoogleSheetData> fetchData(LocalProperties localProperties) {
        List<GoogleSheetData> googleSheetDataList = null;
        try {
            googleSheetDataList = fetchGoogleSheetData(localProperties);
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
        return googleSheetDataList;

    }

    /**
     * Creates a request to Google Sheet API, parses the response with {@link GoogleSheetDataParser}
     * and returns the list of parsed response data.
     *
     * @param localProperties
     * @return list of parsed response data for each row of Google Sheet
     * @throws GeneralSecurityException
     * @throws IOException
     */
    private List<GoogleSheetData> fetchGoogleSheetData(LocalProperties localProperties) throws GeneralSecurityException, IOException {
        List<GoogleSheetData> googleSheetDataList = null;
        final String spreadsheetId = localProperties.getSpreadsheetId();
        final String range = localProperties.getSpreadsheetRange();

        Sheets googleSheetService = googleSheetConnector.createConnection();
        // Request to the GoogleSheet API
        ValueRange googleSheetResponse = googleSheetService.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();

        List<List<Object>> values = googleSheetResponse.getValues();
        if (Util.isNonEmpty(values)) {
            googleSheetDataList = new ArrayList<>();
            for (List row : values) {
                try {
                    GoogleSheetData googleSheetData = dataParser.parseData(row);
                    googleSheetDataList.add(googleSheetData);
                } catch (RuntimeException e) {
                    // TODO: 2019-08-14 report/statistics for failed parsed data & user notify
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("No data found.");
        }
        return googleSheetDataList;
    }

}
