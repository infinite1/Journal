package com.example.journal3;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SaveShare extends AppCompatActivity {

    private String workingPath;//输出文件目录
    private ArrayList<String> videosToMerge = new ArrayList<>();//需要合并的视频的路径集合
    private String outName = "mergeVideo.mp4";//输出的视频名称
    private VideoView mVideoView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.share_save_horizontal);
            Log.i("info", "landscape");
        }
        else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.share_save);

            Log.i("info", "portrait");
        }

        Uri videoUri = Uri.parse(getIntent().getExtras().getString("videoUri"));
        Uri videoUri2 = Uri.parse(getIntent().getExtras().getString("videoUri2"));
        String content1 = getIntent().getExtras().getString("videoUri");
        String content2 = getIntent().getExtras().getString("videoUri2");

        videosToMerge.add(content1);
        videosToMerge.add(content2);

        Log.v("Test",content1);
        Log.v("Test",content2);


        String DCIMPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString();
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/output";
//        workingPath = path;


//        workingPath = path;
        int count = videosToMerge.size();
        try {
            Movie[] inMovies = new Movie[count];
            for (int i = 0; i < count; i++) {
                inMovies[i] = MovieCreator.build(videosToMerge.get(i));
            }

            List<Track> videoTracks = new LinkedList<>();
            List<Track> audioTracks = new LinkedList<>();

            //提取所有视频和音频的通道
            for (Movie m : inMovies) {
                for (Track t : m.getTracks()) {
                    if (t.getHandler().equals("soun")) {
                        audioTracks.add(t);
                    }
                    if (t.getHandler().equals("vide")) {
                        videoTracks.add(t);
                    }
                    if (t.getHandler().equals("")) {

                    }
                }
            }

            //添加通道到新的视频里
            Movie result = new Movie();
            if (audioTracks.size() > 0) {
                result.addTrack(new AppendTrack(audioTracks
                        .toArray(new Track[audioTracks.size()])));
            }
            if (videoTracks.size() > 0) {
                result.addTrack(new AppendTrack(videoTracks
                        .toArray(new Track[videoTracks.size()])));
            }
            Container mp4file = new DefaultMp4Builder()
                    .build(result);


            //开始生产mp4文件
//            File storagePath = new File(workingPath);
//            storagePath.mkdirs();
            FileOutputStream fos = new FileOutputStream(new File("/storage/emulated/0/DCIM/Camera/out1.mp4"));
            FileChannel fco = fos.getChannel();
            mp4file.writeContainer(fco);
            fco.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.v("TEST","video aleady producted");
        final File mergefile = new File("/storage/emulated/0/DCIM/Camera/out1.mp4");//打开软件直接播放的视频名字是movie.mp4
        mVideoView = findViewById(R.id.merge_video);
        Log.v("test", mergefile.getPath());
        mVideoView.setVideoPath(mergefile.getPath());
        mVideoView.seekTo(0);
        mVideoView.requestFocus();
        mVideoView.start();


        Button btnshare_to_other_app;
        btnshare_to_other_app=findViewById(R.id.share_to_other_app);
        btnshare_to_other_app.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);            //分享视频只能单个分享
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(mergefile.getPath()));
                shareIntent.setType("video/*");
                startActivity(Intent.createChooser(shareIntent, "Share to...."));
            }
        });

    }

    public String convertMediaUriToPath(Uri uri) {
        String [] proj={MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, proj,  null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();
        return path;
    }




}
