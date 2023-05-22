package com.example.whacaboy;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.applibrary.IService;
import com.example.applibrary.MusicService;
import com.example.applibrary.SETTINGS;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerView;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

public class MainActivity extends AppCompatActivity {

    protected void hideStatusNavigationBars(){
        Window window = getWindow();
        View decorView = window.getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        );

    }
    protected void showSplash(){
        final ViewGroup parent = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);

        LayoutInflater inflater = getLayoutInflater();
        View splash =  inflater.inflate(com.example.applibrary.R.layout.splash, null);
        parent.addView(splash);

    }


    PlayerView playerView;
    ExoPlayer exoPlayer;
    ExoPlayer musicPlayer;

    AppCompatButton settingsButton;
    AppCompatButton playButton;
    AppCompatButton backButton;
    AppCompatButton lowLevelButton;
    AppCompatButton mediumLevelButton;
    AppCompatButton hardLevelButton;
    View musicButton;

    SharedPreferences settings;
    SharedPreferences.Editor editor;

    LinearLayout launchLayout;
    LinearLayout settingsLayout;
    RelativeLayout relativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        SETTINGS.showSplash(this, SETTINGS.SHORT_C);


        ConstraintLayout mainLayout = (ConstraintLayout)findViewById(R.id.mainLayout);

        getWindow().setFormat(PixelFormat.RGBX_8888);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        musicPlayer = new ExoPlayer.Builder(MainActivity.this).build();
        MediaItem musicItem = MediaItem.fromUri(Uri.parse(
                "android.resource://" + getPackageName() + "/" + R.raw.music
        ));
        musicPlayer.setMediaItem(musicItem);
        musicPlayer.setRepeatMode(ExoPlayer.REPEAT_MODE_ALL);
        musicPlayer.prepare();

        launchLayout = (LinearLayout)findViewById(R.id.launchLayout);
        settingsLayout = (LinearLayout)findViewById(R.id.settingsLayout);
        relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout);

        playButton = (AppCompatButton) findViewById(R.id.playButton);
        settingsButton = (AppCompatButton) findViewById(R.id.settingsButton);
        backButton = (AppCompatButton) findViewById(R.id.backButton);
        lowLevelButton = (AppCompatButton)findViewById(R.id.lowLevelButton);
        mediumLevelButton = (AppCompatButton)findViewById(R.id.mediumLevelButton);
        hardLevelButton = (AppCompatButton)findViewById(R.id.hardLevelButton);
        musicButton = (View) findViewById(R.id.musicButton);

        playerView = (PlayerView) findViewById(R.id.playerView);

        exoPlayer = new ExoPlayer.Builder(MainActivity.this).build();
        MediaItem menuItem = MediaItem.fromUri(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.menu));
        MediaItem menuRevItem = MediaItem.fromUri(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.menu_rev));

        playerView.setPlayer(exoPlayer);
        exoPlayer.addMediaItem(menuItem);
        exoPlayer.addMediaItem(menuRevItem);

        exoPlayer.setRepeatMode(ExoPlayer.REPEAT_MODE_ALL);
        exoPlayer.prepare();
        exoPlayer.addListener(new Player.Listener() {
            @Override
            public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {
                Player.Listener.super.onMediaItemTransition(mediaItem, reason);
                exoPlayer.pause();

                setClickableButtons(true);
            }
        });

        settings = getSharedPreferences(SETTINGS.APP_SETTINGS_FILE,MODE_PRIVATE);
        editor = settings.edit();

        playButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainActivity.this, UnityPlayerActivity.class);
                intent.putExtra(SETTINGS.LEVEL, settings.getInt(SETTINGS.LEVEL,0));
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                MainActivity.this.startActivity(intent);
                MainActivity.this.overridePendingTransition(0,0);

            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exoPlayer.play();

                setClickableButtons(false);

                launchLayout.animate()
                            .alpha(0.0f)
                            .setInterpolator(new LinearInterpolator())
                            .translationY(-launchLayout.getHeight()/2)
                            .setDuration(getResources().getInteger(R.integer.animDuration)/2);

                settingsLayout.setTranslationX(settingsLayout.getWidth());
                settingsLayout.animate()
                        .translationX(0)
                        .alpha(1.0f)
                        .setInterpolator(new LinearInterpolator())
                        .setDuration(getResources().getInteger(R.integer.animDuration));

            }
        });

        lowLevelButton.setOnClickListener(levelButtonListener);
        mediumLevelButton.setOnClickListener(levelButtonListener);
        hardLevelButton.setOnClickListener(levelButtonListener);

        musicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (settings.getBoolean(SETTINGS.PLAYMUSIC,true)){
                    SETTINGS.pauseMusic();
                    musicButton.setActivated(false);
                    editor.putBoolean(SETTINGS.PLAYMUSIC, false);
                }else{
                    SETTINGS.resumeMusic();
                    musicButton.setActivated(true);
                    editor.putBoolean(SETTINGS.PLAYMUSIC,true);
                }
                editor.apply();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exoPlayer.play();

                setClickableButtons(false);
                
                settingsLayout.animate()
                        .alpha(0.0f)
                        .setDuration(getResources().getInteger(R.integer.animDuration))
                        .translationX(settingsLayout.getWidth())
                        .setInterpolator(new LinearInterpolator());

                launchLayout.animate()
                        .alpha(1.0f)
                        .translationY(0)
                        .setDuration(getResources().getInteger(R.integer.animDuration))
                        .setInterpolator(new LinearInterpolator());
            }
        });


        hideStatusNavigationBars();
        resetLevelButtonsDrawable();


        if (settings.getBoolean(SETTINGS.PLAYMUSIC,true)){
            musicButton.setActivated(true);
            SETTINGS.prepareMusic(getApplicationContext(), true);
        }
        else {
            musicButton.setActivated(false);
            SETTINGS.prepareMusic(getApplicationContext(), false);
        }
    }


    public void setClickableButtons(boolean clickable){

        for (int i = 0; i < launchLayout.getChildCount(); i++){
            launchLayout.getChildAt(i).setClickable(clickable);
        }

        for (int i = 0; i < settingsLayout.getChildCount(); i++){
            settingsLayout.getChildAt(i).setClickable(clickable);
        }
    }

    public void resetLevelButtonsDrawable(){
        switch (settings.getInt(SETTINGS.LEVEL,0)){
            case 0:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    lowLevelButton.setBackgroundDrawable(getApplicationContext().getDrawable(R.drawable.level_button_state));

                    mediumLevelButton.setBackgroundDrawable(getApplicationContext().getDrawable(R.drawable.level_button_unstate));
                    hardLevelButton.setBackgroundDrawable(getApplicationContext().getDrawable(R.drawable.level_button_unstate));
                }
                break;
            case 1:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mediumLevelButton.setBackgroundDrawable(getApplicationContext().getDrawable(R.drawable.level_button_state));

                    lowLevelButton.setBackgroundDrawable(getApplicationContext().getDrawable(R.drawable.level_button_unstate));
                    hardLevelButton.setBackgroundDrawable(getApplicationContext().getDrawable(R.drawable.level_button_unstate));
                }
                break;
            case 2:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    hardLevelButton.setBackgroundDrawable(getApplicationContext().getDrawable(R.drawable.level_button_state));

                    lowLevelButton.setBackgroundDrawable(getApplicationContext().getDrawable(R.drawable.level_button_unstate));
                    mediumLevelButton.setBackgroundDrawable(getApplicationContext().getDrawable(R.drawable.level_button_unstate));
                }
                break;
        }

    }


    View.OnClickListener levelButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.lowLevelButton:
                    editor.putInt(SETTINGS.LEVEL,0);
                    break;
                case R.id.mediumLevelButton:
                    editor.putInt(SETTINGS.LEVEL,1);
                    break;
                case R.id.hardLevelButton:
                    editor.putInt(SETTINGS.LEVEL,2);
                    break;
            }
            editor.apply();
            resetLevelButtonsDrawable();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (settings.getBoolean(SETTINGS.PLAYMUSIC,true)) SETTINGS.resumeMusic();
    }

    @Override
    protected void onPause() {
        super.onPause();
       // SETTINGS.pauseMusic();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}