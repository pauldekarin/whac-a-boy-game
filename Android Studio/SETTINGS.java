package com.example.applibrary;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;

import androidx.appcompat.app.AppCompatActivity;

public class SETTINGS extends AppCompatActivity {

    public static final int SHORT_C = 1000;
    public static final int LONG_C = 2000;

    public static final String LEVEL = "LEVEL";
    public static final String APP_SETTINGS_FILE = "SETTING_FILE";
    public static final String PLAYMUSIC = "PLAYMUSIC";



    public SETTINGS(){

    }


    private static IService musicService;
    private static boolean isBound = false;
    private static boolean toPlayAfterPrepared = false;
    private static ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicService = IService.Stub.asInterface(service);

            if (toPlayAfterPrepared){
                try {
                    musicService.resume();
                }catch (RemoteException e){
                    e.printStackTrace();
                }
            }
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicService = null;
            isBound = false;
        }
    };

    public static void prepareMusic(Context context, boolean play){
        context.bindService(new Intent(context,MusicService.class),serviceConnection, Context.BIND_AUTO_CREATE);
        toPlayAfterPrepared = play;
    }

    public static boolean isPlayingMusic(){
        if (isBound){
            try {
                return musicService.isPlaying();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        return false;
    }
    public static void pauseMusic(){
        try{
            if (isBound) musicService.pause();
        }catch (RemoteException e){
            e.printStackTrace();
        }

    }
    public static void resumeMusic(){
        try{
            if (isBound) musicService.resume();
        }catch (RemoteException e){
            e.printStackTrace();
        }
    }

    public static void stopMusic(){
        try{
            if(isBound) musicService.stop();
        }catch (RemoteException e){
            e.printStackTrace();
        }
    }
    public static void showSplash(Activity activity, int duration){
        ViewGroup parent = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View splash = inflater.inflate(R.layout.splash,null);

        splash.animate()
                .alpha(0)
                .setInterpolator(new Interpolator() {
                    @Override
                    public float getInterpolation(float v) {
                        return 0;
                    }
                })
                .setDuration(duration);

        parent.addView(splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                parent.removeView(splash);
            }
        },duration);
    }


}
