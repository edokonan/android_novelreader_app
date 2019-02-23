package com.fcm;

import android.text.TextUtils;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by ksymac on 2018/7/9.
 */

public class MyInstanceIdService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        // tokenをサーバに送信するなど、端末外で永続的に保存するための処理をこのあたりに書きます
        // あとでテストに使用したいので、ここではLogcatに出力しています（本来はNG）
        if (!TextUtils.isEmpty(token)) {
            android.util.Log.d("FCM-TEST", "token = " + token);
        }
    }
}