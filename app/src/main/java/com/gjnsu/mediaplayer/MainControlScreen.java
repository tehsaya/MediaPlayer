package com.gjnsu.mediaplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import service.MusicService;

import static com.gjnsu.mediaplayer.R.id.panel_control_element_btn_previous;

public class MainControlScreen extends FragmentActivity {
    public static final int NUMBER_OF_PAGERS = 3;
    private ViewPager viewPager;
    private ImageButton btnPlay;
    private ImageButton btnNext;
    public SeekBar sbDuration;
    private ImageButton btnPrev;
    private TextView tvDuration;
    private BRMusic brMusic;
    private IntentFilter filter;
    private Utils utils;


    public static final String SC_CLICK_ITEM_LISTPLAY = "maincontrolscreen.action.click.item_listplay";
    public static final String KEY_DATA = "maincontrolscreen.value.keydata";
    private static final int DEFAULT_DATA = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_control_screen);
        initView();
        initFilter();
        //Set onClick for each button controls.
        btnPlay.setOnClickListener(listener);
        btnNext.setOnClickListener(listener);
        btnPrev.setOnClickListener(listener);
        sbDuration.setOnSeekBarChangeListener(sbListener);
    }

    private void initFilter() {
        brMusic = new BRMusic();
        filter = new IntentFilter();
        filter.addAction(MusicService.IM_PAUSE);
        filter.addAction(MusicService.IM_PLAYING);
        filter.addAction(MusicService.MUSIC_CHANGE);
        filter.addAction(MusicService.START_SEEKBAR);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(brMusic, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(brMusic);
    }

    private void initView() {
        tvDuration = (TextView) findViewById(R.id.tv_total_duration);
        sbDuration = (SeekBar) findViewById(R.id.custom_sb);
        viewPager = (ViewPager) findViewById(R.id.vp_main_content);
        btnPlay = (ImageButton) findViewById(R.id.panel_control_element_btn_play);
        btnNext = (ImageButton) findViewById(R.id.panel_control_element_btn_next);
        btnPrev = (ImageButton) findViewById(panel_control_element_btn_previous);
        utils = new Utils();
        FragmentManager fragmentManager = getSupportFragmentManager();
        //create Adapter for viewpager.
        final ViewPagerAdapter adapter = new ViewPagerAdapter(fragmentManager);
        viewPager.setAdapter(adapter);

    }

    SeekBar.OnSeekBarChangeListener sbListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            Log.d("Progress", seekBar.getProgress() + "");
        }
    };
    //Handle Event buttons are clicked.
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.panel_control_element_btn_play:
                    createIntent(MusicService.PLAY_ACTION, DEFAULT_DATA);
                    break;
                case R.id.panel_control_element_btn_next:
                    createIntent(MusicService.NEXT_ACTION, DEFAULT_DATA);
                    break;
                case panel_control_element_btn_previous:
                    createIntent(MusicService.PREV_ACTION, DEFAULT_DATA);
                    break;
                default:
                    break;
            }
        }
    };

    private void createIntent(String action, int data) {
        Intent intent = new Intent(MainControlScreen.this, MusicService.class);
        intent.setAction(action);
        if (data != 0) {
            intent.putExtra(KEY_DATA, data);
        }
        startService(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private LyricFragment lyricFragment;
        private ImageSongFragment imageSongFragment;
        private ListPlayFragment playFragment;


        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            lyricFragment = new LyricFragment();
            imageSongFragment = new ImageSongFragment();
            playFragment = new ListPlayFragment();
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return lyricFragment;
                case 1:
                    return imageSongFragment;
                case 2:
                    return playFragment;
                default:
                    break;
            }
            return null;
        }

        @Override
        public int getCount() {
            return NUMBER_OF_PAGERS;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //Do Nothing
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    class BRMusic extends BroadcastReceiver {
        private final String TAG = BRMusic.class.getName();

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, " : onReceive " + intent.getAction());
            switch (intent.getAction()) {
                case MusicService.START_SEEKBAR :
                    Log.i(TAG, " : onReceive : START_SEEKBAR");
                    final int duration = intent.getIntExtra("Duration", 0);
                    final int amoungToupdate = duration / 100;
                    tvDuration.setText(utils.milisToTimer(duration));

                    Timer mTimer = new Timer();
                    mTimer.schedule(new TimerTask() {

                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    if (!(amoungToupdate * sbDuration.getProgress() >= duration)) {
                                        int p = sbDuration.getProgress();
                                        p += 1;
                                        sbDuration.setProgress(p);
                                    }
                                }
                            });
                        }

                        ;
                    },0,amoungToupdate);

                    break;
                case MusicService.IM_PAUSE:
                    Log.i(TAG, " : onReceive : IM_PAUSE");
                    btnPlay.setImageResource(android.R.drawable.ic_media_play);
                    break;
                case MusicService.IM_PLAYING:
                    Log.i(TAG, " : onReceive : IM_PLAYING");

                    btnPlay.setImageResource(android.R.drawable.ic_media_pause);
                    break;
                case MusicService.MUSIC_CHANGE:
                    //TODO Changes title lyric...
                    break;
                default:
                    break;
            }
        }
    }
}
