package com.fxa.xgmn;

import android.util.LruCache;

import java.util.Collection;

/**
 * Created by 30315 on 2018/2/6.
 */

public class MyMemeryCache extends LruCache<String,Collection<ImageResult>>{
    /**
     * @param maxSize for caches that do not override {@link #sizeOf}, this is
     *                the maximum number of entries in the cache. For all other caches,
     *                this is the maximum sum of the sizes of the entries in this cache.
     */
    public MyMemeryCache(int maxSize) {
        super(maxSize);
    }
}
