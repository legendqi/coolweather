package com.joker.agepride.coolweather.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.joker.agepride.coolweather.R;
import com.joker.agepride.coolweather.fragment.ChooseAreaFragment;

/**
 * Created by legend on 17-11-19.
 */

public class ChooseLocationActivity extends BaseActivity {
    private LocationClient locationClient;
    private MyLocationListener listener;
    private String province;
    private String city;
    private String country;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("legend","Activity onCreate");
    }

    private void initLocation(){
        LocationClientOption option=new LocationClientOption();
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd0911");
        option.setScanSpan(1000);
        locationClient.setLocOption(option);
    }
    class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
             province = bdLocation.getProvince();
             city = bdLocation.getCity();
             country = bdLocation.getCountry();
        }
    }
    @Override
    protected int setLayout() {
        return R.layout.choose_fragment;
    }

    @Override
    protected void initView() {
        setTranslucent(ChooseLocationActivity.this);
    }
}
