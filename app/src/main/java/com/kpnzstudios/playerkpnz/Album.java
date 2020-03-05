package com.kpnzstudios.playerkpnz;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Album {

    private String albumName;
    private String albumArtist;
    private ArrayList<Music> albumMusicas;
    private long id;
    Bitmap art;

    public Album(long id, String name, String artist, Context c) {
        this.albumName = name;
        this.albumArtist = artist;
        this.albumMusicas = new ArrayList<Music>();
        this.id = id;

        Uri sArtworkUri = Uri
                .parse("content://media/external/audio/albumart");
        Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, id);

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(
                    c.getContentResolver(), albumArtUri);
            bitmap = Bitmap.createScaledBitmap(bitmap, 120, 120, true);

        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            this.art = bitmap;
            Log.i("foi","aaa");
        }
    }

    public void addMusic(Music music){
        if (!albumMusicas.contains(music)){
            albumMusicas.add(music);
        }
    }

    public long getId(){
        return this.id;
    }

    public int getSize(){
        return albumMusicas.size();
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getAlbumArtist() {
        return albumArtist;
    }

    public Bitmap getArt(){
        return art;
    }

    public ArrayList<Music> getAlbumMusicas() {
        return albumMusicas;
    }
}
