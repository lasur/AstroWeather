package com.example.astroweather;

import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JsonParser extends AsyncTask {
    @Override
    protected Object doInBackground(Object[] objects){
        JSONObject obj;
        String city;
        if(Data.name != null){
            city= Data.name;
        }
        else{
            city = Data.cities.get(Data.index);
        }
        String units = Data.units;
        String apiCode = "&appid=88630e2a7c4466162cb4a70d2ea4a966";
        String url="https://api.openweathermap.org/data/2.5/weather?q="+city +apiCode
                + units;
        String url2="https://api.openweathermap.org/data/2.5/forecast?q="+city+apiCode+units;
        try {
            obj = readJsonFromUrl(url);
            System.out.println(url);
            if(obj.getString("cod").equals("404")){
                System.out.println("404");
            }
            else {
                Data.temp = obj.getJSONObject("main").getString("temp")+Data.tempUnit;
                Data.pressure = obj.getJSONObject("main").getString("pressure") +" hPa";
                Data.name = obj.getString("name");
                Data.speed = obj.getJSONObject("wind").getString("speed") + Data.windUnit;
                Data.direction = obj.getJSONObject("wind").getString("deg");
                Data.humidity = obj.getJSONObject("main").getString("humidity")+ " %";

                JSONArray arr = obj.getJSONArray("weather");
                Data.description = arr.getJSONObject(0).getString("description");
                Data.image= arr.getJSONObject(0).getString("icon");

                Data.longitudeValue = Double.parseDouble(obj.getJSONObject("coord").getString("lon"));
                Data.latitudeValue = Double.parseDouble(obj.getJSONObject("coord").getString("lat"));
                obj = readJsonFromUrl(url2);
                arr = obj.getJSONArray("list");
                Date date = new Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
                String currentdate = simpleDateFormat.format(date);
                int index = 0;
                for (int i = 0; i < arr.length(); i++) {
                    if (arr.getJSONObject(i).getString("dt_txt").split(" ")[1].equals("12:00:00")) {
                        if (arr.getJSONObject(i).getString("dt_txt").split(" ")[0].equals(currentdate)) {
                            index = i + 8;
                            break;
                        } else {
                            index = i;
                            break;
                        }
                    }
                }
                Data.nextDay = arr.getJSONObject(index).getString("dt_txt").split(" ")[0];
                Data.tempday1 = arr.getJSONObject(index).getJSONObject("main").getString("temp")+Data.tempUnit;
                Data.pressureday1 = arr.getJSONObject(index).getJSONObject("main").getString("pressure").substring(0,4)+"hPa";
                Data.image2= arr.getJSONObject(index).getJSONArray("weather").getJSONObject(0).getString("icon");

                Data.secondNextDay = arr.getJSONObject(index + 8).getString("dt_txt").split(" ")[0];
                Data.tempday2 = arr.getJSONObject(index + 8).getJSONObject("main").getString("temp")+Data.tempUnit;
                Data.pressureday2 = arr.getJSONObject(index+8).getJSONObject("main").getString("pressure").substring(0,4)+"hPa";
                Data.image3= arr.getJSONObject(index+8).getJSONArray("weather").getJSONObject(0).getString("icon");

                Data.thirdNextDay = arr.getJSONObject(index + 16).getString("dt_txt").split(" ")[0];
                Data.tempday3 = arr.getJSONObject(index + 16).getJSONObject("main").getString("temp")+Data.tempUnit;
                Data.pressureday3 = arr.getJSONObject(index+16).getJSONObject("main").getString("pressure").substring(0,4)+"hPa";
                Data.image4= arr.getJSONObject(index+16).getJSONArray("weather").getJSONObject(0).getString("icon");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            Data.description="City not found";
        }
        return null;
    }

    private static String readAll(Reader reader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        int charNumber;
        while ((charNumber = reader.read()) != -1) {
            stringBuilder.append((char) charNumber);
        }
        return stringBuilder.toString();
    }

    private static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream inputStream = new URL(url).openStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        String jsonText = readAll(bufferedReader);
        JSONObject json = new JSONObject(jsonText);
        inputStream.close();
        return json;
    }}