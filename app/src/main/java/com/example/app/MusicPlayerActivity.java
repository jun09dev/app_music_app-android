package com.example.app;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MusicPlayerActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private TextView txtTitle, tvCurrentTime, tvRemainTime;

    private Handler handler = new Handler();
    private String filename;

    private static final String STREAM_URL = "http://61.106.148.251:8080/api/play/";

    private boolean isUserSeeking = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        // ===== Get data =====
        filename = getIntent().getStringExtra("filename");

        // ===== Bind views =====
        txtTitle = findViewById(R.id.txtTitle);
        seekBar = findViewById(R.id.seekBar);
        tvCurrentTime = findViewById(R.id.tvCurrentTime);
        tvRemainTime = findViewById(R.id.tvRemainTime);

        Button btnPlay = findViewById(R.id.btnPlay);
        Button btnPause = findViewById(R.id.btnPause);
        Button btnStop = findViewById(R.id.btnStop);

        txtTitle.setText(filename != null ? filename : "Unknown");

        // ===== MediaPlayer =====
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
        );

        prepareStream();

        // ===== Buttons =====
        btnPlay.setOnClickListener(v -> {
            if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                mediaPlayer.start();
                startSeekBarUpdate();
            }
        });

        btnPause.setOnClickListener(v -> {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
        });

        btnStop.setOnClickListener(v -> {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                handler.removeCallbacks(updateRunnable);
                prepareStream();
            }
        });

        // ===== SeekBar listener (tua nhạc) =====
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mediaPlayer != null) {
                    tvCurrentTime.setText(formatTime(progress));
                    tvRemainTime.setText("-" + formatTime(mediaPlayer.getDuration() - progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isUserSeeking = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mediaPlayer != null) {
                    mediaPlayer.seekTo(seekBar.getProgress());
                }
                isUserSeeking = false;
            }
        });
    }

    // ===== Prepare stream =====
    private void prepareStream() {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(STREAM_URL + filename);
            mediaPlayer.setOnPreparedListener(mp -> {
                seekBar.setMax(mp.getDuration());
                tvCurrentTime.setText("00:00");
                tvRemainTime.setText("-" + formatTime(mp.getDuration()));
                mp.start();
                startSeekBarUpdate();
            });
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===== Update SeekBar + time =====
    private void startSeekBarUpdate() {
        handler.post(updateRunnable);
    }

    private final Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null && mediaPlayer.isPlaying() && !isUserSeeking) {
                int current = mediaPlayer.getCurrentPosition();
                int duration = mediaPlayer.getDuration();

                seekBar.setProgress(current);
                tvCurrentTime.setText(formatTime(current));
                tvRemainTime.setText("-" + formatTime(duration - current));
            }
            handler.postDelayed(this, 1000);
        }
    };

    // ===== Format time mm:ss =====
    private String formatTime(int millis) {
        int totalSeconds = millis / 1000;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
