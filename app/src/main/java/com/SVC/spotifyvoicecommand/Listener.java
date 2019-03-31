package com.SVC.spotifyvoicecommand;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;

public class Listener extends AppCompatActivity implements RecognitionListener {

    // codes
    private static final int REQUEST_RECORD_AUDIO = 1;
    private static final int SPEECH_PROMPTED = 2;

    // create speech recognizer
    SpeechRecognizer speechRecognizer = SpeechRecognizer.createSpeechRecognizer(Listener.this);

    // create intents
    Intent

    private Boolean isSpeechEnabled = new MainActivity().getIsSpeechEnabled();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    }

    @Override
    public void onPartialResults(Bundle bundle) {

    }

    @Override
    public void onEvent(int i, Bundle bundle) {

    }

}