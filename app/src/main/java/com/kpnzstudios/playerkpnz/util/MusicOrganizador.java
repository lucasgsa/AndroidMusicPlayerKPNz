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
        lista_musicas = new ArrayList<Music>();
        lista_albuns = new HashMap<Long, Album>();
        lista_artistas = new HashMap<String, Artist>();
        this.c = arg0;
    }

    public void carregar(){
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

    private void reloadListaMusicas(){
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

    private void addInAlbum(Music musica){
        if (!lista_albuns.containsKey(musica.getAlbumID())){
            lista_albuns.put(musica.getAlbumID(), new Album(musica.getAlbumID(), musica.getAlbum(), musica.getArtist(), c));
        }
        lista_albuns.get(musica.getAlbumID()).addMusic(musica);
        addInArtist(lista_albuns.get(musica.getAlbumID()));
    }

    private void addInArtist(Album album){
        if (!lista_artistas.containsKey(album.getAlbumArtist())){
            lista_artistas.put(album.getAlbumArtist(), new Artist(album.getAlbumArtist()));
        }
        lista_artistas.get(album.getAlbumArtist()).addAlbum(album);
    }

}
