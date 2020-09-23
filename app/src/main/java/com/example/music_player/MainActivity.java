package com.example.music_player;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.sql.Time;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private TextView starttimer, endtimer;
    private SeekBar seek;
    private CircularImageView playpause, prev, next,iv;
    private boolean isplaying = false;
    private double starttime = 0, finaltime = 0;
    private MediaPlayer mplayer;
    private Handler myhandler  = new Handler();
    private int height,width;
    private ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        starttimer = findViewById(R.id.starttimer);
        endtimer = findViewById(R.id.endtimer);
        seek = findViewById(R.id.seek);
        playpause = findViewById(R.id.playpause);
        prev = findViewById(R.id.prev);
        next = findViewById(R.id.next);
        iv = findViewById(R.id.iv);
        lv = findViewById(R.id.list);
        height = iv.getLayoutParams().height;
        width = iv.getLayoutParams().width;
        seek.setClickable(true);

        mplayer = MediaPlayer.create(this, R.raw.darkside);
        finaltime =  mplayer.getDuration()/1000;

        lyrics lyr = new lyrics(this);
        ArrayList<String> strings = lyr.lyr;

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.list_item,strings);
        lv.setAdapter(adapter);

        int min = (int)finaltime/60;
        int sec = (int)finaltime - min*60;
        endtimer.setText(String.format("%02d:%02d", min,sec));

        playpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isplaying){ //start playing
                    playpause.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                    isplaying=!isplaying;
                    mplayer.pause();
                    myhandler.removeCallbacks(updatetime);
                }
                else{
                    playpause.setImageResource(R.drawable.ic_pause_black_24dp);
                    isplaying=!isplaying;
                    mplayer.start();
                    myhandler.postDelayed(updatetime,100);

                }
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mplayer.seekTo(0);
            }
        });

    seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            if(b)
            mplayer.seekTo((int)(i*finaltime*10));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    });

    next.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int i = seek.getProgress();
            i+=5;
            seek.setProgress(i);
            mplayer.seekTo((int)(finaltime*10*i));

        }
    });


    }
    private Runnable updatetime = new Runnable() {
        @Override
        public void run() {
            starttime = mplayer.getCurrentPosition()/1000;
            int min = (int)starttime/60;
            int sec = (int)starttime - min*60;
            starttimer.setText(String.format("%02d:%02d", min,sec));
            int progress = (int)(starttime/finaltime*100);
            seek.setProgress(progress);
            if(height==525){
                height+=10;
                width+=10;
                iv.getLayoutParams().height = height;
                iv.getLayoutParams().width = width;
            }
            else
            {
                height-=10;
                width-=10;
                iv.getLayoutParams().height = height;
                iv.getLayoutParams().width = width;
            }
            myhandler.postDelayed(this, 100);

        }
    };
}
