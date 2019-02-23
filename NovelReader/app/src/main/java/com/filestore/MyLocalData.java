package com.filestore;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ksymac on 2018/9/25.
 */

public class MyLocalData {
    private static final String MyLocalData = "MyLocalData";
    private static final String FCMToken = "FCMToken";
    private static final String FCMToken_Enable = "FCMToken_Enable";
    private static final String FCMLocal_Enable = "FCMLocal_Enable";

    public static void setFcmToken(Context ctx, String token){
        SharedPreferences data = ctx.getSharedPreferences(MyLocalData, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putString(FCMToken, token);
        editor.apply();
    }
    public static String getFcmToken(Context ctx){
        SharedPreferences data = ctx.getSharedPreferences(MyLocalData, Context.MODE_PRIVATE);
        Boolean ret = data.getBoolean(FCMToken_Enable, true );
        Boolean localret = data.getBoolean(FCMLocal_Enable, true );
        String fcmtoken = data.getString(FCMToken, null );
        if (ret != true || localret != true ){
            return null;
        }
        return fcmtoken;
    }
    public static void setEnableFcmToken(Context ctx, boolean enable){
        SharedPreferences data = ctx.getSharedPreferences(MyLocalData, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putBoolean(FCMToken_Enable, enable);
        editor.apply();
    }
    public static void setEnableFcmLocal(Context ctx, boolean enable){
        SharedPreferences data = ctx.getSharedPreferences(MyLocalData, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putBoolean(FCMLocal_Enable, enable);
        editor.apply();
    }
    public static Boolean getEnableFcmLocal(Context ctx){
        SharedPreferences data = ctx.getSharedPreferences(MyLocalData, Context.MODE_PRIVATE);
        Boolean ret = data.getBoolean(FCMLocal_Enable, true );
        return ret;
    }
//    public static Boolean getEnableFcmToken(Context ctx){
//        SharedPreferences data = ctx.getSharedPreferences(MyLocalData, Context.MODE_PRIVATE);
//        Boolean ret = data.getBoolean(FCMToken_Enable, false );
//        return ret;
//    }
}
