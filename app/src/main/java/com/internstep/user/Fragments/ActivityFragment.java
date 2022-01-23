package com.internstep.user.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.internstep.user.Adapters.ActivityAdapter;
import com.internstep.user.InitialProfileActivity;
import com.internstep.user.Models.Companies;
import com.internstep.user.Models.Jobs;
import com.internstep.user.Models.User;
import com.internstep.user.R;
import com.internstep.user.StartActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ActivityFragment extends Fragment {
    private RecyclerView recyclerView;
    private Button menu;
    private PopupMenu popup;
    private ActivityAdapter activityAdapter;
    private List<Companies> companies;
    private List<Jobs> jobsList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_activity, container, false);
        menu = view.findViewById(R.id.search_text);
        recyclerView = view.findViewById(R.id.activityview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        companies = new ArrayList<>();
        jobsList = new ArrayList<>();
        displayJobs();

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPopup(v);

            }
        });

        return view;
    }

    private void displayJobs() {
        final String[] job_name = new String[1];
        final String[] username = new String[1];
        final String company_logo ;
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 User users =  dataSnapshot.getValue(User.class);
                assert users!= null;
                username[0] = users.getName();
                DataSnapshot dataSnapshot1= dataSnapshot.child("companies");
                companies.clear();
                jobsList.clear();
                for(DataSnapshot snapshot : dataSnapshot1.getChildren()){
                    Companies company = snapshot.getValue(Companies.class);
                    assert company != null;
                    for(DataSnapshot jobsSnapshot : snapshot.child("job_roles").getChildren() ) {
                        Jobs jobs = jobsSnapshot.getValue(Jobs.class);


                        assert jobs != null;
                        //job_name[0] = jobs.getJob_name();
                        if (!jobs.getJob_status().equals("default")) {
                            //companies.add(company);
                            Log.i("jobs",jobs.getJob_name());
                            jobsList.add(jobs);
                        }
                    }

                }
                Log.i("count",String.valueOf(jobsList.size()));
                activityAdapter = new ActivityAdapter(getContext(), jobsList,username[0]);
                recyclerView.setAdapter(activityAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void showPopup(View v) {
         popup = new PopupMenu(Objects.requireNonNull(getActivity()), v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.edit_profile:
                        Intent in = new Intent(getActivity(), InitialProfileActivity.class);
                        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(in);
                        Objects.requireNonNull(getActivity()).finish();
                        break;
                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getActivity(), StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                        Objects.requireNonNull(getActivity()).finish();
                        break;
                    case R.id.support:
                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        //Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        //emailIntent.setType("plain/text");
                        emailIntent.putExtra(Intent.EXTRA_EMAIL,new String[] {"ceo@internstep.in"});
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Re: Support Regarding [ Enter your reason here ]");
                        emailIntent.setType("message/rfc822");
                        Objects.requireNonNull(getContext()).startActivity(Intent.createChooser(emailIntent, null));

                        break;
                    case R.id.feedback:
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://forms.gle/Q3cnKZNazju7qQGg9"));
                        Objects.requireNonNull(getContext()).startActivity(browserIntent);


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
}
