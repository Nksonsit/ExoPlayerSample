package com.yusufcakmak.exoplayersample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.id3.ApicFrame;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import app.minimize.com.seek_bar_compat.SeekBarCompat;

public class RadioPlayerActivity extends AppCompatActivity {

    private int time = 0;
    private SeekBarCompat seekBar;
    private TextView txtCurrentTime;
    private TextView txtTotalTime;
    private ImageView play;
    private boolean isPlaying = false;
    private boolean isComplete = false;
    private ImageView imgMusic;
    private int viewHeight;
    private int viewWidth;

    private SimpleExoPlayer player;
    public BandwidthMeter bandwidthMeter;
    public ExtractorsFactory extractorsFactory;
    public TrackSelection.Factory trackSelectionFactory;
    public TrackSelector trackSelector;
    public DefaultBandwidthMeter defaultBandwidthMeter;
    public DataSource.Factory dataSourceFactory;
    public MediaSource mediaSource;
    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio_player);

        seekBar = (SeekBarCompat) findViewById(R.id.seekBar);
        txtCurrentTime = (TextView) findViewById(R.id.currentTime);
        txtTotalTime = (TextView) findViewById(R.id.totalTime);
        play = (ImageView) findViewById(R.id.imgPlay);
        imgMusic = (ImageView) findViewById(R.id.imgMusic);

        bandwidthMeter = new DefaultBandwidthMeter();
        extractorsFactory = new DefaultExtractorsFactory();
        trackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        trackSelector = new DefaultTrackSelector(trackSelectionFactory);
        defaultBandwidthMeter = new DefaultBandwidthMeter();

        dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "mediaPlayerSample"), defaultBandwidthMeter);

        mediaSource = new ExtractorMediaSource(Uri.parse("https://www.stuffdown.com/2017/Benash%20-%20CDG%20-%20(www.SongsLover.club)/04.%20CDG%20-%20(www.SongsLover.club).mp3"), dataSourceFactory, extractorsFactory, null, null);

        if (BackgroudService.player == null) {
            BackgroudService.initializePlayer(RadioPlayerActivity.this, trackSelector, mediaSource);
        }

        player = BackgroudService.player;

        BackgroudService.playStream();
        play.setImageResource(R.drawable.pause);
        isPlaying = true;

        if (((int) player.getDuration() / 1000) % 60 == 0) {
            txtTotalTime.setText(((int) player.getDuration() / 1000) + "");
        } else {
            txtTotalTime.setText(((int) player.getDuration() / 1000) / 60 + ":" + ((int) player.getDuration() / 1000) % 60 + "");
        }

        seekBar.setMax((int) player.getDuration());

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("playing", isPlaying + "");
                if (isPlaying) {
                    play.setImageResource(R.drawable.play);
                    isPlaying = false;
                    BackgroudService.stopStream();
                } else {
                    play.setImageResource(R.drawable.pause);
                    isPlaying = true;
                    BackgroudService.playStream();
                    if (isComplete) {
                        new BackgroundTask().execute();
                        isComplete = false;
                    }
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.e("progress start", seekBar.getProgress() + "");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.e("progress stop", seekBar.getProgress() + "");
                BackgroudService.player.seekTo(seekBar.getProgress());
            }
        });

        ViewTreeObserver viewTreeObserver = imgMusic.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    imgMusic.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    viewWidth = imgMusic.getWidth();
                    viewHeight = imgMusic.getHeight();
                }
            });
        }

        player.setAudioDebugListener(new AudioRendererEventListener() {
            @Override
            public void onAudioEnabled(DecoderCounters counters) {

            }

            @Override
            public void onAudioSessionId(int audioSessionId) {
                Log.e("session id", audioSessionId + "");
            }

            @Override
            public void onAudioDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {
                Log.e("time", "" + initializedTimestampMs + " " + initializationDurationMs);
                new BackgroundTask().execute();
                TrackSelectionArray selections = BackgroudService.player.getCurrentTrackSelections();
                for (int i = 0; i < selections.length; i++) {
                    TrackSelection selection = selections.get(i);
                    if (selection != null) {
                        for (int j = 0; j < selection.length(); j++) {
                            Metadata metadata = selection.getFormat(j).metadata;
                            if (metadata != null && setArtworkFromMetadata(metadata)) {
                            }
                        }
                    }
                }
            }

            @Override
            public void onAudioInputFormatChanged(Format format) {

            }

            @Override
            public void onAudioTrackUnderrun(int bufferSize, long bufferSizeMs, long elapsedSinceLastFeedMs) {
            }

            @Override
            public void onAudioDisabled(DecoderCounters counters) {

            }
        });

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String s = intent.getStringExtra("msg");
                Log.e("message", s + "");
                /*updateUi();
                if(s.equalsIgnoreCase("post")){
                    seekBar.setProgress(0);
                    BackgroudService.player.seekTo(0);
                    BackgroudService.player.setPlayWhenReady(false);
                    play.setImageResource(R.drawable.play);
                    isPlaying = false;
                }*/
            }
        };
    }

    private void updateUi() {
        Log.e("loading", BackgroudService.player.isLoading() + "");
        if (((int) BackgroudService.player.getDuration() / 1000) % 60 == 0) {
            txtCurrentTime.setText("00:" + String.format("%02d", ((int) BackgroudService.player.getCurrentPosition() / 1000)));
        } else {
            txtCurrentTime.setText(String.format("%02d:%02d", ((int) BackgroudService.player.getCurrentPosition() / 1000) / 60, ((int) BackgroudService.player.getCurrentPosition() / 1000) % 60));
        }
        seekBar.setProgress((int) BackgroudService.player.getCurrentPosition());
    }

    private boolean setArtworkFromMetadata(Metadata metadata) {
        for (int i = 0; i < metadata.length(); i++) {
            Metadata.Entry metadataEntry = metadata.get(i);
            if (metadataEntry instanceof ApicFrame) {
                byte[] bitmapData = ((ApicFrame) metadataEntry).pictureData;
                Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length);
                return setArtworkFromBitmap(bitmap);
            }
        }
        return false;
    }

    private boolean setArtworkFromBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            int bitmapWidth = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();
            if (bitmapWidth > 0 && bitmapHeight > 0) {
                if (viewWidth > 0 && viewHeight > 0) {
                    imgMusic.setImageBitmap(Bitmap.createScaledBitmap(bitmap, viewWidth, viewHeight, false));
                } else {
                    imgMusic.setImageBitmap(bitmap);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.setPlayWhenReady(false);
    }

    public class BackgroundTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            txtCurrentTime.setText("00:00");
            if (((int) BackgroudService.player.getDuration() / 1000) % 60 == 0) {
                txtTotalTime.setText("00:" + String.format("%02d", (int) BackgroudService.player.getDuration() / 1000));
            } else {
                txtTotalTime.setText(String.format("%02d:%02d", ((int) BackgroudService.player.getDuration() / 1000) / 60, ((int) BackgroudService.player.getDuration() / 1000) % 60));
            }
            seekBar.setMax((int) BackgroudService.player.getDuration());
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            if (((int) player.getDuration() / 1000) % 60 == 0) {
                txtCurrentTime.setText("00:" + String.format("%02d", ((int) BackgroudService.player.getCurrentPosition() / 1000)));
            } else {
                txtCurrentTime.setText(String.format("%02d:%02d", ((int) BackgroudService.player.getCurrentPosition() / 1000) / 60, ((int) BackgroudService.player.getCurrentPosition() / 1000) % 60));
            }

            seekBar.setProgress((int) BackgroudService.player.getCurrentPosition());
        }

        @Override
        protected Void doInBackground(Void... voids) {
            while (BackgroudService.player.getCurrentPosition() < BackgroudService.player.getDuration()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onProgressUpdate();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            isComplete = true;
            Log.e("stop", "stop");
            seekBar.setProgress(0);
            BackgroudService.player.seekTo(0);
            BackgroudService.player.setPlayWhenReady(false);
            play.setImageResource(R.drawable.play);
            txtCurrentTime.setText("00:00");
            isPlaying = false;
        }
    }
}
