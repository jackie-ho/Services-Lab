package com.adi.ho.jackie.musicservice;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    MediaPlayer player;
    Thread thread;

    public MusicService() {


    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        String url = "http://download.lisztonian.com/music/download/Clair+de+Lune-113.mp3";
        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            player.setDataSource(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.prepareAsync();

        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                player.start();
            }
        };
        thread = new Thread(runnable);
//        if(intent.getStringExtra("STOP") != null || intent.getStringExtra("STOP") == "stop" ){
//            stopSelf();
//        }

        if (intent.getBooleanExtra("PLAY",false)) {
            Log.i("MUSIC","PLAY");
            thread.start();
        }
        else {
            Log.i("MUSIC","PAUSE");
            player.pause();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
            onDestroy();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        player.start();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
       player.release();

    }
}
