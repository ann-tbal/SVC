package com.SVC.spotifyvoicecommand;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;

public class Listener extends AppCompatActivity implements RecognitionListener {

    // codes
    private static final int REQUEST_RECORD_AUDIO = 1;
    private static final int SPEECH_PROMPTED = 2;

    // Speech recognizer stuff
    SpeechRecognizer speechRecognizer;
    Intent recognizerIntent;

    private Boolean isSpeechEnabled = new MainActivity().getIsSpeechEnabled();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // create speech recognizer
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(Listener.this);

        // create intents
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,"en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);

        // if the speech is enabled
        if (isSpeechEnabled) {
            // if permission is not granted
            if (ContextCompat.checkSelfPermission(Listener.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Listener.this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO);
            } else {
                speechRecognizer.startListening(recognizerIntent);
            }

        }

    }

    /**
     * This receives the permissions result(s) from the user.
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode) {
            case REQUEST_RECORD_AUDIO:
                speechRecognizer.startListening(recognizerIntent);
        }

    }

    @Override
    public void onBeginningOfSpeech () {

    }

    @Override
    public void onReadyForSpeech(Bundle bundle) {

    }


    @Override
    public void onRmsChanged(float v) {

    }

    @Override
    public void onBufferReceived(byte[] bytes) {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int i) {

    }

    @Override
    public void onResults(Bundle bundle) {
        String strResults = "";
        ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        for (int i = 0; i < data.size(); i++) {
            strResults += data.get(i);
        }
        Log.i("RESULTS, ", strResults);
    }

    @Override
    public void onPartialResults(Bundle bundle) {

    }

    @Override
    public void onEvent(int i, Bundle bundle) {

    }

}