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

        v.setOnTouchListener(new OnSwipeTouchListener(v.getContext()) {

            @Override
            public void onClick() {
                super.onClick();
                Toast.makeText(v.getContext(), "Click", Toast.LENGTH_SHORT).show();
                // your on click here
            }

            @Override
            public void onDoubleClick() {
                super.onDoubleClick();
                // your on onDoubleClick here
            }

            @Override
            public void onLongClick() {
                super.onLongClick();
                // your on onLongClick here
            }

            @Override
            public void onSwipeUp() {
                super.onSwipeUp();
                Toast.makeText(v.getContext(), "Swipe Up", Toast.LENGTH_SHORT).show();
                // your swipe up here
            }

            @Override
            public void onSwipeDown() {
                super.onSwipeDown();
                // your swipe down here.
            }

            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                // your swipe left here.
            }

            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                // your swipe right here.
            }
        });
        //MainActivity.noShowController.show(0);
//        imageButton = new Button(getActivity());
        return v;
    }


}