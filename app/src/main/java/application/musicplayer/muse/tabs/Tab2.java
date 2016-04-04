package application.musicplayer.muse.tabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.Toast;

import application.musicplayer.muse.MainActivity;
import application.musicplayer.muse.OnSwipeTouchListener;
import application.musicplayer.muse.R;


public class Tab2 extends Fragment {
    public static Button imageButton;
    //public static View v;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       final View v = inflater.inflate(R.layout.tab_2,container,false);
        //MainActivity.noShowController.show(0);
//        imageButton = new Button(getActivity());
        return v;
    }


}