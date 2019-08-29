package jukebox.youtube.response;

import java.util.List;

public class ItemSectionContents {

    private List<VideoRenderer> videoRenderer;

    public ItemSectionContents() {
    }

    public List<VideoRenderer> getVideoRenderer() {
        return videoRenderer;
    }

    public void setVideoRenderer(List<VideoRenderer> videoRenderer) {
        this.videoRenderer = videoRenderer;
    }
}
