package com.kpnzstudios.playerkpnz;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.kpnzstudios.playerkpnz.adapters.adapterAlbum;
import com.kpnzstudios.playerkpnz.adapters.adapterMusic;

public class MainActivity extends AppCompatActivity {

    MusicOrganizador musicOrganizador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checarPermissao();
        musicOrganizador = new MusicOrganizador(getBaseContext());
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

    public void setList(String type){
        if ("all".equalsIgnoreCase(type)){
            setListAll();
        }
    }

    public void setListAll(){
        ListView listView = findViewById(R.id.list_view);
        adapterMusic adapter = new adapterMusic(musicOrganizador.getMusicas(), this);
        listView.setAdapter(adapter);
    }
    public void setListAlbum(){
        ListView listView = findViewById(R.id.list_view);
        adapterAlbum adapter = new adapterAlbum(musicOrganizador.getAlbums(), this);
        listView.setAdapter(adapter);
    }

    public void buttonTodas(View view){
        setListAll();
    }

    public void buttonAlbum(View view){
        setListAlbum();
    }
}
