package com.example.journal3;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;


public class VideoPlay extends AppCompatActivity {

//    private StorageReference mStorageRef;

    private VideoView mainVideoView;
//    private ImageView playBtn;
//    private TextView currentTimer;
//    private TextView durationTimer;
//    private ProgressBar currentProgress;
//    private ProgressBar bufferProgress;

//    private boolean isPlaying;



    private Uri videoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendarvideo);

        MediaController mediaController =new MediaController(this);

        mainVideoView = (VideoView) findViewById(R.id.vid);

        mainVideoView.setMediaController(mediaController);
        mediaController.setAnchorView(mainVideoView);
//        playBtn = (ImageView)findViewById(R.id.playBtn);
//        currentProgress = (ProgressBar)findViewById(R.id.videoProgress);
//        currentTimer = (TextView) findViewById(R.id.currentTimer);
//        durationTimer = (TextView) findViewById(R.id.durationTimer);
//        bufferProgress = (ProgressBar)findViewById(R.id.bufferProgress);

        videoUri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/fir-videodemo-ae7b7.appspot.com/o/Video%20File%20Formats%20-%20MP4%2C%20MOV%2C%20MKV.mp4?alt=media&token=d369d35b-028e-4c56-bd7a-8df833779310");
        mainVideoView.setVideoURI(videoUri);
//        mainVideoView.requestFocus();
        mainVideoView.start();
    }
}


