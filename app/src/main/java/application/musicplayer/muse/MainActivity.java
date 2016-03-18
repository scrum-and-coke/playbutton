package application.musicplayer.muse;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.content.LocalBroadcastManager;
import android.content.BroadcastReceiver;
import application.musicplayer.muse.customViews.ScrimInsetsFrameLayout;
import application.musicplayer.muse.tabs.Tab1;
import application.musicplayer.muse.tabs.Tab3;
import application.musicplayer.muse.utils.UtilsDevice;
import application.musicplayer.muse.utils.UtilsMiscellaneous;
import application.musicplayer.muse.sliding.SlidingTabLayout;
import application.musicplayer.muse.tabs.ViewPagerAdapter;
import application.musicplayer.muse.tabs.Tab2;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import android.os.IBinder;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.widget.ListView;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.graphics.Color;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
class SongInfo {
    String Album = "";
    String Artist = "";
    String Genre = "";
    String Year = "";

}


public class MainActivity extends ActionBarActivity implements MediaPlayerControl {

    // Declaring Your View and Variables
    private SongAdapter songAdapt;
    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    public static ListView listView;
    //CharSequence Titles[]={"PlayLists","MusE","SongList"};
    //int Numboftabs = 3;
    CharSequence Titles[] = {"PlayLists", "MusE", "SongList"};
    int Numboftabs = 3;
    public boolean songPlaying = false;
    //music player
    public static ArrayList<Song> songList;
    public static ArrayList<Song> songListTemp = new ArrayList<Song>();
    public static ListView songView;
    //service
    public static ArrayList<String> playList1 = new ArrayList<String>();
    public static ArrayList<File> playListFileDir = new ArrayList<File>();
    public static ArrayList<String> mySongsName = new ArrayList<String>();
    private MusicService musicSrv;
    public static ArrayList<File> song = new ArrayList<File>();
    private Intent playIntent;
    //binding
    private boolean musicBound = false;
    //controller
    public static MusicController controller;
    //activity and playback pause flags
    private boolean paused = false, playbackPaused = false;
    public static boolean loading_play = false;
public static ArrayList<File> songListTempHold = new ArrayList<File>();

    public Button imageButton;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    public static Boolean cPlaylistMode = false;
    public static ArrayList<String> checkedPlaylist = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init_slider();
        init_navigator();
        //LoadPlaylist();
        File f = new File("/data/data/application.musicplayer.muse/files");
        playListFileDir = findPlaylist(f);
        for (File file : playListFileDir) {
            playList1.add(file.getName());
        }
        ArrayList<String> tempList = new ArrayList<String>();
        for (String str : playList1) {
            str = str.replace(".txt", "");
            tempList.add(str);

        }
        song = findSongs(Environment.getExternalStorageDirectory());
        playList1 = tempList;


