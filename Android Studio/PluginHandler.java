package com.example.unityplugin;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.*;
import com.example.applibrary.SETTINGS;
import com.example.whacaboy.MainActivity;
import com.example.whacaboy.ResultActivity;
import com.unity3d.player.UnityPlayerActivity;

public class PluginHandler extends AppCompatActivity{

    public static void Stop(Activity activity, int score,int total){
        Intent intent = new Intent(activity, ResultActivity.class);
        intent.putExtra("score",score);
        intent.putExtra("total",total);
        activity.finish();
        activity.startActivity(intent);
        activity.overridePendingTransition(0,0);

    }

    public static void Start(){

    }
}
