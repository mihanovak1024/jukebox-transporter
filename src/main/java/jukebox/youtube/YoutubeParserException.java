package jukebox.youtube;

public class YoutubeParserException extends RuntimeException {
    public static final String SEARCH_QUERY_CREATION_ERROR = "Both artist and song are null.";
    public static final String SEARCH_JSON_RESPONSE_PARSE_ERROR = "Error while parsing search Json response.";
    public static final String SEARCH_DATA_PARSE_ERROR = "Not all required fields are present in the Json response.";
    public static final String SEARCH_DATA_VIDEO_RENDERER_PARSE_ERROR = "No appropriate VideoRenderer available.";
    public static final String URL_VIDEO_ID_PARSE_ERROR = "Error while extracting video id from the url.";

    public YoutubeParserException(String message) {
        super(message);
    }
}
