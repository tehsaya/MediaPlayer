package com.gjnsu.mediaplayer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by GjnSu on 7/22/2015.
 */
public class LyricFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.lyric_fragment, null);
        TextView tv = (TextView) view.findViewById(R.id.lyric_fragment_tv);
        tv.setText("Hello Lyric Fragment");
        return view;
    }
}
