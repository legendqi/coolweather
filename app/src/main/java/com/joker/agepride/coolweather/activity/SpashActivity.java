package com.joker.agepride.coolweather.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationSet;
import android.widget.ImageView;

import com.joker.agepride.coolweather.R;

import java.util.Random;

public class SpashActivity extends Activity {
    private static final String YOUR_PREF_FILE_NAME ="isFirst" ;
    private static final String TAG="SpashActivity";
    //    private static final int ANIMOTION_DURATION=2000;
//    private static final float SCALE_END=1.13F;
//    private static final int[] SPLASH_NUMBER={
//            R.drawable.splash0,
//            R.drawable.splash1,
//            R.drawable.splash2,
//            R.drawable.splash3,
//            R.drawable.splash4,
//            R.drawable.splash5,
//            R.drawable.splash6,
//            R.drawable.splash7,
//            R.drawable.splash8,
//            R.drawable.splash9,
//            R.drawable.splash10,
//            R.drawable.splash12,
//            R.drawable.splash13,
//            R.drawable.splash14,
//            R.drawable.splash15,
//            R.drawable.splash16,
//    };
//    ImageView splash_pic;
//    private static final float SCALE_END = 1.13F;
//    private static final int ANIMATION_DURATION = 2000;
    Handler handler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        splash_pic= (ImageView) findViewById(R.id.splash_pic);
//        Random random=new Random(SystemClock.elapsedRealtime());
//        splash_pic.setImageResource(SPLASH_NUMBER[random.nextInt(SPLASH_NUMBER.length)]);
//        animateImage();
//        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        SharedPreferences setting = getSharedPreferences(YOUR_PREF_FILE_NAME, 0);
        Boolean user_first = setting.getBoolean("FIRST",true);
//        if (user_first) {
            setting.edit().putBoolean("FIRST", false).commit();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.i("legend","enterChooseAcitivuty");
                    enterChooseLocationActivity();
                }
            },2000);
//        }else {
//            enterHomeActivity();
//        }
    }

    private void enterChooseLocationActivity() {
        startActivity(new Intent(SpashActivity.this,ChooseLocationActivity.class));
        finish();
    }
    private void enterHomeActivity(){

    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (grantResults!=null){
//            if (grantResults[])
//        }

    }

//    private void animateImage(){
//        ObjectAnimator animatorX=ObjectAnimator.ofFloat(splash_pic,View.SCALE_X,1f,SCALE_END);
//        ObjectAnimator animatorY=ObjectAnimator.ofFloat(splash_pic,View.SCALE_Y,1f,SCALE_END);
//        AnimatorSet set=new AnimatorSet();
//        set.setDuration(ANIMATION_DURATION).play(animatorX).with(animatorY);
//        set.start();
//        set.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                startActivity(new Intent(SpashActivity.this,HomeActivity.class));
//                finish();
//            }
//        });
//    }

}
