package com.dua.fusegram.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dua.fusegram.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth emailAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser mUser;
    Button userLogin;
    EditText userEmailId;
    EditText userPassword;
    private ProgressDialog progressDialog;


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userEmailId=findViewById(R.id.edtEmailId);
        userPassword=findViewById(R.id.edtPassword);
        userLogin=findViewById(R.id.btnLogin);
        progressDialog=new ProgressDialog(this);

        emailAuth=FirebaseAuth.getInstance();

        mAuthListener= new FirebaseAuth.AuthStateListener()
        {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = firebaseAuth.getCurrentUser();
                if (mUser != null) {
                    // User is signed in
                    Log.d("LoginActivity", "onAuthStateChanged:signed_in:" + mUser.getUid());
                } else {
                    // User is signed out
                    Log.d("LoginActivity", "onAuthStateChanged:signed_out");
                }
            }
        };

        userEmailId.addTextChangedListener(mTextWatcher);
        userPassword.addTextChangedListener(mTextWatcher);

        // run once to disable if empty
        checkFieldsForEmptyValues();


        userLogin.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                String email=userEmailId.getText().toString().trim();
                String password=userPassword.getText().toString();

                logInUser(email,password);
                //default login id is ajaykhanna123ak@gmail.com
                //////password=123456
            }
        });

       // If the user is logged in then navigate to HomeActivity and call 'finish()'
        if(emailAuth.getCurrentUser() != null){
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

    }


    public void startRegisterActivity(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);;
        startActivity(intent);
    }






    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // check Fields For Empty Values
            checkFieldsForEmptyValues();
        }
    };

    @SuppressLint("ResourceAsColor")
    void checkFieldsForEmptyValues(){

        String email = userEmailId.getText().toString();
        String password = userPassword.getText().toString();

        if(email.equals("")|| password.equals("")){
            userLogin.setEnabled(false);
            userLogin.setBackgroundResource(R.drawable.rounded_btn1);
            int c=getResources().getColor(R.color.buttonDisabledColor);
            userLogin.setTextColor(c);
        } else {
            userLogin.setEnabled(true);
            userLogin.setBackgroundResource(R.drawable.rounded_btn);
            userLogin.setTextColor(Color.WHITE);
        }
    }




    private void logInUser(String email, String password)
    {
        progressDialog.setTitle("Log In Account");
        progressDialog.setMessage("Please wait while we verify your Account...");
        progressDialog.show();
        emailAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        FirebaseUser user =emailAuth.getCurrentUser();

                        if(task.isSuccessful())
                        {
                            try{
                                if(user.isEmailVerified()){
                                    Toast.makeText(LoginActivity.this, "Email is verified..Successful Login...", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                    Toast.makeText(LoginActivity.this, "Email is not Verified..Check Mail inbox...", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    emailAuth.signOut();
                                }
                            }
                            catch(NullPointerException e){
                                Log.d("LoginActivity","OnComplete : NullPointerException: "+e.getMessage());
                            }

                        }
                        else
                        {
                            String message=task.getException().toString();
                            Toast.makeText(LoginActivity.this,"Error Occured: "+message,Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        emailAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener!=null)
            emailAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
