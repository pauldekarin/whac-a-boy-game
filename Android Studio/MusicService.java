package com.example.applibrary;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;

public class MusicService extends Service {
    private static ExoPlayer musicPlayer;

    private final IService.Stub binder = new IService.Stub() {
        @Override
        public void pause() throws RemoteException {
            musicPlayer.pause();
        }

        @Override
        public void stop() throws RemoteException {
            musicPlayer.stop();
        }

        @Override
        public void resume() throws RemoteException {
            musicPlayer.play();
        }

        @Override
        public void play() throws RemoteException {
            musicPlayer.prepare();
            musicPlayer.play();
        }

        @Override
        public boolean isPlaying() throws RemoteException {
            return musicPlayer.isPlaying();
        }

    };

    @Override
    public void onCreate() {
        super.onCreate();
        musicPlayer = new ExoPlayer.Builder(this).build();
        musicPlayer.setMediaItem(MediaItem.fromUri(
                "android.resource://" + getPackageName() + "/" + R.raw.music
        ));
        musicPlayer.setRepeatMode(ExoPlayer.REPEAT_MODE_ALL);
        musicPlayer.prepare();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        musicPlayer.play();
        return  super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
