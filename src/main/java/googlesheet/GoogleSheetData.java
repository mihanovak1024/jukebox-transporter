package googlesheet;

import java.util.List;

public class GoogleSheetData {

    private GoogleSheetStatus googleSheetStatus;
    private String artist;
    private String song;
    private String youtubeVideoTitle;
    private String youtubeVideoLink;
    private List<String> allYoutubeVideoLinks;

    public GoogleSheetStatus getGoogleSheetStatus() {
        return googleSheetStatus;
    }

    public void setGoogleSheetStatus(GoogleSheetStatus googleSheetStatus) {
        this.googleSheetStatus = googleSheetStatus;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getYoutubeVideoTitle() {
        return youtubeVideoTitle;
    }

    public void setYoutubeVideoTitle(String youtubeVideoTitle) {
        this.youtubeVideoTitle = youtubeVideoTitle;
    }

    public List<String> getAllYoutubeVideoLinks() {
        return allYoutubeVideoLinks;
    }

    public void setAllYoutubeVideoLinks(List<String> allYoutubeVideoLinks) {
        this.allYoutubeVideoLinks = allYoutubeVideoLinks;
    }
}
