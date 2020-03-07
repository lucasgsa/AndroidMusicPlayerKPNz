package com.kpnzstudios.playerkpnz.models;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;

public class Music implements Comparable<Music>, Serializable {

    private long id;
    private String title;
    private String artistName;
    private String albumName;
    private long albumID;
    private String type;
    private int duration;

    public Music(long songID, String songTitle, String songArtist, String songAlbum, long songAlbumID, int durationSong, String songType) {
        this.id=songID;
        this.title=songTitle;
        this.artistName=songArtist;
        this.albumName = songAlbum;
        this.type = songType;
        this.duration = durationSong/1000;
        this.albumID = songAlbumID;
    }

    public String getDuration(){
        return String.format("%02d:%02d", duration / 60, duration % 60);
    }

    public long getAlbumID(){
        return albumID;
    }

    public long getID(){
        return id;
    }


    public String getTitle(){
        return title;
    }
    public String getArtist(){
        return artistName;
    }
    public String getAlbum(){
        return albumName;
    }

    @Override
    public String toString() {
        return title+" - "+artistName;
    }

    @Override
    public int compareTo(Music o) {
        return this.getTitle().compareTo(o.getTitle());
    }

    public Bitmap getArt(Context c) {
        Uri sArtworkUri = Uri
                .parse("content://media/external/audio/albumart");
        Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumID);
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(
                    c.getContentResolver(), albumArtUri);
            bitmap = Bitmap.createScaledBitmap(bitmap, 120, 120, true);

        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
            bitmap = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            return bitmap;
        }
    }
}

