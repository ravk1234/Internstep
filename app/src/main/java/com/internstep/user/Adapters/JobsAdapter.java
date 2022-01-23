package com.internstep.user.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.internstep.user.JobsDescription;
import com.internstep.user.Models.Companies;
import com.internstep.user.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JobsAdapter extends RecyclerView.Adapter<JobsAdapter.ViewHolder> {
    private Context mcontext;
    private List<Companies> mcompanies;
    private ArrayList<String> mjob_positions;
    private String mjobs;
    private String screen;
    private String u_company_logo,u_job_position;
    private String u_company_name;
    public JobsAdapter(Context mcontext, ArrayList<String> mjob_positions,String screen) {
        this.mcontext = mcontext;
        this.mjob_positions = mjob_positions;
        this.screen = screen;



    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.job_position_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        final String str1 = mjob_positions.get(i);

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Companies");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    if(Objects.requireNonNull(snapshot.child("company_name").getValue()).toString().equals(screen)) {
                        Companies company = snapshot.getValue(Companies.class);

                        assert company != null;

                        u_company_logo = company.getCompany_logo();
                        u_company_name = company.getCompany_name();
                        Glide.with(mcontext).load(u_company_logo).into(viewHolder.company_logo);
                        Log.i("name",u_company_name);

                    }
                    //}
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });








        viewHolder.job_position.setText(str1);


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, JobsDescription.class);
                //intent.putExtra("userid", user.getId());
                intent.putExtra("job_position",str1);
                intent.putExtra("company_name",screen);
                mcontext.startActivity(intent);

            }
        });

    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView company_logo,next;
        public TextView job_position;

        public ViewHolder(View itemView) {
            super(itemView);

            company_logo = itemView.findViewById(R.id.company_logo);
            job_position = itemView.findViewById(R.id.job_title);
            next = itemView.findViewById(R.id.next);
        }


    }

    @Override
    public int getItemCount() {

        int arr = 0;
        try{
            if(mjob_positions.size()==0){
                arr = 0;
            }
            else{
                arr=mjob_positions.size();
            }

        }catch (Exception e){
        }
        return arr;
        }

        //return mcompanies.size();

}
