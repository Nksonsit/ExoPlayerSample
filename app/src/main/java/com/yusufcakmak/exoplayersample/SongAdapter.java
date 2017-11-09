package com.yusufcakmak.exoplayersample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yusufcakmak.exoplayersample.model.Song;

import java.util.List;

/**
 * Created by ishan on 09-11-2017.
 */

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.MyViewHolder> {
    private OnClickItem onClickItem;
    private List<Song> list;
    private Context context;

    public SongAdapter(Context context, List<Song> list, OnClickItem onClickItem) {
        this.context = context;
        this.list = list;
        this.onClickItem = onClickItem;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_song, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        holder.setValues(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTitle, txtArtist, txtDuration;
        private ImageView imgMusic;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            txtArtist = (TextView) itemView.findViewById(R.id.txtArtist);
            txtDuration = (TextView) itemView.findViewById(R.id.txtDuration);
            imgMusic = (ImageView) itemView.findViewById(R.id.imgMusic);
        }

        public void setValues(Song song) {

            txtTitle.setText(song.getTITLE());
            if (song.getARTIST() != null && song.getARTIST().trim().length() > 0) {
                txtArtist.setText(song.getARTIST());
                txtArtist.setVisibility(View.VISIBLE);
            } else {
                txtArtist.setVisibility(View.GONE);
            }
            txtDuration.setText(song.getDURATIO());


            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(song.getDATA());

            byte[] data = mmr.getEmbeddedPicture();
            if (data != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
//                Glide.with(context).load(bitmap).placeholder(R.drawable.music).error(R.drawable.music).centerCrop().into(imgMusic);
                imgMusic.setImageBitmap(bitmap);
            } else {
                Glide.with(context).load(R.drawable.music).asBitmap().placeholder(R.drawable.music).error(R.drawable.music).into(imgMusic);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickItem.onClickItem(imgMusic,txtTitle,getAdapterPosition());
                }
            });
        }
    }

    public interface OnClickItem {
        void onClickItem(View imgMusic,View txtTitle,int position);
    }
}
