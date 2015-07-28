package com.gjnsu.mediaplayer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Created by GjnSu on 7/22/2015.
 */
public class ImageSongFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.image_song_fragment, null);
        ImageView imgSong = (ImageView) view.findViewById(R.id.image_song_content);
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.spin_around);
        imgSong.setAnimation(animation);
        return view;
    }
}
