package com.gjnsu.mediaplayer;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import model.Song;

/**
 * Created by GjnSu on 7/22/2015.
 */
public class ListPlayAdapter extends ArrayAdapter<Song> {
    private ArrayList<Song> mListSong;


    public ListPlayAdapter(Context context, int resource, ArrayList<Song> mListSong) {
        super(context, resource);
        this.mListSong = mListSong;
    }


    @Override
    public int getCount() {
        return mListSong.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(getContext(), R.layout.item_listplay, null);
            viewHolder.tvSongName = (TextView) convertView
                    .findViewById(R.id.item_playlist_song_name);
            viewHolder.tvArtistName = (TextView) convertView
                    .findViewById(R.id.item_playlist_artist_name);
            viewHolder.imgWave = (ImageView) convertView.findViewById(R.id.item_listplay_iv_wave);
            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.tvSongName.setText(mListSong.get(position).getSongName());
        viewHolder.tvArtistName.setText(mListSong.get(position).getArtistName());

        return convertView;
    }

    private class ViewHolder {
        TextView tvSongName;
        TextView tvArtistName;
        ImageView imgWave;
    }

    @Override
    public Song getItem(int position) {
        return mListSong.get(position);
    }
}
