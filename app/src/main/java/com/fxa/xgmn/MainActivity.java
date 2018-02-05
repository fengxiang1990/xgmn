package com.fxa.xgmn;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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

    public static String TYPE_ALL = "all";
    public static String TYPE_XG = "xgmn";
    public static String TYPE_QC = "qcmn";
    public static String TYPE_NY = "nymn";
    public static String TYPE_SW = "swmn";
    public static String TYPE_BJN = "bjnmn";
    public static String TYPE_HG = "hgmn";
    public static String TYPE_WG = "wgmv";


    Toolbar toolbar;

    TabLayout tabLayout;

    ViewPager viewPager;
    List<String> titles;


    SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        sqLiteDatabase = DBManager.initDB(this);

        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        titles = new ArrayList();
        titles.add("所有");
        titles.add("性感");
        titles.add("清纯");
        titles.add("内衣");
        titles.add("丝袜");
        titles.add("比基尼");
        titles.add("韩国");
        titles.add("外国");


        ImageFragment all = (ImageFragment) ImageFragment.instantiate(this, ImageFragment.class.getName());
        ImageFragment xg = (ImageFragment) ImageFragment.instantiate(this, ImageFragment.class.getName());
        ImageFragment qc = (ImageFragment) ImageFragment.instantiate(this, ImageFragment.class.getName());
        ImageFragment ny = (ImageFragment) ImageFragment.instantiate(this, ImageFragment.class.getName());
        ImageFragment sw = (ImageFragment) ImageFragment.instantiate(this, ImageFragment.class.getName());
        ImageFragment bjn = (ImageFragment) ImageFragment.instantiate(this, ImageFragment.class.getName());
        ImageFragment hg = (ImageFragment) ImageFragment.instantiate(this, ImageFragment.class.getName());
        ImageFragment wg = (ImageFragment) ImageFragment.instantiate(this, ImageFragment.class.getName());


        Bundle bundle = new Bundle();
        bundle.putString("type", TYPE_ALL);
        all.setArguments(bundle);

        bundle = new Bundle();
        bundle.putString("type", TYPE_XG);
        xg.setArguments(bundle);

        bundle = new Bundle();
        bundle.putString("type", TYPE_QC);
        qc.setArguments(bundle);

        bundle = new Bundle();
        bundle.putString("type", TYPE_NY);
        ny.setArguments(bundle);

        bundle = new Bundle();
        bundle.putString("type", TYPE_SW);
        sw.setArguments(bundle);

        bundle = new Bundle();
        bundle.putString("type", TYPE_BJN);
        bjn.setArguments(bundle);

        bundle = new Bundle();
        bundle.putString("type", TYPE_HG);
        hg.setArguments(bundle);

        bundle = new Bundle();
        bundle.putString("type", TYPE_WG);
        wg.setArguments(bundle);


        List<ImageFragment> fragments = new ArrayList<>();
        fragments.add(all);
        fragments.add(xg);
        fragments.add(qc);
        fragments.add(ny);
        fragments.add(sw);
        fragments.add(bjn);
        fragments.add(hg);
        fragments.add(wg);

        tabLayout.addTab(tabLayout.newTab().setText(titles.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(1)));
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(2)));
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(3)));
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(4)));
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(5)));
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(6)));
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(7)));
        viewPager.setAdapter(new ImageFragmentPager(getSupportFragmentManager(), fragments));

        tabLayout.setupWithViewPager(viewPager);
    }


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
            return titles.get(position);
        }
    }
}
