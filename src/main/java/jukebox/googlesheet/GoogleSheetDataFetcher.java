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

public class GoogleSheetDataFetcher implements NetworkDataFetcher<Object, List<GoogleSheetData>> {

    private DataParser<GoogleSheetData, List> dataParser;
    private GoogleSheetConnector googleSheetConnector;
    private LocalProperties localProperties;

    public GoogleSheetDataFetcher(DataParser<GoogleSheetData, List> dataParser, GoogleSheetConnector googleSheetConnector, LocalProperties localProperties) {
        this.dataParser = dataParser;
        this.googleSheetConnector = googleSheetConnector;
        this.localProperties = localProperties;
    }

    public void fetchDataAsync(Object data, NetworkDataCallback<List<GoogleSheetData>> callback, ExecutorService executorService) {
        executorService.execute(() -> {
            try {
                List<GoogleSheetData> googleSheetData = fetchGoogleSheetData();
                callback.onDataReceived(googleSheetData);
            } catch (GeneralSecurityException | IOException e) {
                e.printStackTrace();
                callback.onFailure(e.getClass().getSimpleName());
            }
        });
    }

    public List<GoogleSheetData> fetchData(Object data) {
        List<GoogleSheetData> googleSheetDataList = null;
        try {
            googleSheetDataList = fetchGoogleSheetData();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
        return googleSheetDataList;

    }

    /**
     * Creates a request to Google Sheet API, parses the response with {@link GoogleSheetDataParser}
     * and returns the list of parsed response data.
     *
     * @return list of parsed response data for each row of Google Sheet
     * @throws GeneralSecurityException
     * @throws IOException
     */
    private List<GoogleSheetData> fetchGoogleSheetData() throws GeneralSecurityException, IOException {
        List<GoogleSheetData> googleSheetDataList = null;
        final String spreadsheetId = localProperties.getSpreadsheetId();
        final String range = googleSheetConnector.getSpreadsheetRangeFormat(localProperties, GoogleSheetConnector.SPREADSHEET_RANGE_MAX_VERTICAL, 0);

        Sheets googleSheetService = googleSheetConnector.createConnection();
        // Request to the GoogleSheet API
        ValueRange googleSheetResponse = googleSheetService.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();

        List<List<Object>> values = googleSheetResponse.getValues();
        if (Util.isNonEmpty(values)) {
            googleSheetDataList = new ArrayList<>();
            for (int index = 0; index < values.size(); index++) {
                List<Object> row = values.get(index);
                row.add(String.valueOf(index));
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
