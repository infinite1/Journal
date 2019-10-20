package com.example.journal3;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;


public class CalendarActivity extends AppCompatActivity {

    CalendarView calendarView;
    TextView myDate;
    StorageReference storageRef;
    StorageReference fileRef;

    private static final String TAG = "calendarActivity";
    private List<String> acceptList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);

        acceptList = (List<String>) getIntent().getSerializableExtra("recordlist");
        System.out.println("ACCEPT SIZE IS "+ acceptList.size());

        calendarView = (CalendarView) findViewById(R.id.calendarView);
        myDate = (TextView) findViewById(R.id.myDate);


        storageRef = FirebaseStorage.getInstance().getReference();
//        fileRef = storageRef.child("videos/10,07,2019");
//
//        fileRef.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
//            @Override
//            public void onSuccess(StorageMetadata storageMetadata) {
//                System.out.println("The size of the file is: "+storageMetadata.getSizeBytes()); //getName()
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                System.out.println("Get metadata fail!!!!"+e.getLocalizedMessage());
//            }
//        });

//        mFirebaseDatabase = FirebaseDatabase.getInstance();
//        databaseReference =    mFirebaseDatabase.getReference().child("videos");


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
//                (CalendarView view, int year, int month, int dayOfMonth)

                String dayOfMonth = String.format("%02d",i2);
//                System.out.println("NEW IS "+dayOfMonth);
                String date = (i1 + 1) + "_" +dayOfMonth + "_" + i;  //String date = (i1 + 1) + "/" +i2 + "/" + i;
                myDate.setText(date);
                Context context = getApplicationContext();
                System.out.println("***************date is "+date+" *******************");
                if(acceptList.indexOf(date)==-1) {                //cannot find record from recordlist
                    Toast.makeText(context,"No Video Record for That Day",Toast.LENGTH_SHORT).show();

                }
                else {
                    fileRef = storageRef.child("videos/"+date);

                    /******************************add metadata*************************/
                    StorageMetadata metadata = new StorageMetadata.Builder()
                            .setContentType("video/mp4")
                            .setCustomMetadata("Location", "Clayton")
                            .build();
                    // Update metadata properties
                    fileRef.updateMetadata(metadata)
                            .addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                                @Override
                                public void onSuccess(StorageMetadata storageMetadata) {
                                    System.out.println("Add location successfully!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Uh-oh, an error occurred!
                                    System.out.println("Add location Failed: "+exception.toString());

                                }
                            });



                    File localFile = null;
                    try {
                        localFile = File.createTempFile("videos", ".mp4");

//                        System.out.println("path: "+ localFile.getPath());
                        File des = new File("storage/emulated/0/DCIM/"+date+".mp4");

                        copy(localFile,des);

                        System.out.println("copy file exists: "+des.exists());


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    fileRef.getFile(localFile)
                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    // Successfully downloaded data to local file
                                    // ...
                                    Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                                    while(!uri.isComplete());
                                    Uri url = uri.getResult();
                                    System.out.println("url is "+url.toString());

                                    String name = taskSnapshot.getStorage().getName();
                                    System.out.println("File name is "+name);


                                    Toast.makeText(CalendarActivity.this, "Download complete:"+
                                                    url.toString(),
                                            Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(CalendarActivity.this, videoPlayNew.class);
                                    intent.putExtra("videourl" ,url.toString());
                                    startActivity(intent);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(CalendarActivity.this,
                                    "Download failed: " + exception.getLocalizedMessage(),
                                    Toast.LENGTH_LONG).show();
                            // Handle failed download
                            // ...
                        }
                    });


                }

//                databaseReference.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//
//                        for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
//                            Log.v(TAG,""+ childDataSnapshot.getKey()); //displays the key for the node
////                    Log.v(TAG,""+ childDataSnapshot.child(--ENTER THE KEY NAME eg. firstname or email etc.--).getValue());   //gives the value for given keyname
//                        }
//                    }
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//
//                if(date.compareTo("9,22,2019")==0) {
//                    Toast.makeText(context,"Video Loading",Toast.LENGTH_SHORT).show();
//
//                    openVideoPlay();
//
//                }
//                else {
//                    Toast.makeText(context,"No Video Record for That Day",Toast.LENGTH_SHORT).show();
//                }

//                fileRef.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
//                    @Override
//                    public void onSuccess(StorageMetadata storageMetadata) {
//                        String filename = storageMetadata.getName();
//
//                        System.out.println("filename is "+ filename);
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception exception) {
//                        // Uh-oh, an error occurred!
//                    }
//                });



            }
        });


    }
    public static void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        try {
            OutputStream out = new FileOutputStream(dst);
            try {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }



}
