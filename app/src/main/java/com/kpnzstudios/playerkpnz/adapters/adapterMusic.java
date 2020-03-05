package com.kpnzstudios.playerkpnz.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kpnzstudios.playerkpnz.Music;
import com.kpnzstudios.playerkpnz.R;

import java.util.ArrayList;

public class adapterMusic extends BaseAdapter {

    private ArrayList<Music> musicas;

    private final Activity act;

    public adapterMusic(ArrayList<Music> arg0, Activity act){
        this.musicas = arg0;
        this.act = act;
    }

    @Override
    public int getCount() {
        return musicas.size();
    }

    @Override
    public Object getItem(int position) {
        return musicas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = act.getLayoutInflater().inflate(R.layout.music_list, parent, false);
        Music music = musicas.get(position);
        ((TextView) view.findViewById(R.id.text_title)).setText(music.getTitle());
        ((TextView) view.findViewById(R.id.text_artista)).setText(music.getArtist());
        ((TextView) view.findViewById(R.id.text_total)).setText(music.getDuration());
        return view;
    }
}
