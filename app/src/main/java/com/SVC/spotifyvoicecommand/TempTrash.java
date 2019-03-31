//package com.SVC.spotifyvoicecommand;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.speech.RecognitionListener;
//import android.speech.RecognitionService;
//import android.speech.RecognizerIntent;
//import android.speech.SpeechRecognizer;
//import android.util.Log;
//import android.view.View;
//
//import java.util.ArrayList;
//
//public class TempTrash {
//    /* create speech recognizer */
////    SpeechRecognizer recognizer = SpeechRecognizer.createSpeechRecognizer();
//    // create recognition listener
//    RecognitionListener listener = new RecognitionListener() {
//        @Override
//        public void onReadyForSpeech(Bundle bundle) {
//
//            Log.e("MAIN", "onReadyForSpeech");
//        }
//
//        @Override
//        public void onBeginningOfSpeech() {
//            Log.e("MAIN", "onBeginningOfSpeech");
//        }
//
//        @Override
//        public void onRmsChanged(float v) {
//            Log.e("MAIN", "onRmsChanged");
//
//        }
//
//        @Override
//        public void onBufferReceived(byte[] bytes) {
//            Log.e("MAIN", "onBufferReceived");
//
//        }
//
//        @Override
//        public void onEndOfSpeech() {
//            Log.e("MAIN", "onEndOfSpeech");
//
//        }
//
//        @Override
//        public void onError(int i) {
//            Log.e("MAIN", "onError");
//
//        }
//
//        @Override
//        public void onResults(Bundle bundle) {
//            Log.e("MAIN", "onResults");
//            ArrayList data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
//            String strData = "";
//            for (int i = 0; i<data.size(); i++) {
//                strData += data.get(i);
//            }
//            Log.e("RESULTS", strData);
//
//        }
//
//        @Override
//        public void onPartialResults(Bundle bundle) {
//            Log.e("MAIN", "onPartial");
//
//        }
//
//        @Override
//        public void onEvent(int i, Bundle bundle) {
//            Log.e("MAIN", "onEvent");
//
//        }
//    };
//
//    // create prompt to trigger activity
//    Intent recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,"en");
//        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
//        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
//        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
//
//    // create recognition listener
//    RecognitionListener listener = new RecognitionListener() {
//        @Override
//        public void onReadyForSpeech(Bundle bundle) {
//
//            Log.e("MAIN", "onReadyForSpeech");
//        }
//
//        @Override
//        public void onBeginningOfSpeech() {
//            Log.e("MAIN", "onBeginningOfSpeech");
//        }
//
//        @Override
//        public void onRmsChanged(float v) {
//            Log.e("MAIN", "onRmsChanged");
//
//        }
//
//        @Override
//        public void onBufferReceived(byte[] bytes) {
//            Log.e("MAIN", "onBufferReceived");
//
//        }
//
//        @Override
//        public void onEndOfSpeech() {
//            Log.e("MAIN", "onEndOfSpeech");
//
//        }
//
//        @Override
//        public void onError(int i) {
//            Log.e("MAIN", "onError");
//
//        }
//
//        @Override
//        public void onResults(Bundle bundle) {
//            Log.e("MAIN", "onResults");
//            ArrayList data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
//            String strData = "";
//            for (int i = 0; i<data.size(); i++) {
//                strData += data.get(i);
//            }
//            Log.e("RESULTS", strData);
//
//        }
//
//        @Override
//        public void onPartialResults(Bundle bundle) {
//            Log.e("MAIN", "onPartial");
//
//        }
//
//        @Override
//        public void onEvent(int i, Bundle bundle) {
//            Log.e("MAIN", "onEvent");
//
//        }
//    };
//    // create speech recognizer
//    SpeechRecognizer recognizer = SpeechRecognizer.createSpeechRecognizer(TempTrash.this);
//        recognizer.setRecognitionListener(listener);
//
//    RecognitionService recognitionService = new RecognitionService() {
//        @Override
//        protected void onStartListening(Intent intent, Callback callback) {
//            recognizer.startListening(intent);
//        }
//
//        @Override
//        protected void onCancel(Callback callback) {
//
//        }
//
//        @Override
//        protected void onStopListening(Callback callback) {
//            recognizer.stopListening();
//            recognizer.destroy();
//        }
//    };
//
//        commandState.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            // create recognition service
//            recognizer.startListening(recognizerIntent);
//        }
//    });
//}
//
//
