package application.musicplayer.muse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Davis on 4/4/2016.
 */
public class PlaylistAdapter extends BaseAdapter {
    private ArrayList<Playlist> playlists;
    private LayoutInflater playLInf;

    public PlaylistAdapter(Context c, ArrayList<Playlist> pl)
    {
        playlists = pl;
        playLInf = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return playlists.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //map to song layout
        LinearLayout playLay = (LinearLayout)playLInf.inflate
                (R.layout.playlist, parent, false);
        //get title and artist views
        TextView playlistView = (TextView)playLay.findViewById(R.id.playlist_title);
        //get song using position
        Playlist currPlayL = playlists.get(position);
        //get title and artist strings
        playlistView.setText(currPlayL.name);
        //set position as tag
        playLay.setTag(position);
        return playLay;
    }
}
