package com.zuk.ireader.business.config;

import com.zuk.ireader.realm.DBManagerApiCache;

/**
 * Created by ksymac on 2019/2/2.
 */

public class ComRate {
    public  static void addEvent(){
        String numstr = DBManagerApiCache.getJsouFromCache("app_rate_event", "1");
        if(numstr==null){
            numstr = "0";
            DBManagerApiCache.saveToCache("app_rate_event", "1" , numstr);
        }
        int event = Integer.valueOf(numstr);
        event += 1;
        DBManagerApiCache.saveToCache("app_rate_event", "1" , String.valueOf(event));
    }
    public  static Boolean checkCanPopRate(){
        String numstr = DBManagerApiCache.getJsouFromCache("app_rate_event", "1");
        if(numstr==null){
            numstr = "0";
            DBManagerApiCache.saveToCache("app_rate_event", "1" , numstr);
        }
        int event = Integer.valueOf(numstr);
        if(event>2){
            return true;
        }
        return false;
    }
    public  static void addEventIsZero(){
        DBManagerApiCache.saveToCache("app_rate_event", "1" , "0");
    }
}
