package com.fxa.xgmn;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import abc.abc.abc.AdManager;
import abc.abc.abc.nm.sp.SplashViewSettings;
import abc.abc.abc.nm.sp.SpotListener;
import abc.abc.abc.nm.sp.SpotManager;
import abc.abc.abc.nm.sp.SpotRequestListener;
import fxa.com.xgmn.R;

/**
 * Created by 30315 on 2018/2/6.
 */

public class SplashActivity extends AppCompatActivity {

    String tag = "SplashActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        AdManager.getInstance(this).init("b02f983d8cf2277e", "c7028cc715170563", true);
        ImageView imageView = findViewById(R.id.img);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.5f, 1f);
        alphaAnimation.setDuration(1500);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageView.startAnimation(alphaAnimation);


        ViewGroup adRoot = findViewById(R.id.ad_root);

        SpotManager.getInstance(this).requestSpot(new SpotRequestListener() {
            @Override
            public void onRequestSuccess() {
                Log.e(tag, "requestSpot onRequestSuccess");
            }

            @Override
            public void onRequestFailed(int i) {
                Log.e(tag, "requestSpot onRequestFailed " + i);

            }
        });

        SplashViewSettings splashViewSettings = new SplashViewSettings();
        splashViewSettings.setAutoJumpToTargetWhenShowFailed(true);
        splashViewSettings.setTargetClass(MainActivity.class);
        splashViewSettings.setSplashViewContainer(adRoot);

        SpotManager.getInstance(this).showSplash(this,
                splashViewSettings, new SpotListener() {
                    @Override
                    public void onShowSuccess() {
                        Log.e(tag, "showSplash onShowSuccess");
                    }

                    @Override
                    public void onShowFailed(int i) {
                        Log.e(tag, "showSplash onShowFailed");
                    }

                    @Override
                    public void onSpotClosed() {
                        Log.e(tag, "showSplash onSpotClosed");
                    }

                    @Override
                    public void onSpotClicked(boolean b) {
                        Log.e(tag, "showSplash onSpotClicked");
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 开屏展示界面的 onDestroy() 回调方法中调用
        SpotManager.getInstance(this).onDestroy();
    }
}
