package jukebox.youtube.song;

// TODO: 2019-10-07 check if class is threadsafe
public class YoutubeSongDecipherer {

    private YoutubeSongDecipherer instance;

    public YoutubeSongDecipherer getInstance() {
        if (instance == null) {
            instance = new YoutubeSongDecipherer();
        }
        return instance;
    }

    private YoutubeSongDecipherer() {
    }

    public boolean isSongProtected(String videoInfo) {
        // TODO: 2019-10-07 implementation
        return false;
    }

    public String getDecipheredSongInfo(String videoInfo) {
        // TODO: 2019-10-07 implementation 
        return "";
    }

}
