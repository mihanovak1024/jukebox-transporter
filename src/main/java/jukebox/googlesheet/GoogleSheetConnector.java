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
import jukebox.LocalProperties;
import jukebox.Util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

/**
 * Class responsible for connecting with Google Sheet API.
 */
public class GoogleSheetConnector {
    static final int SPREADSHEET_RANGE_MAX_VERTICAL = -1;
    private static final String APPLICATION_NAME = "JukeboxTransporter";
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /**
     * Method responsible for creating the v4 Google API connection component {@link Sheets}.
     *
     * @return Google Sheet API component
     * @throws GeneralSecurityException
     * @throws IOException
     */
    Sheets createConnection() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    /**
     * Creates the GoogleSheet {@link Credential} component out of local credentials specification file.
     *
     * @param netHttpTransport
     * @return Google Sheet Authorisation component
     * @throws IOException
     */
    private Credential getCredentials(final NetHttpTransport netHttpTransport) throws IOException {
        InputStream in = Util.readFromLocalResourceToInputStream(CREDENTIALS_FILE_PATH);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                netHttpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    String getSpreadsheetRangeFormat(LocalProperties localProperties, int indexEnd, int indexStart) {
        final String rangeName = localProperties.getSpreadsheetName();
        final String rangeFormat = localProperties.getSpreadsheetRangeFormat();
        final int rangeMinVertical = localProperties.getSpreadsheetRangeMinVertical();
        final int rangeMaxHorizontal = localProperties.getSpreadsheetRangeMaxHorizontal();

        if (indexEnd == SPREADSHEET_RANGE_MAX_VERTICAL) {
            indexEnd = rangeMaxHorizontal - rangeMinVertical;
        }

        if (indexEnd < 0 || indexStart < 0) {
            throw new GoogleSheetParserException(GoogleSheetParserException.OUT_OF_BOUND_SHEET_RANGE);
        }

        return String.format(
                rangeFormat,
                rangeName,
                rangeMinVertical + indexStart,
                rangeMinVertical + indexEnd
        );
    }
}
