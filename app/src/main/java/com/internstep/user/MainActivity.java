package com.internstep.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.internstep.user.Fragments.ActivityFragment;
import com.internstep.user.Fragments.ProfileFragment;
import com.internstep.user.Fragments.SearchFragment;
import com.internstep.user.Models.User;



public class MainActivity extends AppCompatActivity {

    private final static String TAG ="SEARCH";
    private final static String TAG1 = "BEAT";
    private final static String TAG2 = "PROFILE";
    private static int next,history;
    BottomNavigationView bottomNav;
    String insta_link,dribble_link,linked_link;
    FragmentManager fm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fm = getSupportFragmentManager();
         bottomNav = findViewById(R.id.main_nav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        bottomNav.setItemIconTintList(null);
        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        if (b == null) {

            loadFragment(new SearchFragment(),TAG);
        }
        else{

            loadFragment(new ProfileFragment(),TAG2);
            bottomNav.setSelectedItemId(R.id.profile);
        }
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                insta_link = user.getGithub();
                dribble_link = user.getDribble();
                linked_link = user.getLinkedin();




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //mProgress.dismiss();
            }
        });

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;


                    switch (item.getItemId()){
                        case R.id.search:

                            selectedFragment = new SearchFragment();

                            loadFragment(selectedFragment,TAG);
                            break;
                        case R.id.beat:

                            selectedFragment = new ActivityFragment();
                            loadFragment(selectedFragment,TAG1);
                            break;
                        case R.id.profile:

                            selectedFragment = new ProfileFragment();
                            Bundle bundle = new Bundle();
                            //bundle.putString("insta",insta_link);
                            bundle.putString("linkedin",linked_link);
                            bundle.putString("dribble",dribble_link);
                            selectedFragment.setArguments(bundle);
                            loadFragment(selectedFragment,TAG2);
                            break;

                    }


                    return true;
                }
            };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation,menu);
        return true;
    }

    @Override
    public void onBackPressed(){

        int selectedItemId = bottomNav.getSelectedItemId();

        Fragment f = fm.findFragmentByTag("search");
        Fragment f1 = fm.findFragmentByTag(TAG2);
        String currentFragmentTag = fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1).getName();


        assert f1 != null;
        if(fm.getBackStackEntryCount() > 0){
            fm.popBackStack();
            assert f1.getTag() != null;
            assert currentFragmentTag != null;
            Log.i("fragment",currentFragmentTag);
            if(currentFragmentTag.equals(TAG2)){
                bottomNav.setSelectedItemId(R.id.beat);
            }
            else if(currentFragmentTag.equals(TAG1)){
                bottomNav.setSelectedItemId(R.id.search);
            }
            else{
                bottomNav.setSelectedItemId(R.id.search);
            }

            //if(bottomNav.getSe)

        } else {
            bottomNav.setSelectedItemId(R.id.search);
            finish();
            super.onBackPressed();


        }






    }

    private void loadFragment(Fragment fragment ,String fragment1){
         FragmentManager fm = MainActivity.this.getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment,fragment1);
        if(!fragment1.equals(TAG)) {
            transaction.addToBackStack(fragment1);
        }
        transaction.commit();

        Log.i("stack", String.valueOf(getSupportFragmentManager().getBackStackEntryCount()));

    }


}
