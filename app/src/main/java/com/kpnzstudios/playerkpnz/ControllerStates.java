package com.kpnzstudios.playerkpnz;

public class ControllerStates {

    private boolean prepared = false;
    private boolean isPreparing = false;
    private boolean nuller = true;
    private boolean played = false;
    private boolean paused = false;
    private boolean stopped = false;

    public ControllerStates(){

    }

    public void songAdded(){
        nuller = false;
        prepared = false;
        isPreparing = false;
        played = false;
        paused = false;
        stopped = false;
    }

    public void preparing(){
        nuller = false;
        prepared = false;
        isPreparing = true;
        played = false;
        paused = false;
        stopped = false;
    }

}
