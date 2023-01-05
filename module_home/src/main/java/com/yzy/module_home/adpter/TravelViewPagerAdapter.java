package com.yzy.module_home.adpter;


import com.yzy.module_home.bean.Travel;
import com.yzy.module_home.widget.expandPage.ExpandAdapter;
import com.yzy.module_home.widget.expandPage.fragments.TravelExpandFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


public class TravelViewPagerAdapter extends ExpandAdapter {

    List<Travel> travels;

    public TravelViewPagerAdapter(FragmentManager fm) {
        super(fm);
        travels = new ArrayList<>();
    }

    public void addAll(List<Travel> travels) {
        this.travels.addAll(travels);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        Travel travel = travels.get(position);
        return TravelExpandFragment.newInstance(travel);
    }

    @Override
    public int getCount() {
        return travels.size();
    }

}
