package jukebox.youtube;

class YoutubeSearchParserException extends RuntimeException {
    static final String SEARCH_JSON_RESPONSE_PARSE_ERROR = "Error while parsing search Json response.";

    YoutubeSearchParserException(String message) {
        super(message);
    }
}
