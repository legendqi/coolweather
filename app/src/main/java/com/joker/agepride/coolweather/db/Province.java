package com.joker.agepride.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by legend on 17-11-17.
 */

public class Province extends DataSupport {
    private int id;
    //省名
    private String provinceName;
    //省代号
    private int provinceCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }
}
