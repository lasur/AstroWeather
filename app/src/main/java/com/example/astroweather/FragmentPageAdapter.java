package com.example.astroweather;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import java.util.HashMap;
import java.util.Objects;

public class FragmentPageAdapter extends FragmentPagerAdapter {

    private static HashMap<String, Fragment> fragments = new HashMap<>();
    private String [] titles = {"Moon", "Sun","Weather","Details","Forecast"};

    FragmentPageAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        fragments.put("moon", Moon.create(1, titles[0]));
        fragments.put("sun", Sun.create(2, titles[1]));
        fragments.put("weather", Weather.create(3, titles[2]));
        fragments.put("weatherdetails", WeatherDetails.create(4, titles[3]));
        fragments.put("weatherforecast", WeatherForecast.create(5, titles[4]));
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return Objects.requireNonNull(fragments.get("moon"));
            case 1:
                return Objects.requireNonNull(fragments.get("sun"));
            case 2:
                return Objects.requireNonNull(fragments.get("weather"));
            case 3:
                return Objects.requireNonNull(fragments.get("weatherdetails"));
            case 4:
                return Objects.requireNonNull(fragments.get("weatherforecast"));
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}