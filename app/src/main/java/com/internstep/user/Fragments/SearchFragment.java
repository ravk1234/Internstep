package com.internstep.user.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.internstep.user.Adapters.CompanyAdapter;
import com.internstep.user.InitialProfileActivity;
import com.internstep.user.Models.Companies;
import com.internstep.user.R;
import com.internstep.user.StartActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchFragment extends Fragment {
    private RecyclerView recyclerView;
    private CompanyAdapter companyAdapter;
    //EditText search;
    Button button;
    private List<Companies> companies;
    PopupMenu popup;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        recyclerView = view.findViewById(R.id.searchview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        button = view.findViewById(R.id.search_text);
        companies = new ArrayList<>();
        readUsers();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });

        /*search = view.findViewById(R.id.search_job);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchUser(s.toString());
                //searchUser(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(getView() == null){
            return;
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    // handle back button's click listener
                    //finish();
                    //Objects.requireNonNull(getActivity()).finish();
                    Objects.requireNonNull(getActivity()).finishAffinity();
                    return true;
                }
                return false;
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
                        in.putExtra("hello","hello");
                        startActivity(in);
                        Objects.requireNonNull(getActivity()).finish();
                    break;
                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getActivity(),StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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


    private void searchUser(final String str) {
        final int[] x = {0};
        final int[] y = {0};
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //Query query = FirebaseDatabase.getInstance().getReference("Companies").orderByChild("search").startAt(str).endAt(str + "\uf8ff");
        Log.i("string",str);
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Companies");

            Query query = FirebaseDatabase.getInstance().getReference("Companies").orderByChild("company_name").startAt(str).endAt(str + "\uf8ff");
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    companies.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Companies company = snapshot.getValue(Companies.class);


                        assert company != null;
                        //assert firebaseUser != null;

                            companies.add(company);
                            Log.i("company",company.getCompany_name());


                        //Log.i("hybrid",company.getJob_roles().toString());

                    }


                    companyAdapter = new CompanyAdapter(getContext(), companies, "user");
                    recyclerView.setAdapter(companyAdapter);
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
    }



    private void readUsers() {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Companies");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                companies.clear();
                 //companies = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Companies company = snapshot.getValue(Companies.class);

                    assert company!= null;
                    //assert firebaseUser != null;
                    //if (!user.getId().equals(firebaseUser.getUid())) {
                    if(company.getOpen_positions() != 0) {
                        companies.add(company);
                        Log.d("companyname", company.getCompany_name());
                    }
                    //}
                }
                companyAdapter = new CompanyAdapter(getContext(), companies, "user");
                recyclerView.setAdapter(companyAdapter);
                //Log.i("rapid", "fire");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



}
