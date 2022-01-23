package com.internstep.user;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

import com.internstep.user.Models.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class Register extends AppCompatActivity {
    EditText fullName,emailAddress,phoneNumber,dob;
    Button next,back;
    FirebaseAuth mAuth;
    Spinner spinner1;
    private boolean isValidUser;

    String gender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);

        fullName = findViewById(R.id.fullname);
        spinner1 = findViewById(R.id.gender);
        emailAddress = findViewById(R.id.emailId);
        phoneNumber = findViewById(R.id.phoneNo);
        dob = findViewById(R.id.dob);
        next = findViewById(R.id.next);
        back = findViewById(R.id.toolbar_signIn);
        mAuth = FirebaseAuth.getInstance();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, StartActivity.class);
                //intent.putExtra("user_data",user);
                startActivity(intent);
                //finish();

            }
        });


        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new
                DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        //updateLabel();
                        String myFormat = "dd/MM/yy"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                        dob.setText(sdf.format(myCalendar.getTime()));
                    }

                };
        dob.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    new DatePickerDialog(Register.this, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
                return false;
            }
        });








        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(Register.this,android.R.layout.
                simple_list_item_1,getResources().getStringArray(R.array.gender));

        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(myAdapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //spinner1.setSelection(i);
                if(adapterView.getItemAtPosition(i).equals("Gender")){

                }else{
                    gender =(String)adapterView.getItemAtPosition(i);

                }

                //gender =(String)adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //spinner1.setSelection(0);
            }
        });



        /*User user = new User();
        user.setName(fullName.toString());
        user.setEmailAddress(emailAddress.toString());
        user.setGender(spinner1.toString());
        user.setPhoneNumber(phoneNumber.toString());
        user.setDob(dob.toString());*/

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean val =true;
                if (val){
                    String f_name = fullName.getText().toString();
                    String emailId = emailAddress.getText().toString();
                    //String gender = spinner1.toString();
                    String date = dob.getText().toString();
                    String phoneNo = phoneNumber.getText().toString();

                    if(TextUtils.isEmpty(f_name) || TextUtils.isEmpty(emailId) ||
                            TextUtils.isEmpty(gender) || TextUtils.isEmpty(date) || TextUtils.isEmpty(phoneNo)  ){
                        //mProgress.dismiss();
                        Toast.makeText(Register.this, "All credentials are required!", Toast.LENGTH_SHORT).show();

                    } else if(!Patterns.EMAIL_ADDRESS.matcher(emailId).matches()){
                        Toast.makeText(Register.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();

                    }else if(!Patterns.PHONE.matcher(phoneNo).matches()){
                        Toast.makeText(Register.this, "Please enter a correct mobile number", Toast.LENGTH_SHORT).show();

                    }else if(checkEmailExistsOrNot()){
                        Toast.makeText(Register.this,"Email id is already registered", Toast.LENGTH_SHORT).show();
                    }
                    else {

                        User user = new User();
                        user.setName(f_name);
                        user.setEmailAddress(emailId);
                        user.setGender(gender);
                        user.setPhoneNumber(phoneNo);
                        user.setDob(date);
                        user.setUid("default");
                        user.setShort_description("default");
                        user.setJob_title("default");
                        user.setEducation("default");
                        user.setLinkedin("default");
                        user.setDribble("default");
                        user.setGithub("default");
                        user.setImgUrl("default");
                        user.setIdcard("default");



                        Intent intent = new Intent(Register.this, Password.class);
                        intent.putExtra("user_data",user);
                        startActivity(intent);
                        //openNewActivity();
                        //finish();
                    }


                }else {
                    next.setEnabled(false);
                    Toast.makeText(Register.this, "button is not working ", Toast.LENGTH_SHORT).show();
                }


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

    /*private  void openNewActivity() {
        Intent intent = new Intent(this, Password.class);
        startActivity(intent);
    }*/
    public boolean checkEmailExistsOrNot(){
        //final int[] x = {0};
        mAuth.fetchSignInMethodsForEmail(emailAddress.getText().toString()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                Log.d("email",""+ Objects.requireNonNull(task.getResult()).getSignInMethods().size());
                /*if (task.getResult().getSignInMethods().size() == 0){
                    // email not existed
                   // x[0] =1; //true
                }else {
                    // email existed
                    //x[0] = 0;
                }*/
                isValidUser = task.getResult().getSignInMethods().isEmpty();
                if(isValidUser){
                    Log.e("tag","true");
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });

        return isValidUser;

        /*if(x[0]==0)
            return false;
        else
            return true;*/

    }



}
