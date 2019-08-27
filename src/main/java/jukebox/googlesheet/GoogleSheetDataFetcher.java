package jukebox.googlesheet;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import jukebox.LocalProperties;
import jukebox.Util;
import jukebox.network.DataParser;
import jukebox.network.NetworkDataCallback;
import jukebox.network.NetworkDataFetcher;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class GoogleSheetDataFetcher implements NetworkDataFetcher<List<GoogleSheetData>> {
    private static final String APPLICATION_NAME = "JukeboxTransporter";
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private DataParser<GoogleSheetData, List> dataParser;
    private LocalProperties localProperties;

    public GoogleSheetDataFetcher(DataParser<GoogleSheetData, List> dataParser, LocalProperties localProperties) {
        this.dataParser = dataParser;
        this.localProperties = localProperties;
    }

    public void fetchDataAsync(String url, NetworkDataCallback<List<GoogleSheetData>> callback, ExecutorService executorService) {
        executorService.execute(() -> {
            try {
                List<GoogleSheetData> googleSheetData = fetchGoogleSheetData(url);
                callback.onDataReceived(googleSheetData);
            } catch (GeneralSecurityException | IOException e) {
                e.printStackTrace();
                callback.onFailure(e.getClass().getSimpleName());
            }
        });
    }

    public List<GoogleSheetData> fetchData(String url) {
        List<GoogleSheetData> googleSheetDataList = null;
        try {
            googleSheetDataList = fetchGoogleSheetData(url);
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
        return googleSheetDataList;

    }

    private List<GoogleSheetData> fetchGoogleSheetData(String url) throws GeneralSecurityException, IOException {
        List<GoogleSheetData> googleSheetDataList = null;
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final String spreadsheetId = localProperties.getSpreadsheetId();
        final String range = localProperties.getSpreadsheetRange();
        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();
        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
        } else {
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
        }
        return googleSheetDataList;
    }

    private Credential getCredentials(final NetHttpTransport netHttpTransport) throws IOException {
        InputStream in = Util.readFromLocalResourceToInputStream(CREDENTIALS_FILE_PATH);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                netHttpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

}
