package com.example.whacaboy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.applibrary.MusicService;
import com.example.applibrary.SETTINGS;
import com.unity3d.player.UnityPlayerActivity;
//imporort com.unity3d.player.UnityPlayerActivity;

public class ResultActivity extends AppCompatActivity {

    private static ResultActivity instance;

    public static ResultActivity getInstance(){
        return instance;
    }

    TextView scoreTextView;
    AppCompatButton homeButton;
    AppCompatButton replayButton;
    View musicButton;

    SharedPreferences settings;
    SharedPreferences.Editor editor;

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

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_result);
        hideStatusNavigationBars();
        SETTINGS.showSplash(this, SETTINGS.SHORT_C);

        scoreTextView = (TextView)findViewById(R.id.scoreTextView);
        homeButton = (AppCompatButton)findViewById(R.id.homeButton);
        replayButton = (AppCompatButton)findViewById(R.id.replayButton);
        musicButton = findViewById(R.id.musicButton);

        settings = getSharedPreferences(SETTINGS.APP_SETTINGS_FILE,MODE_PRIVATE);
        editor = settings.edit();

        if (settings.getBoolean(SETTINGS.PLAYMUSIC,true)){
            musicButton.setActivated(true);
        }
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, MainActivity.class);

                ResultActivity.this.finish();
                ResultActivity.this.startActivity(intent);
                ResultActivity.this.overridePendingTransition(0,0);
            }
        });

        replayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, UnityPlayerActivity.class);
                intent.putExtra(SETTINGS.LEVEL, settings.getInt(SETTINGS.LEVEL,0));


                ResultActivity.this.startActivity(intent);
                ResultActivity.this.overridePendingTransition(0,0);
            }
        });

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

        scoreTextView.setText("SCORE:" +
                String.valueOf(getIntent().getIntExtra("score",0))
                + " | "
                + String.valueOf(getIntent().getIntExtra("total",0)));
    }

    @Override
    protected void onPause() {
        super.onPause();
        SETTINGS.pauseMusic();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (settings.getBoolean(SETTINGS.PLAYMUSIC,true)) SETTINGS.resumeMusic();
    }
}
