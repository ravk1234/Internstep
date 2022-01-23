package com.internstep.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.internstep.user.Models.Companies;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    EditText email,pass1;
    Button submit,back;
    FirebaseAuth mAuth;
    private ProgressDialog mProgress;
    DatabaseReference reference;
    FirebaseUser firebaseUser;
    private List<Companies> companies,companies1;
    Companies company1;
    TextView forgot_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        email = findViewById(R.id.email);
        pass1 = findViewById(R.id.password);
        submit = findViewById(R.id.submit);
        back = findViewById(R.id.toolbar11_button);
        forgot_password = findViewById(R.id.click_here);
        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();
        mProgress = new ProgressDialog(this,R.style.MyAlertDialogStyle);
        mProgress.setTitle("Logging in...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);
        companies = new ArrayList<>();
        companies1 = new ArrayList<>();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, StartActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String u_email = email.getText().toString();
                String u_password = pass1.getText().toString();
                if (TextUtils.isEmpty(u_email) || TextUtils.isEmpty(u_password)) {
                    Toast.makeText(LoginActivity.this, "All credentials are required!", Toast.LENGTH_SHORT).show();
                } else if(!Patterns.EMAIL_ADDRESS.matcher(u_email).matches()){
                    Toast.makeText(LoginActivity.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();

                }else {
                    mProgress.show();
                    mAuth.signInWithEmailAndPassword(u_email, u_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                final int[] count = {0};
                                final FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                assert firebaseUser != null;
                                DatabaseReference companyref = FirebaseDatabase.getInstance().getReference("Companies");
                                final DatabaseReference userref = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("companies");


                                companyref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        companies.clear();
                                        for(DataSnapshot companySnapshot:dataSnapshot.getChildren()){
                                            Companies company = companySnapshot.getValue(Companies.class);
                                            //userref.setValue(companySnapshot.getChildren());

                                            companies.add(company);
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                userref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()){
                                            count[0] = (int) snapshot.getChildrenCount();
                                            Log.i("size",String.valueOf(count[0]));
                                            if(count[0]<companies.size())
                                                userref.setValue(companies);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                Log.i("count", String.valueOf(companies1.size()));
                                Intent intent = new Intent(LoginActivity.this, InitialProfileActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                mProgress.dismiss();
                                Toast.makeText(LoginActivity.this, "Failed to Login with these credentials", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });

                }
            }
        });


        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                //Intent emailIntent = new Intent(Intent.ACTION_SEND);
                //emailIntent.setType("plain/text");
                emailIntent.putExtra(Intent.EXTRA_EMAIL,new String[] {"ceo@internstep.in"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Re: Help me retrieve my password for my email [Enter your registered email address]");
                emailIntent.setType("message/rfc822");
                startActivity(Intent.createChooser(emailIntent,null));

            }
        });


    }



    @Override
    public void onBackPressed() {


        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.mProgress.cancel();
    }
}
