package com.zuk.ireader.business.firebase;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.filestore.MyFireStore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.gson.Gson;
import com.zuk.ireader.business.config.ComCode;
import com.zuk.ireader.business.config.NovelConfigRankingClass;
import com.zuk.ireader.model.bean.novel.NovelGenreBean;
import com.zuk.ireader.realm.DBManagerApiCache;
import com.zuk.ireader.realm.TableApiCache;
import com.zuk.ireader.ui.fragment.novel.NovelSiteDataFragment;
import com.zuk.ireader.utils.Constant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;

import static android.content.ContentValues.TAG;

/**
 * Created by ksymac on 2019/1/31.
 */

public class ComHomeData {
    String homedatakey ;
    public class NovelRankingBean {
        public int no;
        public String documentid;
        public String link;
    }
    public String getRankingUrl (String siteno, String genrelink){
        String url =String.format(Constant.API_BASE_URL_NOVEL + "/novelgenre?genre=%s&site=%s",genrelink,siteno);
        return url;
    }
    public String getdocid (String siteno, int rankdataid){
        String docid = String.format("homev%ss%sn%d", ComCode.apiver_genre, siteno, rankdataid);
        return docid;
    }
    public NovelRankingBean getWorkBeanWithNo(int no){
        NovelConfigRankingClass config = new NovelConfigRankingClass();
        NovelConfigRankingClass.NovelConfigRankingItem item = config.getItem(no);
        NovelRankingBean bean = new NovelRankingBean();
        bean.link = getRankingUrl(item.siteno,item.genreurl);
        bean.no = no;
        bean.documentid = getdocid(item.siteno,no);
        return bean;
    }
    public void getHomeRankingData(Activity ctx, NovelSiteDataFragment fragment, int workno){
        homedatakey = createHomeDatakey(workno);
        //from db
        TableApiCache data = getHomeRankingDataFromDB(ctx,fragment,workno);
        boolean needloadNetData = true;
        if(data!=null && data.jsondata !=null  && data.jsondata.length() >10) {
            if(fragment!=null){
                fragment.loadDataWithJsonString(data.jsondata);
            }
            if(data.updatetime != null ){
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) - ComCode.apicache_novelinfo_hoursago_2);
                Date upd = data.updatetime;
                Date cDate = calendar.getTime();
                //android.util.Log.d("TEST",cDate.toString());
                //android.util.Log.d("TEST",upd.toString());
                // 让日期加1
                if(upd.after(cDate)){
                    needloadNetData = false;
                }else{
                    needloadNetData = true;
                }
            }
        }else{
            getHomeRankingDataFromFile(ctx,fragment,workno);
            needloadNetData = true;
        }
        if(needloadNetData){
            this.getHomeRankingDataFromGaeFireStore(ctx,fragment,workno);
            if(fragment!=null){
                fragment.showInfo();
            }
        }
    }
    public String createHomeDatakey(int workno){
        NovelRankingBean bean = getWorkBeanWithNo(workno);
        return String.format("homedata:%s",bean.documentid);
    }
    public TableApiCache getHomeRankingDataFromDB(Activity ctx, NovelSiteDataFragment fragment, int workno){
        //首先从DB中获取
        String key = createHomeDatakey(workno);
        TableApiCache cache = DBManagerApiCache.getDataFromCache(key,ComCode.apiver_genre);
        return cache;
    }

    public void getHomeRankingDataFromFile(Activity ctx, NovelSiteDataFragment fragment, int workno){
        String fileName = "homev3s2n0";
        if ( workno == 0 || workno == 200 || workno == 701 || workno == 401 || workno == 501 || workno == 601 || workno == 101){
            NovelConfigRankingClass config = new NovelConfigRankingClass();
            NovelConfigRankingClass.NovelConfigRankingItem item = config.getItem(workno);
            fileName = getdocid(item.siteno, workno);;
        }else{
            return;
        }
        String json = this.getJsonFromLocalJsonFile(ctx, fileName);
        if(json!=null){
            DBManagerApiCache.saveToCache(homedatakey,ComCode.apiver_genre,json);
            if(fragment!=null){
                fragment.loadDataWithJsonString(json);
            }
        }
    }

    public void getHomeRankingDataFromGaeFireStore(Activity ctx, NovelSiteDataFragment fragment, int workno) {
        NovelRankingBean bean = this.getWorkBeanWithNo(workno);
        MyFireStore.firestoredb.getInstance()
                .collection("novel_rank")
                .document(bean.documentid)
                .get().addOnCompleteListener(ctx,new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult() == null) Log.d(TAG, "getResult is null");
                    Log.d(TAG, "getResult: " + task.getResult());
                    DocumentSnapshot document = task.getResult();
                    System.out.println("Document data: " + document.getData());

                    if(fragment != null){
                        String json = document.get("json").toString();
                        DBManagerApiCache.saveToCache(homedatakey,ComCode.apiver_genre,json);
                        fragment.loadDataWithJsonString(json);
                    }
                }else{
                    System.out.println("No such document!");
                }
            }
        });
    }






    public static String getJsonFromLocalJsonFile(Context context, String fileName){
        StringBuilder stringBuilder = new StringBuilder();
        //获得assets资源管理器
        AssetManager assetManager = context.getAssets();
        //使用IO流读取json文件内容
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName),"utf-8"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
    public  static <T> T JsonToObject(String json, Class<T> type) {
        Gson gson =new Gson();
        return gson.fromJson(json, type);
    }
}
