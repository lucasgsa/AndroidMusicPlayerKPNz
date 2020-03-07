package com.kpnzstudios.playerkpnz.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.kpnzstudios.playerkpnz.models.Fila;
import com.kpnzstudios.playerkpnz.service.MusicService;
import com.kpnzstudios.playerkpnz.util.MusicOrganizador;
import com.kpnzstudios.playerkpnz.R;
import com.kpnzstudios.playerkpnz.adapters.adapterAlbum;
import com.kpnzstudios.playerkpnz.adapters.adapterArtist;
import com.kpnzstudios.playerkpnz.adapters.adapterMusic;
import com.kpnzstudios.playerkpnz.communicator.AlbumSender;
import com.kpnzstudios.playerkpnz.communicator.ArtistSender;
import com.kpnzstudios.playerkpnz.models.Album;
import com.kpnzstudios.playerkpnz.models.Artist;
import com.kpnzstudios.playerkpnz.models.Music;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener{

    MusicOrganizador musicOrganizador;

    String listType;

    int atualLista;

    public static MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checarPermissao();
        musicOrganizador = new MusicOrganizador(getBaseContext());
        onClick();
        instance = this;
        setListAll();
        linkarService();
        ((SeekBar) findViewById(R.id.seekBar)).setOnSeekBarChangeListener(this);
    }

    public void linkarService(){
        Intent intent = new Intent(this, MusicService.class);
        intent.setAction("kpnz.activityOpened");
        startForegroundService(intent);
    }

    public void onClick(){
        ListView lista_view = findViewById(R.id.list_view);
        lista_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listType.equalsIgnoreCase("music")) {
                    Music selected = ((Music) parent.getItemAtPosition(position));
                    musicSelected(musicOrganizador.getMusicas(), position);
                }
                else if(listType.equalsIgnoreCase("album")){
                    Album selected = ((Album) parent.getItemAtPosition(position));
                    setListAlbumEspecifico(selected);
                }
                else if(listType.equalsIgnoreCase("artist")){
                    Artist selected = ((Artist) parent.getItemAtPosition(position));
                    setListArtistaEspecifico(selected);
                }
            };
        });
    }

    public void checarPermissao(){
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                musicOrganizador.carregar();
                setListAll();
            } else {
                checarPermissao();
            }
        }
    }

    public void setListAll(){
        atualLista = 0;
        listType = "music";
        ListView listView = findViewById(R.id.list_view);
        adapterMusic adapter = new adapterMusic(musicOrganizador.getMusicas(), this);
        listView.setAdapter(adapter);
    }
    public void setListAlbum(){
        atualLista = 1;
        listType = "album";
        ListView listView = findViewById(R.id.list_view);
        adapterAlbum adapter = new adapterAlbum(musicOrganizador.getAlbums(), this);
        listView.setAdapter(adapter);
    }

    public void setListArtist(){
        atualLista = 2;
        listType = "artist";
        ListView listView = findViewById(R.id.list_view);
        adapterArtist adapter = new adapterArtist(musicOrganizador.getArtists(), this);
        listView.setAdapter(adapter);
    }

    public void setListAlbumEspecifico(Album album){
        Intent intent = new Intent(this, AlbumActivity.class);
        AlbumSender tempAlbum = new AlbumSender(album);
        intent.putExtra("album", tempAlbum);
        startActivity(intent);
    }

    public void setListArtistaEspecifico(Artist artista){
        Intent intent = new Intent(this, ArtistActivity.class);
        intent.putExtra("artist", new ArtistSender(artista));
        startActivity(intent);
    }

    public void buttonTodas(View view){
        setListAll();
    }

    public void buttonAlbum(View view){
        setListAlbum();
    }

    public void buttonArtist(View view){ setListArtist(); }

    public void swipeDireita(){
        if (atualLista == 0){
            setListAlbum();
        }
        else if(atualLista == 1){
            setListArtist();
        }
    }

    public void swipeEsquerda(){
        if (atualLista == 1){
            setListAll();
        }
        else if (atualLista == 2){
            setListAlbum();
        }
    }

    public void resumeOrPause(View view){
        Intent intent = new Intent(this, MusicService.class);
        intent.setAction("kpnz.resumeOrPause");
        ContextCompat.startForegroundService(this, intent);
    }

    public void nextMusic(View view){
        Intent intent = new Intent(this, MusicService.class);
        intent.setAction("kpnz.next");
        ContextCompat.startForegroundService(this, intent);
    }

    public void backMusic(View view){
        Intent intent = new Intent(this, MusicService.class);
        intent.setAction("kpnz.back");
        ContextCompat.startForegroundService(this, intent);
    }

    private void musicSelected(ArrayList<Music> musicas, int posicaoInicial){
        Fila fila = new Fila(musicas, posicaoInicial);
        Intent intent = new Intent(this, MusicService.class);
        intent.setAction("kpnz.start");
        intent.putExtra("fila", fila);
        ContextCompat.startForegroundService(this, intent);;
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent(this, MusicService.class);
        intent.setAction("kpnz.activityFinished");
        startService(intent);
        super.onDestroy();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        SeekBar sk = MainActivity.instance.findViewById(R.id.seekBar);
        Intent intent = new Intent(this, MusicService.class);
        if (!fromUser) return;
        intent.setAction("kpnz.changedSeekBar");
        intent.putExtra("progress", progress);
        startService(intent);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
