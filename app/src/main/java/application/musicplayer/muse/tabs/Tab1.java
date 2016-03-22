package application.musicplayer.muse.tabs;

import android.app.AlertDialog;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import android.os.IBinder;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.graphics.Color;
import android.widget.TextView;


import application.musicplayer.muse.MainActivity;
import application.musicplayer.muse.R;
import application.musicplayer.muse.Song;
import application.musicplayer.muse.SongAdapter;

class SongInfo {
    String Album = "";
    String Artist = "";
    String Genre = "";
    String Year = "";

}

public class Tab1 extends Fragment{
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.tab_1,container,false);



        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_list_item_1, MainActivity.playList1);
        MainActivity.listView = (ListView) v.findViewById(R.id.listView);
        MainActivity.listView.setAdapter(itemsAdapter);
        MainActivity.listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View clickView,
                                    int position, long id) {

                String text = (String) (MainActivity.listView.getItemAtPosition(position));
                if (MainActivity.cPlaylistMode == true) {

                    MainActivity.checkedPlaylist.add(text + ".mp3");

                } else if (MainActivity.cPlaylistMode == false && MainActivity.loading_play == false){
                    File f = new File("/data/data/application.musicplayer.muse/files/" + text + ".txt");

                    LoadPlaylist(v, f, text);
                } else {

                    try{
                    InputStream in = v.getContext().openFileInput(text + ".txt");

                    if (in != null) {

                        InputStreamReader tmp = new InputStreamReader(in);
                        BufferedReader reader = new BufferedReader(tmp);
                        String str;
                        StringBuilder buf = new StringBuilder();
                        MediaMetadataRetriever metaRetriver;
                        metaRetriver = new MediaMetadataRetriever();
                        while ((str = reader.readLine()) != null) {



                            File test = null;
                            for (File f : MainActivity.song){
                                String x = f.getName();
                                if (f.getName().equals(str)){
                                    test = new File(f.getAbsolutePath());
                                    break;
                                }
                            }

                            MainActivity.songListTempHold.add(test);

                        }

                    }
                        in.close();

                        }catch(Exception ex){
                            System.out.print(ex.getMessage());

                        }


                    }


                }


        });





        return v;
    }

    public ArrayList<String> LoadPlaylist(View v,File file, String name){
        ArrayList<String> mySongsInput = new ArrayList<String>();
        try{

            String STORETEXT=name + ".txt";
            InputStream in = v.getContext().openFileInput(STORETEXT);

            if (in != null) {

                InputStreamReader tmp = new InputStreamReader(in);

                BufferedReader reader = new BufferedReader(tmp);

                String str;

                StringBuilder buf = new StringBuilder();


                while ((str = reader.readLine()) != null) {
//this is where we can read back in the playlist

                    try{


                        mySongsInput.add(str.replace(".mp3", ""));
                    }catch (Exception ex){}
                }


                in.close();

            }
        }catch(Exception ex){
            System.out.print(ex.getMessage());

        }
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_list_item_1, mySongsInput);
        MainActivity.listView = (ListView) v.findViewById(R.id.listView);
        MainActivity.listView.setAdapter(itemsAdapter);
        return mySongsInput;
    }

}
