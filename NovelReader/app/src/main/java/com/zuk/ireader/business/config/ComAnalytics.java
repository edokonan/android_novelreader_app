package com.zuk.ireader.business.config;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by ksymac on 2019/2/9.
 */

public class ComAnalytics {
    private static FirebaseAnalytics mFirebaseAnalytics;
    public static Context mAppCnt;
    public static boolean addEventLog(String siteno, String novelno, String novelurl ,String noveltitle){
        // ...
        // Obtain the FirebaseAnalytics instance.
        if (mFirebaseAnalytics == null){
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(mAppCnt);
        }
        if (mFirebaseAnalytics!=null){
            //        Bundle bundle = new Bundle();
            //        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
            //        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
            //        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
            //        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
            Bundle params = new Bundle();
            params.putString("siteno", siteno);
            params.putString("novelno", novelno);
            params.putString("novelurl", novelurl);
            params.putString("noveltitle", noveltitle);
            mFirebaseAnalytics.logEvent("read_novel", params);
        }

        return false;
    }

}
