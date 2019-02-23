package com.zuk.ireader.realm;


import com.zuk.ireader.model.bean.novel.NovelInfoBean;
import com.zuk.ireader.model.bean.novel.NovelInfoChapterdata;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by ksymac on 2018/12/23.
 */

public class DBManagerReadHistory {
    //
    public static boolean haveHistoryWithNovelUrl(String novelurl){
        TableReadHistory history = getHistoryWithNovelUrl(novelurl);
        if(history != null){
            return  true;
        }
        return false;
    }
    public static boolean haveHistory(String chapterurl){
        TableReadHistory history = getHistory(chapterurl);
        if(history != null){
            return  true;
        }
        return false;
    }
    //
    public static String getLastestTxtPath(String chapterurl){
        if( chapterurl!=null && chapterurl.length() > 5){
            TableReadHistory historybean = getHistory(chapterurl);
            if(historybean != null){
                TableNovelChapter chapter = DBManagerBookChapter.getChapterInfo(historybean.chapterurl,
                        historybean.pageno);
                if(chapter != null && chapter.txtfilepath!=null && chapter.txtfilepath.length() > 5){
                    File txtfile = new File(chapter.txtfilepath);
                    if(txtfile.exists()){
                        return chapter.txtfilepath;
                    }
                }
            }
        }
        return null;
    }
    //
    public static TableReadHistory getHistoryWithNovelUrl(String novelurl){
        Realm realm = Realm.getDefaultInstance();
        RealmResults<TableReadHistory> r1 = realm.where(TableReadHistory.class)
                .equalTo("novelurl",novelurl)
                .findAll()
                .sort("updatetime", Sort.DESCENDING);;
        if (r1.size() >0) {
            return r1.first();
        }else{
            return null;
        }
    }
    public static TableReadHistory getHistory(String chapterurl){
        Realm realm = Realm.getDefaultInstance();
        RealmResults<TableReadHistory> r1 = realm.where(TableReadHistory.class)
                .equalTo("chapterurl",chapterurl)
                .findAll()
                .sort("updatetime", Sort.DESCENDING);
        if (r1.size() >0) {
            return r1.first();
        }else{
            return null;
        }
    }
    //
    public static void insertHistory(NovelInfoBean mNovelBean, NovelInfoChapterdata mChapterBean, int pageno){
        removeHistoryWithnovelurl(mNovelBean.novelinfodata.novel_url);
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TableReadHistory book = realm.createObject(TableReadHistory.class);
                book.novelurl = mNovelBean.novelinfodata.novel_url;
                book.chapterurl = mChapterBean.chapter_url;

                book.siteno = mNovelBean.novelinfodata.siteno;
                book.novelimgurl = mNovelBean.novelinfodata.novel_imgurl;
                book.title = mNovelBean.novelinfodata.novel_title;
                book.author = mNovelBean.novelinfodata.novel_writername;
                book.summary = mNovelBean.novelinfodata.novel_ex;
                book.time = mNovelBean.novelinfodata.novel_lastupdatetime;
                book.novel_original_no = mNovelBean.novelinfodata.novel_original_no;

                book.time = mNovelBean.novelinfodata.novel_lastupdatetime;
                book.novel_original_no = mNovelBean.novelinfodata.novel_original_no;
                book.chaptertime = mChapterBean.chapter_time;
                book.chapterno = mChapterBean.chapter_no;

                book.pageno = pageno;
                book.updatetime = new Date();
            }
        });
    }
    //
    public static void removeHistoryWithnovelurl(String novelurl){
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<TableReadHistory> rows = realm.where(TableReadHistory.class)
                        .equalTo("novelurl",novelurl)
                        .findAll();
//                rows.clear();
                rows.deleteAllFromRealm();
            }
        });
    }
    public static void deleteALL(){
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<TableReadHistory> rows = realm.where(TableReadHistory.class)
                        .findAll();
//                rows.clear();
                rows.deleteAllFromRealm();
            }
        });
    }
    //
    public static List<TableReadHistory> getBooklist(){
        List<TableReadHistory> list = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        RealmResults<TableReadHistory> r1 = realm.where(TableReadHistory.class)
                .findAll()
                .sort("updatetime", Sort.DESCENDING);
        list.addAll(realm.copyFromRealm(r1));


        for (TableReadHistory item: list){
            if (item.siteno == null || item.siteno.length() ==0){
                list.remove(item);
            }
        }
        return list;
    }
}
