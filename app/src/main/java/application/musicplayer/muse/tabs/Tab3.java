package application.musicplayer.muse.tabs;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import application.musicplayer.muse.MainActivity;
import application.musicplayer.muse.SongAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import application.musicplayer.muse.R;

public class Tab3 extends Fragment  {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.tab_3,container,false);
        MainActivity.songView = (ListView)v.findViewById(R.id.song_list);

        //create and set adapter
        SongAdapter songAdt = new SongAdapter(v.getContext(), MainActivity.songList);
        MainActivity.songView.setAdapter(songAdt);

        return v;
    }

}

