package com.example.astroweather;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.HashMap;
import java.util.Objects;

public class PageAdapter extends FragmentPagerAdapter {

    private String [] titles = {"Moon", "Sun"};
    private static HashMap<String, Fragment> fragments = new HashMap<>();

    PageAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        fragmentInit();
    }

    private void fragmentInit(){
        fragments.put("moon", Moon.create(1, titles[0]));
        fragments.put("sun", Sun.create(2, titles[1]));
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return Objects.requireNonNull(fragments.get("moon"));
            case 1:
                return Objects.requireNonNull(fragments.get("sun"));
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}