package application.musicplayer.muse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ben on 4/4/2016.
 */
public class Playlist implements Serializable {

    public String name;

    public List<Song> songList;

    public Playlist() {
        songList = new ArrayList<Song>();
    }

    public Playlist(String name){
        this.name = name;
        songList = new ArrayList<Song>();
    }
    
    public void add(Song s) {
        songList.add(s);
    }

    public Playlist merge(Playlist pl) {
        this.songList.addAll(pl.songList);
        return this;
    }
}
