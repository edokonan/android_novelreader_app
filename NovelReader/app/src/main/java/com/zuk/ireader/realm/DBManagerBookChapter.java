package com.zuk.ireader.realm;

import com.google.gson.Gson;
import com.zuk.ireader.model.bean.novel.NovelInfoChapterdata;
import com.zuk.ireader.model.bean.novel.NovelInfoBean;
import com.zuk.ireader.model.bean.novel.NovelGenreDataBookData;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by ksymac on 2018/12/23.
 */

public class DBManagerBookChapter {
    //
    public static boolean haveTxtFile(String chapterurl,int pageno){
        TableNovelChapter chapter = getChapterInfo(chapterurl,pageno);
        if(chapter != null && chapter.txtfilepath != null && chapter.txtfilepath.length() >5){
           File txtfile = new File(chapter.txtfilepath);
           if(txtfile.exists()){
               return true;
            }
        }
        return false;
    }
    //
    public static void delChapterInfo(String chapterurl){
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<TableNovelChapter> rows = realm.where(TableNovelChapter.class)
                        .equalTo("chapterurl", chapterurl)
                        .findAll();
                rows.deleteAllFromRealm();
            }
        });
    }
    //
    public static TableNovelChapter getChapterInfo(String chapterurl,int pageno){
        Realm realm = Realm.getDefaultInstance();
        RealmResults<TableNovelChapter> r1 = realm.where(TableNovelChapter.class)
                .equalTo("chapterurl", chapterurl)
                .equalTo("pageno", pageno)
                .findAll();
        if (r1.size() >0) {
            return r1.first();
        }else{
            return null;
        }
    }
    //get pages
    public static List<TableNovelChapter> getChapterPages(String chapterurl){
        Realm realm = Realm.getDefaultInstance();
        RealmResults<TableNovelChapter> rs = realm.where(TableNovelChapter.class)
                .equalTo("chapterurl", chapterurl)
                .findAll();
        List<TableNovelChapter> list = realm.copyFromRealm(rs);
        return list;
    }

    // Create the Realm instance
    public static void insertChapterInfo(NovelInfoBean mNovelBean, NovelInfoChapterdata mChapterItem,int pageno){
        TableNovelChapter tbitem = getChapterInfo(mChapterItem.chapter_url,pageno);
        if( tbitem != null){
            return ;
        }
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TableNovelChapter chapter = realm.createObject(TableNovelChapter.class);
                chapter.novelurl = mNovelBean.novelinfodata.novel_url;
                chapter.chapterurl = mChapterItem.chapter_url;
                chapter.siteno = mNovelBean.novelinfodata.siteno;

                chapter.no = mChapterItem.no;
                chapter.chapterno = mChapterItem.chapter_no;
                chapter.chaptertime = mChapterItem.chapter_time;
                chapter.chaptertitle = mChapterItem.chapter_title;
                chapter.chapterother = mChapterItem.chapter_otherinfo;
                chapter.chapterpagestart = mChapterItem.chapter_pagestart;
                chapter.chapterpageend = mChapterItem.chapter_pageend;
                chapter.pageno = pageno;
                chapter.updatetime = new Date();
            }
        });
    }
    public static  void updateTxtFilepath(String chapterurl,  String pageurl, int pageno, String filepath){
        Realm mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();
        RealmResults<TableNovelChapter> results =
                mRealm.where(TableNovelChapter.class)
                        .equalTo("chapterurl", chapterurl)
                        .equalTo("pageno", pageno)
                        .findAll();
        int resultCount = results.size();
        for (int count = 0; count < resultCount; ++count) {
            TableNovelChapter obj = results.get(count);
            obj.txtfilepath = filepath;
            obj.pageurl = pageurl;
            obj.updatetime = new Date();
        }
        mRealm.commitTransaction();
    }
    public static  void updateContentHtml(String chapterurl,  String pageurl, int pageno, String html){
        Realm mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();
        RealmResults<TableNovelChapter> results = mRealm.where(TableNovelChapter.class)
                .equalTo("chapterurl", chapterurl)
                .equalTo("pageno", pageno)
                .findAll();
        int resultCount = results.size();
        for (int count = 0; count < resultCount; ++count) {
            TableNovelChapter obj = results.get(count);
            obj.strhtml = html;
            obj.pageurl = pageurl;
            obj.updatetime = new Date();
        }
        mRealm.commitTransaction();
    }
}
