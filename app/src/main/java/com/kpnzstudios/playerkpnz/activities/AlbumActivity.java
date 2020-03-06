package com.kpnzstudios.playerkpnz.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentUris;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kpnzstudios.playerkpnz.R;
import com.kpnzstudios.playerkpnz.adapters.adapterMusic;
import com.kpnzstudios.playerkpnz.communicator.AlbumSender;
import com.kpnzstudios.playerkpnz.models.Album;
import com.kpnzstudios.playerkpnz.models.Music;

import java.io.FileNotFoundException;
import java.io.IOException;

public class AlbumActivity extends AppCompatActivity {

    private Album album;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        AlbumSender albumSender = (AlbumSender) getIntent().getExtras().getSerializable("album");
        album = albumSender.getRealAlbum(getBaseContext());
        if (album.getArt() != null) {
            ((ImageView) findViewById(R.id.album_image)).setImageBitmap(album.getArt());
        }
        ((TextView) findViewById(R.id.album_ArtistName)).setText(album.getAlbumArtist());
        ((TextView) findViewById(R.id.album_albumName)).setText(album.getAlbumName());
        setList();
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
                Log.i("selected", "selected");
            };
        });
    }

    public Bitmap reloadArt(long id) {
        Uri sArtworkUri = Uri
                .parse("content://media/external/audio/albumart");
        Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, id);

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(
                    getBaseContext().getContentResolver(), albumArtUri);
            bitmap = Bitmap.createScaledBitmap(bitmap, 120, 120, true);

        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            return bitmap;
        }
    }
}
