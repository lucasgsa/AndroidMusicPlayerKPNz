package com.kpnzstudios.playerkpnz;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.Collections;

public class MusicOrganizador {

    private ArrayList<Music> lista_musicas;
    private ArrayList<Album> lista_albuns;

    private Context c;

    public MusicOrganizador(Context arg0){
        this.c = arg0;
        reloadAlbum();
        reloadListaMusicas();
    }

    public ArrayList<Music> getMusicas(){
        return lista_musicas;
    }
    public ArrayList<Album> getAlbums() { return lista_albuns; }

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
                addInAlbum(temp);
                lista_musicas.add(temp);
            }
            while (musicCursor.moveToNext());
        }
        Collections.sort(lista_musicas);
    }

    public void addInAlbum(Music musica){
        for (Album album:lista_albuns){
            if (album.getId() == musica.getAlbumID()){
                album.addMusic(musica);
                return;
            }
        }
    }

    public void reloadAlbum(){
        lista_albuns = new ArrayList<Album>();
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
                lista_albuns.add(new Album(thisAlbumID, thisTitle, thisArtist, this.c));
            }
            while (musicCursor.moveToNext());
        }
    }
}
