package com.joker.agepride.coolweather.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by legend on 17-11-17.
 */

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState, persistentState);
    }
    protected abstract void initView();
    protected void hideActionBar(boolean flag){
        if (flag){
            ActionBar bar = getSupportActionBar();
            bar.hide();
        }
    }
    protected void setStatusBarFullTransparent(){
        Window window = getWindow();
        if (Build.VERSION.SDK_INT>21){
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN|
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //虚拟键盘也透明
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        }
    /**
     * 半透明状态栏
     */
    protected void setHalfTransparent() {

        if (Build.VERSION.SDK_INT >= 21) {//21表示5.0
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //虚拟键盘也透明
            // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }
    /**
     * 如果需要内容紧贴着StatusBar
     * 应该在对应的xml布局文件中，设置根布局fitsSystemWindows=true。  
     */
    private View contentViewGroup;

//    protected void setFitSystemWindow(boolean fitSystemWindow) {
//        if (contentViewGroup == null) {
//            contentViewGroup = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
//        }
//        contentViewGroup.setFitsSystemWindows(fitSystemWindow);
//    }
//    /**
//     * 为了兼容4.4的抽屉布局->透明状态栏
//     */
//    protected void setDrawerLayoutFitSystemWindow() {
//        if (Build.VERSION.SDK_INT == 19) {//19表示4.4
//            int statusBarHeight = getStatusHeight(this);
//            if (contentViewGroup == null) {
//                contentViewGroup = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
//            }
//            if (contentViewGroup instanceof DrawerLayout) {
//                DrawerLayout drawerLayout = (DrawerLayout) contentViewGroup;
//                drawerLayout.setClipToPadding(true);
//                drawerLayout.setFitsSystemWindows(false);
//                for (int i = 0; i < drawerLayout.getChildCount(); i++) {
//                    View child = drawerLayout.getChildAt(i);
//                    child.setFitsSystemWindows(false);
//                    child.setPadding(0, statusBarHeight, 0, 0);
//                }
//
//            }
//        }
//    }

}
