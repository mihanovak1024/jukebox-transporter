package jukebox.googlesheet;

import java.util.List;

public class GoogleSheetData {

    private final int index;
    private final GoogleSheetStatus googleSheetStatus;
    private final String artist;
    private final String song;
    private final String youtubeVideoTitle;
    private final String youtubeVideoUrl;
    private final String directory;
    private final List<String> allYoutubeVideoUrls;

    public GoogleSheetData(GoogleSheetStatus googleSheetStatus, String artist, String song, String youtubeVideoTitle, String youtubeVideoUrl, String directory, List<String> allYoutubeVideoUrls, int index) {
        this.googleSheetStatus = googleSheetStatus;
        this.artist = artist;
        this.song = song;
        this.youtubeVideoTitle = youtubeVideoTitle;
        this.youtubeVideoUrl = youtubeVideoUrl;
        this.directory = directory;
        this.allYoutubeVideoUrls = allYoutubeVideoUrls;
        this.index = index;
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

    public String getYoutubeVideoUrl() {
        return youtubeVideoUrl;
    }

    public String getDirectory() {
        return directory;
    }

    public List<String> getAllYoutubeVideoUrls() {
        return allYoutubeVideoUrls;
    }

    public int getIndex() {
        return index;
    }
}
