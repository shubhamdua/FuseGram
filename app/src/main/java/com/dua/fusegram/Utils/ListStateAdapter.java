package com.dua.fusegram.Utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListStateAdapter extends FragmentStatePagerAdapter {

    public final List<Fragment> mFragList = new ArrayList<>();
    public final HashMap<Fragment,Integer> mFragment = new HashMap<>();
    public final HashMap<String,Integer> mFragNum = new HashMap<>();
    public final HashMap<Integer,String> mFragName = new HashMap<>();

    public ListStateAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment frag, String mName){

        mFragList.add(frag);
        mFragment.put(frag,mFragList.size()-1);
        mFragNum.put(mName,mFragList.size()-1);
        mFragName.put(mFragList.size()-1,mName);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragList.get(position);
    }

    @Override
    public int getCount() {
        return mFragList.size();
    }

    public Integer getFragNumber(String fragName){
        if(mFragNum.containsKey(fragName)){
            return mFragNum.get(fragName);
        }
        else{
            return null;
        }
    }

    public Integer getFragNumber(Fragment frag){
        if(mFragNum.containsKey(frag)){
            return mFragNum.get(frag);
        }
        else{
            return null;
        }
    }

    public String getFragNumber(Integer fragNum){
        if(mFragNum.containsKey(fragNum)){
            return mFragName.get(fragNum);
        }
        else{
            return null;
        }
    }
}
