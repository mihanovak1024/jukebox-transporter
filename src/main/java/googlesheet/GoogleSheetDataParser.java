package googlesheet;

import network.DataParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class GoogleSheetDataParser implements DataParser<GoogleSheetData, List> {

    private static final int GOOGLE_SHEET_NUMBER_OF_COLUMNS = 7;

    private static final int GOOGLE_SHEET_COLUMN_ARTIST = 0;
    private static final int GOOGLE_SHEET_COLUMN_SONG = 1;
    private static final int GOOGLE_SHEET_COLUMN_TITLE = 2;
    private static final int GOOGLE_SHEET_COLUMN_LINK = 3;
    private static final int GOOGLE_SHEET_COLUMN_DIRECTORY = 4;
    private static final int GOOGLE_SHEET_COLUMN_STATUS = 5;
    private static final int GOOGLE_SHEET_COLUMN_LINK_LIST = 6;

    @Override
    public GoogleSheetData parseData(List data) {
        if (!isDataOfTypeString(data)) {
            throw new GoogleSheetParserException(GoogleSheetParserException.NOT_OF_TYPE_STRING);
        }
        List<String> stringData = replaceEmptyStringsWithNull(data);

        if (stringData.size() != GOOGLE_SHEET_NUMBER_OF_COLUMNS) {
            throw new GoogleSheetParserException(GoogleSheetParserException.INCORRECT_COLUMN_SIZE);
        }

        String artist = stringData.get(GOOGLE_SHEET_COLUMN_ARTIST);
        String song = stringData.get(GOOGLE_SHEET_COLUMN_SONG);
        String title = stringData.get(GOOGLE_SHEET_COLUMN_TITLE);
        String link = stringData.get(GOOGLE_SHEET_COLUMN_LINK);
        String directory = stringData.get(GOOGLE_SHEET_COLUMN_DIRECTORY);

        String statusString = stringData.get(GOOGLE_SHEET_COLUMN_STATUS);
        GoogleSheetStatus status = GoogleSheetStatus.getStatusFromName(statusString);

        String allLinksListString = stringData.get(GOOGLE_SHEET_COLUMN_LINK_LIST);
        final List<String> allLinkList;
        if (allLinksListString == null) {
            allLinkList = new ArrayList<>();
        } else {
            allLinkList = Arrays.asList(allLinksListString.split(","));
        }
        if (link != null) {
            allLinkList.add(0, link);
        }

        return new GoogleSheetData(
                status,
                artist,
                song,
                title,
                link,
                directory,
                allLinkList
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
