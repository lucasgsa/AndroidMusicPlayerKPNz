package com.kpnzstudios.playerkpnz.models;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class Album implements Serializable {

    private String albumName;
    private String albumArtist;
    private ArrayList<Music> albumMusicas;
    private long id;
    private Bitmap bitmap;
    private Context c;

    public Album(long id, String name, String artist, Context c) {
        this.albumName = name;
        this.albumArtist = artist;
        this.albumMusicas = new ArrayList<Music>();
        this.id = id;
        this.c = c;
        this.bitmap = reloadArt();
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

    public Bitmap reloadArt() {
        Uri sArtworkUri = Uri
                .parse("content://media/external/audio/albumart");
        Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, id);

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(
                    this.c.getContentResolver(), albumArtUri);
            bitmap = Bitmap.createScaledBitmap(bitmap, 120, 120, true);

        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            return bitmap;
        }
    }

    public Bitmap getArt(){
        return bitmap;
    }

    public ArrayList<Music> getAlbumMusicas() {
        return albumMusicas;
    }
}
