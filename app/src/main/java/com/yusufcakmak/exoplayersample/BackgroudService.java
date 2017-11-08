package com.yusufcakmak.exoplayersample;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.TrackSelector;


public class BackgroudService extends Service {

    private LocalBroadcastManager broadcaster;
    static final public String COPA_RESULT = "com.controlj.copame.backend.COPAService.REQUEST_PROCESSED";


    public static SimpleExoPlayer player;

    public void sendResult(String msg) {
        Intent intent = new Intent(COPA_RESULT);
        intent.putExtra("msg", msg);
        broadcaster.sendBroadcast(intent);
    }

    @Override
    public void onCreate() {
        broadcaster = LocalBroadcastManager.getInstance(this);
        try {
            if (player == null) {
            } else {
                player.setPlayWhenReady(true);
            }
        } catch (Exception e) {
        }


        super.onCreate();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public static void initializePlayer(Context context, TrackSelector trackSelector, MediaSource mediaSource) {
        player = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
        player.prepare(mediaSource);
    }


    public static void playStream() {
        player.setPlayWhenReady(true);
    }

    public static void stopStream() {
        player.setPlayWhenReady(false);
    }
}
