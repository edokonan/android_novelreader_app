package com.filestore;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.zuk.ireader.model.bean.novel.NovelGenreDataBookData;
import com.zuk.ireader.model.bean.novel.NovelInfoBean;
import com.zuk.ireader.realm.DBManagerApiCache;
import com.zuk.ireader.realm.DBManagerReadBookShelf;
import com.zuk.ireader.realm.TableReadBookShelf;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import static android.content.ContentValues.TAG;


/**
 * Created by ksymac on 2018/7/9.
 */
public class MyFireStore {
    static String firestore_dbname = "novel_fav";
    public static FirebaseFirestore firestoredb = FirebaseFirestore.getInstance();
    public static FirebaseDatabase realtimedbb = FirebaseDatabase.getInstance();
    public static DatabaseReference mDatabase = realtimedbb.getReference("novelaos");;
//    //MARK: - 修改key
//    public static void updaKeyToFcm(Context ctx,String oldno,TrackMain newbean){
//        String token = MyLocalData.getFcmToken(ctx);
//        if(token == null){
//            return;
//        }
//        if(oldno == newbean.getTrackNo()){
//            updateToFCM(ctx,newbean);
//        }else{
//            deleteDataToFCM(ctx,oldno);
//            addDataToFCM(ctx,newbean);
//        }
//    }
    static String GAEFavDataKey = "GAEFavDataKey";

    public static String initGaeDocument(Context ctx){
        return getDocumentID(ctx);
    }


    public static  String getDocumentID(Context ctx){
        String documentid = DBManagerApiCache.getJsouFromCache(GAEFavDataKey,"1");
        if(documentid==null){
            Calendar calendar = Calendar.getInstance();
            String time = new SimpleDateFormat("yyMMddHHmmss", Locale.JAPAN).format(calendar.getTime());
            long l = System.currentTimeMillis();
            int i = (int)( l % 100 );
            int d = Math.abs(UUID.randomUUID().hashCode());
            String str = String.format("%6d", d).replace(" ", "0");
            documentid = String.format("%s:%d%s",time,i,str.substring(0,5));
            addDocumentAtGae(ctx,documentid);
        }
        return documentid;
    }



//    public static  String getGaeDbPath(){
////        String path = String.format("%s%s",firestore_dbname,getDocumentID());
////        return path;
//        return getDocumentID();
//    }
    //,NovelInfoBean bean
    public static void addDocumentAtGae(Context ctx,String documentid){
        String token = MyLocalData.getFcmToken(ctx);
        if(token == null){
            return;
        }
        Map<String, Object> docData = new HashMap<>();
//        docData.put("novelurl", bean.novelinfodata.novel_url);
//        docData.put("noveltitle", bean.novelinfodata.novel_title);
        docData.put("d_token", token);
        Date date = new Date();
        docData.put("updatedApp", "aos");
        docData.put("createdAt", date);
        docData.put("updatedAt", date);
//        docData.put("stringExample", "Hello world!");
//        docData.put("booleanExample", true);
//        docData.put("numberExample", 3.14159265);
//        docData.put("dateExample", new Date());
//        docData.put("listExample", Arrays.asList(1, 2, 3));
//        docData.put("nullExample", null);
//        Map<String, Object> nestedData = new HashMap<>();
//        nestedData.put("a", 5);
//        nestedData.put("b", true);
//        docData.put("objectExample", nestedData);
//         = getDocumentID();

        firestoredb.collection(firestore_dbname).document(documentid)
                .set(docData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        DBManagerApiCache.saveToCache(GAEFavDataKey,"1",documentid);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    public static void addNovelToGaeFav(Context ctx, NovelInfoBean bean){
        String token = MyLocalData.getFcmToken(ctx);
        if(token == null){
            return;
        }
        List<Map<String, Object>> array = new ArrayList<Map<String, Object>>();

        List<TableReadBookShelf> list = DBManagerReadBookShelf.getBooklist();
        for(TableReadBookShelf item : list){
            Map<String, Object> nestedData = new HashMap<>();
            nestedData.put("no", item.novel_original_no);
            nestedData.put("s", item.siteno);
            array.add(nestedData);
        }

        String documentid = getDocumentID(ctx);
        DocumentReference washingtonRef =  firestoredb.collection(firestore_dbname)
                .document(documentid);
        Date date = new Date();
        washingtonRef
                .update("flist", array,
                        "updatedAt", date )
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
//        FireStoreNovelBean b = new FireStoreNovelBean();
//        b.no = bean.novelinfodata.novel_original_no;
//        b.site = bean.novelinfodata.siteno;
//        b.ver = "1";
//        mDatabase.child("novel").child(bean.novelinfodata.siteno)
//                .child(bean.novelinfodata.novel_original_no)
//                .setValue(b);
    }






//    public static void updateToFCM(Context ctx, TrackMain bean){
//        String token = MyLocalData.getFcmToken(ctx);
//        if(token == null){
//            return;
//        }
//        String[] array = {"1", "2","3","9"};
//        List<String> tempList = Arrays.asList(array);
//        if (!tempList.contains(bean.getCompany().toString()) ){
//            return;
//        }
//        //如果已经收到了，就删除云端数据
//        boolean deli_over = ComFunc.isDeliveryOver(bean);
//        if(deli_over == true){
//            deleteDataToFCM(ctx, bean.getTrackNo());
//            return;
//        }
//        int err_level = 0;
//        if(ComFunc.isErrNo(bean)){
//            err_level = 1;
//        }
//        int finalErr_level = err_level;
//        db.collection(firestore_dbname)
//                .whereEqualTo("trackNo", bean.getTrackNo())
//                .whereEqualTo("d_token", token)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            if(task.getResult().size() == 0){
//                                addDataToFCM(ctx, bean);
//                            }
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d(TAG, document.getId() + " => " + document.getData());
//                                String strstatus = ComFunc.getNowStatus(bean);
//                                Map<String,Object> updates = new HashMap<>();
//                                updates.put("title", bean.getComment());
//                                updates.put("comment", "");
//                                updates.put("trackType", bean.getCompany().toString());
//                                updates.put("d_usermail", "");
//                                updates.put("strStatus", strstatus);
//                                updates.put("deli_over", deli_over);
//                                updates.put("err_level", finalErr_level);
//                                Date date = new Date();
//                                updates.put("updatedAt", date);
//                                document.getReference().update(
//                                        updates
//                                );
//                            }
//                        } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
//                        }
//                    }
//                });
//    }
//    //deleDataToFCM
//    public static void deleteDataToFCM(Context ctx,String strackno){
//        String token = MyLocalData.getFcmToken(ctx);
//        if(token == null){
//            return;
//        }
//        db.collection(firestore_dbname)
//                .whereEqualTo("trackNo", strackno)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d(TAG, document.getId() + " => " + document.getData());
//                                document.getReference().delete();
//                            }
//                        } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
//                        }
//                    }
//                });
//    }
//
//    public static void deleteDataByFCMToken(Context ctx){
//        String token = MyLocalData.getFcmToken(ctx);
//        if(token == null){
//            return;
//        }
//        db.collection(firestore_dbname)
//                .whereEqualTo("d_token", token)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d(TAG, document.getId() + " => " + document.getData());
//                                document.getReference().delete();
//                            }
//                        } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
//                        }
//                    }
//                });
//    }
}
