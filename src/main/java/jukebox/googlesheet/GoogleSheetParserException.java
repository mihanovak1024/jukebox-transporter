package jukebox.googlesheet;

class GoogleSheetParserException extends RuntimeException {
    static final String NOT_OF_TYPE_STRING = "A value in data list is not of type String.";
    static final String INCORRECT_COLUMN_SIZE = "The data list is not the expected size.";
    static final String SONG_NULL = "Song name must not be null.";
    static final String OUT_OF_BOUND_SHEET_RANGE = "Sheet range is out of bound.";

    GoogleSheetParserException(String message) {
        super(message);
    }
}
