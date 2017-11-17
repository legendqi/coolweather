package com.joker.agepride.coolweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SpashActivity extends AppCompatActivity {
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash);
    }
}
