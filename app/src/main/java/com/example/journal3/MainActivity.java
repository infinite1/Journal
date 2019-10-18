package com.example.journal3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener {
    private Toolbar myToolbar;
    private Button mSignIn;
    private BottomNavigationView mBtmView;
    //add
    private Uri videouri;
    private static final int REQUEST_CODE = 101;
    private ProgressDialog progressDialog;
    private StorageReference videoref;
    private DatabaseReference mDatabaseRef;
    private StorageReference storageRef;
    private String strDate;
    private SimpleDateFormat simpleDateFormat;
    private Date date;
    private long timeStamp;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private TextView emailView;

    public static final int MAX_SIZE = 100;

    private List<String> recordList = new ArrayList<String>(MAX_SIZE);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_activity);
        mAuth = FirebaseAuth.getInstance();
        myToolbar = findViewById(R.id.my_toolbar);
        mBtmView = findViewById(R.id.bot_nav);
        mBtmView.setOnNavigationItemSelectedListener(this);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bot_nav);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerview = navigationView.getHeaderView(0);


        // signIn button if user not exist, show email address if user exists
        mSignIn = headerview.findViewById(R.id.nav_sign_in_btn);
        emailView = headerview.findViewById(R.id.emailView);

        currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            mSignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainActivity.this, LogInActivity.class);
                    startActivity(i);
                }
            });
        } else {
            mSignIn.setVisibility(View.GONE);
            emailView.setVisibility(View.VISIBLE);
            String email = currentUser.getEmail();
            emailView.setText(email);
        }

        // Add select item listener for navigationView
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.signOut: {
                                System.out.println("select signout");
                                mSignIn.setVisibility(View.VISIBLE);
                                emailView.setVisibility(View.GONE);
                                mAuth.signOut();
                                System.out.println("Sign out complete");
                            }
                            break;
                        }
                        return true;
                    }
                });


        //add
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        storageRef = FirebaseStorage.getInstance().getReference();


//        videoref =mStorageRef.child("/videos" + "/userIntro.3gp");

    }

    // Add select item listener for bottomNavigation
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mesh: {
                System.out.println("select mesh");
            }
            break;
            case R.id.calendar: {
                System.out.println("select calendar");
                Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
                intent.putExtra("recordlist", (Serializable) recordList);
                startActivity(intent);
            }
            break;
        }
        return true;
    }

    //add

    public void upload(View view) {

        long timeStamp = System.currentTimeMillis();
        System.out.println("Time is : " + timeStamp);

        simpleDateFormat = new SimpleDateFormat("MM,dd,yyyy");
        date = new Date(timeStamp);
        strDate = simpleDateFormat.format(date);
        System.out.println("Date is : " + strDate);
        videoref = storageRef.child("/videos" + "/" + strDate);

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading file..");
        progressDialog.setProgress(0);
        progressDialog.show();
        if (videouri != null) {
            UploadTask uploadTask = videoref.putFile(videouri);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this,
                            "Upload failed: " + e.getLocalizedMessage(),
                            Toast.LENGTH_LONG).show();

                }
            }).addOnSuccessListener(
                    new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(MainActivity.this, "Upload complete",
                                    Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                            recordList.add(strDate);
//                            System.out.println("list size is "+list.size());
                        }
                    }).addOnProgressListener(
                    new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                            updateProgress(taskSnapshot);

                            int currentProgress = (int) (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setProgress(currentProgress);

                        }
                    });
        } else {
            Toast.makeText(MainActivity.this, "Nothing to upload",
                    Toast.LENGTH_LONG).show();
        }

    }

    public void record(View view) {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 3);


        startActivityForResult(intent, REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        videouri = data.getData();
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Video saved to:\n" +
                        videouri, Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Video recording cancelled.",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Failed to record video",
                        Toast.LENGTH_LONG).show();
            }
        }
    }


}
