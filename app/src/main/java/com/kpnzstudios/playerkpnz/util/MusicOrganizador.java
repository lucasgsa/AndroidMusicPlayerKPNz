package com.kpnzstudios.playerkpnz.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.kpnzstudios.playerkpnz.models.Album;
import com.kpnzstudios.playerkpnz.models.Artist;
import com.kpnzstudios.playerkpnz.models.Music;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class MusicOrganizador {

    private ArrayList<Music> lista_musicas;
    private HashMap<Long, Album> lista_albuns;
    private HashMap<String, Artist> lista_artistas;

    private Context c;

    public MusicOrganizador(Context arg0){
        this.c = arg0;
        reloadArtist();
        reloadAlbum();
        reloadListaMusicas();
    }

    public ArrayList<Music> getMusicas(){
        return lista_musicas;
    }
    public ArrayList<Album> getAlbums() {
        ArrayList<Album> temp = new ArrayList<Album>();
        temp.addAll(lista_albuns.values());
        return temp;
    }

    public ArrayList<Artist> getArtists() {
        ArrayList<Artist> temp = new ArrayList<Artist>();
        temp.addAll(lista_artistas.values());
        return temp;
    }

    public void reloadListaMusicas(){
        lista_musicas = new ArrayList<Music>();
        ContentResolver musicResolver = c.getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ARTIST);
            int albumColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ALBUM);
            int albumIDColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ALBUM_ID);
            int isMusic = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.IS_MUSIC);
            int isPodcast = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.IS_PODCAST);
            int durationColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.DURATION);
            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                String thisAlbum = musicCursor.getString(albumColumn);
                long thisAlbumID = musicCursor.getLong(albumIDColumn);
                int thisDuration = musicCursor.getInt(durationColumn);
                int pod = musicCursor.getInt(isPodcast);
                int mus = musicCursor.getInt(isMusic);
                Music temp = null;
                if (pod != 0) {
                    temp = new Music(thisId, thisTitle, thisArtist, thisAlbum, thisAlbumID, thisDuration, "podcast");
                }
                else if (mus != 0){
                    temp = new Music(thisId, thisTitle, thisArtist, thisAlbum, thisAlbumID, thisDuration,"music");
                }
                else{
                    continue;
                }
                addInAlbum(temp);
                lista_musicas.add(temp);
            }
            while (musicCursor.moveToNext());
        }
        Collections.sort(lista_musicas);
    }

    public void addInAlbum(Music musica){
        lista_albuns.get(musica.getAlbumID()).addMusic(musica);
    }

    public void addInArtist(Album album){
        lista_artistas.get(album.getAlbumArtist()).addAlbum(album);
    }

    public void reloadAlbum(){
        lista_albuns = new HashMap<Long, Album>();
        ContentResolver musicResolver = c.getContentResolver();
        Uri musicUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns
            int artistColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Albums.ARTIST);
            int nameColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Albums.ALBUM);
            int idColumn = musicCursor.getColumnIndexOrThrow
                    (MediaStore.Audio.Albums._ID);
            //add songs to list
            do {
                String thisTitle = musicCursor.getString(nameColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                long thisAlbumID = musicCursor.getLong(idColumn);
                Album temp = new Album(thisAlbumID, thisTitle, thisArtist, this.c);
                lista_albuns.put(thisAlbumID ,temp);
                addInArtist(temp);
            }
            while (musicCursor.moveToNext());
        }
    }

    public void reloadArtist(){
        lista_artistas = new HashMap<String, Artist>();
        ContentResolver musicResolver = c.getContentResolver();
        Uri musicUri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns
            int artistColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Artists.ARTIST);
            //add songs to list
            do {
                String thisArtist = musicCursor.getString(artistColumn);
                Artist temp = new Artist(thisArtist);
                lista_artistas.put(thisArtist,temp);
            }
            while (musicCursor.moveToNext());
        }
    }
}
