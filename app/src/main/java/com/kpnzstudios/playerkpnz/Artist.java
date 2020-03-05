package com.kpnzstudios.playerkpnz;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Artist implements Serializable {

    private ArrayList<Album> albums;

    private String artistName;

    public Artist(String name){
        this.artistName = name;
        albums = new ArrayList<Album>();
    }

    public void addAlbum(Album album){
        albums.add(album);
    }

    public String getName(){
        return artistName;
    }

    public ArrayList<Album> getAlbums(){
        return albums;
    }

    public ArrayList<Music> getMusics(){
        ArrayList<Music> temp = new ArrayList<Music>();
        for (Album album:albums){
            temp.addAll(album.getAlbumMusicas());
        }
        Collections.sort(temp);
        return temp;
    }

    public int getAlbumsCount(){
        return albums.size();
    }

    public int getMusicsCount(){
        int temp = 0;
        for (Album album:albums){
            temp += album.getSize();
        }
        return temp;
    }

}
