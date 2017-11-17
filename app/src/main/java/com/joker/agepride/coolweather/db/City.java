package com.joker.agepride.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by legend on 17-11-17.
 */

public class City extends DataSupport {
    private int id;
    //城市名字
    private String cityName;
    //城市代号
    private int cityCode;
    //所属省的id
    private int provincdId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int getProvincdId() {
        return provincdId;
    }

    public void setProvincdId(int provincdId) {
        this.provincdId = provincdId;
    }
}
