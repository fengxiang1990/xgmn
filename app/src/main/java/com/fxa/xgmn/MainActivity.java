package com.fxa.xgmn;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import fxa.com.xgmn.R;

public class MainActivity extends AppCompatActivity {

    public static String tag = "MainActivity";


    public static String[] titles = new String[]{
            "所有", "风光", "植物", "动物", "美女", "唯美", "创意", "节庆", "影视", "明星", "动漫", "卡通"
    };

    Toolbar toolbar;

    TabLayout tabLayout;

    ViewPager viewPager;


    SQLiteDatabase sqLiteDatabase;
    MyMemeryCache cache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        sqLiteDatabase = ((MyApplication) getApplication()).sqLiteDatabase;
        cache = ((MyApplication) getApplication()).cache;
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        List<ImageFragment> fragments = new ArrayList<>();

        for (String type : titles) {
            ImageFragment imageFragment = (ImageFragment) ImageFragment.instantiate(this, ImageFragment.class.getName());
            Bundle bundle = new Bundle();
            bundle.putString("type", type.equals(titles[0]) ? null : type);
            imageFragment.setArguments(bundle);
            fragments.add(imageFragment);
        }

        for (String title : titles) {
            tabLayout.addTab(tabLayout.newTab().setText(title));
        }
        viewPager.setAdapter(new ImageFragmentPager(getSupportFragmentManager(), fragments));

        tabLayout.setupWithViewPager(viewPager);
        if (cache.get("data") == null) {
            new Thread() {
                @Override
                public void run() {
                    ArrayList<ImageResult> data = DBManager.query(sqLiteDatabase, null, 1, DBManager.count(sqLiteDatabase, null));
                    Message message = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("data", data);
                    message.setData(bundle);
                    message.what = load_all;
                    handler.sendMessage(message);
                }
            }.start();
        }
    }

    final int load_all = 3;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case load_all:
                    Bundle bundle = msg.getData();
                    List<ImageResult> data = bundle.getParcelableArrayList("data");
                    cache.put("data", data);
                    break;
            }
        }
    };

    class ImageFragmentPager extends FragmentPagerAdapter {

        public List<ImageFragment> fragments;

        public ImageFragmentPager(FragmentManager fm, List<ImageFragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
