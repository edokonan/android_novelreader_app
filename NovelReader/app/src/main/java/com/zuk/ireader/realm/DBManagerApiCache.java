package com.zuk.ireader.realm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by ksymac on 2018/12/15.
 */

public class DBManagerApiCache {

    public static void saveToCache(String key, String ver , String jsonstr){
        removeFromCache(key);
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TableApiCache item = realm.createObject(TableApiCache.class);
                item.key = key;
                item.ver = ver;
                item.jsondata = jsonstr;
                item.updatetime = new Date();
            }
        });
    }

    public static void removeFromCache(String key){
        // Create the Realm instance
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<TableApiCache> rows =
                        realm.where(TableApiCache.class).equalTo("key",key).findAll();
                rows.deleteAllFromRealm();
            }
        });
    }

    public static String getJsouFromCache(String key,String ver){
        Realm realm = Realm.getDefaultInstance();
        RealmResults<TableApiCache> r1 = realm.where(TableApiCache.class).equalTo("key",key)
                .equalTo("ver",ver)
                .findAll();
        if(r1.size() > 0){
            return r1.first().jsondata;
        }else {
            return null;
        }
    }

    public static TableApiCache getDataFromCache(String key,String ver){
        Realm realm = Realm.getDefaultInstance();
        RealmResults<TableApiCache> r1 = realm.where(TableApiCache.class).equalTo("key",key)
                .equalTo("ver",ver)
                .findAll();
        if(r1.size() > 0){
            return r1.first();
        }else {
            return null;
        }
    }


    public static String getJsouFromCache(String key,String ver,int hoursAgo){
        Realm realm = Realm.getDefaultInstance();
        RealmResults<TableApiCache> r1 = realm.where(TableApiCache.class).equalTo("key",key)
                .equalTo("ver",ver)
                .findAll();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) - hoursAgo);// 让日期加1
        System.out.println(calendar.get(Calendar.DATE));// 加1之后的日期Top
        if(r1.size() > 0){
            for(TableApiCache tb : r1){
                Date temp = tb.updatetime;
                if(temp.after(calendar.getTime())){
                    return tb.jsondata;
                }
            }
        }
        return null;
    }
}
