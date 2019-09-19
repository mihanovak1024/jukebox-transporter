package jukebox.googlesheet;

import com.google.common.annotations.VisibleForTesting;
import jukebox.Util;
import jukebox.network.DataParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class GoogleSheetDataParser implements DataParser<GoogleSheetData, List> {
    @VisibleForTesting
    static final int GOOGLE_SHEET_NUMBER_OF_COLUMNS = 7;

    @VisibleForTesting
    static final int GOOGLE_SHEET_COLUMN_ARTIST = 0;
    @VisibleForTesting
    static final int GOOGLE_SHEET_COLUMN_SONG = 1;
    @VisibleForTesting
    static final int GOOGLE_SHEET_COLUMN_TITLE = 2;
    @VisibleForTesting
    static final int GOOGLE_SHEET_COLUMN_URL = 3;
    @VisibleForTesting
    static final int GOOGLE_SHEET_COLUMN_DIRECTORY = 4;
    @VisibleForTesting
    static final int GOOGLE_SHEET_COLUMN_URL_LIST = 5;
    @VisibleForTesting
    static final int GOOGLE_SHEET_COLUMN_STATUS = 6;

    @Override
    public GoogleSheetData parseData(List data) throws RuntimeException {
        if (!isDataOfTypeString(data)) {
            throw new GoogleSheetParserException(GoogleSheetParserException.NOT_OF_TYPE_STRING);
        }
        List<String> stringData = replaceEmptyStringsWithNull(data);

        if (stringData.size() != GOOGLE_SHEET_NUMBER_OF_COLUMNS) {
            throw new GoogleSheetParserException(GoogleSheetParserException.INCORRECT_COLUMN_SIZE);
        }

        String song = stringData.get(GOOGLE_SHEET_COLUMN_SONG);
        if (song == null) {
            throw new GoogleSheetParserException(GoogleSheetParserException.SONG_NULL);
        }

        String artist = stringData.get(GOOGLE_SHEET_COLUMN_ARTIST);
        String title = stringData.get(GOOGLE_SHEET_COLUMN_TITLE);
        String url = stringData.get(GOOGLE_SHEET_COLUMN_URL);
        String directory = stringData.get(GOOGLE_SHEET_COLUMN_DIRECTORY);

        String statusString = stringData.get(GOOGLE_SHEET_COLUMN_STATUS);
        GoogleSheetStatus status = GoogleSheetStatus.getStatusFromName(statusString);

        String allUrlsListString = stringData.get(GOOGLE_SHEET_COLUMN_URL_LIST);
        List<String> allUrlList = new ArrayList<>();
        if (!Util.isNullOrEmpty(allUrlsListString)) {
            allUrlList = new ArrayList<>(Arrays.asList(allUrlsListString.split(",")));
        }

        return new GoogleSheetData(
                status,
                artist,
                song,
                title,
                url,
                directory,
                allUrlList
        );
    }

    private boolean isDataOfTypeString(List data) {
        boolean isOfTypeString = true;
        for (Object value : data) {
            if (!(value instanceof String)) {
                isOfTypeString = false;
                break;
            }
        }
        return isOfTypeString;
    }

    private List<String> replaceEmptyStringsWithNull(List<String> stringList) {
        List<String> newStringList = new ArrayList<>(stringList.size());
        for (String string : stringList) {
            String nullOrNonEmptyString = changeToNullIfEmpty(string);
            newStringList.add(nullOrNonEmptyString);
        }
        return newStringList;
    }

    private String changeToNullIfEmpty(String string) {
        if (string != null && string.length() == 0) {
            return null;
        }
        return string;
    }
}
