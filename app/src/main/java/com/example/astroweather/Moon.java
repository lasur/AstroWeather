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
import java.text.NumberFormat;
import java.util.Calendar;

public class Moon extends Fragment {
    private static final String pageNumber = "page";
    private static final String title = "title";
    private final Handler refreshHandler = new Handler();
    private Runnable runnable;

    public Moon() {}

    static Moon create(int page, String title) {
        Moon fragment = new Moon();
        Bundle args = new Bundle();
        args.putInt(pageNumber, page);
        args.putString(Moon.title, title);
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
        final View v = inflater.inflate(R.layout.fragment_moon, container, false);
        update(v);

        final double seconds = Settings.refreshTimeValue*60;
            runnable = new Runnable() {
                @Override
                public void run() {
                        update(v);
                        refreshHandler.postDelayed(this, (long) (seconds*1000));
                }
            };
            refreshHandler.postDelayed(runnable, (long) (seconds*1000));

        return v;
    }
    private void update(View v){
        AstroCalculator.Location astroLocation = new AstroCalculator.Location(
                Data.latitudeValue,
                Data.longitudeValue
        );
        NumberFormat formatter = new DecimalFormat("#0.00");

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
        AstroCalculator.MoonInfo moonInfo = astroCalculator.getMoonInfo();

        TextView moonrise =  v.findViewById(R.id.moonRise);
        moonrise.setText(moonInfo.getMoonrise().toString());

        TextView moonset =  v.findViewById(R.id.moonSet);
        moonset.setText(moonInfo.getMoonset().toString());

        TextView moonfull =  v.findViewById(R.id.fullMoon);
        moonfull.setText(moonInfo.getNextFullMoon().toString());

        TextView newmoon =  v.findViewById(R.id.newMoon);
        newmoon.setText(moonInfo.getNextNewMoon().toString());

        TextView lunar_phase = v.findViewById(R.id.phase);
        String lunarPhasePercent =formatter.format(moonInfo.getIllumination() * 100);
        lunar_phase.setText(lunarPhasePercent + " %");

        TextView day_of_lunar_month =v.findViewById(R.id.lunalDay);
        formatter = new DecimalFormat("#0");
        day_of_lunar_month.setText(formatter.format(Math.abs(moonInfo.getAge() % 29.5306)));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        refreshHandler.removeCallbacks(runnable);
    }
    interface OnFragmentInteractionListener {
    }
}