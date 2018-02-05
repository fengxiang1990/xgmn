package com.fxa.xgmn;

import android.app.WallpaperManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fxa.com.xgmn.R;

/**
 * Created by 30315 on 2018/2/2.
 */

public class DetailActivity extends AppCompatActivity {

    String tag = "DetailActivity";

    View btn_setwallpage;

    ProgressBar progressBar;

    WallpaperManager wallpaperManager;

    PhotoViewPager mViewPager;

    ImageResult imageResult;

    MyImageAdapter adapter;

    List<ImageResult> imageResultList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        wallpaperManager = WallpaperManager.getInstance(this);
        btn_setwallpage = findViewById(R.id.btn_setwallpage);
        progressBar = findViewById(R.id.progressBar);
        imageResult = (ImageResult) getIntent().getSerializableExtra("result");
        mViewPager = findViewById(R.id.viewPager);
        imageResultList = new ArrayList<>();
        adapter = new MyImageAdapter(imageResultList, this);
        mViewPager.setAdapter(adapter);
        SQLiteDatabase sqLiteDatabase = DBManager.initDB(this);
        imageResultList.addAll(DBManager.query(sqLiteDatabase, null, 1, DBManager.count(sqLiteDatabase, null)));
        adapter.notifyDataSetChanged();

        int item = 0;
        Log.e(tag, imageResult.toString());
        for (int i = 0; i < imageResultList.size(); i++) {
            ImageResult imageResult1 = imageResultList.get(i);
            if (imageResult1.id == imageResult.id) {
                item = i;
                break;
            }
        }
        mViewPager.setCurrentItem(item);

        btn_setwallpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                Glide.with(DetailActivity.this)
                        .asBitmap()
                        .apply(RequestOptions.placeholderOf(R.mipmap.loading_icon))
                        .load(imageResultList.get(mViewPager.getCurrentItem()).url).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull final Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        if (resource != null) {
                            new Thread() {
                                @Override
                                public void run() {
                                    Bitmap bitmap = Bitmap.createBitmap(resource, 0, 0, resource.getWidth(), resource.getHeight() - 100);
                                    try {
                                        wallpaperManager.setBitmap(bitmap);
                                        handler.sendEmptyMessage(set_success);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        handler.sendEmptyMessage(set_faild);
                                    }
                                }
                            }.start();
                        }
                    }
                });



            }
        });

    }


    final int set_success = 1;
    final int set_faild = 2;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case set_success:
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(DetailActivity.this, "设置壁纸成功", Toast.LENGTH_SHORT).show();
                    break;
                case set_faild:
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(DetailActivity.this, "设置壁纸失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
