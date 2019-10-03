package jukebox.youtube.search.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class YoutubeSearchResponse {

    @JsonProperty("contents")
    private RootContents rootContents;

    public YoutubeSearchResponse() {
    }

    public RootContents getRootContents() {
        return rootContents;
    }

    public void setRootContents(RootContents rootContents) {
        this.rootContents = rootContents;
    }
}
