package com.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.WorkerThread;
import android.support.v4.app.NotificationCompat;
import com.zuk.ireader.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.zuk.ireader.ui.activity.novel.NovelMainActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

/**
 * Created by ksymac on 2018/7/9.
 */

public class MyMessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(String var1) {
        android.util.Log.d("FCM-TEST", "メッセージタイプ: 通知\nタイトル: " + var1);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // FCMメッセージを受信したときに呼び出される
        // 通知メッセージの受信
        if (remoteMessage.getNotification() != null) {
            RemoteMessage.Notification notification = remoteMessage.getNotification();
            String title = notification.getTitle();
            String body = notification.getBody();
            android.util.Log.d("FCM-TEST", "メッセージタイプ: 通知\nタイトル: " + title + "\n本文: " + body);
            sendNotification(title,body);
        }

//        // データメッセージの受信
//        if (remoteMessage.getData().size() > 0) {
//            Map<String, String> data = remoteMessage.getData();
//            String trackno = data.get("trackno");
//            String subject = data.get("title");
//            String msgbody = data.get("body");
//            String tracktype = data.get("tracktype");
//            String datafrom = data.get("datafrom");
//            String dataflg = data.get("dataflg");
//            android.util.Log.d("FCM-TEST", "メッセージタイプ: データ\nタイトル: " + subject + "\n本文: " + msgbody);
//            sendNotification(data);
//        }
    }

    private void sendNotification(String title,String msg) {
        Intent intent = new Intent(this, NovelMainActivity.class);
        intent.putExtra("Message", msg);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_ALL);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
                this).setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title)
                .setContentText(msg)
                .setAutoCancel(true)
//                .setTicker("更新")
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        final int notifyId = getNotifyId();
        notificationManager.notify(notifyId, notificationBuilder.build());
    }
    private void sendNotification(Map<String, String> data) {
        // 履歴削除,activity再利用
        final Intent intent = new Intent(this, NovelMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//        intent.putExtra(MainActivity.EXTRA_NOTIFY_URL, data.get(EXTRA_URL));
        final PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        final Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        String subject = data.get("subject");
        String text = data.get("text");
//        String trackno = data.get("trackno");

        android.support.v4.app.NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(subject)
                .setContentText(text)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(contentIntent);
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        final int notifyId = getNotifyId();
        notificationManager.notify(notifyId, builder.build());
    }

    // notification複数表示
    private int getNotifyId() {
        Calendar calendar = Calendar.getInstance();
        return Integer.parseInt(new SimpleDateFormat("ddHHmmss", Locale.JAPAN).format(calendar.getTime()));
    }
}