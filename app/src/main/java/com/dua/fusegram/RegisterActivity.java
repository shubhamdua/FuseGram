package com.dua.fusegram;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "RegisterActivity";

    private Context mContext;
    ImageButton datePickImgBtn;
    EditText DOB,username,pName,password,confirmPass,email,contact;
    Button btnNext;
    TextView loginHere;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference mDatabase;
    private FirebaseMethods firebaseMethods;
    ProgressBar progressBar;
    String sUsername,sName,sPassword,sConfirmPass,sEmail,sContact,sDOB;

    private String append = "";


    int year_x, month_x, date_x;
    static final int DIALOG_ID=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mContext = RegisterActivity.this;
        firebaseMethods = new FirebaseMethods(mContext);
        Log.d(TAG, "onCreate: started.");

        username = (EditText) findViewById(R.id.edtUsername);
        pName = (EditText) findViewById(R.id.edtName);
        password = (EditText) findViewById(R.id.edtPassword);
        confirmPass = (EditText) findViewById(R.id.edtConfirmPass);
        email = (EditText) findViewById(R.id.edtEmail);
        contact = (EditText) findViewById(R.id.edtContact);
        DOB = (EditText) findViewById(R.id.edtDOB);
        btnNext = (Button) findViewById(R.id.btnNext);
        loginHere = (TextView) findViewById(R.id.txtLoginHere);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        setupFirebaseAuth();

        btnNext.setOnClickListener(this);
        loginHere.setOnClickListener(this);

        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        date_x = cal.get(Calendar.DAY_OF_MONTH);

        showDatePickerDialog();
    }

    public void showDatePickerDialog(){

        datePickImgBtn = findViewById(R.id.datePickImgBtn);
        datePickImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DIALOG_ID);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id){
        if(id==DIALOG_ID)
            return new DatePickerDialog(this,datePickerListener,year_x,month_x,date_x);
        return null;

    }

    private DatePickerDialog.OnDateSetListener datePickerListener= new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

            year_x=year;
            month_x=monthOfYear+1;
            date_x=dayOfMonth;
            DOB.setText(date_x+"-"+month_x+"-"+year_x);

        }
    };

    @Override
    public void onClick(View view) {

        if(view==btnNext){
            registerUser();
        }
        else if(view==loginHere){
            startActivity(new Intent(this,LoginActivity.class));
        }
    }

    private void registerUser() {

        sUsername = username.getText().toString();
        sName = pName.getText().toString();
        sPassword = password.getText().toString();
        sConfirmPass = confirmPass.getText().toString();
        sEmail = email.getText().toString();
        sContact = contact.getText().toString();
        sDOB = DOB.getText().toString();

        if(checkInputs(sUsername,sName,sPassword,sConfirmPass,sEmail,sContact,sDOB)){
            progressBar.setVisibility(View.VISIBLE);
            firebaseMethods.registerNewEmail(sEmail, sPassword);

        }


    }


    private boolean checkInputs( String username, String name, String password, String cpassword, String email, String contact, String dob){
        Log.d(TAG, "checkInputs: checking inputs for null values.");
        if(username.equals("") || name.equals("") || password.equals("") || cpassword.equals("") || email.equals("") || contact.equals("") || dob.equals("")){
            Toast.makeText(mContext, "All fields must be filled out.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(password.length()<6){
            Toast.makeText(mContext, "Password length must be atleast 6 characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!password.equals(cpassword)){
            Toast.makeText(mContext, "Password and Confirmed Password are not same...", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean isStringNull(String string){
        Log.d(TAG, "isStringNull: checking string if null.");

        if(string.equals("")){
            return true;
        }
        else{
            return false;
        }
    }


    /**
     * Setup the firebase auth object
     */
    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();
       mDatabase = FirebaseDatabase.getInstance().getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //1st check: Make sure the username is not already in use
                            if(firebaseMethods.checkIfUsernameExists(sUsername, dataSnapshot)){
                                append = mDatabase.push().getKey().substring(3,10);
                                Log.d(TAG, "onDataChange: username already exists. Appending random string to name: " + append);
                            }
                            sUsername = sUsername + append;

                            //add new user to the database
                            firebaseMethods.addNewUser(sUsername, sName, sEmail, sContact,sDOB,"","","");
                            Toast.makeText(mContext, "Signup successful. Sending verification email.", Toast.LENGTH_SHORT).show();

                            mAuth.signOut();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    finish();

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
