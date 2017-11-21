package com.joker.agepride.coolweather.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.joker.agepride.coolweather.R;
import com.joker.agepride.coolweather.activity.ChooseLocationActivity;
import com.joker.agepride.coolweather.activity.WeatherActivity;
import com.joker.agepride.coolweather.util.ConstructValue;

import java.util.List;

/**
 * Created by legend on 17-11-20.
 */

public class ManagerAreaFragment extends Fragment {
    private ImageView img_edit;
    private ImageView img_add;
    private RecyclerView recyclerview;
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.drawer_fragment,container,false);
        final View view2=inflater.inflate(R.layout.choose_fragment,container,false);
        img_add= (ImageView) view.findViewById(R.id.img_add);
        img_edit= (ImageView) view.findViewById(R.id.img_edit);
        img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerview.findViewById(R.id.img_delete_info).getVisibility()==View.GONE){
                    recyclerview.findViewById(R.id.img_delete_info).setVisibility(View.VISIBLE);
                }else {
                    recyclerview.findViewById(R.id.img_delete_info).setVisibility(View.GONE);
                }
            }
        });
        img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstructValue.chooseFlag=true;
                startActivity(new Intent(getActivity(),ChooseLocationActivity.class));
            }
        });
        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
        Log.i(ConstructValue.TAG,"ConstructValue.weatherInfoList"+ConstructValue.weatherInfoList.get(0).getLocationName());
        WeatherAdapater adapater=new WeatherAdapater(ConstructValue.weatherInfoList);
        LinearLayoutManager manager=new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(manager);
        recyclerview.setAdapter(adapater);
        return view;
    }
    public class WeatherAdapater extends RecyclerView.Adapter<WeatherAdapater.ViewHolder> {
        private List<FragmentWeatherInfo> weatherInfos;

        public WeatherAdapater(List<FragmentWeatherInfo> weatherInfos) {
            this.weatherInfos = weatherInfos;
        }
         class ViewHolder extends RecyclerView.ViewHolder{
            TextView fragment_weather_city;
            TextView fragment_weather_info;
            ImageView img_delete_info;
            public ViewHolder(View view) {
                super(view);
                fragment_weather_city= (TextView) view.findViewById(R.id.fragment_weather_city);
                fragment_weather_info= (TextView) view.findViewById(R.id.fragment_weather_info);
                img_delete_info= (ImageView) view.findViewById(R.id.img_delete_info);
            }
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
            final ViewHolder holder= new ViewHolder(view);

//            int position = holder.getAdapterPosition();
//            Log.i(ConstructValue.TAG,"onCreateViewHolder"+position);
//            FragmentWeatherInfo weather = weatherInfos.get(position);
//            Log.i(ConstructValue.TAG,"position"+position);
//            holder.fragment_weather_city.setText(weather.getLocationName());
//            holder.fragment_weather_info.setText(weather.getWeatherInfo()+" "+weather.getDegree()+"℃");

            holder.img_delete_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (weatherInfos.size()>1){
                        int position = holder.getAdapterPosition();
                        weatherInfos.remove(position);
                        notifyDataSetChanged();
                    }else
                        Toast.makeText(getActivity(),"只有一条数据",Toast.LENGTH_SHORT).show();

                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            FragmentWeatherInfo weather = weatherInfos.get(position);
            Log.i(ConstructValue.TAG,"position"+position);
            holder.fragment_weather_city.setText(weather.getLocationName());
            holder.fragment_weather_info.setText(weather.getWeatherInfo()+" "+weather.getDegree());
        }
        @Override
        public int getItemCount() {
            return weatherInfos.size();
        }

    }
}
