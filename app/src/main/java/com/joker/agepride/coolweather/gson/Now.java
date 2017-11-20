package com.joker.agepride.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by legend on 17-11-20.
 */

public class Now {
    @SerializedName("tmp")
    public String temperature;
    @SerializedName("cond")
    public More more;
    public class More{
        @SerializedName("txt")
        public String info;
    }
}
