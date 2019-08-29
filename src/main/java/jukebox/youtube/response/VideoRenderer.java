package jukebox.youtube.response;

public class VideoRenderer {

    private String videoId;
    private Title title;

    public VideoRenderer() {
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }
}
