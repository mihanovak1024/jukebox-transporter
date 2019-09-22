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
    public GoogleSheetData parseData(List data) throws GoogleSheetParserException {
        if (!Util.isDataOfTypeString(data)) {
            throw new GoogleSheetParserException(GoogleSheetParserException.NOT_OF_TYPE_STRING);
        }
        List<String> stringData = Util.replaceEmptyStringsWithNull(data);

        // Number of columns + added index field
        if (stringData.size() != GOOGLE_SHEET_NUMBER_OF_COLUMNS + 1) {
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
        if (Util.isNonEmpty(allUrlsListString)) {
            allUrlList = new ArrayList<>(Arrays.asList(allUrlsListString.split(",")));
        }

        String index = stringData.get(stringData.size() - 1);

        return new GoogleSheetData(
                status,
                artist,
                song,
                title,
                url,
                directory,
                allUrlList,
                Integer.parseInt(index)
        );
    }

    @Override
    public List reverseParseData(GoogleSheetData googleSheetData) {
        List<String> googleSheetDataParsedList = new ArrayList<>();

        googleSheetDataParsedList.add(GOOGLE_SHEET_COLUMN_ARTIST, googleSheetData.getArtist());
        googleSheetDataParsedList.add(GOOGLE_SHEET_COLUMN_SONG, googleSheetData.getSong());
        googleSheetDataParsedList.add(GOOGLE_SHEET_COLUMN_TITLE, googleSheetData.getYoutubeVideoTitle());
        googleSheetDataParsedList.add(GOOGLE_SHEET_COLUMN_URL, googleSheetData.getYoutubeVideoUrl());
        googleSheetDataParsedList.add(GOOGLE_SHEET_COLUMN_DIRECTORY, googleSheetData.getDirectory());

        StringBuilder youtubeUrlListStringBuilder = new StringBuilder();
        List<String> allYoutubeVideoUrls = googleSheetData.getAllYoutubeVideoUrls();
        for (int index = 0; index < allYoutubeVideoUrls.size(); index++) {
            String youtubeVideoUrl = allYoutubeVideoUrls.get(index);
            youtubeUrlListStringBuilder.append(youtubeVideoUrl);
            if (index < allYoutubeVideoUrls.size() - 1) {
                youtubeUrlListStringBuilder.append(",");
            }
        }

        googleSheetDataParsedList.add(GOOGLE_SHEET_COLUMN_URL_LIST, youtubeUrlListStringBuilder.toString());
        googleSheetDataParsedList.add(GOOGLE_SHEET_COLUMN_STATUS, googleSheetData.getGoogleSheetStatus().name());

        // Google sheet columns - index field
        if (googleSheetDataParsedList.size() != GOOGLE_SHEET_NUMBER_OF_COLUMNS) {
            throw new GoogleSheetParserException(GoogleSheetParserException.INCORRECT_COLUMN_SIZE);
        }
        return googleSheetDataParsedList;
    }
}
