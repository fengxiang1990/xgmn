package com.fxa.xgmn;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import fxa.com.xgmn.R;

/**
 * Created by 30315 on 2018/2/7.
 */

public class MyLoadmoreView extends LinearLayout implements SwipeMenuRecyclerView.LoadMoreView, View.OnClickListener {

    SwipeMenuRecyclerView.LoadMoreListener loadMoreListener;

    public MyLoadmoreView(Context context) {
        super(context);
        initView();
    }

    public MyLoadmoreView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    View rootView;

    void initView() {
        rootView = LayoutInflater.from(getContext()).inflate(R.layout.loading_more, null);
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
        setPadding(0, 30, 0, 30);
        ProgressBar progressBar = new ProgressBar(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                (int) getResources().getDimension(R.dimen.progress_bar_wh),
                (int) getResources().getDimension(R.dimen.progress_bar_wh));
        addView(progressBar, layoutParams);
        TextView textView = new TextView(getContext());
        textView.setText("loading...");
        textView.setTextSize(16);
        layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = 40;
        addView(textView, layoutParams);
        setOnClickListener(this);
    }

    @Override
    public void onLoading() {
        rootView.setVisibility(VISIBLE);
    }

    @Override
    public void onLoadFinish(boolean dataEmpty, boolean hasMore) {
        rootView.setVisibility(GONE);
    }

    @Override
    public void onWaitToLoadMore(SwipeMenuRecyclerView.LoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    @Override
    public void onLoadError(int errorCode, String errorMessage) {

    }

    @Override
    public void onClick(View v) {
        if (loadMoreListener != null) loadMoreListener.onLoadMore();
    }
}
