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
import android.util.SparseArray;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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
    SQLiteDatabase sqLiteDatabase;
    MyMemeryCache cache;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        wallpaperManager = WallpaperManager.getInstance(this);
        btn_setwallpage = findViewById(R.id.btn_setwallpage);
        progressBar = findViewById(R.id.progressBar);
        imageResult = getIntent().getParcelableExtra("result");
        mViewPager = findViewById(R.id.viewPager);
        imageResultList = new ArrayList<>();
//        imageResultList.add(imageResult);
        adapter = new MyImageAdapter(imageResultList, this);
        mViewPager.setAdapter(adapter);
        sqLiteDatabase = ((MyApplication) getApplication()).sqLiteDatabase;
        cache  = ((MyApplication) getApplication()).cache;
        progressBar.setVisibility(View.GONE);
        if(cache.get("data") ==null){
            new Thread() {
                @Override
                public void run() {
                    List<ImageResult> data = DBManager.query(sqLiteDatabase, null, 1, DBManager.count(sqLiteDatabase, null));
                    Message message = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    SparseArray<ImageResult> array = new SparseArray<>();
                    for (int i = 0; i < data.size(); i++) {
                        array.put(i, data.get(i));
                    }
                    bundle.putSparseParcelableArray("data", array);
                    message.setData(bundle);
                    message.what = load_all;
                    handler.sendMessage(message);
                }
            }.start();
        }else{
            imageResultList.addAll(cache.get("data"));
            adapter.notifyDataSetChanged();
            int item = 0;
            Log.e(tag, imageResult.toString());
            for (int i = 0; i < imageResultList.size(); i++) {
                ImageResult imageResult1 = imageResultList.get(i);
                if (imageResult1.id == DetailActivity.this.imageResult.id) {
                    item = i;
                    break;
                }
            }
            mViewPager.setCurrentItem(item, true);
        }

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
                                    Bitmap bitmap = Bitmap.createBitmap(resource, 0, 0, resource.getWidth(), resource.getHeight() - 150);
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


    final int load_all = 3;
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
                case load_all:
                    Bundle bundle = msg.getData();
                    SparseArray<ImageResult> array = bundle.getSparseParcelableArray("data");
                    for (int i = 0; i < array.size(); i++) {
                        imageResultList.add(array.get(i));
                    }
                    adapter.notifyDataSetChanged();
                    int item = 0;
                    Log.e(tag, imageResult.toString());
                    for (int i = 0; i < imageResultList.size(); i++) {
                        ImageResult imageResult1 = imageResultList.get(i);
                        if (imageResult1.id == DetailActivity.this.imageResult.id) {
                            item = i;
                            break;
                        }
                    }
                    mViewPager.setCurrentItem(item, true);

                    cache.put("data",imageResultList);
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
