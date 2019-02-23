package com.zuk.ireader.business.config;

import android.content.Context;

import com.bifan.txtreaderlib.main.TxtConfig;
import com.zuk.ireader.App;
import com.zuk.ireader.model.bean.novel.NovelInfoChapterdata;
import com.zuk.ireader.model.bean.novel.NovelInfoBean;
import com.zuk.ireader.business.config.ComCode;
import com.zuk.ireader.realm.DBManagerBookChapter;
import com.zuk.ireader.realm.DBManagerReadBookShelf;
import com.zuk.ireader.realm.DBManagerReadHistory;
import com.zuk.ireader.realm.TableNovelChapter;
import com.zuk.ireader.realm.TableReadHistory;
import com.zuk.ireader.ui.activity.web.NovelWebPagesActivity;
import com.zuk.ireader.ui.activity.web.NovelWebSinglePageActivity;
import com.zuk.ireader.ui.reader.TxtPlayActivity;

/**
 * Created by ksymac on 2018/12/31.
 */

public class ReaderCom {
    //从History中获取数据
    public static TableNovelChapter getLastReadFromHistory(String novelurl){
        TableReadHistory history = DBManagerReadHistory.getHistoryWithNovelUrl(novelurl);
        if(history != null){
            TableNovelChapter tbchapter = DBManagerBookChapter.getChapterInfo(history.chapterurl,history.pageno);
            return tbchapter;
        }else{
            return null;
        }
    }

//    public static void addBookShelf(NovelInfoBean mNovelBean){
//        DBManagerReadBookShelf.addToBookShelf(getApplicationContext(),mNovelBean);
//    }



    public static void openWebReader(NovelInfoBean mNovelBean,
                                    NovelInfoChapterdata mChapterBean){
        DBManagerReadBookShelf.updateChapterToBookShelf(mNovelBean, mChapterBean, mChapterBean.chapter_pagestart);
        DBManagerReadHistory.insertHistory(mNovelBean,
                mChapterBean,
                mChapterBean.chapter_pagestart);
        ComRate.addEvent();
        ComAnalytics.addEventLog(mNovelBean.novelinfodata.siteno,
                mNovelBean.novelinfodata.novel_original_no,mNovelBean.novelinfodata.novel_url,
                mNovelBean.novelinfodata.novel_title);

        Context context = App.sInstance;
        NovelWebPagesActivity.StartActivity(context, mNovelBean, mChapterBean);
    }

    public static void openSingleHtmlReader(String filepath,
                                      NovelInfoBean mNovelBean,
                                      NovelInfoChapterdata mChapterBean,
                                      int pageno){
        DBManagerReadBookShelf.updateChapterToBookShelf(mNovelBean, mChapterBean, pageno);
        DBManagerReadHistory.insertHistory(mNovelBean, mChapterBean,pageno);
        ComRate.addEvent();
        ComAnalytics.addEventLog(mNovelBean.novelinfodata.siteno,
                mNovelBean.novelinfodata.novel_original_no,
                mNovelBean.novelinfodata.novel_url,
                mNovelBean.novelinfodata.novel_title);


        Context context = App.sInstance;
        NovelWebSinglePageActivity.StartActivity(context,
                mNovelBean.novelinfodata.novel_url,
                mChapterBean.chapter_url,
                mNovelBean, mChapterBean, pageno);
    }
    public static void openTxtReader(String filepath,
                                     NovelInfoBean mNovelBean, NovelInfoChapterdata mChapterBean,
                                     int pageno,
                                    boolean progressAtZero){
//        DBManagerBookChapter.insertChapterInfo(mNovelBean,mChapterBean);
//        DBManagerBookChapter.updateTxtFilepath(chapterurl,filepath);
        DBManagerReadBookShelf.updateChapterToBookShelf(mNovelBean,mChapterBean,pageno);
        DBManagerReadHistory.insertHistory(mNovelBean, mChapterBean,pageno);
        ComRate.addEvent();
        ComAnalytics.addEventLog(mNovelBean.novelinfodata.siteno,
                mNovelBean.novelinfodata.novel_original_no,mNovelBean.novelinfodata.novel_url,
                mNovelBean.novelinfodata.novel_title);


        Context context = App.sInstance;
        TxtConfig.saveIsOnVerticalPageMode(context,false);
        TxtPlayActivity.StartActivity(context, filepath,null,
                mNovelBean.novelinfodata.novel_url, mChapterBean.chapter_url,
                mNovelBean, mChapterBean, progressAtZero);
    }

    public static  NovelInfoChapterdata getPreChapterPage(NovelInfoBean mNovelBean, NovelInfoChapterdata mChapterBean,int pageno){
        int prepageno = pageno-1;

        if(ComCode.isMultWebPages(mNovelBean.novelinfodata.siteno)){
            if(prepageno<= mChapterBean.chapter_pageend && prepageno >= mChapterBean.chapter_pagestart){
                mChapterBean.currentpageno = prepageno;
                return mChapterBean;
            }
        }

        NovelInfoChapterdata retitem = null;
        for(NovelInfoChapterdata item : mNovelBean.getChapterItems()){
            if(item.no < mChapterBean.no && item.chapter_url!=null && item.chapter_url.length()>0){
                item.currentpageno = item.chapter_pagestart;
                retitem = item;
            }
        }
        return retitem;
    }
    public static NovelInfoChapterdata getNextChapterPage(NovelInfoBean mNovelBean,
                                                      NovelInfoChapterdata mChapterBean,
                                                      int pageno){
        int nextpageno = pageno+1;
        if(ComCode.isMultWebPages(mNovelBean.novelinfodata.siteno)){
            if(nextpageno<= mChapterBean.chapter_pageend && nextpageno >= mChapterBean.chapter_pagestart){
                mChapterBean.currentpageno = nextpageno;
                return mChapterBean;
            }
        }

        for(NovelInfoChapterdata item : mNovelBean.getChapterItems()){
            if(item.no > mChapterBean.no && item.chapter_url!=null && item.chapter_url.length()>0){
                item.currentpageno = item.chapter_pagestart;
                return item;
            }
        }
        return null;
    }
}
