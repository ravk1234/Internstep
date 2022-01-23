package com.internstep.user.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.internstep.user.InitialProfileActivity;
import com.internstep.user.Models.User;
import com.internstep.user.R;
import com.internstep.user.StartActivity;

import java.util.Objects;

public class ProfileFragment extends Fragment {
    TextView job_title,description,name;
    ImageView profile_photo,linkedin,github,dribble;
    DatabaseReference reference;
    FirebaseUser firebaseUser;
    Button button;
    String u_linkedin,u_insta,u_dribble;
    private ProgressDialog mProgress;
    private PopupMenu popup;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Bundle bundle = this.getArguments();
        if(bundle != null){
            u_dribble = bundle.getString("dribble");
            u_insta = bundle.getString("insta");
            u_linkedin = bundle.getString("linkedin");
            //String yourText = getArguments().getString("key");
        }
        job_title = view.findViewById(R.id.nameofskill);
        name = view.findViewById(R.id.name);
        description = view.findViewById(R.id.description);
        profile_photo = view.findViewById(R.id.person);
        linkedin = view.findViewById(R.id.linkedin);
        github = view.findViewById(R.id.github);
        dribble = view.findViewById(R.id.dribble);
        button = view.findViewById(R.id.search_text);




        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);
                //user.setImgUrl(imgurl);
                name.setText(user.getName());
                job_title.setText(user.getJob_title());
                description.setText(user.getShort_description());
                if(user.getImgUrl().equals("default")){
                    profile_photo.setImageResource(R.mipmap.ic_launcher);
                }else{
                    if(isAdded())
                        Glide.with(Objects.requireNonNull(getActivity())).load(user.getImgUrl()).into(profile_photo);
                }
                if(user.getDribble().equals("default")){
                    dribble.setVisibility(View.INVISIBLE);
                }else{
                    if(isAdded()) {
                        u_dribble = user.getDribble();
                        dribble.setImageResource(R.drawable.dribble_color);
                        dribble.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!u_dribble.startsWith("http://") && !u_dribble.startsWith("https://"))
                                    u_dribble = "http://" + u_dribble;
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(u_dribble));
                                startActivity(browserIntent);
                            }
                        });
                    }
                }
                if (user.getGithub().equals("default")){
                    github.setVisibility(View.INVISIBLE);
                }else {
                    if (isAdded()) {
                        github.setImageResource(R.drawable.github_color);
                        u_insta = user.getGithub();
                        github.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!u_insta.startsWith("http://") && !u_insta.startsWith("https://"))
                                    u_insta = "http://" + u_insta;
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(u_insta));
                                startActivity(browserIntent);
                            }
                        });


                    }
                }
                if(user.getLinkedin().equals("default")){
                    linkedin.setVisibility(View.INVISIBLE);
                }else {
                    if (isAdded()) {
                        linkedin.setImageResource(R.drawable.linkedin_coloured);
                        u_linkedin = user.getLinkedin();
                        linkedin.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                    if (!u_linkedin.startsWith("http://") && !u_linkedin.startsWith("https://"))
                                        u_linkedin = "https://" + u_linkedin;


                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(u_linkedin));
                                        startActivity(browserIntent);

                            }
                        });

                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //mProgress.dismiss();
            }
        });






        return view;
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
                        Objects.requireNonNull(getActivity()).finish();
                        startActivity(in);
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
                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getActivity(), StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                        Objects.requireNonNull(getActivity()).finish();
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

    }


}
