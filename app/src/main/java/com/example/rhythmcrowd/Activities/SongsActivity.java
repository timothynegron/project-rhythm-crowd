package com.example.rhythmcrowd.Activities;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rhythmcrowd.R;

public class SongsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private MediaPlayer player;
    private Button buttonPlay;
    private Button buttonPause;
    private Button buttonStop;
    private Spinner spinnerChorus;
    private Spinner spinnerVerse1;
    private Spinner spinnerVerse2;
    private Spinner spinnerLength;
    private Spinner spinnerLyrics;
    private Spinner spinnerBeat;
    private Spinner spinnerTempo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs);
        initializeAttributes();
        setButtons();
        setSpinners();
    }

    private void setSpinners() {
        spinnerChorus = findViewById(R.id.spinnerChorus);
        spinnerVerse1 = findViewById(R.id.spinnerVerse1);
        spinnerVerse2= findViewById(R.id.spinnerVerse2);
        spinnerLength = findViewById(R.id.spinnerLength);
        spinnerLyrics = findViewById(R.id.spinnerLyrics);
        spinnerBeat = findViewById(R.id.spinnerBeat);
        spinnerTempo = findViewById(R.id.spinnerTempo);

        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(this, R.array.numbers, android.R.layout.simple_spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerChorus.setAdapter(adapterSpinner);
        spinnerVerse1.setAdapter(adapterSpinner);
        spinnerVerse2.setAdapter(adapterSpinner);
        spinnerTempo.setAdapter(adapterSpinner);
        spinnerBeat.setAdapter(adapterSpinner);
        spinnerLyrics.setAdapter(adapterSpinner);
        spinnerLength.setAdapter(adapterSpinner);
    }

    private void initializeAttributes() {
        buttonStop = findViewById(R.id.buttonSongsStop);
        buttonPause = findViewById(R.id.buttonSongsPause);
        buttonPlay = findViewById(R.id.buttonSongsPlay);
    }


    private void setButtons() {
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play(v);
            }
        });

        buttonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pause(v);
            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopPlayer();
            }
        });
    }


    public void play(View v){
        if(player == null){
            player = MediaPlayer.create(this, R.raw.ella);
            Toast.makeText(this, "Song Now Playing", Toast.LENGTH_SHORT).show();
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlayer();
                }
            });
        }

        player.start();
    }

    public void pause(View v){
        if(player != null){
            player.pause();
            Toast.makeText(this, "Song Paused", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopPlayer(){
        if(player != null){
            player.release();
            player = null;
            Toast.makeText(this, "Song Stop", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop(){
        super.onStop();
        stopPlayer();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
