package jukebox.youtube;

class YoutubeParserException extends RuntimeException {
    static final String SEARCH_JSON_RESPONSE_PARSE_ERROR = "Error while parsing search Json response.";
    static final String SEARCH_DATA_PARSE_ERROR = "Not all required fields are present in the Json response.";
    static final String SEARCH_DATA_VIDEO_RENDERER_PARSE_ERROR = "No appropriate VideoRenderer available.";
    static final String LINK_VIDEO_ID_PARSE_ERROR = "Error while extracting video id from the link.";

    YoutubeParserException(String message) {
        super(message);
    }
}
