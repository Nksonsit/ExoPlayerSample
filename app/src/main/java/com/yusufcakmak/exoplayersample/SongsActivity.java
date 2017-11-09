package com.yusufcakmak.exoplayersample;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.yusufcakmak.exoplayersample.model.Song;

import java.util.ArrayList;
import java.util.List;

public class SongsActivity extends AppCompatActivity {

    private Cursor cursor;
    private List<Song> songs = new ArrayList<>();
    private RecyclerView recyclerView;
    private SongAdapter adapter;
    private TextView txtTitle;
    private Toolbar toolbar;
    private TextView txtLiveStreaming;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs);

        TedPermission.with(this)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        init();
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {

                    }
                })
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    private void init() {
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION
        };
        cursor = getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                null);

        while (cursor.moveToNext()) {
            Song song = new Song();
            song.set_ID(cursor.getString(0));
            song.setARTIST(cursor.getString(1));
            song.setTITLE(cursor.getString(2));
            song.setDATA(cursor.getString(3));
            song.setDISPLAY_NAME(cursor.getString(4));
            if (((int) (Long.parseLong(cursor.getString(5)) / 1000)) % 60 == 0) {
                song.setDURATIO("00:" + String.format("%02d", (int) (Long.parseLong(cursor.getString(5)) / 1000)));
            } else {
                song.setDURATIO(String.format("%02d:%02d", ((int) (Long.parseLong(cursor.getString(5)) / 1000)) / 60, ((int) (Long.parseLong(cursor.getString(5)) / 1000)) % 60));
            }
            songs.add(song);
        }


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(20, 30, 20, 0);
            }
        });
        adapter = new SongAdapter(this, songs, new SongAdapter.OnClickItem() {
            @Override
            public void onClickItem(View imgMusic, View txtTitle, int position) {
                Intent intent = new Intent(SongsActivity.this, PlayerActivity.class);
                intent.putExtra("song", songs.get(position));
                ActivityOptions options = null;
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                    options = ActivityOptions.makeSceneTransitionAnimation(SongsActivity.this,
//                            new Pair[]{Pair.create(imgMusic, "songImage"), Pair.create(txtTitle, "songName")});
//                    startActivity(intent, options.toBundle());
//                } else {
                startActivity(intent);
//                }
            }
        });
        recyclerView.setAdapter(adapter);
        initToolbar();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtLiveStreaming = (TextView) findViewById(R.id.txtLiveStreaming);
        txtTitle.setText("Exo-Player Demo");

        txtLiveStreaming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SongsActivity.this, PlayerActivity.class));
            }
        });
    }
}
