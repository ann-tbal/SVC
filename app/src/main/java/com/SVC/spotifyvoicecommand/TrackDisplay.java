package com.SVC.spotifyvoicecommand;

import android.app.Activity;

import android.view.View;
import android.widget.TextView;

import com.spotify.protocol.types.Artist;
import com.spotify.protocol.types.ImageUri;

import com.SVC.spotifyvoicecommand.MainActivity.*;

/**
 * Class handles displaying information about the track such as the artist name, song title, and
 * the image for the specific song.
 */
public class TrackDisplay extends Activity {


    public void displaySong(String title) {
        TextView trackTitle = findViewById(R.id.trackTitle);
        trackTitle.setText(title);
    }

    private void displayImageURI (ImageUri imageUri) {

    }

    private void displayArtistName (Artist artist) {
        TextView artistName = findViewById(R.id.artistName);
        artistName.append(artist.toString());
    }

    public void setDisplay (String title, ImageUri imageUri, Artist artist) {
        displaySong(title);
        displayArtistName(artist);
        displayImageURI(imageUri);
    }
}
