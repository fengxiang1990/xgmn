package com.fxa.xgmn;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fxa.com.xgmn.R;

/**
 * Created by 30315 on 2018/2/2.
 */

public class ImageFragment extends Fragment {

    String tag = "ImageFragment";

    View rootView;
    SwipeMenuRecyclerView recyclerViewFinal;
    SwipeRefreshLayout swipeRefreshLayout;
    List<ImageResult> imageResultList = new ArrayList<>();
    MyAdapter adapter;
    String type;
    int totalCount = 0;
    int pageNumber = 1;
    int pageSize = 12;
    int screenWidth = 0;
    int totalPage = 0;
    boolean isRefresh = false;
    MyLoadmoreView myLoadmoreView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_imagelist, null, true);
        screenWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        recyclerViewFinal = rootView.findViewById(R.id.recyclerView);
        swipeRefreshLayout = rootView.findViewById(R.id.swipRefeshLayout);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerViewFinal.setLayoutManager(gridLayoutManager);
        adapter = new MyAdapter(imageResultList);
        recyclerViewFinal.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNumber = randInt(1, totalPage);
                isRefresh = true;
                loadData();
            }
        });

        myLoadmoreView = new MyLoadmoreView(getActivity());
        recyclerViewFinal.addFooterView(myLoadmoreView);
        recyclerViewFinal.setLoadMoreView(myLoadmoreView);
        recyclerViewFinal.setAutoLoadMore(false);
        recyclerViewFinal.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (isToBottom(recyclerView)) {
                    if (myLoadmoreView != null && myLoadmoreView.loadMoreListener != null) {
                        myLoadmoreView.loadMoreListener.onLoadMore();
                    }
                }
            }
        });
        recyclerViewFinal.setLoadMoreListener(new SwipeMenuRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                pageNumber++;
                isRefresh = false;
                loadData();
            }
        });

        type = getArguments().getString("type");
        totalCount = DBManager.count(((MainActivity) getActivity()).sqLiteDatabase, type);
        totalPage = totalCount / pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;
        pageNumber = randInt(1, totalPage / 2);
        Log.e(tag, type + " " + totalCount + " " + totalPage + " " + pageNumber);
        loadData();
        return rootView;
    }

    public int randInt(int a, int b) {
        Random random = new Random();
        int min = Math.min(a, b);
        int max = Math.max(a, b);
        return random.nextInt((max - min) + 1) + min;
    }


    void loadData() {
        Log.e(tag, "pageNumber-->" + pageNumber);
        if (isRefresh) {
            imageResultList.clear();
        }
        List<ImageResult> imageResults = DBManager.query(((MainActivity) getActivity()).sqLiteDatabase, type, pageNumber, pageSize);
        imageResultList.addAll(imageResults);
        adapter.notifyDataSetChanged();
        onLoadComplete();
    }

    void onLoadComplete() {
        recyclerViewFinal.loadMoreFinish(false, true);
        swipeRefreshLayout.setRefreshing(false);
    }

    class MyAdapter extends RecyclerView.Adapter<ImageHolder> {

        List<ImageResult> data;

        public MyAdapter(List<ImageResult> imageResults) {
            this.data = imageResults;
        }

        @Override
        public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_image, parent, false);
            return new ImageHolder(view);
        }

        @Override
        public void onBindViewHolder(final ImageHolder holder, int position) {
            final ImageResult imageResult = data.get(position);
            int imgWidth = screenWidth / 2;
            int imgHeight = 4 * imgWidth / 3;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(imgWidth, imgHeight);
            holder.imageView.setLayoutParams(layoutParams);
            Glide.with(getContext())
                    .asBitmap()
                    .apply(RequestOptions.overrideOf(400,300))
                    .load(Uri.parse(imageResult.url))
                    .into(new ImageViewTarget<Bitmap>(holder.imageView) {

                        @Override
                        public void onLoadStarted(@Nullable Drawable placeholder) {
                            super.onLoadStarted(placeholder);
                            holder.imageView.setScaleType(ImageView.ScaleType.CENTER);
                            holder.imageView.setImageResource(R.mipmap.loading_icon);
                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);
                            holder.imageView.setScaleType(ImageView.ScaleType.CENTER);
                            holder.imageView.setImageResource(R.mipmap.load_faild);
                        }

                        @Override
                        protected void setResource(@Nullable final Bitmap resource) {
                            if (resource == null) return;
                            holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            holder.imageView.setImageBitmap(resource);
                            holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    try {
                                        getActivity().setWallpaper(resource);
                                        Toast.makeText(getActivity(), "设置壁纸成功", Toast.LENGTH_SHORT).show();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    return false;
                                }
                            });
                            holder.imageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                                    intent.putExtra("result", imageResult);
                                    startActivity(intent);
                                }
                            });
                        }
                    });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    class ImageHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ImageHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }


    public static boolean isToBottom(RecyclerView recyclerView) {
        GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int state = recyclerView.getScrollState();
        if (visibleItemCount > 0 && lastVisibleItemPosition == totalItemCount - 1 && state == recyclerView.SCROLL_STATE_IDLE) {
            return true;
        } else {
            return false;
        }
    }


}
