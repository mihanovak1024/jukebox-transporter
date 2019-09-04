package jukebox.youtube;

public class YoutubeSearchData {

    private String title;
    private String youtubeLink;

    public YoutubeSearchData(String title, String youtubeLink) {
        this.title = title;
        this.youtubeLink = youtubeLink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYoutubeLink() {
        return youtubeLink;
    }

    public void setYoutubeLink(String youtubeLink) {
        this.youtubeLink = youtubeLink;
    }
}
