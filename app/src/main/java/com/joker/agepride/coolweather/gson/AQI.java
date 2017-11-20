package com.joker.agepride.coolweather.gson;

/**
 * Created by legend on 17-11-20.
 */

public class AQI {
    public AQICity city;
    public class AQICity{
        public String aqi;
        public String pm25;
    }
}
