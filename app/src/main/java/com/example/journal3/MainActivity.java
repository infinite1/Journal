package com.example.journal3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    private FirebaseFirestore db;
    private Uri downloadUri;


    public static final int MAX_SIZE = 100;
    private static final String TAG = "Upload Video";

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

        // Add select item listener for navigation drawer
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
                                Intent i = new Intent(MainActivity.this,
                                        MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                MainActivity.this.finish();
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

        // Can only upload when the user sign in
        if (currentUser != null) {
            long timeStamp = System.currentTimeMillis();
            System.out.println("Time is : " + timeStamp);

            // initialize Firestore
            db = FirebaseFirestore.getInstance();
            simpleDateFormat = new SimpleDateFormat("MM_dd_yyyy");
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
                        }).addOnCompleteListener(
                        new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                videoref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        downloadUri = uri;
                                        uploadRefToDatabase(currentUser, strDate);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        System.out.println("Fail to upload uri");
                                    }
                                });
                            }
                        }
                );
            } else {
                Toast.makeText(MainActivity.this, "Nothing to upload",
                        Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(MainActivity.this, "User doesn't sign in",
                    Toast.LENGTH_LONG).show();
        }


    }

    // upload video reference to firebase
    public void uploadRefToDatabase(FirebaseUser currentUser,
                                    String strDate) {

        System.out.println("upload " + downloadUri.toString());
        // upload videos to cloud
        Map<String, Object> user = new HashMap<>();
        user.put(strDate, downloadUri.toString());

        // Add a new document with user email as id
        db.collection("users").document(Objects.requireNonNull(currentUser.getEmail()))
                .set(user, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
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
