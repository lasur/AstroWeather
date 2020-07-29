package com.example.astroweather;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.squareup.picasso.Picasso;

public class Weather extends Fragment {
    private static final String pageNumber = "page";
    private static final String title = "title";
    private final Handler refreshHandler = new Handler();
    private Runnable runnable;
    public Weather(){}
    static Weather create(int page, String title) {
        Weather fragment = new Weather();
        Bundle args = new Bundle();
        args.putInt(pageNumber, page);
        args.putString(Weather.title, title);
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
        final View v = inflater.inflate(R.layout.fragment_weather, container, false);
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
                .load("http://openweathermap.org/img/w/"+Data.image+".png")
                .into(imageView1);
        TextView city =  v.findViewById(R.id.city);
        if(Data.name!=null)
        {
            city.setText(Data.name);
        }
        TextView temperature =  v.findViewById(R.id.temperature);
        temperature.setText(Data.temp);

        TextView pressure =  v.findViewById(R.id.pressure);
        pressure.setText(Data.pressure);

        TextView description =  v.findViewById(R.id.description);
        description.setText(Data.description);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        refreshHandler.removeCallbacks(runnable);
    }
    interface OnFragmentInteractionListener {
    }
}