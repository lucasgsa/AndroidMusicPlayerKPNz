package com.kpnzstudios.playerkpnz.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.kpnzstudios.playerkpnz.R;
import com.kpnzstudios.playerkpnz.activities.MainActivity;
import com.kpnzstudios.playerkpnz.models.Fila;

import java.io.IOException;

public class MusicService extends Service implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener{

    private Fila fila;

    private MediaPlayer mp;

    private boolean prepared;

    private boolean ActivityOpen;

    private boolean barShow;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equalsIgnoreCase("kpnz.start")) {
            prepared = false;
            Fila fila= (Fila) intent.getExtras().getSerializable("fila");
            this.fila = fila;
            playFila();
            if (ActivityOpen) ((LinearLayout) MainActivity.instance.findViewById(R.id.layout_barDown)).setVisibility(View.VISIBLE);
            barShow = true;
        }
        else if (intent.getAction().equalsIgnoreCase("kpnz.activityOpened")){
            this.ActivityOpen = true;
            onOpenActivity();
        }
        else if (intent.getAction().equalsIgnoreCase("kpnz.activityFinished")){
            this.ActivityOpen = false;
        }
        else if(intent.getAction().equalsIgnoreCase("kpnz.changedSeekBar")){
            int progress= intent.getExtras().getInt("progress");
            SeekBar sk = MainActivity.instance.findViewById(R.id.seekBar);
            sk.setProgress(progress);
            mp.seekTo(progress*1000);
        }
        else if(intent.getAction().equalsIgnoreCase("kpnz.resumeOrPause")){
            pauseOrResume();
        }
        else if(intent.getAction().equalsIgnoreCase("kpnz.next")){
            nextMusic();
        }
        else if(intent.getAction().equalsIgnoreCase("kpnz.back")){
            backMusic();
        }
        else if(intent.getAction().equalsIgnoreCase("kpnz.close")){
            onDestroy();
            stopSelf();
            fecharNaActivity();
        }
        return START_NOT_STICKY;
    }

    public void fecharNaActivity(){
        barShow = false;
        if (ActivityOpen){
            ((LinearLayout) MainActivity.instance.findViewById(R.id.layout_barDown)).setVisibility(View.GONE);
        }
    }

    public void onOpenActivity(){
        if (mp != null) ((LinearLayout) MainActivity.instance.findViewById(R.id.layout_barDown)).setVisibility(View.VISIBLE);
        new Thread(){
            @Override
            public void run() {
                while (ActivityOpen || barShow){
                    try {
                        if (mp != null && prepared)
                        if (ActivityOpen) ((SeekBar) MainActivity.instance.findViewById(R.id.seekBar)).setProgress(mp.getCurrentPosition()/1000);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    catch (IllegalStateException ex){
                        ex.printStackTrace();
                    }
                }
            }
        }.start();
        if (prepared) atualizarActivity();
    }

    public void playFila(){
        zerarMP();
        Uri contentUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, this.fila.getMusicaAtual().getID());
        mp = new MediaPlayer();
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mp.setDataSource(getBaseContext(), contentUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mp.setOnCompletionListener(this);
        mp.setOnPreparedListener(this);
        mp.prepareAsync();
    }

    private void atualizarActivity(){
        if (ActivityOpen && mp != null){
            SeekBar sk = MainActivity.instance.findViewById(R.id.seekBar);
            sk.setProgress(0);
            sk.setMax(mp.getDuration()/1000);
            if (mp != null && prepared) sk.setProgress(mp.getCurrentPosition()/1000);
            ((TextView) MainActivity.instance.findViewById(R.id.main_artista)).setText(fila.getMusicaAtual().getArtist());
            ((TextView) MainActivity.instance.findViewById(R.id.main_titulo)).setText(fila.getMusicaAtual().getTitle());
            if (mp.isPlaying()) ((Button) MainActivity.instance.findViewById(R.id.button_playOrPause)).setBackgroundResource(R.drawable.ic_pause);
            else ((Button) MainActivity.instance.findViewById(R.id.button_playOrPause)).setBackgroundResource(R.drawable.ic_play);
        }
    }

    @Override
    public void onPrepared(MediaPlayer player) {
        prepared = true;
        player.start();
        atualizarActivity();
        sendNotification();
    }

    private void zerarMP(){
        if (mp != null){
            mp.release();
            mp = null;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        fila.next();
        playFila();
    }

    private void pauseOrResume(){
        if (mp == null) return;
        if (!prepared) return;
        if (mp.isPlaying()){
            mp.pause();
        }
        else{
            mp.start();
        }
        atualizarActivity();
        sendNotification();
    }

    public void nextMusic(){
        if (mp == null) return;
        if (!prepared) return;
        fila.next();
        playFila();
    }

    public void backMusic(){
        if (mp == null) return;
        if (!prepared) return;
        fila.back();
        playFila();
    }

    private String channelID = "mainMusicKPNz";
    private String channelName = "MediaKPNz";
    private int idNotification = 100;

    NotificationManager manager;
    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channelMain = new NotificationChannel(
                    channelID,
                    channelName,
                    NotificationManager.IMPORTANCE_LOW
            );
            channelMain.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channelMain);
        }
    }

    private void sendNotification(){
        int playOrPause;
        if (mp.isPlaying()) {
            playOrPause = R.drawable.ic_pause;
        }
        else {
            playOrPause = R.drawable.ic_play;
        }

        Intent backIntent = new Intent(this, MusicService.class);
        backIntent.setAction("kpnz.back");
        PendingIntent backPI = PendingIntent.getService(this, 0, backIntent, 0);

        Intent playIntent = new Intent(this, MusicService.class);
        playIntent.setAction("kpnz.resumeOrPause");
        PendingIntent playPI = PendingIntent.getService(this, 0, playIntent, 0);

        Intent nextIntent = new Intent(this, MusicService.class);
        nextIntent.setAction("kpnz.next");
        PendingIntent nextPI = PendingIntent.getService(this, 0, nextIntent, 0);

        Intent closeIntent = new Intent(this, MusicService.class);
        nextIntent.setAction("kpnz.close");
        PendingIntent closePI = PendingIntent.getService(this, 0, nextIntent, 0);

        Bitmap bit = fila.getMusicaAtual().getArt(getApplicationContext());

        boolean fechavel = !mp.isPlaying();

        int draw;

        if (mp.isPlaying()){
            draw = R.drawable.ic_play;
        }
        else{
            draw = R.drawable.ic_pause;
        }
        createNotificationChannel();
        Notification notificationMain = new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle(fila.getMusicaAtual().getTitle())
                .setContentText(fila.getMusicaAtual().getArtist())
                .setSmallIcon(draw)
                .setLargeIcon(bit)
                .addAction(R.drawable.ic_back, "resumeOrPause", backPI)
                .addAction(playOrPause, "resumeOrPause", playPI)
                .addAction(R.drawable.ic_skip, "resumeOrPause", nextPI)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowCancelButton(true)
                        .setCancelButtonIntent(closePI)
                        .setShowActionsInCompactView(0,1,2))
                .setDeleteIntent(closePI)
                .build();
        if (fechavel){
            stopForeground(true);
            manager.notify(idNotification, notificationMain);
        }
        else {
            startForeground(idNotification, notificationMain);
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void finalizar(){
        zerarMP();
    }


}
