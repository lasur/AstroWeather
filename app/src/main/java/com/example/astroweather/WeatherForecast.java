package com.example.astroweather;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import androidx.fragment.app.Fragment;

public class WeatherForecast extends Fragment {
    private static final String pageNumber = "page";
    private static final String title = "title";
    private final Handler refreshHandler = new Handler();
    private Runnable runnable;
    public WeatherForecast(){}
    static WeatherForecast create(int page, String title) {
        WeatherForecast fragment = new WeatherForecast();
        Bundle args = new Bundle();
        args.putInt(pageNumber, page);
        args.putString(WeatherForecast.title, title);
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
        final View v = inflater.inflate(R.layout.fragment_weather_forecast, container, false);
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
        ImageView imageView1 =  v.findViewById(R.id.imageView);
        Picasso.get()
                .load("http://openweathermap.org/img/w/"+Data.image2+".png")
                .into(imageView1);
        TextView date =  v.findViewById(R.id.date);
        date.setText(Data.nextDay);

        TextView temperature =  v.findViewById(R.id.temperature2);
        temperature.setText(Data.tempday1);

        TextView pressure =  v.findViewById(R.id.pressure2);
        pressure.setText(Data.pressureday1);

        ImageView imageView2 =  v.findViewById(R.id.imageView2);
        Picasso.get()
                .load("http://openweathermap.org/img/w/"+Data.image3+".png")
                .into(imageView2);

        TextView date2 =  v.findViewById(R.id.date2);
        date2.setText(Data.secondNextDay);

        TextView temperature2 =  v.findViewById(R.id.temperature3);
        temperature2.setText(Data.tempday2);

        TextView pressure2 =  v.findViewById(R.id.pressure3);
        pressure2.setText(Data.pressureday2);

        ImageView imageView3 =  v.findViewById(R.id.imageView3);
        Picasso.get()
                .load("http://openweathermap.org/img/w/"+Data.image4+".png")
                .into(imageView3);

        TextView date3 =  v.findViewById(R.id.date3);
        date3.setText(Data.thirdNextDay);

        TextView temperature3 =  v.findViewById(R.id.temperature4);
        temperature3.setText(Data.tempday3);

        TextView pressure3 =  v.findViewById(R.id.pressure4);
        pressure3.setText(Data.pressureday3);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        refreshHandler.removeCallbacks(runnable);
    }
    interface OnFragmentInteractionListener {
    }
}