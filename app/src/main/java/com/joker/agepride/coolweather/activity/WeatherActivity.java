package com.joker.agepride.coolweather.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.joker.agepride.coolweather.R;
import com.joker.agepride.coolweather.fragment.FragmentWeatherInfo;
import com.joker.agepride.coolweather.gson.Forecast;
import com.joker.agepride.coolweather.gson.Weather;
import com.joker.agepride.coolweather.util.ConstructValue;
import com.joker.agepride.coolweather.util.HttpUtil;
import com.joker.agepride.coolweather.util.Utility;

import java.io.IOException;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {
    private static final int ANIMOTION_DURATION=2000;
    private static final float SCALE_END=1.13F;
    private static final int[] SPLASH_NUMBER={
            R.drawable.splash0,
            R.drawable.splash1,
            R.drawable.splash2,
            R.drawable.splash3,
            R.drawable.splash4,
            R.drawable.splash5,
            R.drawable.splash6,
            R.drawable.splash7,
            R.drawable.splash8,
            R.drawable.splash9,
            R.drawable.splash10,
            R.drawable.splash12,
            R.drawable.splash13,
            R.drawable.splash14,
            R.drawable.splash15,
            R.drawable.splash16,
    };
    private ScrollView weather_layout;
    private TextView title_city;
    private TextView title_updata_time;
    private TextView degree_text;
    private TextView weather_info_text;
    private LinearLayout forecast_layout;
    private TextView aqi_text;
    private TextView pm25_text;
    private TextView comfort_text;
    private TextView car_wash_text;
    private TextView sport_text;
    private ImageView back_pic_img;
    private SwipeRefreshLayout swipe_refresh;
    private String weatherId;
    private Button nav_button;
    private Button share_button;
    private DrawerLayout drawer_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT>=21){
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
//            |View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_weather);
        back_pic_img= (ImageView) findViewById(R.id.back_pic_img);
        drawer_layout= (DrawerLayout) findViewById(R.id.drawer_layout);

        nav_button= (Button) findViewById(R.id.nav_button);
        share_button= (Button) findViewById(R.id.share_button);

        nav_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer_layout.openDrawer(GravityCompat.START);
//                drawer_layout.setDrawerShadow(R.layout.drawer_fragment,GravityCompat.START);
            }
        });
        share_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        swipe_refresh= (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipe_refresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        weather_layout= (ScrollView) findViewById(R.id.weather_layout);
        title_city= (TextView) findViewById(R.id.title_city);
        title_updata_time= (TextView) findViewById(R.id.title_updata_time);
        degree_text= (TextView) findViewById(R.id.degree_text);
        weather_info_text= (TextView) findViewById(R.id.weather_info_text);
        forecast_layout= (LinearLayout) findViewById(R.id.forecast_layout);
        aqi_text= (TextView) findViewById(R.id.aqi_text);
        pm25_text= (TextView) findViewById(R.id.pm25_text);
        comfort_text= (TextView) findViewById(R.id.comfort_text);
        car_wash_text= (TextView) findViewById(R.id.car_wash_text);
        sport_text= (TextView) findViewById(R.id.sport_text);
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        String bing_pic = prefs.getString("bing_pic", null);
        if (bing_pic!=null){
            Glide.with(this).load(bing_pic).into(back_pic_img);
        }else {
            loadBingPic();
        }
        String weatherString = prefs.getString("weather", null);
        if (weatherString!=null){
            Weather weather = Utility.handleWeatherResponse(weatherString);
//            Log.i(ConstructValue.TAG,"weather"+weather);
//            Log.i(ConstructValue.TAG,"weather"+weather.suggestion.sport.info);
//            Log.i(ConstructValue.TAG,"weather"+weather.basic.cityName);
            weatherId = weather.basic.weatherId;
            showWeatherInfo(weather);
        }else {
            weatherId = getIntent().getStringExtra("weather_id");
            weather_layout.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);
        }
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(weatherId);
            }
        });
    }

    /**
     * 加载必应每日一图片
     */
    private void loadBingPic() {
        String requestBingPic="http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Random random=new Random(SystemClock.elapsedRealtime());
                Glide.with(WeatherActivity.this).load(SPLASH_NUMBER[random.nextInt(SPLASH_NUMBER.length)]).into(back_pic_img);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(back_pic_img);
                    }
                });
            }
        });
    }

    /**
     * 根据天气的id请求城市天气信息
     * @param weatherId
     */
    private void requestWeather(String weatherId) {
        String weatherUrl="http://guolin.tech/api/weather?cityid="
                +weatherId+"&key=b5c004744193406591f83f1200c99368";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"获取天气失败",Toast.LENGTH_SHORT).show();
                        swipe_refresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather!=null&&"ok".equals(weather.status)){
                            SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather",responseText);
                            editor.apply();
                            showWeatherInfo(weather);
                        }else {
                            Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                        }
                        swipe_refresh.setRefreshing(false);
                    }
                });
            }
        });
        loadBingPic();
    }

    /**
     * 处理并展示Weather实体类中的数据
     * @param weather
     */
    private void showWeatherInfo(Weather weather) {

        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature + "℃";
        String weatherInfo = weather.now.more.info;
        if (ConstructValue.weatherInfoList!=null&&ConstructValue.weatherInfoList.size()==0){
            FragmentWeatherInfo info=new FragmentWeatherInfo();
            info.setLocationName(cityName);
            info.setWeatherInfo(weatherInfo);
            info.setDegree(degree);
            ConstructValue.weatherInfoList.add(info);
        }
        if (ConstructValue.weatherInfoList!=null&&ConstructValue.weatherInfoList.size()>0){
            for (FragmentWeatherInfo weatherInfo1:ConstructValue.weatherInfoList){
                if (weatherInfo1.getLocationName().equals(cityName)){
                    weatherInfo1.setDegree(degree);
                    weatherInfo1.setWeatherInfo(weatherInfo);
                }
            }
        }
        Log.i(ConstructValue.TAG,"showWeatherInfo---"+ConstructValue.weatherInfoList);
        Log.i(ConstructValue.TAG,"showWeatherInfo--size"+ConstructValue.weatherInfoList.size());
        title_city.setText(cityName);
        title_updata_time.setText(updateTime);
        degree_text.setText(degree);
        weather_info_text.setText(weatherInfo);
        forecast_layout.removeAllViews();
        for (Forecast forecast:weather.forecastList){
            View view= LayoutInflater.from(this).inflate(R.layout.forecast_item,forecast_layout,false);
            TextView date_text = (TextView) view.findViewById(R.id.date_text);
            TextView info_text = (TextView) view.findViewById(R.id.info_text);
            TextView max_text = (TextView) view.findViewById(R.id.max_text);
            TextView min_text = (TextView) view.findViewById(R.id.min_text);
            date_text.setText(forecast.date);
            info_text.setText(forecast.more.info);
            max_text.setText(forecast.temperature.max);
            min_text.setText(forecast.temperature.min);
            forecast_layout.addView(view);
        }
        if (weather.aqi!=null){
            aqi_text.setText(weather.aqi.city.aqi);
            pm25_text.setText(weather.aqi.city.pm25);
        }
        String comfort = weather.suggestion.comfort.info;
        String carWash = weather.suggestion.carWash.info;
        String sport = weather.suggestion.sport.info;
        comfort_text.setText(comfort);
        car_wash_text.setText(carWash);
        sport_text.setText(sport);
        weather_layout.setVisibility(View.VISIBLE);
    }
        private void animateImage(){
        ObjectAnimator animatorX= ObjectAnimator.ofFloat(back_pic_img,View.SCALE_X,1f,SCALE_END);
        ObjectAnimator animatorY=ObjectAnimator.ofFloat(back_pic_img,View.SCALE_Y,1f,SCALE_END);
        AnimatorSet set=new AnimatorSet();
        set.setDuration(ANIMOTION_DURATION).play(animatorX).with(animatorY);
        set.start();

    }
}
