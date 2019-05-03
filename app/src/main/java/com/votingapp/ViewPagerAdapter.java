package com.votingapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.votingapp.fragments.ListVotingsFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        ListVotingsFragment listVotingsFragment = new ListVotingsFragment();
        position++;

        Bundle bundle = new Bundle();
        bundle.putString("message", "Fragment :" + position);
        listVotingsFragment.setArguments(bundle);

        return listVotingsFragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
