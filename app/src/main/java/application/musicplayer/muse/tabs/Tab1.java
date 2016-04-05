package application.musicplayer.muse.tabs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import android.content.Context;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.view.View.OnClickListener;
import android.graphics.Color;


import application.musicplayer.muse.MainActivity;
import application.musicplayer.muse.Playlist;
import application.musicplayer.muse.PlaylistAdapter;
import application.musicplayer.muse.R;
import application.musicplayer.muse.Song;
import application.musicplayer.muse.SongAdapter;



public class Tab1 extends Fragment{
    //if we dont use anywhere else get rid of static
    public static ListView listView;
    private Playlist tempPlaylist;
    private Context context;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.tab_1, container, false);
        context = v.getContext();

        ImageButton buttonClose = (ImageButton) v.findViewById(R.id.cancel);
        buttonClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                    ImageButton ib = (ImageButton) v.findViewById(R.id.addPL);
                    ib.setVisibility(View.VISIBLE);

                    ImageButton ib2 = (ImageButton) v.findViewById(R.id.next);
                    ib2.setVisibility(View.INVISIBLE);

                    ImageButton ib3 = (ImageButton) v.findViewById(R.id.cancel);
                    ib3.setVisibility(View.INVISIBLE);

                    MainActivity.tab1CreatePlaylistMode = false;
            }
        });

        ImageButton buttonAdd = (ImageButton) v.findViewById(R.id.next);
        buttonAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.tab1CreatePlaylistMode = true;

                // components from main.xml
                ImageButton button = (ImageButton) v.findViewById(R.id.next);

                // add button listener
                button.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {

                        // get prompts.xml view
                        LayoutInflater li = LayoutInflater.from(v.getContext());
                        View promptsView = li.inflate(R.layout.prompts, null);

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                v.getContext());

                        // set prompts.xml to alertdialog builder
                        alertDialogBuilder.setView(promptsView);

                        final EditText userInput = (EditText) promptsView
                                .findViewById(R.id.editTextDialogUserInput);

                        // set dialog message
                        alertDialogBuilder
                                .setCancelable(false)
                                .setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                savePlaylist(tempPlaylist);
                                                MainActivity.playlists.add(tempPlaylist);

                                                MainActivity.songList = tempPlaylist;

                                                MainActivity.tab1CreatePlaylistMode = false;

                                                ImageButton ib = (ImageButton) v.findViewById(R.id.addPL);
                                                ib.setVisibility(View.VISIBLE);

                                                ImageButton ib2 = (ImageButton) v.findViewById(R.id.next);
                                                ib2.setVisibility(View.INVISIBLE);

                                                ImageButton ib3 = (ImageButton) v.findViewById(R.id.cancel);
                                                ib3.setVisibility(View.INVISIBLE);

                                                MainActivity.pager.setCurrentItem(3);
                                            }
                                        })
                                .setNegativeButton("Cancel",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });

                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        // show it
                        alertDialog.show();

                    }
                });

                CreatePlaylistMode(v);
            }
        });

        return v;
    }

    public void refreshView(final View v)
    {
        BaseAdapter itemsAdapter;
        if (!MainActivity.tab1CreatePlaylistMode ){
            itemsAdapter = new PlaylistAdapter(v.getContext(), MainActivity.playlists);
            listView = (ListView) v.findViewById(R.id.listView);
            listView.setAdapter(itemsAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                     public void onItemClick(AdapterView<?> parent, View clickView, int position, long id) {
                                                         MainActivity.songList = (Playlist)listView.getItemAtPosition(position);
                                                         MainActivity.pager.setCurrentItem(3);
                                                     }
                                                 }
            );
        } else {
            itemsAdapter = new SongAdapter(v.getContext(), MainActivity.defaultSongList);
            listView = (ListView) v.findViewById(R.id.listView);
            listView.setAdapter(itemsAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                 public void onItemClick(AdapterView<?> parent, View clickView, int position, long id) {
                     Song song = (Song)listView.getItemAtPosition(position);
                     boolean s = true;
                     //CHECK THIS OUT L8R
                     for (int i = 0; i < tempPlaylist.songList.size(); i++) {
                         if (tempPlaylist.songList.contains(song)) {
                             listView.getChildAt(position).setBackgroundColor(Color.TRANSPARENT);
                             tempPlaylist.songList.remove(i);
                             s = false;
                         }
                     }
                     if (s) {
                         tempPlaylist.songList.add(song);
                         listView.getChildAt(position).setBackgroundColor(Color.GRAY);
                     }
                 }
             });
        }
    }

    public void CreatePlaylistMode(View v){

        ImageButton ib = (ImageButton)v.findViewById(R.id.addPL);
        ib.setVisibility(View.INVISIBLE);

        ImageButton ib2 = (ImageButton)v.findViewById(R.id.next);
        ib2.setVisibility(View.VISIBLE);

        ImageButton ib3 = (ImageButton)v.findViewById(R.id.cancel);
        ib3.setVisibility(View.VISIBLE);
    }


    private void savePlaylist(Playlist pl) {

        try
        {
            FileOutputStream fos = context.openFileOutput( "/data/data/application.musicplayer.muse/files/" + pl.name + ".txt", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(pl);
            os.close();
            fos.close();
        }
        catch(IOException i)
        {
            i.printStackTrace();
        }
    }
}