        /*imageButton = (Button)this.findViewById(R.id.shuffleButtonSelector);
        imageButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                musicSrv.setShuffle();
                //Toast.makeText(MainActivity.this,
                //      "ImageButton (selector) is clicked!",
                //    Toast.LENGTH_SHORT).show();

            }



        });*/

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
    /* public void addListenerOnButton() {
         Tab2.imageButton.setOnClickListener(new OnClickListener() {

             @Override
             public void onClick(View arg0) {
                 musicSrv.setShuffle();
                 //Toast.makeText(MainActivity.this,
                 //      "ImageButton (selector) is clicked!",
                 //    Toast.LENGTH_SHORT).show();

             }
         });


    }*/
    //Broadcast receiver to determine when music player has been prepared
    private BroadcastReceiver onPrepareReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent i) {
            // When music player has been prepared, show controller
            controller.show(0);
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement

          if (id == R.id.shuffleButtonSelector) {
            musicSrv.setShuffle();
            if (musicSrv.getShuffle()) {
                item.setIcon(getResources().getDrawable(R.drawable.shuffleon));
            } else
                item.setIcon(getResources().getDrawable(R.drawable.shuffleoff));
        }else if(id == R.id.Create_Play){
                CreatePlaylistMode();

        }else if(id == R.id.Save_Play){
            EditText mEdit = (EditText)findViewById(R.id.editText);

            CreatePlaylist(mEdit.getText().toString(), checkedPlaylist);
            checkedPlaylist = new ArrayList<String>();
            cPlaylistMode = false;
            File f = new File("/data/data/application.musicplayer.muse/files");
            playListFileDir = findPlaylist(f);

            ArrayList<String> s = new ArrayList<String>();
            for (File file : playListFileDir){
                s.add(file.getName().replace(".txt", ""));
            }
            playList1 = s;
        }else if(id == R.id.Load_Play){

            loading_play = true;


        }

        return super.onOptionsItemSelected(item);
    }


    private void init_slider() {
        // Creating The Toolbar and setting it as the Toolbar for the activity

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);


        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles, Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);
        pager.setCurrentItem(1, false);
        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);

    }

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private ScrimInsetsFrameLayout mScrimInsetsFrameLayout;

    private void init_navigator() {
        // Navigation Drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_activity_DrawerLayout);
        mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.primaryDark));
        mScrimInsetsFrameLayout = (ScrimInsetsFrameLayout) findViewById(R.id.main_activity_navigation_drawer_rootLayout);

        mActionBarDrawerToggle = new ActionBarDrawerToggle
                (
                        this,
                        mDrawerLayout,
                        toolbar,
                        R.string.navigation_drawer_opened,
                        R.string.navigation_drawer_closed
                ) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                // Disables the burger/arrow animation by default
                super.onDrawerSlide(drawerView, 0);
            }
        };

        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        mActionBarDrawerToggle.syncState();

        // Navigation Drawer layout width
        int possibleMinDrawerWidth = UtilsDevice.getScreenWidth(this) -
                UtilsMiscellaneous.getThemeAttributeDimensionSize(this, android.R.attr.actionBarSize);
        int maxDrawerWidth = getResources().getDimensionPixelSize(R.dimen.navigation_drawer_max_width);

        mScrimInsetsFrameLayout.getLayoutParams().width = Math.min(possibleMinDrawerWidth, maxDrawerWidth);
        // Set the first item as selected for the first time
        getSupportActionBar().setTitle(R.string.toolbar_title_home);
    }

    //connect to the service
    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            //get service
            musicSrv = binder.getService();
            //pass list
            musicSrv.setList(songList);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    //start and bind the service when the activity starts
    @Override
    public void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        if (playIntent == null) {
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://application.musicplayer.muse/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }
    private View cSelected;
    //user song select
    public void songClicked(View view) {
        musicSrv.setSong(Integer.parseInt(view.getTag().toString()));
        musicSrv.playSong();
     if(cSelected != null)
            cSelected.setBackgroundColor(0xffbf00);

        view.setBackgroundColor(Color.TRANSPARENT);
        //Toast.makeText(this," ", Toast.LENGTH_LONG).show();

        view.setBackgroundColor(Color.GRAY);

        if (playbackPaused) {
            setController();
            playbackPaused = false;
        }
        setController();
        //else if(pl)
        controller.show(0);
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        if (musicSrv != null && musicBound && musicSrv.isPng() || playbackPaused)
            return musicSrv.getPosn();
        else return 0;
    }

    @Override
    public int getDuration() {
        if (musicSrv != null && musicBound && musicSrv.isPng() || playbackPaused)
            return musicSrv.getDur();
        else return 0;
    }

    @Override
    public boolean isPlaying() {
        if (musicSrv != null && musicBound)
            return musicSrv.isPng();
        return false;
    }

    @Override
    public void pause() {
        playbackPaused = true;
        musicSrv.pausePlayer();

    }

    @Override
    public void seekTo(int pos) {
        musicSrv.seek(pos);
    }

    @Override
    public void start() {
        musicSrv.go();
    }


    //set the controller up
    private void setController() {
        if (controller == null) controller = new MusicController(this);
        //set previous and next button listeners
        controller.setPrevNextListeners(new OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
            }
        }, new OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrev();
            }
        });
        //set and show
        controller.setMediaPlayer(this);
        controller.setAnchorView(findViewById(R.id.song_list));
        controller.setEnabled(true);
    }

    private void playNext() {
        musicSrv.playNext();
        if (playbackPaused) {
            //setController();
            playbackPaused = false;
        }
        controller.show(0);
    }

    private void playPrev() {
        musicSrv.playPrev();
        if (playbackPaused) {
            //setController();
            playbackPaused = false;
        }
        controller.show(0);
    }

    @Override
    public void onPause() {
        musicSrv.getPosn();
        super.onPause();
        paused = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (paused) {
            //setController();
            paused = false;
        }


        // Set up receiver for media player onPrepared broadcast
        LocalBroadcastManager.getInstance(this).registerReceiver(onPrepareReceiver,
                new IntentFilter("MEDIA_PLAYER_PREPARED"));
        //controller.show(0);

    }

    /* public static void noShowController()
     {
         controller.show(0);
     }*/
    @Override
    public void onStop() {
        controller.hide();
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://application.musicplayer.muse/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }

    @Override
    public void onDestroy() {
        stopService(playIntent);
        musicSrv = null;
        super.onDestroy();
    }




    public void CreatePlaylist(String name, ArrayList<String> listName){

      //  ArrayList<File> mySongs = findSongs(Environment.getExternalStorageDirectory());

        try {
            String STORETEXT=name + ".txt";
            OutputStreamWriter out = new OutputStreamWriter(openFileOutput(STORETEXT, 0));
            MediaMetadataRetriever metaRetriver;
            metaRetriver = new MediaMetadataRetriever();


           // ArrayList<SongInfo> holdSong = new ArrayList<SongInfo>();
            for (String x : listName) {
              //  metaRetriver.setDataSource(x.getPath().toString());
                try {
                 //   SongInfo song = new SongInfo();
                 //   song.Album = (metaRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
                //    song.Artist = (metaRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
                 //   song.Genre = (metaRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE));
                 //   song.Year = (metaRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR));
                //    holdSong.add(song);

                    out.write(x);
                    out.write('\n');
                } catch (Exception e) {

                    //album.setText("Unknown Album");
                    ///artist.setText("Unknown Artist");
                    //genre.setText("Unknown Genre");
                }

            }

            out.close();
        } catch (Exception ex) {
            System.out.print(ex.getMessage());
        }

    }




    //for playlist
    public ArrayList<File> findSongs(File root) {
        ArrayList<File> al = new ArrayList<File>();
        File[] files = root.listFiles();
        for (File singleFile : files) {
            if (singleFile.isDirectory() && !singleFile.isHidden()) {
                al.addAll(findSongs(singleFile));
            } else if (singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav")) {
                al.add(singleFile);
            }
        }
        return al;
    }

    //for playlist
    public ArrayList<File> findPlaylist(File root) {
        ArrayList<File> al = new ArrayList<File>();
        File[] files = root.listFiles();
        for (File singleFile : files) {
            if (singleFile.isDirectory() && !singleFile.isHidden()) {
                al.addAll(findSongs(singleFile));
            } else if (singleFile.getName().endsWith(".txt")) {
                al.add(singleFile);
            }
        }
        return al;
    }


public void CreatePlaylistMode(){
cPlaylistMode = true;
    ArrayList<File> song = new ArrayList<File>();
    ArrayList<String> songs = new ArrayList<String>();

    song = findSongs(Environment.getExternalStorageDirectory());
    for(File f : song){
        songs.add(f.getName().replace(".mp3", ""));
    }


    ArrayAdapter<String> itemsAdapter =
            new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, songs);
    listView = (ListView) findViewById(R.id.listView);
    listView.setAdapter(itemsAdapter);


}



}







