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

        videoUri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/fir-videodemo-ae7b7.appspot.com/o/sample_video.MP4?alt=media&token=fd29824f-8302-4f14-9978-35bd3754a8a9");
        mainVideoView.setVideoURI(videoUri);
//        mainVideoView.requestFocus();
        mainVideoView.start();


//        isPlaying = true;
//
//        playBtn.setImageResource(R.mipmap.pauseicon);
//        playBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(isPlaying) {
//                    mainVideoView.pause();
//                    isPlaying = false;
//                    playBtn.setImageResource(R.mipmap.playicon);
//
//                }
//                else {
//                    mainVideoView.start();
//                    isPlaying = true;
//                    playBtn.setImageResource(R.mipmap.pauseicon);
//                }
//            }
//        });

//        mStorageRef = FirebaseStorage.getInstance().getReference();
    }
}


