package application.musicplayer.muse;


import android.net.Uri;

import java.io.Serializable;

public class Song implements Serializable {
	
	private Uri uri;
	private String title;
	private String artist;
	
	public Song(Uri uri, String songTitle, String songArtist){
		this.uri=uri;
		title=songTitle;
		artist=songArtist;
	}
	
	public Uri getUri(){return uri;}
	public String getTitle(){return title;}
	public String getArtist(){return artist;}

}
