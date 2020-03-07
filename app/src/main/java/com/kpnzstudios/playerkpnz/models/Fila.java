package com.kpnzstudios.playerkpnz.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Fila implements Serializable {

    private int posicao;
    private ArrayList<Music> fila;

    public Fila(ArrayList<Music> fila, int inicial){
        this.fila = fila;
        this.posicao = inicial;
    }

    public void next(){
        if (posicao != fila.size()-1) posicao++;
        else posicao = 0;
    }

    public void back(){
        if (posicao != 0) posicao--;
        else posicao = fila.size()-1;
    }

    public Music getMusicaAtual(){
        return fila.get(posicao);
    }

}
