package com.internstep.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.internstep.user.Adapters.JobsAdapter;
import com.internstep.user.Models.Jobs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class JobsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private JobsAdapter jobsAdapter;
    private ArrayList<String> job_roles1;
    private Map<String,Jobs> jobsHashMap;
    private String str1,status;
    private String company_name;
    private List<Jobs> jobs;
    private Button back,menu;
    private PopupMenu popup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_search2);

        recyclerView = findViewById(R.id.searchview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(JobsActivity.this));
        back = findViewById(R.id.toolbar_back_search2);
        menu = findViewById(R.id.search_text);
        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        if(b!=null)
        {
             company_name =(String) b.get("company_name");

        }
        assert company_name != null;
        //Log.d("company", company_name);
        job_roles1 = new ArrayList<>();
        jobs = new ArrayList<>();
        jobsHashMap = new HashMap<>();
        getJobRoles(company_name);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JobsActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v);
            }
        });

    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(Objects.requireNonNull(JobsActivity.this), v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.edit_profile:
                        Intent in = new Intent(JobsActivity.this, InitialProfileActivity.class);
                        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(in);
                        finish();
                        break;
                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(JobsActivity.this,StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                        break;
                }

                return true;
            }
        });

        popup.show();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(popup!=null)
        popup.dismiss();
        //this.mProgress.cancel();
    }


    private void getJobRoles(final String str) {
        final int[] count = {0};
        final String[] jobs1 = new String[1];

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
         DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Companies");
        reference.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                count[0]++;
                //company1 = snapshot.getValue(Companies.class);
                //Log.i("name1", company1.getCompany_name());
                //companies1.add(company1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //companies.clear();
                //companies = new ArrayList<>();
                int x =0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if(Objects.requireNonNull(snapshot.child("company_name").getValue()).toString().equals(str)) {
                        //Jobs job_role =snapshot.child("job_roles").getValue(Jobs.class);
                        /*for(int j=0 ; j<=count[0];j++){
                            if(snapshot.child("job_roles").exists()){
                                 //job_role = snapshot.child("job_roles").
                                Iterable<DataSnapshot> jobSnapshot = snapshot.child("job_roles").getChildren();
                            }
                        }*/
                         //job_roles1 = new ArrayList<>();
                        for(DataSnapshot jobSnapshot:snapshot.child("job_roles").getChildren()){
                            x++;

                            //Jobs job_role = new Jobs();
                            Jobs job_role = jobSnapshot.getValue(Jobs.class);
                            assert job_role != null;
                             str1 = job_role.getJob_name();
                             //status = job_role.getJob_status();

                            //if(status.equals("default"))
                            job_roles1.add(str1);

                            //jobsHashMap = (Map<String, Jobs>) jobSnapshot.getValue();
                        }





                        //Companies company = snapshot.getValue(Companies.class);

                        //assert company!= null;
                        //assert firebaseUser != null;
                        //if (!user.getId().equals(firebaseUser.getUid())) {
                        //companies.add(company);
                        Log.d("jobname", str1);
                        //Log.d("name",snapshot.child("company_name").getValue().toString());

                    }
                    //}
                }
                /*for(int j =0;j<jobs.size();j++){
                    Jobs job = jobs.get(j);
                    str1 = job.getJob_name();

                }*/
                //String str[] =new String[job_roles.size()];
                /*for(int j =0;j<job_roles.size();j++){
                      str1 =job_roles.get(j);
                     jobsAdapter = new JobsAdapter(JobsActivity.this,str1, "user");
                    recyclerView.setAdapter(jobsAdapter);
                }*/

                jobsAdapter = new JobsAdapter(JobsActivity.this,job_roles1, str);
                recyclerView.setAdapter(jobsAdapter);
                //Log.i("string",str1);

                //Log.i("rapid", "fire");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        //this.finish();
        //Intent intent = new Intent(Register.this, StartActivity.class);
        //intent.putExtra("user_data",user);
        //startActivity(intent);

        super.onBackPressed();
        finish();
    }
}
