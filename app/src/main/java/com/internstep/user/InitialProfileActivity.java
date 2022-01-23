package com.internstep.user;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;

import android.content.Intent;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.internstep.user.Models.User;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class InitialProfileActivity extends AppCompatActivity {
    String uid;
    StorageReference ref;
    ImageView add;
    Button about_me, my_documents, social_links, submit;
    private CircleImageView profile_picture;
    private static final int picture = 1;
    private SharedPref pref;
    Uri imageuri,uri;
    private final int PICK_IMAGE_REQUEST = 22;
    String imgurl,short_description,education,job_title,github,dribble,linkedin,imgurl_string,id_card;
    private StorageTask uploadTask;
    DatabaseReference reference;
    FirebaseUser firebaseUser;
    String full_name;
    TextView profile_name;
    ProgressDialog progressDialog;
    private String pictureFilePath;
    private Bitmap bitmap;
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;
    int x =0;


    @Override
    protected void onStart() {
        super.onStart();
        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        if(b==null) {

            pref = new SharedPref(this);
            if (pref.isComplete()) {
                Intent intent = new Intent(InitialProfileActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_details);
        about_me = findViewById(R.id.about_me);
        my_documents = findViewById(R.id.my_documents);
        social_links = findViewById(R.id.my_social_links);
        submit = findViewById(R.id.done);
        add = findViewById(R.id.add);
        profile_picture = findViewById(R.id.account_picture);
        profile_name = findViewById(R.id.profile_name);
        pref = new SharedPref(this);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                //user.setImgUrl(imgurl);
                full_name = user.getName();
                short_description = user.getShort_description();
                education = user.getEducation();
                job_title = user.getJob_title();
                github = user.getGithub();
                dribble = user.getDribble();
                linkedin = user.getLinkedin();
                id_card = user.getIdcard();
                imgurl_string = user.getImgUrl();
                profile_name.setText(full_name);

                //if()

                if(imgurl_string.equals("default")){
                    profile_picture.setImageResource(R.mipmap.ic_launcher);
                }
                else{
                    add.setVisibility(View.INVISIBLE);
                    Glide.with(getApplicationContext()).load(imgurl_string).into(profile_picture);
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        about_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(InitialProfileActivity.this, about_me.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                //finish();


            }


        });

        my_documents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(InitialProfileActivity.this, my_documents.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                //finish();


            }


        });

        social_links.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(InitialProfileActivity.this, social_links.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                //finish();


            }


        });




        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                reference.addValueEventListener(new ValueEventListener() {
                    @SuppressLint("ApplySharedPref")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        //user.setImgUrl(imgurl);
                        assert user != null;
                        full_name = user.getName();
                        short_description = user.getShort_description();
                        education = user.getEducation();
                        job_title = user.getJob_title();
                        github = user.getGithub();
                        dribble = user.getDribble();
                        linkedin = user.getLinkedin();
                        id_card = user.getIdcard();
                        imgurl_string = user.getImgUrl();
                        profile_name.setText(full_name);
                        if(education.equals("default") || job_title.equals("default") || short_description.equals("default")  ||   id_card.equals("default")  || (github.equals("default") && linkedin.equals("default")
                                && dribble.equals("default")) ){
                            x=1;

                            Toast.makeText(InitialProfileActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();

                        }

                        else {

                             pref.setIsProfile(true);


                                    Intent intent = new Intent(InitialProfileActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                        Log.i("value",String.valueOf(x));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });





            }


        });


        profile_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //selectedImage(InitialProfileActivity.this);
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(InitialProfileActivity.this);


            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


       CropImage.ActivityResult result=null;
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
             result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                assert result != null;
                imageuri = result.getUri();


                try {

                    // Setting image on image view using Bitmap
                    bitmap = MediaStore
                            .Images
                            .Media
                            .getBitmap(
                                    getContentResolver(),
                                    imageuri);
                    // handleUpload(bitmap);
                    profile_picture.setImageBitmap(bitmap);

                    handleUpload(bitmap);

                } catch (IOException e) {
                    // Log the exception
                    e.printStackTrace();
                }
            }
        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            Exception error = result.getError();
        }
    }

    private void handleUpload(Bitmap bitmap) {
        int currSize;
        if (imageuri != null) {
             progressDialog = new ProgressDialog(this,R.style.MyAlertDialogStyle);
            progressDialog.setTitle("Uploading...");
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.show();
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);
            currSize = baos.toByteArray().length;
            Log.i("size",String.valueOf(currSize));
            final StorageReference ref = FirebaseStorage.getInstance().getReference()
                    .child(
                            "profile_images/").child(full_name).child(uid + ".jpeg");
            uploadTask = ref.putBytes(baos.toByteArray()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //progressDialog.dismiss();
                    imgurl = ref.getDownloadUrl().toString();
                }
            });

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    // Continue with the task to get the download URL
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        //progressDialog.dismiss();
                        Uri downloadUri = task.getResult();
                        String muri = downloadUri.toString();

                        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
                        Map<String, Object> hashmap = new HashMap<>();
                        hashmap.put("imgUrl", muri);
                        reference.updateChildren(hashmap).addOnSuccessListener(
                                new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressDialog.dismiss();
                                        Toast.makeText(InitialProfileActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                    }

                                });

                    } else {
                        // Handle failures
                        // ...
                        progressDialog.dismiss();
                        Toast.makeText(InitialProfileActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });





        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if(progressDialog!=null  && progressDialog.isShowing())
        this.progressDialog.dismiss();
    }

}


