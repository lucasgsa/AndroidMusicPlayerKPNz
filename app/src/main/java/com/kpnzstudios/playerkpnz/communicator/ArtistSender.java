package com.kpnzstudios.playerkpnz.communicator;

import android.content.Context;

import com.kpnzstudios.playerkpnz.models.Album;
import com.kpnzstudios.playerkpnz.models.Artist;

import java.io.Serializable;
import java.util.ArrayList;

public class ArtistSender implements Serializable {

    private ArrayList<AlbumSender> albums;
    private String artistName;

    public ArtistSender(Artist artist){
        this.artistName = artist.getName();
        albums = new ArrayList<AlbumSender>();
        for (Album album:artist.getAlbums()){
            albums.add(new AlbumSender(album));
        }
    }

    public String getName(){
        return artistName;
    }

    public ArrayList<AlbumSender> getAlbums(){
        return albums;
    }

    public Artist getArtistReal(Context c){
        Artist temp = new Artist(artistName);
        for (AlbumSender album:albums){
            temp.addAlbum(album.getRealAlbum(c));
        }
        return temp;
    }


}
