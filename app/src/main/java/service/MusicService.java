package service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.gjnsu.mediaplayer.MainControlScreen;
import com.gjnsu.mediaplayer.R;

import java.io.IOException;
import java.util.ArrayList;

import Controller.SongManager;
import model.Song;

/**
 * Created by GjnSu on 7/22/2015.
 */
public class MusicService extends Service implements MediaPlayer.OnPreparedListener
        , MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {
    private MediaPlayer mPlayer;
    private int mSongPosition;
    private ArrayList<Song> mPlayList;
    private boolean isShuffle = false;
    private boolean isFirstTime = true;
    public static final String TAG = "MUSICSERVICE";

    public static final String IM_PLAYING = "service.MusicService.status.playing";
    public static final String IM_PAUSE = "service.MusicService.status.pause";
    public static final String MUSIC_CHANGE = "service.MusicService.event.music_change";
    public static final String PREV_ACTION = "service.MusicService.action.prev";
    public static final String NEXT_ACTION = "service.MusicService.action.next";
    public static final String PLAY_ACTION = "service.MusicService.action.play";
    public static final String START_SEEKBAR = "service.MusicService.action.start_seekbar";

    public static final int FOREGROUND_ID = 8893;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        switch (intent.getAction()) {
            case PREV_ACTION:
                if (isFirstTime) {
                    startService();
                    isFirstTime = false;
                } else {
                    prevSong();
                }
                break;
            case NEXT_ACTION:
                if (isFirstTime) {
                    startService();
                    isFirstTime = false;
                } else {
                    nextSong();
                }
                break;
            case PLAY_ACTION:
                if (isFirstTime) {
                    startService();
                    isFirstTime = false;
                } else {
                    playSong();
                }
                break;
            default:
                break;
        }
        return START_STICKY;
    }

    private void startService() {
        Log.i(TAG, "Received StartForeground Intent");

        Intent notificationIntent = new Intent(this, MainControlScreen.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);


        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Intent previousIntent = new Intent(this, MusicService.class);
        previousIntent.setAction(PREV_ACTION);
        PendingIntent ppreviousIntent = PendingIntent.getService(this, 0,
                previousIntent, 0);

        Intent playIntent = new Intent(this, MusicService.class);
        playIntent.setAction(PLAY_ACTION);
        PendingIntent pplayIntent = PendingIntent.getService(this, 0,
                playIntent, 0);

        Intent nextIntent = new Intent(this, MusicService.class);
        nextIntent.setAction(NEXT_ACTION);
        PendingIntent pnextIntent = PendingIntent.getService(this, 0,
                nextIntent, 0);

        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.shiba_inu);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Truiton Music Player")
                .setTicker("Truiton Music Player")
                .setContentText("My Music")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(
                        Bitmap.createScaledBitmap(icon, 128, 128, false))
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .addAction(android.R.drawable.ic_media_previous,
                        "Previous", ppreviousIntent)
                .addAction(android.R.drawable.ic_media_play, "Play",
                        pplayIntent)
                .addAction(android.R.drawable.ic_media_next, "Next",
                        pnextIntent).build();
        startForeground(FOREGROUND_ID, notification);
        startSong(mSongPosition);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPlayList = new SongManager(this).getAllSongs();
        mSongPosition = 0;
        mPlayer = new MediaPlayer();
        initMusicPlayer();
    }


    private void initMusicPlayer() {
        mPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPlayer.setOnCompletionListener(this);
        mPlayer.setOnErrorListener(this);
        mPlayer.setOnPreparedListener(this);
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        if (isShuffle) {
            //TODO next random music
        } else {
            mSongPosition += 1;
        }
        stopSong();
        playSong();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();

        Intent intent = new Intent(START_SEEKBAR);
        intent.putExtra("Duration",  mPlayer.getDuration());
        sendBroadcast(intent);

    }


    /**
     * Play Other Song when user click Button Previous.
     */
    public void prevSong() {
        if (isFirstTime) {
            isFirstTime = false;
        }
        stopSong();
        if (mSongPosition != 0) {
            mSongPosition -= 1;
        } else {
            mSongPosition = mPlayList.size() - 1;
        }
        startSong(mSongPosition);
        sendBr(MUSIC_CHANGE);
    }

    /**
     * When user click button play on GUI.
     */
    public void playSong() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            sendBr(IM_PAUSE);


        } else {
            sendBr(IM_PLAYING);
            mPlayer.start();
        }
        Log.i(TAG, "isPlaying :" + mPlayer.isPlaying());
    }

    /**
     * Next to other song when user clicks button next.
     */
    public void nextSong() {
        if (isFirstTime) {
            isFirstTime = false;
        }
        if (!mPlayer.isPlaying()) {
            //TODO next selector.
            // if media playing is not playing so next focus to other song and do nothing.
            focusNextOne();
        } else {
            stopSong();
            mSongPosition += 1;
            startSong(mSongPosition);
        }
        sendBr(MUSIC_CHANGE);
    }

    public void seekTime() {
        //TODO
    }

    private void focusNextOne() {
        //TODO
    }

    // private Methods.

    /**
     * Starts a new Song then create intent to inform.
     *
     * @param position plays song at the position.
     */
    private void startSong(int position) {
        Log.i(TAG, "startSong :" + position);
        // Get song is chosen to play.
        Song song = mPlayList.get(position);
        String duration = song.getDuration();
        if (mPlayer == null) {
            mPlayer = new MediaPlayer();
        }
        try {
            mPlayer.setDataSource(getApplicationContext(), Uri.parse(song.getPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mPlayer.prepareAsync();
        sendBr(IM_PLAYING);

    }

    /**
     * If Media player is playing then stop it restart it after that create an intent to inform.
     */
    private void stopSong() {
        if (mPlayer.isPlaying()) {
            mPlayer.stop();
        }
        mPlayer.reset();
        sendBr(IM_PAUSE);
    }

    /**
     * Create a Intent and send it via broadcast.
     *
     * @param action is used to create Intent.
     */
    private void sendBr(String action) {
        Intent intent = new Intent(action);
        sendBroadcast(intent);
    }


}
