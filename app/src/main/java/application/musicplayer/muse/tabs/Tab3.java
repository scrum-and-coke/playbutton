package application.musicplayer.muse.tabs;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import application.musicplayer.muse.MainActivity;
import application.musicplayer.muse.Song;
import application.musicplayer.muse.SongAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.net.Uri;
import android.content.ContentResolver;
import android.database.Cursor;
import android.widget.ListView;

import application.musicplayer.muse.R;

public class Tab3 extends Fragment  {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_3,container,false);
        //retrieve list view
        MainActivity.songView = (ListView)v.findViewById(R.id.song_list);
        //instantiate list
        MainActivity.songList = new ArrayList<Song>();
        //get songs from device
        //contextOfApplication = Tab3.getContextOfApplication();
        if(MainActivity.loading_play == false) {
            getSongList();
        }else{
            getSongList(MainActivity.songListTempHold);
            MainActivity.songList = MainActivity.songListTemp;
            MainActivity.songListTemp = new ArrayList<Song>();
        }

        MainActivity.loading_play = false;
        //sort alphabetically by title
        Collections.sort(MainActivity.songList, new Comparator<Song>() {
            public int compare(Song a, Song b) {
                return a.getTitle().compareTo(b.getTitle());
            }
        });
        //create and set adapter
        SongAdapter songAdt = new SongAdapter(v.getContext(), MainActivity.songList);
        MainActivity.songView.setAdapter(songAdt);

        //setup controller
       //MainActivity.setController();
        //MainActivity.setControllerFromMain();
        return v;
    }

    //method to retrieve song info from device
    public void getSongList(){
        //query external audio
        ContentResolver musicResolver = getActivity().getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
        //iterate over results if valid
        if(musicCursor!=null && musicCursor.moveToFirst()){
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                MainActivity.songList.add(new Song(thisId, thisTitle, thisArtist));
            }
            while (musicCursor.moveToNext());
        }
    }
    //overload
    public void getSongList(ArrayList<File> testFile){
        //query external audio
        ContentResolver musicResolver = getActivity().getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
        //iterate over results if valid
        if(musicCursor!=null && musicCursor.moveToFirst()){
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                for(File f : testFile){
               //     String x = f.getName().replace("-", "");
                    if(f.getName().replace(".mp3", "").contains(thisTitle)){
                        MainActivity.songListTemp.add(new Song(thisId, thisTitle, thisArtist));
                        break;
                    }
                }
               // MainActivity.songList.add(new Song(thisId, thisTitle, thisArtist));
            }
            while (musicCursor.moveToNext());

        }
        MainActivity.songListTempHold = new ArrayList<File>();
    }

}

