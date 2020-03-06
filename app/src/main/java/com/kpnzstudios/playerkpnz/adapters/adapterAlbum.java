package com.kpnzstudios.playerkpnz.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kpnzstudios.playerkpnz.models.Album;
import com.kpnzstudios.playerkpnz.R;

import java.util.ArrayList;

public class adapterAlbum extends BaseAdapter {
    private ArrayList<Album> albums;

    private final Activity act;

    public adapterAlbum(ArrayList<Album> arg0, Activity act){
        this.albums = arg0;
        this.act = act;
    }

    @Override
    public int getCount() {
        return albums.size();
    }

    @Override
    public Object getItem(int position) {
        return albums.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = act.getLayoutInflater().inflate(R.layout.album_list, parent, false);
        Album album = albums.get(position);
        ((TextView) view.findViewById(R.id.albumadapter_album)).setText(album.getAlbumName());
        ((TextView) view.findViewById(R.id.albumadapter_artist)).setText(album.getAlbumArtist());
        int sizeTemp = album.getSize();
        if (sizeTemp == 1){
            ((TextView) view.findViewById(R.id.albumadapter_total)).setText(sizeTemp+" música");
        }
        else{
            ((TextView) view.findViewById(R.id.albumadapter_total)).setText(album.getSize()+" músicas");
        }

        if (album.getArt() != null){
            ((ImageView) view.findViewById(R.id.albumadapter_art)).setImageBitmap(album.getArt());
        }
        return view;
    }
}
