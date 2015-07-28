package model;

/**
 * Created by GjnSu on 7/17/2015.
 */
public class Song {

    private String songName;
    private String artistName;
    private String duration;
    private String path;

    public Song() {
    }

    public Song(String songName, String artistName, String duration, String path) {
        this.songName = songName;
        this.artistName = artistName;
        this.duration = duration;
        this.path = path;
    }

    @Override
    public String toString() {
        return "Song{" +
                "songName='" + songName + '\'' +
                ", artistName='" + artistName + '\'' +
                ", duration='" + duration + '\'' +
                ", path='" + path + '\'' +
                '}';
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
