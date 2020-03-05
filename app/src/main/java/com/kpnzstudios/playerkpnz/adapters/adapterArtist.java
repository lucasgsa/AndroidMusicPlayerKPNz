package com.kpnzstudios.playerkpnz.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kpnzstudios.playerkpnz.Artist;
import com.kpnzstudios.playerkpnz.R;

import java.util.ArrayList;

public class adapterArtist extends BaseAdapter {

    private ArrayList<Artist> artistas;

    private final Activity act;

    public adapterArtist(ArrayList<Artist> artistas, Activity act){
        this.artistas = artistas;
        this.act = act;
    }

    @Override
    public int getCount() {
        return artistas.size();
    }

    @Override
    public Object getItem(int position) {
        return artistas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = act.getLayoutInflater().inflate(R.layout.artist_list, parent, false);
        Artist artist = artistas.get(position);
        ((TextView) view.findViewById(R.id.artistadapter_artista)).setText(artist.getName());
        ((TextView) view.findViewById(R.id.artistadapter_musicsSize)).setText(artist.getMusicsCount()+" músicas");
        ((TextView) view.findViewById(R.id.artistadapter_albumSize)).setText(artist.getAlbumsCount()+" álbuns");
        return view;
    }
}
