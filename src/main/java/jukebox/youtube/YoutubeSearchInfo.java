package jukebox.youtube;

import java.util.ArrayList;
import java.util.List;

public class YoutubeSearchInfo {
    private final String artist;
    private final String song;
    private final List<String> previousUrls;

    private YoutubeSearchInfo(YoutubeSearchInfoBuilder builder) {
        this.artist = builder.artist;
        this.song = builder.song;
        this.previousUrls = builder.previousUrls;
    }

    public List<String> getPreviousUrls() {
        return previousUrls;
    }

    public String getArtist() {
        return artist;
    }

    public String getSong() {
        return song;
    }

    public static class YoutubeSearchInfoBuilder {
        private String artist;
        private String song;
        private List<String> previousUrls = new ArrayList<>();

        public YoutubeSearchInfoBuilder artist(String artist) {
            this.artist = artist;
            return this;
        }

        public YoutubeSearchInfoBuilder song(String song) {
            this.song = song;
            return this;
        }

        public YoutubeSearchInfoBuilder previousUrls(List<String> previousUrls) {
            this.previousUrls = previousUrls;
            return this;
        }

        public YoutubeSearchInfo build() {
            return new YoutubeSearchInfo(this);
        }
    }
}
