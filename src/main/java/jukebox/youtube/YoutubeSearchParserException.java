package jukebox.youtube;

class YoutubeSearchParserException extends RuntimeException {
    static final String SEARCH_JSON_RESPONSE_PARSE_ERROR = "Error while parsing search Json response.";
    static final String SEARCH_DATA_PARSE_ERROR = "Not all required fields are present in the Json response.";

    YoutubeSearchParserException(String message) {
        super(message);
    }
}
