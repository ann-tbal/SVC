package com.SVC.spotifyvoicecommand;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PlayAuthentication extends AppCompatActivity {

    private String CLIENT_ID = "";
    private String REDIRECT_URI = "";
    private String [] scopes = {};

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CLIENT_ID = getString(R.string.client_id);
        REDIRECT_URI = getString(R.string.redirect_uri);
        scopes = getResources().getStringArray(R.array.scopes);

        createAuthURL();

    }

    private String createVerifier () {
        String verifier = "";
        SecureRandom secureRandom = new SecureRandom();
        byte[] code = new byte[32];
        secureRandom.nextBytes(code);
        verifier = android.util.Base64.encodeToString(code, Base64.NO_WRAP | Base64.NO_PADDING | Base64.NO_WRAP);
        Log.e("creating verifier: ", verifier);
        return verifier;
    }

    private String createChallenge(String verifier) {
        String challenge = "";
        try {
            byte[] bytes = verifier.getBytes("US-ASCII");

            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(bytes, 0, bytes.length);
            byte[] digest = messageDigest.digest();
            challenge = Base64.encodeToString(digest, Base64.URL_SAFE, Base64.NO_PADDING, Base64.NO_WRAP);
            Log.e("creating challenge: ", challenge);
        } catch (UnsupportedEncodingException e) {
            Log.e("Creating challenge: ", e.toString()) ;
        } catch (NoSuchAlgorithmException e) {
            Log.e("Creating challenge", e.toString());
        }

        return challenge;
    }

    private String createAuthURL () {
        // formats the scopes
        String scopesString = "";
        for (String item: scopes) {
            scopesString = scopesString + item + "%20";
        }

        String authURL = "https://accounts.spotify.com/authorize" +
                "?response_type=code" +
                "?client_id=" + CLIENT_ID +
                "?code_challenge=" + createChallenge(createVerifier()) +
                "?code_challenge_method=S256" +
                "?redirect_uri=" + REDIRECT_URI +
                "?scope=" + scopesString; 

        Log.e("creating auth URL: ", authURL);

        return authURL;
    }



}
