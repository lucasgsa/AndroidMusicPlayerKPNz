package com.kpnzstudios.playerkpnz.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.kpnzstudios.playerkpnz.R;
import com.kpnzstudios.playerkpnz.adapters.adapterAlbum;
import com.kpnzstudios.playerkpnz.adapters.adapterMusic;
import com.kpnzstudios.playerkpnz.communicator.AlbumSender;
import com.kpnzstudios.playerkpnz.communicator.ArtistSender;
import com.kpnzstudios.playerkpnz.models.Album;
import com.kpnzstudios.playerkpnz.models.Artist;
import com.kpnzstudios.playerkpnz.models.Fila;
import com.kpnzstudios.playerkpnz.models.Music;
import com.kpnzstudios.playerkpnz.service.MusicService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class ArtistActivity extends AppCompatActivity {

    private Artist artista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);
        ArtistSender artistaSender = (ArtistSender) getIntent().getSerializableExtra("artist");
        artista = artistaSender.getArtistReal(getBaseContext());
        ((TextView) findViewById(R.id.artist_ArtistName)).setText(artista.getName());
        ((TextView) findViewById(R.id.artist_albumsSize)).setText(artista.getAlbumsCount()+" álbuns");
        ((TextView) findViewById(R.id.artist_musicsSize)).setText(artista.getMusicsCount()+" músicas");

        configListAlbum();
        configListMusic();
    }

    public void configListAlbum(){
        ListView lista_view = findViewById(R.id.artist_list_albuns);
        adapterAlbum adapter = new adapterAlbum(artista.getAlbums(), this);
        lista_view.setAdapter(adapter);
        lista_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ArtistActivity.this, AlbumActivity.class);
                AlbumSender tempAlbum = new AlbumSender((Album) parent.getItemAtPosition(position));
                intent.putExtra("album", tempAlbum);
                startActivity(intent);
            };
        });
    }

    public void configListMusic(){
        ListView lista_view = findViewById(R.id.artist_list_musicas);
        adapterMusic adapter = new adapterMusic(artista.getMusics(), this);
        lista_view.setAdapter(adapter);
        lista_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Music selected = ((Music) parent.getItemAtPosition(position));
                musicSelected(artista.getMusics(), position);
            };
        });
    }

    private void musicSelected(ArrayList<Music> musicas, int posicaoInicial){
        Fila fila = new Fila(musicas, posicaoInicial);
        Intent intent = new Intent(this, MusicService.class);
        intent.setAction("kpnz.start");
        intent.putExtra("fila", fila);
        ContextCompat.startForegroundService(this, intent);
    }

    public void random(View view){
        ArrayList<Music> temp = new ArrayList<Music>(artista.getMusics());
        Collections.shuffle(temp);
        musicSelected(temp, 0);
    }
}
