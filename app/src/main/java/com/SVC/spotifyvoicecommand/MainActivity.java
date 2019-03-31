package com.SVC.spotifyvoicecommand;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.speech.RecognitionListener;
import android.speech.RecognitionService;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.protocol.types.Track;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    // required info for authentication
    private static final String REDIRECT_URI = "com.SVC.spotifyvoicecommand://callback";
    private static final String CLIENT_ID = "92348339626f44faa05efdedf8ac93d1";
    private static final String [] scopes = {"app-remote-control", "streaming"};

    // track info
    private TextView artistName;
    private TextView trackTitle;
    private Button commandState;

    // player state's state
    private static Boolean isSpeechEnabled = false;

    // app remote used to access Spotify features
    private SpotifyAppRemote mSpotifyAppRemote;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // track info
        trackTitle = findViewById(R.id.trackTitle);
        artistName = findViewById(R.id.artistName);

        // button
        commandState = findViewById(R.id.commandState);

        // if user clicked the button
        commandState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSpeechEnabled = true;
                // instantiate Listener class
                Listener listener = new Listener();
                Intent recognizerIntent = listener.getIntent();
                listener.startActivity(recognizerIntent);

                // if there's no speech recognition (i.e. user wants to command spotify player state)

                    // get the results of the speech recognition
                    // get the specific command
                    // change spotify player state

                // if speech recognition is enabled (i.e. user has already issued commands to player state

                    // change the button to stop commanding

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        
    }

    @Override
    protected void onStart() {
        super.onStart();

        // creates an auth request and opens up a link to the auth url
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();


        // connects to the spotify app remote
        SpotifyAppRemote.connect(this, connectionParams,
                new Connector.ConnectionListener() {

                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        Log.d("MainActivity", "Connected! Yay!");

                        // Now you can start interacting with App Remote
                        connected();
                    }

                    public void onFailure(Throwable throwable) {
                        Log.e("MyActivity", throwable.getMessage(), throwable);

                        // Something went wrong when attempting to connect! Handle errors here
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }

    private void connected() {

        // Subscribe to PlayerState
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()

                // create a listener that checks for any changes to player's state
                .setEventCallback(playerState -> {

                    // get the player's track
                    final Track track = playerState.track;

                    // if the track exists
                    if (track != null) {

                        // update log on track info
                        Log.d("MainActivity", track.name + " by " + track.artist.name);

                        // update main view on track info
                        setTrackDisplay(track.name, track.artist.name);

                    }
                });
    }

    /**
     * Update track information (info = title of the song currently playing, artist) on the main
     * view
     * @param title of the Song
     * @param name of the Artist
     */
    private void setTrackDisplay(String title, String name) {
        trackTitle.setText(title);
        artistName.setText(name);
    }

    public Boolean getIsSpeechEnabled() {
        return isSpeechEnabled;
    }


}
