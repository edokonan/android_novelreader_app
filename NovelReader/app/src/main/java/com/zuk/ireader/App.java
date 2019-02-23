package com.zuk.ireader;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;

import com.zuk.ireader.business.config.ComAnalytics;
import com.zuk.ireader.realm.DBManagerApiCache;
import com.zuk.ireader.realm.DBManagerReadHistory;
import com.zuk.ireader.realm.RealmMigrations;
import com.zuk.ireader.service.DownloadService;
import com.squareup.leakcanary.LeakCanary;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by newbiechen on 17-4-15.
 */

public class App extends Application {
    public static Context sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        ComAnalytics.mAppCnt = getContext();
        Realm.init(this);
        String dbname = "realm201901.realm";
//        final RealmConfiguration configuration =
//                new RealmConfiguration.Builder().name("realm201901.realm")
//                        .schemaVersion(1).build();
        final RealmConfiguration configuration =
                new RealmConfiguration.Builder().name(dbname)
                        .schemaVersion(2).migration(new RealmMigrations()).build();
        Realm.setDefaultConfiguration(configuration);
        Realm.getInstance(configuration);

        String oldver =  DBManagerApiCache.getJsouFromCache("dbver","1");
        if(oldver==null){
            DBManagerReadHistory.deleteALL();
        }
        DBManagerApiCache.saveToCache("dbver","1","2");

        //        startService(new Intent(getContext(), DownloadService.class));
        // 初始化内存分析工具
        if (!LeakCanary.isInAnalyzerProcess(this)) {
            LeakCanary.install(this);
        }
    }

    public static Context getContext(){
        return sInstance;
    }

    @Override
    public void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        try {
            MultiDex.install(this);
        } catch (RuntimeException multiDexException) {
            multiDexException.printStackTrace();
        }
    }
}