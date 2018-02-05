package com.fxa.xgmn;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ImageViewTarget;

import java.io.IOException;

import fxa.com.xgmn.R;

/**
 * Created by 30315 on 2018/2/2.
 */

public class DetailActivity extends AppCompatActivity {


    PhotoView photoView;

    View btn_setwallpage;


    ProgressBar progressBar;

    Bitmap resource;

    WallpaperManager wallpaperManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        wallpaperManager = WallpaperManager.getInstance(this);
        btn_setwallpage = findViewById(R.id.btn_setwallpage);
        progressBar = findViewById(R.id.progressBar);
        btn_setwallpage.setEnabled(false);
        photoView = findViewById(R.id.img);
        photoView.enable();
        String url = getIntent().getStringExtra("url");
        Glide.with(this)
                .asBitmap()
                .load(url).into(new ImageViewTarget<Bitmap>(photoView) {
            @Override
            protected void setResource(@Nullable Bitmap resource) {
                if (resource != null) {
                    DetailActivity.this.resource = Bitmap.createBitmap(resource, 0, 0, resource.getWidth(), resource.getHeight() - 100);
                    photoView.setImageBitmap(DetailActivity.this.resource);
                    btn_setwallpage.setEnabled(true);
                }
            }
        });

        btn_setwallpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            wallpaperManager.setBitmap(DetailActivity.this.resource);
                            handler.sendEmptyMessage(set_success);
                        } catch (IOException e) {
                            e.printStackTrace();
                            handler.sendEmptyMessage(set_faild);
                        }
                    }
                }.start();

            }
        });
    }

    final int set_success = 1;
    final int set_faild = 2;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
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
