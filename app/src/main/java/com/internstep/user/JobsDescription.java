package com.internstep.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.internstep.user.Models.Companies;
import com.internstep.user.Models.Jobs;

import java.util.HashMap;
import java.util.Map;

public class JobsDescription  extends AppCompatActivity {
    TextView job_description,location,job_name,response,job_ex,job_edu,job_loc,lang;
    ImageView company_logo;
    Button apply,back;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    String company_name,job_position;
    FirebaseAuth mAuth;
    boolean applied = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_description);

        job_description = findViewById(R.id.description);
        response = findViewById(R.id.responsibilities_content);
        job_ex = findViewById(R.id.experience_content);
        job_edu = findViewById(R.id.education_content);
        job_loc = findViewById(R.id.Location_content);
        lang = findViewById(R.id.Language_content);


        job_name = findViewById(R.id.job_name);
        location = findViewById(R.id.location);
        company_logo = findViewById(R.id.company_logo);
        apply = findViewById(R.id.apply);
        back = findViewById(R.id.toolbar_back_job_description);
        mAuth = FirebaseAuth.getInstance();
        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        if(b!=null)
        {
            company_name =(String) b.get("company_name");
            job_position = (String)b.get("job_position");


        }

        job_name.setText(job_position);
        firebaseUser = mAuth.getCurrentUser();
        assert firebaseUser != null;
        final Map<String,Object> hashmap = new HashMap<>();
        Log.i("job2",company_name);
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("companies");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot companySnapshot:snapshot.getChildren()){
                        Companies companies = companySnapshot.getValue(Companies.class);

                        assert companies != null;
                        if(companies.getCompany_name().equals(company_name)){
                            //companies.getJob_roles().
                            location.setText(companies.getLocation());
                            Glide.with(getApplicationContext()).load(companies.getCompany_logo()).placeholder(R.drawable.google).into(company_logo);
                            for(DataSnapshot jobSnapshot:companySnapshot.child("job_roles").getChildren()){
                                Jobs jobs = jobSnapshot.getValue(Jobs.class);
                                assert jobs != null;
                                if(jobs.getJob_name().equals(job_position)) {

                                    job_description.setText(jobs.getJob_description());
                                    response.setText(jobs.getResponsibility());
                                    job_ex.setText(jobs.getExperience());
                                    job_edu.setText(jobs.getJob_edu());
                                    job_loc.setText(jobs.getJob_loc());
                                    lang.setText(jobs.getLang());




                                }
                            }
                        }
                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JobsDescription.this,JobsActivity.class);
                intent.putExtra("company_name",company_name);

                startActivity(intent);

            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("companies");



                reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot companySnapshot : snapshot.getChildren()) {
                            Companies companies = companySnapshot.getValue(Companies.class);

                            assert companies != null;
                            if (companies.getCompany_name().equals(company_name)) {
                                location.setText(companies.getLocation());
                                Glide.with(getApplicationContext()).load(companies.getCompany_logo()).placeholder(R.drawable.google).into(company_logo);
                                for (DataSnapshot jobSnapshot : companySnapshot.child("job_roles").getChildren()) {
                                    Jobs jobs = jobSnapshot.getValue(Jobs.class);
                                    assert jobs != null;

                                   // Here jobs.getApplied() should return a boolean to check whether it is true or not
                                    //change the type of applied in jobs.java to boolean  and also in database to do the changes
                                      applied = jobs.getApplied();


                                }
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }


                });

                if (!applied) {
                    Intent intent = new Intent(JobsDescription.this, SmartApplication.class);
                    intent.putExtra("company_name", company_name);
                    intent.putExtra("job_position", job_position);
                    startActivity(intent);
                    //applied = "Yes";



                }
                else{
                    Toast.makeText(JobsDescription.this,"Already Applied",Toast.LENGTH_SHORT).show();

                }

            }



        });
    }

    @Override
    public void onBackPressed() {


        super.onBackPressed();
        finish();
    }
}
