package jukebox.youtube;

import java.util.List;

public class YoutubeSearchInfo {
    private final String artist;
    private final String song;
    private final List<String> previousLinks;

    private YoutubeSearchInfo(YoutubeSearchInfoBuilder builder) {
        this.artist = builder.artist;
        this.song = builder.song;
        this.previousLinks = builder.previousLinks;
    }

    public List<String> getPreviousLinks() {
        return previousLinks;
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
        private List<String> previousLinks;

        public YoutubeSearchInfoBuilder artist(String artist) {
            this.artist = artist;
            return this;
        }

        public YoutubeSearchInfoBuilder song(String song) {
            this.song = song;
            return this;
        }

        public YoutubeSearchInfoBuilder previousLinks(List<String> previousLinks) {
            this.previousLinks = previousLinks;
            return this;
        }

        public YoutubeSearchInfo build() {
            return new YoutubeSearchInfo(this);
        }
    }
}
