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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.internstep.user.JobsActivity;
import com.internstep.user.Models.Companies;
import com.internstep.user.R;

import java.util.List;

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.ViewHolder> {
    private Context mcontext;
    private List<Companies> mcompanies;
    private String screen;
    String lastMessage;
    FirebaseAuth mAuth;
    DatabaseReference reference;
    FirebaseUser firebaseUser;
    StorageReference ref;
    String u_company_name,u_company_logo;
    int u_company_positions;
    private boolean isChat;

    public CompanyAdapter(Context mcontext, List<Companies> mcompanies, String screen) {
        this.mcontext = mcontext;
        this.mcompanies = mcompanies;
        this.screen = screen;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.company_item,parent,false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        final Companies company = mcompanies.get(i);
        /*firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Companies");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    Companies companies = snapshot.getValue(Companies.class);
                    //user.setImgUrl(imgurl);
                    assert companies != null;
                     u_company_name = companies.getCompany_name();
                     u_company_logo = companies.getCompany_logo();
                     u_company_positions = companies.getOpen_positions();
                    Log.i("hybrid",u_company_name);


                }*/

                //} catch (Exception e) {
                //  e.printStackTrace();

                // }
           // }


          //  @Override
           // public void onCancelled(@NonNull DatabaseError databaseError) {

            //}
        //});

            //if(u_company_logo)
            Glide.with(mcontext).load(company.getCompany_logo()).into(viewHolder.company_logo);
            viewHolder.company_name.setText(company.getCompany_name());
            u_company_positions = company.getOpen_positions();
            Log.i("number",company.getCompany_name());
        String company_positions = u_company_positions + " open positions";
        viewHolder.job_positions.setText(company_positions);




        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, JobsActivity.class);
                intent.putExtra("company_name", company.getCompany_name());
                mcontext.startActivity(intent);
                //mcontext.finish();
                //(ge().mcontext).finish();

            }
        });

    }

    @Override
    public int getItemCount() {
        return mcompanies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView company_name;
        public TextView job_positions;
        public ImageView company_logo,next;

        public ViewHolder(View itemView){
            super(itemView);

            company_name = itemView.findViewById(R.id.name_of_company_heads);
            next = itemView.findViewById(R.id.next);
            job_positions = itemView.findViewById(R.id.application_process);
            company_logo = itemView.findViewById(R.id.logo);

            //company_name.setText(company.getCompany_name());
            //String company_positions = u_company_positions + "open positions";
            //job_positions.setText(company_positions);

        }
    }


}
