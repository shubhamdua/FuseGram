package com.dua.fusegram.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.dua.fusegram.Fragments.EditProfileFragment;
import com.dua.fusegram.Fragments.LogOutFragment;
import com.dua.fusegram.R;

import java.util.ArrayList;

public class AccountActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    FrameLayout container;
    private String TAG;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        setUpView();

        linearLayout=(LinearLayout) findViewById(R.id.linearLayout);
        container=(FrameLayout) findViewById(R.id.acc_frag_container);
        container.setVisibility(View.GONE);
        ImageView back = (ImageView) findViewById(R.id.imgBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void setUpView(){
        ListView mList = (ListView) findViewById(R.id.settingsList);
        ArrayList<String> mArrayList = new ArrayList<>();
        mArrayList.add(getString(R.string.editProfile));
        mArrayList.add(getString(R.string.logOut));

        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,mArrayList);
        mList.setAdapter(adapter);

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                linearLayout.setVisibility(View.GONE);
                container.setVisibility(View.VISIBLE);
                if(i==0){
                    TAG = "Edit Profile Fragment";
                    fragment = new EditProfileFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.acc_frag_container, fragment, TAG).commit();
                }
                else if(i==1){
                    TAG = "Log Out Fragment";
                    fragment = new LogOutFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.acc_frag_container, fragment, TAG).commit();
                }

            }
        });

    }
}
