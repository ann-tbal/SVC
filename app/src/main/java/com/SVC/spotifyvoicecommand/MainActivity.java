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

    // codes
    private static final int REQUEST_RECORD_AUDIO = 8;

    // track info
    private TextView artistName;
    private TextView trackTitle;
    private Button commandState;

    // request codes
    private int SPEECH_PROMPTED = 8;

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

        /* create speech recognizer */
        SpeechRecognizer recognizer = SpeechRecognizer.createSpeechRecognizer(MainActivity.this);
        // create recognition listener
        RecognitionListener listener = new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

                Log.e("MAIN", "onReadyForSpeech");
            }

            @Override
            public void onBeginningOfSpeech() {
                Log.e("MAIN", "onBeginningOfSpeech");
            }

            @Override
            public void onRmsChanged(float v) {
                Log.e("MAIN", "onRmsChanged");

            }

            @Override
            public void onBufferReceived(byte[] bytes) {
                Log.e("MAIN", "onBufferReceived");

            }

            @Override
            public void onEndOfSpeech() {
                Log.e("MAIN", "onEndOfSpeech");

            }

            @Override
            public void onError(int i) {
                Log.e("MAIN", "onError");

            }

            @Override
            public void onResults(Bundle bundle) {
                Log.e("MAIN", "onResults");
                ArrayList data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                String strData = "";
                for (int i = 0; i<data.size(); i++) {
                    strData += data.get(i);
                }
                Log.e("RESULTS", strData);

            }

            @Override
            public void onPartialResults(Bundle bundle) {
                Log.e("MAIN", "onPartial");

            }

            @Override
            public void onEvent(int i, Bundle bundle) {
                Log.e("MAIN", "onEvent");

            }
        };
        recognizer.setRecognitionListener(listener);

        // create prompt to trigger activity
        Intent recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,"en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);

        commandState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recognizer.startListening(recognizerIntent);
            }
        });


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

    /**
     * Asks for permission for the app to record audio from the user
     * @return hasPermission describes whether or not permission to record audio was/is given
     * */
    private Boolean hasPermissionToRecordAudio(){
        Boolean hasPermission = false;
        // if we don't have permission to record audio
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            // request permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_RECORD_AUDIO);
            hasPermission = true;
        }
        return hasPermission;
    }


}
