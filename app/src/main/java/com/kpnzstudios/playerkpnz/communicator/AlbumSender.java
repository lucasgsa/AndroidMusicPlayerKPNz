package com.kpnzstudios.playerkpnz.communicator;

import android.content.Context;

import com.kpnzstudios.playerkpnz.models.Album;
import com.kpnzstudios.playerkpnz.models.Music;

import java.io.Serializable;
import java.util.ArrayList;

public class AlbumSender implements Serializable {

    private String name;
    private String artist;
    private ArrayList<Music> musics;
    private Long id;

    public Long getId() {
        return id;
    }

    public AlbumSender(Album album){
        this.name = album.getAlbumName();
        this.musics = album.getAlbumMusicas();
        this.id = album.getId();
        this.artist = album.getAlbumArtist();
    }

    public String getAlbumName() {
        return name;
    }

    public String getAlbumArtist() {
        return artist;
    }

    public ArrayList<Music> getAlbumMusicas() {
        return musics;
    }

    public Album getRealAlbum(Context c){
        Album temp = new Album(this.id, this.name, this.artist, c);
        for (Music music:musics){
            temp.addMusic(music);
        }
        return temp;
    }
}
