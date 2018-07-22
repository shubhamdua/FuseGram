package com.dua.fusegram.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dua.fusegram.Activity.LoginActivity;
import com.dua.fusegram.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogOutFragment extends Fragment implements View.OnClickListener{

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    Button logOut,cancel;
    TextView txtProgress;
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.log_out_frag, container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        logOut=(Button) view.findViewById(R.id.btnLogOut);
        cancel=(Button) view.findViewById(R.id.btnCancel);
        progressBar =  (ProgressBar) view.findViewById(R.id.progressBar);
        txtProgress = (TextView) view.findViewById(R.id.txtprogess);
        setupAuth();

        logOut.setOnClickListener(this);
        cancel.setOnClickListener(this);

    }

    private void setupAuth(){

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                } else {
                    // User is signed out
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
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

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.btnLogOut:
                progressBar.setVisibility(View.VISIBLE);
                txtProgress.setVisibility(View.VISIBLE);
                mAuth.signOut();
                getActivity().finish();
            case R.id.btnCancel:
                getActivity().finish();
        }

    }
}
