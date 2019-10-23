package com.example.journal3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;

public class VideoPlayActivity extends AppCompatActivity implements
        SurfaceHolder.Callback, MediaPlayer.OnCompletionListener {

    private MediaPlayer mMediaPlayer;
    private Uri mVideoUri;
    private ImageButton mPlayPauseButton;
    private SurfaceView mSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        mPlayPauseButton=(ImageButton)findViewById(R.id.videoPlayPauseButton);
        mSurfaceView=(SurfaceView)findViewById(R.id.surfaceView);
        Intent Callingintent=this.getIntent();
        if(Callingintent != null)
        {
            mVideoUri=Callingintent.getData();
        }
    }

    public void playPauseClick(View view) {
        if(mMediaPlayer.isPlaying()){
            mediaPause();
        }
        else{
            mediaPlay();
        }
    }

    @Override
    protected void onStop(){
        if(mMediaPlayer != null){
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer=null;
        }
        super.onStop();
    }

    @Override
    protected void onPause(){
        super.onPause();
        mediaPause();
    }

    @Override
    protected void onResume(){
      super.onResume();
      if(mMediaPlayer != null){
          mediaPlay();
      }
      else{
          SurfaceHolder surfaceHolder=mSurfaceView.getHolder();
          surfaceHolder.addCallback(this);
      }
    }


    private void mediaPlay() {
        mMediaPlayer.start();
        mPlayPauseButton.setImageResource(R.mipmap.pause);
    }
    private void mediaPause() {
        mMediaPlayer.pause();
        mPlayPauseButton.setImageResource(R.mipmap.play_button);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mMediaPlayer=MediaPlayer.create(this, mVideoUri, holder);
        mMediaPlayer.setOnCompletionListener(this);
        mediaPlay();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mPlayPauseButton.setImageResource(R.mipmap.play_button);
    }
}
