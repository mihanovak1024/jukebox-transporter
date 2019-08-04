package youtube;

import java.util.ArrayList;
import java.util.List;

public class YoutubeSearchInfo {
    private final String artist;
    private final String song;
    private final List<String> previousLinks; // TODO: 2019-08-04 or just video ids?

    public YoutubeSearchInfo(String artist, String song) {
        this(artist, song, new ArrayList<String>());
    }

    public YoutubeSearchInfo(String artist, String song, List<String> previousLinks) {
        this.artist = artist;
        this.song = song;
        this.previousLinks = previousLinks;
    }

    public List<String> getPreviousLinks() {
        return previousLinks;
    }

    public String getSearchQuery() {
        return createSearchQuery();
    }

    private String createSearchQuery() {
        String searchQuery = "";
        if (artist != null) {
            searchQuery = artist;
        }
        searchQuery += song;
        return searchQuery;
    }
}
