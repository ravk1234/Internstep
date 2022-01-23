package com.internstep.user;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class my_documents extends AppCompatActivity {
    ImageView image;
    Button submit,back;
    private Uri filePath,documentUri;
    private String pictureFilePath;
    private Bitmap bitmap;
    private final int PICK_IMAGE_REQUEST = 22;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    String full_name,imgurl,imgurl_string;
    private Uri mCurrentPhotoPath;
    FirebaseStorage storage;
    StorageReference storageReference;
    private StorageTask uploadTask;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    ProgressDialog progressDialog;
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_documents);
        image = findViewById(R.id.folder);
        submit = findViewById(R.id.submit);
        back = findViewById(R.id.toolbar_back_mydocs);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(my_documents.this, about_me.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

            }
        });

        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User users = dataSnapshot.getValue(User.class);
                //assert users != null;
                full_name =users.getName();
                imgurl = users.getIdcard();
                if(!imgurl.equals("default")){

                        Glide.with(getApplicationContext()).load(imgurl).into(image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //selectedImage(my_documents.this);
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(my_documents.this);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User users = dataSnapshot.getValue(User.class);
                        //assert users != null;
                        full_name =users.getName();
                        imgurl = users.getIdcard();
                        if(imgurl.equals("default")){
                            Toast.makeText(my_documents.this,"Please upload the document",Toast.LENGTH_SHORT).show();

                        }
                        else {
                            Intent intent = new Intent(my_documents.this, social_links.class);
                            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            //finish();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });





            }
        });



    }



    // Select Image method




    private File getPictureFile() throws IOException {
        @SuppressLint("SimpleDateFormat")
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String pictureFile = "INTERNSTEP_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(pictureFile,  ".jpg", storageDir);
        pictureFilePath = image.getAbsolutePath();
        return image;
    }




    // Override onActivityResult method
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view

                    CropImage.ActivityResult result=null;
                   if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                       result = CropImage.getActivityResult(data);
                       if (resultCode == RESULT_OK && data != null) {
                           //imageuri = data.getData();
                           documentUri = result.getUri();
                           Bitmap selectedImage = null;

                           try {

                               // Setting image on image view using Bitmap
                               selectedImage = MediaStore
                                       .Images
                                       .Media
                                       .getBitmap(
                                               getContentResolver(),
                                               documentUri );
                               // handleUpload(bitmap);



                           } catch (IOException e) {
                               // Log the exception
                               e.printStackTrace();
                           }


                           assert selectedImage != null;
                           image.setImageBitmap(selectedImage);
                           uploadImage(selectedImage);

                       }
                   }




            }



    // UploadImage method
    private void uploadImage(Bitmap bitmap)
    {
        if (documentUri != null) {
            progressDialog = new ProgressDialog(this,R.style.MyAlertDialogStyle);
            progressDialog.setTitle("Uploading...");
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.show();
            //DataSnapshot dataSnapshot = null;
            //User user1 = dataSnapshot.getValue(User.class);
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


            // Defining the child of storageReference
            final StorageReference ref
                    = storageReference
                    .child(
                            "images/").child(full_name).child(uid +".jpg");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,75,baos);

            // adding listeners on upload
            // or failure of image
            uploadTask = ref.putBytes(baos.toByteArray())
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    imgurl_string =  ref.getDownloadUrl().toString();
                                    Log.d("imgurl",imgurl_string);

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

                        Uri downloadUri = task.getResult();
                        String muri = downloadUri.toString();

                        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
                        Map<String, Object> hashmap = new HashMap<>();
                        hashmap.put("idcard", muri);
                        reference.updateChildren(hashmap).addOnSuccessListener(
                                new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressDialog.dismiss();
                                        Toast.makeText(my_documents.this, "Success", Toast.LENGTH_SHORT).show();
                                    }

                                });

                    } else {
                        // Handle failures
                        // ...
                        progressDialog.dismiss();
                        Toast.makeText(my_documents.this, "Upload Failed", Toast.LENGTH_SHORT).show();
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
