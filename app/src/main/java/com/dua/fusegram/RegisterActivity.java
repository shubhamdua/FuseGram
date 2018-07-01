package com.dua.fusegram;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    ImageButton datePickImgBtn;
    EditText DOB,username,pName,password,confirmPass,email,contact;
    Button btnNext;
    TextView loginHere;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    ProgressDialog mDialog;
    String sUsername,sName,sPassword,sConfirmPass,sEmail,sContact,sDOB;

    int year_x, month_x, date_x;
    static final int DIALOG_ID=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText) findViewById(R.id.edtUsername);
        pName = (EditText) findViewById(R.id.edtName);
        password = (EditText) findViewById(R.id.edtPassword);
        confirmPass = (EditText) findViewById(R.id.edtConfirmPass);
        email = (EditText) findViewById(R.id.edtEmail);
        contact = (EditText) findViewById(R.id.edtContact);
        DOB = (EditText) findViewById(R.id.edtDOB);
        btnNext = (Button) findViewById(R.id.btnNext);
        loginHere = (TextView) findViewById(R.id.txtLoginHere);

        mAuth= FirebaseAuth.getInstance();

        btnNext.setOnClickListener(this);
        loginHere.setOnClickListener(this);
        mDialog=new ProgressDialog(this);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

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

        if(TextUtils.isEmpty(sUsername)){
            Toast.makeText(this, "Enter Username", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(sName)){
            Toast.makeText(this, "Enter Name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(sPassword)){
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
        }
        else if(sPassword.length()<6){
            Toast.makeText(this, "Password must be of atleast 6 digits", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(sConfirmPass)){
            Toast.makeText(this, "Please Confirm Password", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(sEmail)){
            Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(sContact)){
            Toast.makeText(this, "Enter Contact", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(sDOB)){
            Toast.makeText(this, "Enter Date Of Birth", Toast.LENGTH_SHORT).show();
        }
        else if(!sPassword.equals(sConfirmPass)){
            Toast.makeText(this, "Password and Confirmed Password are not same", Toast.LENGTH_SHORT).show();
        }
        else {
            mDialog.setMessage("Creating Account Please wait...");
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.show();
            mAuth.createUserWithEmailAndPassword(sEmail, sPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        mDialog.dismiss();
                        onAuth(task.getResult().getUser());
                        //startActivity(new Intent(RegisterActivity.this, OTPActivity.class));
                    } else {
                        Toast.makeText(RegisterActivity.this, "Error in account creation !!", Toast.LENGTH_SHORT).show();
                    }
                }


            });
        }

    }

    private void onAuth(FirebaseUser user) {
        createANewUser(user.getUid());
    }

    private void createANewUser(String uid) {
        User user = buildNewUser();
        mDatabase.child(uid).setValue(user);
    }

    private User buildNewUser() {

        return new User(
                getUsername(),
                getPname(),
                getEmail(),
                getContact(),
                getDOB(),
                new Date().getTime()
        );
    }

    public String getUsername() {
        return sUsername;
    }

    public String getPname() {
        return sName;
    }

    public String getEmail() {
        return sEmail;
    }

    public String getContact() {
        return sContact;
    }

    public String getDOB() {
        return sDOB;
    }
}
