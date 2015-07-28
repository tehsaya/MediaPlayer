package Controller;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;

import model.Song;

/**
 * Created by GjnSu on 7/17/2015.
 */
public class SongManager {

    private final static String TAG = SongManager.class.getName();
    private ArrayList<Song> listSong;
    private Context mContext;
    private final Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

    public SongManager(Context context) {
        listSong = new ArrayList<>();
        this.mContext = context;
    }

    public ArrayList<Song> getAllSongs() {

        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        String[] projection = {
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION
        };

        Cursor cursor = mContext.getContentResolver().query(uri, projection, selection, null, null);
        while (cursor.moveToNext()) {
            String artist = cursor.getString(0);
            String songName = cursor.getString(1);
            String path = cursor.getString(2);
            String duration = cursor.getString(3);
            Song s = new Song(songName, artist, duration, path);
            listSong.add(s);
        }
        Log.d(TAG, "SIZE : " + listSong.size());
        return listSong;
    }
}
