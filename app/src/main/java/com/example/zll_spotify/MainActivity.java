package com.example.zll_spotify;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    TextView title,artist,currentTime,totalTime;
    ImageView cover;
    MediaPlayer mediaPlayer;
    ImageButton ibtnPlay, ibtnRewind, ibtnNext, ibtnStop;
SeekBar seekBar;

    int[] Songs = {R.raw.memory_of_the_lost, R.raw.illuminate, R.raw.beat_pf_medley, R.raw.aether};
    int[] Covers = {R.drawable.memoryofthelost, R.drawable.illuminate, R.drawable.beatpfmedley, R.drawable.aether};
    String[] songTitle = {
            "Memory of the Lost",
            "Illuminate",
            "-Beat- Pf Medley",
            "Aether"
    };
    String[] songArtist = {
            "MyReminiscence",
            "Remzcore",
            "ALICESOFT",
            "Powerless"
    };
    int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        title = findViewById(R.id.title);
        artist = findViewById(R.id.artist);
        cover = findViewById(R.id.covr);
        seekBar = findViewById(R.id.seekbar);
        ibtnPlay = findViewById(R.id.imageButtonS);
        ibtnRewind = findViewById(R.id.imageButtonR);
        ibtnNext = findViewById(R.id.imageButtonN);
        ibtnStop = findViewById(R.id.imageButtonStop);
        currentTime = findViewById(R.id.currentTime);
        totalTime = findViewById(R.id.totalTime);

        mediaPlayer = mediaPlayer.create(this, Songs[0]);

     updateSeekBar();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer!=null && fromUser){
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        infoSong();

        ibtnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.reset();
                mediaPlayer = mediaPlayer.create(MainActivity.this, Songs[currentIndex]);
                ibtnPlay.setImageResource(R.drawable.play);
            }
        });

        ibtnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    ibtnPlay.setImageResource(R.drawable.play); //canvi icona
                } else {
                    mediaPlayer.start();
                    ibtnPlay.setImageResource(R.drawable.pause); //canvi icona
                }
            }
        });
        ibtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentIndex == Songs.length - 1) {
                    return;
                }
                currentIndex += 1;

                if (mediaPlayer != null) {
                    mediaPlayer.reset(); //Reset MediaPlayer
                    mediaPlayer.release(); //Release resources
                    mediaPlayer = null;
                }

                //Start MediaPlayer with new song
                mediaPlayer = MediaPlayer.create(MainActivity.this, Songs[currentIndex]);

                //Update songinfo
                infoSong();

                // Update SeekBar and total time
                seekBar.setMax(mediaPlayer.getDuration());
                totalTime.setText(milliSecondsToTimer(mediaPlayer.getDuration()));

                // Start playing the new song (optional)
                mediaPlayer.start();
                ibtnPlay.setImageResource(R.drawable.pause); // Update the play/pause button icon
            }
        });
        ibtnRewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentIndex == 0) {
                    return;
                }
                currentIndex -= 1;

                if (mediaPlayer != null) {
                    mediaPlayer.reset(); //Reset MediaPlayer
                    mediaPlayer.release(); //Release resources
                    mediaPlayer = null;
                }

                //Start MediaPlayer with new song
                mediaPlayer = MediaPlayer.create(MainActivity.this, Songs[currentIndex]);

                //Update songinfo
                infoSong();

                // Update SeekBar and total time
                seekBar.setMax(mediaPlayer.getDuration());
                totalTime.setText(milliSecondsToTimer(mediaPlayer.getDuration()));

                // Start playing the new song (optional)
                mediaPlayer.start();
                ibtnPlay.setImageResource(R.drawable.pause); // Update the play/pause button icon
            }
        });
    }

//TODO
    Handler handler = new Handler(); // inicialitzar Handler
    private void updateSeekBar() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    seekBar.setProgress(currentPosition);
                    currentTime.setText(milliSecondsToTimer(currentPosition));
                }
                handler.postDelayed(this, 100);
            }
        }, 100);
    }
    private String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString;

        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);

        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        return finalTimerString;
    }
    private void infoSong() {
        title.setText(songTitle[currentIndex]);
        artist.setText(songArtist[currentIndex]);
        cover.setImageResource(Covers[currentIndex]);
    }

}