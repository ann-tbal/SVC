package com.SVC.spotifyvoicecommand;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.speech.RecognitionListener;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // required info for authentication
    private final String REDIRECT_URI = getString(R.string.redirect_uri);
    private final String CLIENT_ID = getString(R.string.client_id);

    // track info
    private TextView artistName;
    private TextView trackTitle;
    private TextView playerStateUpdate;
    private Button commandState;

    // app remote used to access Spotify features
    private SpotifyAppRemote mSpotifyAppRemote;

    // codes
    private static final int REQUEST_RECORD_AUDIO = 34;

    // Track
    Track track;

    // Speech recognizer stuff
    SpeechRecognizer speechRecognizer;
    Intent recognizerIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // track info
        trackTitle = findViewById(R.id.trackTitle);
        artistName = findViewById(R.id.artistName);

        // button
        commandState = findViewById(R.id.commandState);

        // update state
        playerStateUpdate = findViewById(R.id.playerStateUpdate);

        // recognition listener
        SpeechRecognitionListener SRL = new SpeechRecognitionListener();

       // create speech recognizer
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(SRL);


        // create intents
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,"en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);


        // if user clicked the button
        commandState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // if permission is not granted
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.RECORD_AUDIO},
                            REQUEST_RECORD_AUDIO);
                    Log.d("MainActivity ", "requesting permission");

                } else {
                    Log.d("MainActivity", "Permission granted");


                    // If there's a speech recognition service available
                    if (speechRecognizer.isRecognitionAvailable(MainActivity.this)) {
                        Log.d("MainActivity", "onBeginningOfSpeech");
                        speechRecognizer.startListening(recognizerIntent);

                    } else {
                        Log.e("MainActivity", "Speech recognition service not available");
                    }
                }
            }
        });
    }


    class SpeechRecognitionListener implements RecognitionListener{

        @Override
        public void onBeginningOfSpeech () {

            Log.e("LISTENER CLASS", "onBeginningOfSpeech");

        }

        @Override
        public void onReadyForSpeech(Bundle bundle) {
            Log.e("LISTENER CLASS", "onReadyForSpeech");

        }

        @Override
        public void onRmsChanged(float v) {
            Log.e("LISTENER CLASS", "onRmsChanged");

        }

        @Override
        public void onBufferReceived(byte[] bytes) {
            Log.e("LISTENER CLASS", "onBufferReceived");

        }

        @Override
        public void onEndOfSpeech() {
            Log.e("LISTENER CLASS", "onEndOfSpeech");

        }

        @Override
        public void onError(int i) {
            Log.e("LISTENER CLASS", "onError");

        }

        @Override
        public void onResults(Bundle bundle) {
            Log.e("LISTENER CLASS", "onResults");

            String strResults = "";
            ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            for (int i = 0; i < data.size(); i++) {
                strResults = strResults + data.get(i) + ", ";
            }
            Log.d("RESULTS, ", strResults);

            adjustPlayerState(data);

        }

        @Override
        public void onPartialResults(Bundle bundle) {
            Log.e("LISTENER CLASS", "onPartialResults");

        }

        @Override
        public void onEvent(int i, Bundle bundle) {
            Log.e("LISTENER CLASS", "onEvent");

        }
    }

    /**
     * This receives the permissions result(s)
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode) {
            case REQUEST_RECORD_AUDIO:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    speechRecognizer.startListening(recognizerIntent);
                } else {
                    Log.e("MainActivity", "Permission not granted.");
                }
                return;
        }

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

                        connected();
                    }

                    public void onFailure(Throwable throwable) {
                        Log.e("MyActivity", throwable.getMessage(), throwable);

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
                    track = playerState.track;

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
     * Converts the player state after a command has been given.
     * @param commands the list of commands user has given.
     * NOTE: the only commands right now are:
     *                 Pause, Play {Artist, Song, album},
     *                 Skip to the next song
     * E.g. if user says, "Pause music", then the track is paused.
     */
    private void adjustPlayerState(ArrayList<String> commands) {

        // organize the command into [Main command, Detailed command]
        String [] detailedCommand = organizeCommand(commands);

        // update log
        Log.d("detailedCommand", detailedCommand[0] + ", " + detailedCommand[1]);

        // adjust player state according to main command, going from simple to complex commands
        switch (detailedCommand[0]) {
            case "pause":
                mSpotifyAppRemote.getPlayerApi().pause();
                playerStateUpdate.setText("MUSIC PAUSED");
                break;
            case "resume":
                mSpotifyAppRemote.getPlayerApi().resume();
                playerStateUpdate.setText("MUSIC RESUMED");
                break;
            case "skip":
                if (detailedCommand[1].contains("previous")) {
                    mSpotifyAppRemote.getPlayerApi().skipPrevious();
                    playerStateUpdate.setText("MUSIC SKIPPED TO PREVIOUS SONG");
                } else {
                    mSpotifyAppRemote.getPlayerApi().skipNext();
                    playerStateUpdate.setText("MUSIC SKIPPED TO NEXT SONG");

                }
                break;
            case "play":
                // mSpotifyAppRemote.getPlayerApi().play("spotify:artist:6qqNVTkY8uBg9cP3Jd7DAH");
                // getURIRequest(detailedCommand[1]);

                Intent startPlayAuthentication = new Intent(this, PlayAuthentication.class);
                startActivity(startPlayAuthentication);


        }

    }

    /**
     * Gets the uri of a {song/artist/album} the user desires so we can play it
     * @param request the URI of the specific {album/artist/song} user wants to play
     * E.g.
     *                input: getURIRequest("billie eilish")
     *                output: 6qqNVTkY8uBg9cP3Jd7DAH
     *
     */
    private String getURIRequest (String request) {
        String uriRequest = "";

        // open a link to spotify search and search for the thing the user wants to play

        String url = "https://api.spotify.com/v1/search/" + request;
        Intent searchSpotify = new Intent(Intent.ACTION_VIEW);
        searchSpotify.setData(Uri.parse(url));
        startActivity(searchSpotify);


        return uriRequest;
    }

    /**
     * @param commands the command the user has given
     * @return organizedCommand, a 2-element array.
     * The first element holds the main command.
     * Main command = "play", "pause", "get"...
     * The second element holds the rest of the command.
     * E.g. command = "Play music now"
     *                  input: organizeCommand("Play music now")
     *                  output: ["Play", "music now"]
     */
    private String[] organizeCommand (ArrayList<String> commands) {

        String [] commandsArr = commands.get(0).split(" ");
        List<String> commandsList = Arrays.asList(commandsArr);

        String main = "";
        String rest = "";

        for (int i = 0; i < commandsList.size(); i++) {
            // if this is the first i, then this is the main command.
            if (i == 0) {
                main = commandsList.get(i);
            } else {
                rest = rest + commandsList.get(i) + " ";
            }
        }

        String [] organizedCommand = {main, rest};
        return organizedCommand;

    }


}
