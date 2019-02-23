package com.fcm;

import android.content.Context;
import android.support.v4.app.NotificationManagerCompat;

import com.filestore.MyLocalData;

/**
 * Created by ksymac on 2018/9/29.
 */

public class NotificationsUtils {

    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

    public static boolean isNotificationEnabled(Context ctx) {
        boolean ret = NotificationManagerCompat.from(ctx).areNotificationsEnabled();
        MyLocalData.setEnableFcmToken(ctx,ret);
        return ret;
//        AppOpsManager mAppOps = (AppOpsManager) ctx.getSystemService(Context.APP_OPS_SERVICE);
//        ApplicationInfo appInfo = ctx.getApplicationInfo();
//        String pkg = ctx.getApplicationContext().getPackageName();
//        int uid = appInfo.uid;
//        try {
//            Class appOpsClass = null; /* Context.APP_OPS_MANAGER */
//            appOpsClass = Class.forName(AppOpsManager.class.getName());
//            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE, String.class);
//            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
//            int value = (int)opPostNotificationValue.get(Integer.class);
//            boolean ret = ((int)checkOpNoThrowMethod.invoke(mAppOps,value, uid, pkg) == AppOpsManager.MODE_ALLOWED);
//            MyLocalData.setEnableFcmToken(ctx,ret);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//        return false;
    }
}

