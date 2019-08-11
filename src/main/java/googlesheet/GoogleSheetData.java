package googlesheet;

import java.util.List;

public class GoogleSheetData {

    private final GoogleSheetStatus googleSheetStatus;
    private final String artist;
    private final String song;
    private final String youtubeVideoTitle;
    private final String youtubeVideoLink;
    private final String directory;
    private final List<String> allYoutubeVideoLinks;

    public GoogleSheetData(GoogleSheetStatus googleSheetStatus, String artist, String song, String youtubeVideoTitle, String youtubeVideoLink, String directory, List<String> allYoutubeVideoLinks) {
        this.googleSheetStatus = googleSheetStatus;
        this.artist = artist;
        this.song = song;
        this.youtubeVideoTitle = youtubeVideoTitle;
        this.youtubeVideoLink = youtubeVideoLink;
        this.directory = directory;
        this.allYoutubeVideoLinks = allYoutubeVideoLinks;
    }

    public GoogleSheetStatus getGoogleSheetStatus() {
        return googleSheetStatus;
    }

    public String getArtist() {
        return artist;
    }

    public String getSong() {
        return song;
    }

    public String getYoutubeVideoTitle() {
        return youtubeVideoTitle;
    }

    public String getYoutubeVideoLink() {
        return youtubeVideoLink;
    }

    public String getDirectory() {
        return directory;
    }

    public List<String> getAllYoutubeVideoLinks() {
        return allYoutubeVideoLinks;
    }
}
