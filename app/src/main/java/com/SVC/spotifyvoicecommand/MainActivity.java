package com.SVC.spotifyvoicecommand;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.spotify.protocol.types.ImageUri;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;



public class MainActivity extends AppCompatActivity {

    // authentication required info
    private static final int REQUEST_CODE = 1138;
    private static final String REDIRECT_URI = "com.SVC.spotifyvoicecommand://callback";
    private static final String CLIENT_ID = "92348339626f44faa05efdedf8ac93d1";
    private static final String [] scopes = {"app-remote-control", "streaming"};

    // track info
    private TextView artistName;
    private TextView trackTitle;
    private ImageUri trackImageUri;


    private SpotifyAppRemote mSpotifyAppRemote;

    protected void openLoginActivity () {
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(scopes);

        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        openLoginActivity();
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


        // connects to the spotify app remote, allowing access to spotify api
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

        // ----------- TESTING PURPOSES, IDK WHERE ELSE TO PUT Lol -----------------------
        // Play a playlist
        mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:0fZm7ygIaFLpTX7AEd38WT");


        // Subscribe to PlayerState
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    final Track track = playerState.track;
                    if (track != null) {

                        Log.d("MainActivity", track.name + " by " + track.artist.name);
                    }
                    // IDK why we can't use Track Display.java and isntead have to create
                    // text on MainActivity. reminder to fix this in the future to make modular

                    setTrackDisplay(track.artist.name, track.name);

                });




    }

    private void setTrackDisplay(String title, String name) {
        trackTitle = findViewById(R.id.trackTitle);
        trackTitle.setText(title);

        artistName = findViewById(R.id.artistName);
        artistName.setText(name);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    // Handle successful response
                    connected();
                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    break;

                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }
    }
}
