package com.example.astroweather;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Settings extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
        public static double refreshTimeValue = 15;
        EditText city;
        EditText refresh;
        boolean isOk = false;
        ArrayAdapter<String> adapter;
        @Override
        protected void onCreate (Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_settings);
            city = findViewById(R.id.city);
            refresh = findViewById(R.id.refresh);
            StringBuilder input = new StringBuilder();
            input.setLength(0);
            input.append(refreshTimeValue);
            refresh.setText(input);
            input.setLength(0);
            SharedPreferences prefs = this.getPreferences(Context.MODE_PRIVATE);
            if(Data.numberofCities==0)
            {
                Data.numberofCities=Integer.parseInt(prefs.getString("numberofCities","0"));
                for(int i=0;i<=Data.numberofCities;i++){
                    Data.cities.add(i,prefs.getString(("miasto"+i),"Lodz"));
                }
                Data.numberofCities++;
            }
            Spinner spinner = findViewById(R.id.spinner);
            adapter= new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Data.cities);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(this);
            ConnectivityManager connectivityManager = (ConnectivityManager)getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

            if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
                Toast.makeText(getBaseContext(),"No Internet access. Data from: "+Data.name+" will be used.",Toast.LENGTH_SHORT).show();
            }
        }

        public void addToFavourites(View view)
        {
            final Switch switch1 = findViewById(R.id.switch1);
            String temp=city.getText().toString();
            if(temp.equals("")){
            }
            else {
                Data.numberofCities++;
                Data.cities.add(temp);
                Data.index = Data.numberofCities;
            }
            if(switch1.isChecked()){
                Data.units="&units=imperial";
                Data.tempUnit =" F";
                Data.windUnit =" miles/h";
            }
            else{
                Data.units="&units=metric";
                Data.tempUnit =" °C";
                Data.windUnit =" m/s";
            }
            Toast.makeText(getBaseContext(), "Saved to favourites", Toast.LENGTH_SHORT).show();
            saveToPreference();
        }

        public void deleteFromFavourites(View view)
        {
            try {
                Data.cities.remove(Data.index);
                if(Data.numberofCities>0)
                {
                    Data.numberofCities--;
                    Data.index = Data.numberofCities;
                }

                adapter.notifyDataSetChanged();
                Toast.makeText(getBaseContext(), "Deleted from favourites", Toast.LENGTH_SHORT).show();
                saveToPreference();
            }
            catch (IndexOutOfBoundsException e)
            {

            }
        }

        public void Save(View view) {
            if (refresh.getText().toString().length() > 0) {
                double ref = Double.parseDouble(refresh.getText().toString());
                if (ref <= 0) {
                    Toast.makeText(getBaseContext(), "Refresh time should be bigger than 0", Toast.LENGTH_SHORT).show();
                    isOk = false;
                } else {
                    refreshTimeValue=ref;
                    isOk = true;
                }
            }
            else{
                Toast.makeText(getBaseContext(), "Refresh time can't be empty", Toast.LENGTH_SHORT).show();
                isOk = false;
            }
            String temp=city.getText().toString();
            if(!temp.equals("")){
                Data.name=temp;
            }
            if (isOk) {
                final Switch switch1 = findViewById(R.id.switch1);
                if(switch1.isChecked()){
                    Data.units="&units=imperial";
                    Data.tempUnit =" F";
                    Data.windUnit =" miles/h";
                }
                else{
                    Data.units="&units=metric";
                    Data.tempUnit =" °C";
                    Data.windUnit =" m/s";
                }
                    startActivity(new Intent(getApplicationContext(), Menu.class));
            }
        }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Data.index= position;
        Data.name=Data.cities.get(Data.index);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void saveToPreference(){
        SharedPreferences prefs = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor myEditor = prefs.edit();

        for(int i=0;i<Data.numberofCities;i++){
            myEditor.putString(("miasto"+i),Data.cities.get(i));
        }
        myEditor.putString("numberofCities",String.valueOf(Data.numberofCities));
        myEditor.putString("index",String.valueOf(Data.index));
        myEditor.apply();
    }
}