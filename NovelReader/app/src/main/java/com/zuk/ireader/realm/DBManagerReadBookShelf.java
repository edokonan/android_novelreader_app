package com.zuk.ireader.realm;

import android.content.Context;

import com.filestore.MyFireStore;
import com.google.gson.Gson;
import com.zuk.ireader.model.bean.novel.NovelInfoChapterdata;
import com.zuk.ireader.model.bean.novel.NovelInfoBean;
import com.zuk.ireader.model.bean.novel.NovelGenreDataBookData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by ksymac on 2018/12/23.
 */

public class DBManagerReadBookShelf {
    // Create the Realm instance
//    public static void addToBookShelf(NovelGenreDataBookData data){
//        Realm realm = Realm.getDefaultInstance();
//        realm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                TableReadBookShelf book1 = realm.createObject(TableReadBookShelf.class);
//                book1.siteno = data.siteno;
////                book1.rankingno = data.rankingno;
//                book1.title = data.title;
//                book1.author = data.author;
////                book1.author_link = data.author_link;
//                book1.summary = data.summary;
//                book1.time = data.time;
//                book1.novelurl = data.link;
//                book1.updatetime = new Date();
////                String json = new Gson().toJson(data);
////                book1.NovelGenreDataBookDataJson = json;
//            }
//        });
//
//    }
    public static void addToBookShelf(Context ctx,NovelInfoBean bean){
        boolean b = hadAddToBookShelf(bean.novelinfodata.novel_url);
        if(b==true){
            return;
        }

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TableReadBookShelf book1 = realm.createObject(TableReadBookShelf.class);
                book1.novelurl = bean.novelinfodata.novel_url;
                book1.novelimgurl = bean.novelinfodata.novel_imgurl;

                book1.siteno = bean.novelinfodata.siteno;
                book1.title = bean.novelinfodata.novel_title;
                book1.author = bean.novelinfodata.novel_writername;
                book1.summary =  bean.novelinfodata.novel_ex;
                book1.novel_original_no =  bean.novelinfodata.novel_original_no;

                if(bean.novelinfodata.chapteritems.size()>0){
                    book1.lastestchapterno = bean.novelinfodata.chapteritems.get(bean.novelinfodata.chapteritems.size()-1).chapter_no;
                    book1.lastestchaptertime = bean.novelinfodata.chapteritems.get(bean.novelinfodata.chapteritems.size()-1).chapter_time;
                    book1.lastestchaptertitle = bean.novelinfodata.chapteritems.get(bean.novelinfodata.chapteritems.size()-1).chapter_title;
                    book1.lastestchapterurl = bean.novelinfodata.chapteritems.get(bean.novelinfodata.chapteritems.size()-1).chapter_url;
                    book1.chaptersnum = bean.novelinfodata.chapteritems.size();
                    book1.time = bean.novelinfodata.chapteritems.get(bean.novelinfodata.chapteritems.size()-1).chapter_time;
                }

                book1.updatetime = new Date();
                book1.isUpdate = false;
//                String json = new Gson().toJson(data);
//                book1.NovelGenreDataBookDataJson = json;
            }
        });
        MyFireStore.addNovelToGaeFav(ctx,bean);
    }

    public static void updateChapterToBookShelf(NovelInfoBean mNovelBean,
                                                NovelInfoChapterdata mChapterBean,
                                                int pageno ){
        Realm mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();
        RealmResults<TableReadBookShelf> results = mRealm.where(TableReadBookShelf.class)
                .equalTo("novelurl", mNovelBean.novelinfodata.novel_url).findAll();
        int resultCount = results.size();
        for (int count = 0; count < resultCount; ++count) {
            TableReadBookShelf obj = results.get(count);
            obj.chapterurl = mChapterBean.chapter_url;
            obj.chapterno = mChapterBean.chapter_no;
            obj.chaptertime = mChapterBean.chapter_time;
            obj.chaptertitle = mChapterBean.chapter_title;
            obj.pageno = pageno;

            obj.time = mNovelBean.novelinfodata.chapteritems.get(mNovelBean.novelinfodata.chapteritems.size()-1).chapter_time;
            obj.chaptersnum  = mNovelBean.novelinfodata.chapteritems.size();

            obj.updatetime = new Date();
        }
        mRealm.commitTransaction();
    }



    //
    public static List<TableReadBookShelf> getBooklist(){
        List<TableReadBookShelf> list = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        RealmResults<TableReadBookShelf> r1 = realm.where(TableReadBookShelf.class)
                .findAll()
                .sort("updatetime", Sort.DESCENDING);
        list.addAll(realm.copyFromRealm(r1));
        return list;
    }
    //
    public static void removeFromBookShelf(String novelurl){
        // Create the Realm instance
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<TableReadBookShelf> rows = realm.where(TableReadBookShelf.class)
                        .equalTo("novelurl",novelurl)
                        .findAll();
//                rows.clear();
                rows.deleteAllFromRealm();
            }
        });
    }
    //
    public static boolean hadAddToBookShelf(String novelurl){
        Realm realm = Realm.getDefaultInstance();
        RealmResults<TableReadBookShelf> r1 = realm.where(TableReadBookShelf.class)
                .equalTo("novelurl",novelurl)
                .findAll();
        if(r1.size() > 0){
            return true;
        }else {
            return false;
        }
    }

    //
    public static void checkIsUpdate(NovelInfoBean bean){
        Realm mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();
        RealmResults<TableReadBookShelf> results = mRealm.where(TableReadBookShelf.class)
                .equalTo("novelurl", bean.novelinfodata.novel_url).findAll();
        int resultCount = results.size();
        for (int count = 0; count < resultCount; ++count) {
            TableReadBookShelf obj = results.get(count);
            if (obj.chaptersnum != bean.getChapterItems().size()){
                obj.isUpdate = true;
                if(bean.novelinfodata.chapteritems.size()>0){
                    obj.lastestchapterno = bean.novelinfodata.chapteritems.get(bean.novelinfodata.chapteritems.size()-1).chapter_no;
                    obj.lastestchaptertime = bean.novelinfodata.chapteritems.get(bean.novelinfodata.chapteritems.size()-1).chapter_time;
                    obj.lastestchaptertitle = bean.novelinfodata.chapteritems.get(bean.novelinfodata.chapteritems.size()-1).chapter_title;
                    obj.lastestchapterurl = bean.novelinfodata.chapteritems.get(bean.novelinfodata.chapteritems.size()-1).chapter_url;
                    obj.chaptersnum = bean.novelinfodata.chapteritems.size();
                    obj.time = bean.novelinfodata.chapteritems.get(bean.novelinfodata.chapteritems.size()-1).chapter_time;
                }
            }
        }
        mRealm.commitTransaction();
    }
    public static  void removeIsUpdate(NovelInfoBean mNovelBean){
        Realm mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();
        RealmResults<TableReadBookShelf> results = mRealm.where(TableReadBookShelf.class)
                .equalTo("novelurl", mNovelBean.novelinfodata.novel_url).findAll();
        int resultCount = results.size();
        for (int count = 0; count < resultCount; ++count) {
            TableReadBookShelf obj = results.get(count);
            obj.isUpdate = false;
        }
        mRealm.commitTransaction();
    }
}
