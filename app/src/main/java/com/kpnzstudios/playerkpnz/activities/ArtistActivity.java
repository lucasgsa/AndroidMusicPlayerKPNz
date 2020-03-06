package com.kpnzstudios.playerkpnz.activities;

import androidx.appcompat.app.AppCompatActivity;

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
                return;
            };
        });
    }
}
