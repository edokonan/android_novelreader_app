package com.fcm;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.filestore.MyFireStore;
import com.filestore.MyLocalData;
import com.google.firebase.iid.FirebaseInstanceId;
import com.zuk.ireader.R;

import java.io.IOException;

/**
 * Created by ksymac on 2018/7/9.
 */
public class RegistrationIntentService extends IntentService {
    public RegistrationIntentService() {
        super("RegistrationIntentService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            String senderId = "728456664935";
            String token = FirebaseInstanceId.getInstance().getToken(senderId,"FCM");
            Log.d("###", "token = " + token);
            insertServer(token);
        } catch ( IOException e) {
            System.out.println( "RegistrationIntentService error" );
        }
    }
    private void insertServer(String token) {
        // サーバーへ登録
        Log.d("###", "insertServer = " );
        MyLocalData.setFcmToken(getApplicationContext(),token);
        MyFireStore.initGaeDocument(getApplicationContext());
    }
}
