package com.joker.agepride.coolweather.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.joker.agepride.coolweather.R;
import com.joker.agepride.coolweather.activity.ChooseLocationActivity;
import com.joker.agepride.coolweather.activity.WeatherActivity;
import com.joker.agepride.coolweather.db.City;
import com.joker.agepride.coolweather.db.Country;
import com.joker.agepride.coolweather.db.Province;
import com.joker.agepride.coolweather.util.ConstructValue;
import com.joker.agepride.coolweather.util.HttpUtil;
import com.joker.agepride.coolweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by legend on 17-11-18.
 */

public class ChooseAreaFragment extends Fragment {
    public static final int LEVEL_PROVINCE=0;
    public static final int LEVEL_CITY=1;
    public static final int LEVEL_COUNTY=2;
    private ProgressDialog progressDialog;
    private TextView title_text;
    private Button back_button;
    private ListView list_view;
    private ArrayAdapter<String> adapter;
    private List<String> dataList=new ArrayList<>();
    private List<Province> provinceList;
    private List<City> cityList;
    private List<Country> countryList;
    private Province selectedProvince;
    private City selectedCity;
    private int currentLevel;
    private TextView tv_location;
    private SharedPreferences sp;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.choose_area,container,false);
        title_text= (TextView) view.findViewById(R.id.title_text);
//        tv_location= (TextView) view.findViewById(R.id.tv_location);
        back_button= (Button) view.findViewById(R.id.back_button);
        list_view= (ListView) view.findViewById(R.id.list_view);
        adapter=new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,dataList);
        list_view.setAdapter(adapter);
        Log.i("legend","onCreateView");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sp= PreferenceManager.getDefaultSharedPreferences(getActivity());
        final String weatherInfo = sp.getString("weatherInfo", null);
        Log.i("legend","onActivityCreated");
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("legend","onItemClick");
                if (currentLevel==LEVEL_PROVINCE){
                    Log.i("legend","currentLevel   LEVEL_PROVINCE");
                    selectedProvince=provinceList.get(position);
                    Log.i(ConstructValue.TAG,"selectedProvince"+selectedProvince);
                    queryCities();
                }else if (currentLevel==LEVEL_CITY){
                    Log.i("legend","currentLevel   LEVEL_CITY");
                    selectedCity=cityList.get(position);
                    queryCounties();
                }else if (currentLevel==LEVEL_COUNTY){
                    String weatherId = countryList.get(position).getWeatherId();
                    Intent intent=new Intent(getActivity(), WeatherActivity.class);
                    if (weatherInfo==null){
                        SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
                        editor.putString("weatherInfo",weatherId);
                        editor.apply();
                    }
                    if (getActivity() instanceof ChooseLocationActivity){
                        intent.putExtra("weather_id",weatherId);
                        startActivity(intent);
                        getActivity().finish();
                    }else if (getActivity() instanceof WeatherActivity){
                        WeatherActivity activity= (WeatherActivity) getActivity();
                        activity.drawer_layout.closeDrawers();
                        activity.swipe_refresh.setRefreshing(true);
                        activity.requestWeather(weatherId);
                    }


                }
            }
        });
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLevel==LEVEL_COUNTY)
                    queryCities();
                else if (currentLevel==LEVEL_CITY)
                    queryProvince();
            }
        });
        queryProvince();
    }
    private void queryProvince(){
        Log.i("legend","queryProvince");
        title_text.setText("中国");
        back_button.setVisibility(View.GONE);
        provinceList= DataSupport.findAll(Province.class);
        if (provinceList.size()>0){
            dataList.clear();
            for (Province province:provinceList)
                dataList.add(province.getProvinceName());
            adapter.notifyDataSetChanged();
            list_view.setSelection(0);
            currentLevel=LEVEL_PROVINCE;
        }else {
            String address="http://guolin.tech/api/china";
            queryFromServer(address,"province");
        }
    }

    private void queryFromServer(String address, final String type) {
        Log.i("legend","queryFromServer");
        showProgressDialog();
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(),"加载失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText=response.body().string();
                Log.i("legend","onResponse");
                boolean result=false;
                if ("province".equals(type)){
                    result= Utility.handleProvinceResponse(responseText);
                }else if ("city".equals(type)){
                    result=Utility.handleCityResponse(responseText,selectedProvince.getId());
                }else if ("county".equals(type)){
                    result=Utility.handleCountryResponse(responseText,selectedCity.getId());
                }
                if (result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type))
                                queryProvince();
                            else if ("city".equals(type))
                                queryCities();
                            else if ("county".equals(type))
                                queryCounties();
                        }
                    });
                }
            }
        });
    }

    private void showProgressDialog() {
        if (progressDialog==null){
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载。。。");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
    private void closeProgressDialog() {
        if (progressDialog!=null)
            progressDialog.dismiss();
    }

    private void queryCounties(){
        title_text.setText(selectedCity.getCityName());
        back_button.setVisibility(View.VISIBLE);
        countryList=DataSupport.where("cityId=?",String.valueOf(selectedCity.getId())).find(Country.class);
        if (countryList.size()>0){
            dataList.clear();
            for (Country country:countryList){
                dataList.add(country.getCountryName());
            }
            adapter.notifyDataSetChanged();
            list_view.setSelection(0);
            currentLevel=LEVEL_COUNTY;
        }
        else {
            int provinceCode = selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            String address="http://guolin.tech/api/china/"+provinceCode+"/"+cityCode;
            queryFromServer(address,"county");
        }
    }
    private void queryCities(){
        Log.i(ConstructValue.TAG,"queryCities");
        title_text.setText(selectedProvince.getProvinceName());
        back_button.setVisibility(View.VISIBLE);
        Log.i(ConstructValue.TAG,"DataSupport.where before");
        Log.i(ConstructValue.TAG,selectedProvince.getId()+"");
        cityList=DataSupport.where("provincdId = ?",String.valueOf(selectedProvince.getId())).find(City.class);
        if (cityList.size()>0){
            Log.i(ConstructValue.TAG,"cityList.size()>0");
            dataList.clear();
            for (City city:cityList){
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            list_view.setSelection(0);
            currentLevel=LEVEL_CITY;
        }
        else {
            int provinceCode = selectedProvince.getProvinceCode();
            String address="http://guolin.tech/api/china/"+provinceCode;
            queryFromServer(address,"city");
        }
    }
}
