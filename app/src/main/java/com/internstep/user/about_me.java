package com.internstep.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class about_me extends AppCompatActivity {
    EditText info_description,info_job;
    Spinner spinner1;
    //Toolbar toolbar;
    FirebaseAuth mAuth;
    ImageView icon,icon1;
    //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    //FirebaseUser firebaseUser;
    String education;
    String str;
    Button submit,back;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_me);
        info_description = findViewById(R.id.description);
        info_job = findViewById(R.id.job_title);
        spinner1 = findViewById(R.id.spinner);
        back = findViewById(R.id.toolbar_back_about_me);
        submit = findViewById(R.id.submit);
        icon = findViewById(R.id.imageView1);
        icon1= findViewById(R.id.imageView2);
        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(about_me.this, InitialProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

            }
        });




        icon.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    if(event.getRawX() >= info_description.getRight() - info_description.getTotalPaddingRight()) {
                        // your action for drawable click event
                        info_description.setHint("I am a 16 years old UI/UX Designer,\n" +
                                "currently a 12th grader in\n" +
                                "high school.Looking\n" +
                                "for internships in a tech company\n" +
                                "as a Product designer");

                        return true;
                    }
                    else{
                        info_description.setHint("A short description...");
                    }
                }
                else{
                    info_description.setHint("A short description...");
                }

                return false;
            }
        });

        icon1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    if(event.getRawX() >= info_job.getRight() - info_job.getTotalPaddingRight()) {
                        // your action for drawable click event
                        info_job.setHint("Android Developer");

                        return true;
                    }
                    else{
                        info_job.setHint("Your Job title");
                    }
                }
                else{
                    info_job.setHint("Your Job title");
                }

                return false;
            }
        });

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(about_me.this,android.R.layout.
                simple_list_item_1,getResources().getStringArray(R.array.education));

        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(myAdapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinner1.setSelection(i);
                 education = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                spinner1.setSelection(0);
            }
        });






        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String str1 = info_description.getText().toString();
                final String str2 = info_job.getText().toString();
                Log.i("hybrid",str2);
                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                assert firebaseUser != null;
                reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
                Map<String,Object> hashmap = new HashMap<>();
                hashmap.put("short_description",str1);
                hashmap.put("job_title",str2);
                //hashmap.put("job_title",str2);
                hashmap.put("education",education);
                reference.updateChildren(hashmap).addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(about_me.this,"Success",Toast.LENGTH_SHORT).show();
                            }
                        }
                );

                  Intent intent = new Intent(about_me.this, my_documents.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                //finish();


            }
        });


    }
}
