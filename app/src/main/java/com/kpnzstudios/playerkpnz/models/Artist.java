package com.kpnzstudios.playerkpnz.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class Artist implements Serializable, Comparable<Artist> {

    private ArrayList<Album> albums;

    private String artistName;

    public Artist(String name){
        this.artistName = name;
        albums = new ArrayList<Album>();
    }

    public void addAlbum(Album album){
        if (!albums.contains(album)) {
            albums.add(album);
        }
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

    @Override
    public int compareTo(Artist o) {
        return this.getName().compareTo(o.getName());
    }
}
