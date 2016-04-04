package application.musicplayer.muse.tabs;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.app.Activity;
import android.content.ContentUris;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import application.musicplayer.muse.MainActivity;
import application.musicplayer.muse.OnSwipeTouchListener;
import application.musicplayer.muse.Song;
import application.musicplayer.muse.SongAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.net.Uri;
import android.content.ContentResolver;
import android.database.Cursor;
import android.widget.ListView;
import android.widget.Toast;

import application.musicplayer.muse.R;

public class Tab3 extends Fragment  {

    //query external audio
    //ContentResolver musicResolver = getActivity().getContentResolver();

    private static Tab3 t3;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.tab_3,container,false);
        MainActivity.songView = (ListView)v.findViewById(R.id.song_list);

        //create and set adapter
        SongAdapter songAdt = new SongAdapter(v.getContext(), MainActivity.songList);
        MainActivity.songView.setAdapter(songAdt);

        return v;
    }

    public static ContentResolver getContentResolver2()
    {
        return t3.getActivity().getContentResolver();
    }

}

