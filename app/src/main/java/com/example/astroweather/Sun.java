package com.example.astroweather;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;

import java.text.DecimalFormat;
import java.util.Calendar;

public class Sun extends Fragment {
    private static final String pageNumber = "page";
    private static final String title = "title";
    private final Handler refreshHandler = new Handler();
    private Runnable runnable;

    public Sun() {}

    static Sun create(int page, String title) {
        Sun fragment = new Sun();
        Bundle args = new Bundle();
        args.putInt(pageNumber, page);
        args.putString(Sun.title, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_sun, container, false);
        update(v);

        final double seconds = Settings.refreshTimeValue*60;
        runnable = new Runnable() {
            @Override
            public void run() {
                update(v);
                refreshHandler.postDelayed(this, (long) (seconds * 1000));
            }
        };
        refreshHandler.postDelayed(runnable, (long) (seconds * 1000));
        return v;
    }
    private void update(View v){
        AstroCalculator.Location astroLocation = new AstroCalculator.Location(
                Data.latitudeValue,
                Data.longitudeValue
        );

        AstroDateTime astroDateTime = new AstroDateTime();
        Calendar calendar = Calendar.getInstance();
        astroDateTime.setYear(calendar.get(Calendar.YEAR));
        astroDateTime.setMonth(calendar.get(Calendar.MONTH)+1);
        astroDateTime.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        astroDateTime.setHour(calendar.get(Calendar.HOUR));
        astroDateTime.setMinute(calendar.get(Calendar.MINUTE));
        astroDateTime.setSecond(calendar.get(Calendar.SECOND));
        astroDateTime.setTimezoneOffset(2);
        AstroCalculator astroCalculator = new AstroCalculator(astroDateTime, astroLocation);
        AstroCalculator.SunInfo sunInfo = astroCalculator.getSunInfo();

        DecimalFormat azimuthFormatter = new DecimalFormat("#.##");
        final String DEGREE  = "\u00b0";

        TextView sunrise =  v.findViewById(R.id.sunRise);
        sunrise.setText(sunInfo.getSunrise().toString());

        TextView sunset =  v.findViewById(R.id.sunSet);
        sunset.setText(sunInfo.getSunset().toString());

        TextView dawn =  v.findViewById(R.id.sunDawn);
        dawn.setText(sunInfo.getTwilightMorning().toString());

        TextView dusk =  v.findViewById(R.id.sunDusk);
        dusk.setText(sunInfo.getTwilightEvening().toString());

        TextView riseAzimuth =  v.findViewById(R.id.riseAzimuth);
        riseAzimuth.setText(azimuthFormatter.format(sunInfo.getAzimuthRise()) + DEGREE);

        TextView setAzimuth =  v.findViewById(R.id.setAzimuth);
        setAzimuth.setText(azimuthFormatter.format(sunInfo.getAzimuthSet()) + DEGREE);

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        refreshHandler.removeCallbacks(runnable);
    }
    interface OnFragmentInteractionListener {
    }
}