package com.kpnzstudios.playerkpnz;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.kpnzstudios.playerkpnz.adapters.adapterAlbum;
import com.kpnzstudios.playerkpnz.adapters.adapterArtist;
import com.kpnzstudios.playerkpnz.adapters.adapterMusic;
import com.kpnzstudios.playerkpnz.communicator.AlbumSender;
import com.kpnzstudios.playerkpnz.communicator.ArtistSender;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    MusicOrganizador musicOrganizador;

    String listType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checarPermissao();
        musicOrganizador = new MusicOrganizador(getBaseContext());
        onClick();
    }

    public void onClick(){
        ListView lista_view = findViewById(R.id.list_view);
        lista_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listType.equalsIgnoreCase("music")) {
                    Music selected = ((Music) parent.getItemAtPosition(position));
                    musicSelected(selected);
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
                setListAll();
            } else {
                checarPermissao();
            }
        }
    }

    public void setListAll(){
        listType = "music";
        ListView listView = findViewById(R.id.list_view);
        adapterMusic adapter = new adapterMusic(musicOrganizador.getMusicas(), this);
        listView.setAdapter(adapter);
    }
    public void setListAlbum(){
        listType = "album";
        ListView listView = findViewById(R.id.list_view);
        adapterAlbum adapter = new adapterAlbum(musicOrganizador.getAlbums(), this);
        listView.setAdapter(adapter);
    }

    public void setListArtist(){
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

    public void musicSelected(Music musica){
        ((LinearLayout) findViewById(R.id.layout_barDown)).setVisibility(View.VISIBLE);
    }
}
