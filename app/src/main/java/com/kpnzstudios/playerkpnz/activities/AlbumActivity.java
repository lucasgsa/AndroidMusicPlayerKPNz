package com.kpnzstudios.playerkpnz.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kpnzstudios.playerkpnz.R;
import com.kpnzstudios.playerkpnz.adapters.adapterMusic;
import com.kpnzstudios.playerkpnz.communicator.AlbumSender;
import com.kpnzstudios.playerkpnz.models.Album;
import com.kpnzstudios.playerkpnz.models.Fila;
import com.kpnzstudios.playerkpnz.models.Music;
import com.kpnzstudios.playerkpnz.service.MusicService;

import java.util.ArrayList;
import java.util.Collections;

public class AlbumActivity extends AppCompatActivity {

    private Album album;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        AlbumSender albumSender = (AlbumSender) getIntent().getExtras().getSerializable("album");
        album = albumSender.getRealAlbum(getBaseContext());
        if (album.getArt() != null) {
            ((ImageView) findViewById(R.id.album_image)).setImageDrawable(getRoundedArt());
        }
        ((TextView) findViewById(R.id.album_ArtistName)).setText(album.getAlbumArtist());
        ((TextView) findViewById(R.id.album_albumName)).setText(album.getAlbumName());
        setList();
        onClick();
    }

    public RoundedBitmapDrawable getRoundedArt(){
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), album.getArt());
        roundedBitmapDrawable.setCircular(true);
        return roundedBitmapDrawable;
    }

    public void setList(){
        ListView listView = findViewById(R.id.list_musics);
        adapterMusic adapter = new adapterMusic(album.getAlbumMusicas(), this);
        listView.setAdapter(adapter);
    }

    public void onClick(){
        ListView lista_view = findViewById(R.id.list_musics);
        lista_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Music selected = ((Music) parent.getItemAtPosition(position));
                musicSelected(album.getAlbumMusicas(), position);
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
        ArrayList<Music> temp = new ArrayList<Music>(album.getAlbumMusicas());
        Collections.shuffle(temp);
        musicSelected(temp, 0);
    }
}
