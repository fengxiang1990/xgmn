package com.fxa.xgmn;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 30315 on 2018/2/1.
 */

public class DBManager {
    private static String DB_NAME = "image.db";

    //把assets目录下的db文件复制到dbpath下
    public static SQLiteDatabase initDB(Context context) {
        String dbPath = "/data/data/" + context.getPackageName()
                + "/databases/";
        File jhPath = new File(dbPath);
        //查看数据库文件是否存在
        if (!jhPath.exists()) {
            //存在则直接返回打开的数据库
            try {
                //得到资源
                AssetManager am = context.getAssets();
                //得到数据库的输入流
                InputStream is = am.open(DB_NAME);
                //用输出流写到SDcard上面
                FileOutputStream fos = new FileOutputStream(jhPath);
                //创建byte数组  用于1KB写一次
                byte[] buffer = new byte[1024];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                //最后关闭就可以了
                fos.flush();
                fos.close();
                is.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                File file = new File(dbPath);
                file.mkdir();
                return null;
            }
        }
        return SQLiteDatabase.openOrCreateDatabase(jhPath, null);
    }

    public static int count(@NonNull SQLiteDatabase sqliteDB, @NonNull String type) {
        StringBuilder sql = new StringBuilder("select count(*) from image where 1=1");
        if (!TextUtils.isEmpty(type) && !type.equals("all")) {
            sql.append(" and type= '" + type + "'");
        }
        Cursor cursor = sqliteDB.rawQuery(sql.toString(), null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        return count;
    }

    //查询
    public static List<ImageResult> query(@NonNull SQLiteDatabase sqliteDB, String imgtype, @NonNull int pageNumber, @NonNull int pageSize) {
        List<ImageResult> imageResults = new ArrayList<>();
        try {
            StringBuilder sql = new StringBuilder("select * from image where 1=1");
            if (!TextUtils.isEmpty(imgtype) && !imgtype.equals("all")) {
                sql.append(" and type= '" + imgtype + "'");
            }
            sql.append(" limit ").append(pageSize);
            sql.append(" offset ").append((pageNumber - 1) * pageSize);
            Log.e("sql", "sql->" + sql.toString());
            Cursor cursor = sqliteDB.rawQuery(sql.toString(), null);
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String type = cursor.getString(cursor.getColumnIndex("type"));
                String url = cursor.getString(cursor.getColumnIndex("url"));
                ImageResult imageResult = new ImageResult();
                imageResult.name = name;
                imageResult.type = type;
                imageResult.url = url;
                imageResults.add(imageResult);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageResults;
    }


    //查询
    public static ImageResult queryById(@NonNull SQLiteDatabase sqliteDB, int id) {
        try {
            StringBuilder sql = new StringBuilder("select * from image where id=" + id + "");
            Log.e("sql", "sql->" + sql.toString());
            Cursor cursor = sqliteDB.rawQuery(sql.toString(), null);
            cursor.moveToFirst();
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String type = cursor.getString(cursor.getColumnIndex("type"));
            String url = cursor.getString(cursor.getColumnIndex("url"));
            ImageResult imageResult = new ImageResult();
            imageResult.name = name;
            imageResult.type = type;
            imageResult.url = url;
            return imageResult;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
