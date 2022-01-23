package com.internstep.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.internstep.user.Models.Companies;
import com.internstep.user.Models.Jobs;
import com.internstep.user.Models.User;

import java.io.IOError;

public class SmartApplication extends AppCompatActivity {

    EditText software, ques_1, ques_2;
    Button submit, resume, back;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    FirebaseAuth mAuth;
    final static int PICK_PDF_CODE = 2342;
    FirebaseStorage storage;
    StorageReference storageReference;
    String full_name, company_name, job_role;
    ProgressDialog progressDialog;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smart_application);

        software = findViewById(R.id.edittext_1);
        ques_1 = findViewById(R.id.edittext_2);
        ques_2 = findViewById(R.id.edittext_3);
        submit = findViewById(R.id.button_continue_a1);
        back = findViewById(R.id.toolbar_back_smart_application);
        resume = findViewById(R.id.button_continue_a1);

        Intent iin = getIntent();

        Bundle b = iin.getExtras();
        if (b != null) {
            company_name = (String) b.get("company_name");
            job_role = (String) b.get("job_position");


        }

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        assert firebaseUser != null;
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User users = dataSnapshot.getValue(User.class);
                assert users != null;
                full_name =users.getName();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SmartApplication.this, JobsDescription.class);
                intent.putExtra("company_name",company_name);
                intent.putExtra("job_position",job_role);
                startActivity(intent);
                finish();
            }
        });

        resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(SmartApplication.this, R.style.MyAlertDialogStyle);
                progressDialog.setTitle("Uploading...");
                progressDialog.setCancelable(false);
                progressDialog.setIndeterminate(true);
                progressDialog.show();

                final String ques_1_string = ques_1.getText().toString();
                final String ques_2_string = ques_2.getText().toString();
                final String soft_list = software.getText().toString();
                final String applied = "Applied";
                //uploadImage();
                if (ques_1_string.isEmpty() || ques_2_string.isEmpty() || soft_list.isEmpty() ||
                 filePath==null) {
                    Toast.makeText(SmartApplication.this,"Please submit all your information",Toast.LENGTH_SHORT).show();

                }else {


                    uploadImage();

                    Toast.makeText(SmartApplication.this, "Success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SmartApplication.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("company_name", company_name);
                    intent.putExtra("job_position", job_role);
                    startActivity(intent);
                    //finish();
                }

            }
        });



    }

    @Override
    public void onBackPressed() {


        super.onBackPressed();
        finish();
    }

    private void SelectImage() {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select PDF from here..."),
                PICK_PDF_CODE);
    }

    // Override onActivityResult method
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_PDF_CODE
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {



            // Get the Uri of data
            try {
                filePath = data.getData();

                //progressDialog.dismiss();

            } catch (IOError error) {
                // Log the exception
                error.printStackTrace();
                //progressDialog.dismiss();
            }

        }
    }

    // UploadImage method
    private void uploadImage() {
        final String ques_1_string = ques_1.getText().toString();
        final String ques_2_string = ques_2.getText().toString();
        final String soft_list = software.getText().toString();
        final String applied = "Applied";
        final String[] muri = new String[1];

        if (filePath != null) {



            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();



            // Defining the child of storageReference
            final StorageReference ref
                    = storageReference
                    .child(
                            "resume/").child(full_name).child(company_name).child(job_role).child(uid + ".pdf");

            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            progressDialog.dismiss();
                            muri[0] = uri.toString();
                            //muri[0] = Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getMetadata()).getReference()).getDownloadUrl().toString();
                            Log.i("url", muri[0]);
                            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("companies");
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot companySnapshot : snapshot.getChildren()) {
                                        Companies companies = companySnapshot.getValue(Companies.class);
                                        assert companies != null;
                                        if (companies.getCompany_name().equals(company_name)) {
                                            //String key1 = companySnapshot.getKey();

                                            for (DataSnapshot jobSnapshot : companySnapshot.child("job_roles").getChildren()) {
                                                Jobs jobs = jobSnapshot.getValue(Jobs.class);
                                                assert jobs != null;
                                                if (jobs.getJob_name().equals(job_role)) {



                                                        if (!muri[0].isEmpty()) {
                                                            progressDialog.dismiss();
                                                            //reference.child("").updateChildren(hashMap);
                                                            jobSnapshot.getRef().child("resume").setValue(muri[0]);
                                                            jobSnapshot.getRef().child("software").setValue(soft_list);
                                                            jobSnapshot.getRef().child("ques1").setValue(ques_1_string);
                                                            jobSnapshot.getRef().child("ques2").setValue(ques_2_string);
                                                            jobSnapshot.getRef().child("job_status").setValue(applied);
                                                            jobSnapshot.getRef().child("applied").setValue(true);


                                                        }



                                                }
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    progressDialog.dismiss();
                                }
                            });
                        }
                    });

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
