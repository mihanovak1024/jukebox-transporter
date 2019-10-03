package jukebox.youtube.song;

import java.io.File;

public class YoutubeSongData {

    private String localSongLocationPath;
    private File localSongFile;

    public YoutubeSongData(String localSongLocationPath, File localSongFile) {
        this.localSongLocationPath = localSongLocationPath;
        this.localSongFile = localSongFile;
    }

    public String getLocalSongLocationPath() {
        return localSongLocationPath;
    }

    public File getLocalSongFile() {
        return localSongFile;
    }
}
