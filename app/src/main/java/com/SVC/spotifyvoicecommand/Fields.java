package com.SVC.spotifyvoicecommand;

import android.widget.Button;
import android.widget.TextView;

import com.spotify.android.appremote.api.SpotifyAppRemote;

public class Fields {

    // required info for authentication
    private String REDIRECT_URI = "com.SVC.spotifyvoicecommand://callback";
    private String CLIENT_ID = "92348339626f44faa05efdedf8ac93d1";
    private String[] scopes = {"app-remote-control", "user-modify-playback-state", "user-read-currently-playing", "streaming"};

    // getters
    public String getCLIENT_ID() {
        return CLIENT_ID;
    }
    public String getREDIRECT_URI() {
        return REDIRECT_URI;
    }
}
