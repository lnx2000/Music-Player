package com.example.music_player;

import android.content.Context;
import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class lyrics {
    public ArrayList<Integer> times = new ArrayList<>();
    public ArrayList<String> lyr = new ArrayList<>();
    public lyrics(Context ctx){
        InputStream inputStream = ctx.getResources().openRawResource(R.raw.darkside_lrc);

        InputStreamReader inputreader = new InputStreamReader(inputStream);
        BufferedReader buffreader = new BufferedReader(inputreader);
        String line;

        try {
            while (( line = buffreader.readLine()) != null) {
                get_time(line);
            }
        } catch (IOException e) {
        }
    }
    public void get_time(String s){
        int min = Integer.parseInt(s.substring(1,3));
        float sec = Float.parseFloat(s.substring(4,9));
        int time = min*60+(int)sec;
        times.add(time);
        s = s.substring(10);
        lyr.add(s);
    }
}
