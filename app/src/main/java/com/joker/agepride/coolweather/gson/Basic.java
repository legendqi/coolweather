package com.joker.agepride.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by legend on 17-11-20.
 */

public class Basic {
    @SerializedName("city")
    public String cityName;
    @SerializedName("id")
    public String weatherId;
    public Update update;
    public class Update{
        @SerializedName("loc")
        public String updateTime;
    }
}
