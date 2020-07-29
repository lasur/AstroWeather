package com.example.astroweather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.tabs.TabLayout;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Astro extends AppCompatActivity
        implements Moon.OnFragmentInteractionListener, Sun.OnFragmentInteractionListener,Weather.OnFragmentInteractionListener,WeatherDetails.OnFragmentInteractionListener{

    FragmentPagerAdapter fragmentPagerAdapter;
    boolean running=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        running=true;
        currentTimeInit();

        SharedPreferences prefs = this.getPreferences(Context.MODE_PRIVATE);
        if(Data.numberofCities==0)
        {
            Data.numberofCities=Integer.parseInt(prefs.getString("numberofCities","0"));
            if(Data.numberofCities==0)
            {
                Data.cities.add(0,prefs.getString(("miasto"+0),"Lodz"));
            }
            else
            {
                for(int i=0;i<=Data.numberofCities;i++){
                    Data.cities.add(i,prefs.getString(("miasto"+i),"Lodz"));
                }
                Data.index=Integer.parseInt(prefs.getString("index","0"));
            }
        }
        ConnectivityManager connectivityManager = (ConnectivityManager)getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if(activeNetworkInfo != null && activeNetworkInfo.isConnected())
        {
            new JsonParser().execute("");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            /*if(Data.description.equals("City not found"))
            {
                Toast.makeText(getBaseContext(),Data.description,Toast.LENGTH_SHORT).show();
            }*/

            saveToPreference();

            setContentView(R.layout.activity_astro);
        }
        else{
            Toast.makeText(getBaseContext(),"No Internet access. Data may be inaccurate.",Toast.LENGTH_SHORT).show();
            loadPreferences();
        }
        if (!isTablet()) {
            setContentView(R.layout.activity_astro);
            ViewPager viewPager = findViewById(R.id.viewPager);
            fragmentPagerAdapter = new FragmentPageAdapter(getSupportFragmentManager());
            viewPager.setAdapter(fragmentPagerAdapter);

            TabLayout tabLayout = findViewById(R.id.TabLayout);
            tabLayout.setupWithViewPager(viewPager);
        }

        TextView coordinates = findViewById(R.id.coordinates);
        coordinates.setText(Data.latitudeValue+", "+Data.longitudeValue);
    }
    boolean isTablet(){
        return (getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }
    private void currentTimeInit() {
        new Thread() {
            @Override
            public void run() {
                try {
                    while (running) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView time = findViewById(R.id.time);
                                time.setText(formatClock("HH:mm:ss"));
                            }
                        });
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    System.out.println("Error: "+e);
                }
            }
        }.start();
    }
    private String formatClock(String pattern){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.US);
        Date now = new Date();
        calendar.setTime(now);
        calendar.add(Calendar.HOUR,0);
        return format.format(calendar.getTime());
    }
    public void loadPreferences()
    {
        SharedPreferences myPreferences = this.getPreferences(Context.MODE_PRIVATE);
        Data.name=myPreferences.getString("name"," ");
        Data.latitudeValue=Double.parseDouble(myPreferences.getString("lat"," "));
        Data.longitudeValue=Double.parseDouble(myPreferences.getString("lon"," "));
        Data.temp=myPreferences.getString("temp"," ");
        Data.pressure=myPreferences.getString("pressure"," ");
        Data.description=myPreferences.getString("description"," ");
        Data.image=myPreferences.getString("image"," ");

        Data.speed=myPreferences.getString("speed"," ");
        Data.direction =myPreferences.getString("kierunek"," ");
        Data.humidity=myPreferences.getString("humidity"," ");

        Data.nextDay =myPreferences.getString("nextDay"," ");
        Data.tempday1=myPreferences.getString("tempday1"," ");
        Data.pressureday1=myPreferences.getString("pressureday1"," ");

        Data.secondNextDay =myPreferences.getString("secondNextDay"," ");
        Data.tempday2=myPreferences.getString("tempday2"," ");
        Data.pressureday2=myPreferences.getString("pressureday2"," ");

        Data.thirdNextDay =myPreferences.getString("thirdNextDay"," ");
        Data.tempday3=myPreferences.getString("tempday3"," ");
        Data.pressureday3=myPreferences.getString("pressureday3"," ");
    }
    public void saveToPreference(){
        SharedPreferences myPreferences = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor myEditor = myPreferences.edit();

        for(int i=0;i<Data.numberofCities;i++){
            myEditor.putString(("miasto"+i),Data.cities.get(i));
        }
        myEditor.putString("numberofCities",String.valueOf(Data.numberofCities));
        myEditor.putString("name",Data.name);
        myEditor.putString("lat", String.valueOf(Data.latitudeValue));
        myEditor.putString("lon", String.valueOf(Data.longitudeValue));
        myEditor.putString("temp",Data.temp);
        myEditor.putString("pressure",Data.pressure);
        myEditor.putString("description",Data.description);
        myEditor.putString("image",Data.image);

        myEditor.putString("speed",Data.speed);
        myEditor.putString("kierunek",Data.direction);
        myEditor.putString("humidity",Data.humidity);

        myEditor.putString("nextDay",Data.nextDay);
        myEditor.putString("tempday1",Data.tempday1);
        myEditor.putString("pressureday1",Data.pressureday1);

        myEditor.putString("secondNextDay",Data.secondNextDay);
        myEditor.putString("tempday2",Data.tempday2);
        myEditor.putString("pressureday2",Data.pressureday2);

        myEditor.putString("thirdNextDay",Data.thirdNextDay);
        myEditor.putString("tempday3",Data.tempday3);
        myEditor.putString("pressureday3",Data.pressureday3);
        myEditor.apply();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        getDelegate().onDestroy();
        running=false;
    }
}