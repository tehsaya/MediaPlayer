package com.gjnsu.mediaplayer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import Controller.SongManager;
import model.Song;

/**
 * Created by GjnSu on 7/22/2015.
 */
public class ListPlayFragment extends ListFragment {

    private ArrayList<Song> mListSong;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListSong = new ArrayList<>();
        SongManager manager = new SongManager(getActivity());
        mListSong = manager.getAllSongs();

        Log.d("DATA", String.valueOf(mListSong.size()));
        ListPlayAdapter adapter = new ListPlayAdapter(getActivity(),
                R.layout.item_listplay, mListSong);
        setListAdapter(adapter);

    }

    public ArrayList<Song> getListPlay() {
        return mListSong;
    }

}
