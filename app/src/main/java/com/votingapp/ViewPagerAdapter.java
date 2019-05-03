package com.votingapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.votingapp.fragments.ListFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        ListFragment listFragment = new ListFragment();
        position++;

        Bundle bundle = new Bundle();
        bundle.putString("currentTab", Integer.toString(position));
        listFragment.setArguments(bundle);

        return listFragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        position++;
        if(position == 1) return "Гласувания";
        else if(position == 2) return "Анкети";
        else if(position == 3) return "Референдуми";
        else return "error";
    }
}
