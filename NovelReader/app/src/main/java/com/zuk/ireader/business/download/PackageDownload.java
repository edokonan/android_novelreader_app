package com.zuk.ireader.business.download;

import android.app.Activity;

import com.zuk.ireader.business.config.ComCode;
import com.zuk.ireader.business.config.ReaderCom;
import com.zuk.ireader.model.bean.novel.NovelInfoBean;
import com.zuk.ireader.model.bean.novel.NovelInfoChapterdata;

/**
 * Created by ksymac on 2019/1/16.
 */

public class PackageDownload implements NovelDownloadManagerInterface {
    public NovelInfoBean mNovelBean;
    public Activity mActivity;
    PackageDownloadInterface mInterface;

    public boolean stopflg = false;

    //
    public NovelInfoChapterdata mCurrentChapterBean;
    public int mCurrentPageno;


    public PackageDownload(Activity activity, PackageDownloadInterface imp, NovelInfoBean novelBean){
        super();
        this.mActivity = activity;
        this.mNovelBean = novelBean;
        this.mInterface = imp;
        mCurrentChapterBean = null;
        mCurrentPageno = 0;
        setFirstDownloadChapter();
    }
    //
    public void packagedownloadStart(){
        if(mCurrentChapterBean== null){
            mInterface.PackageDownloadOk();
            return;
        }
        if(ComCode.isMultWebPages(mNovelBean.novelinfodata.siteno)){
            mInterface.PackageDownRefreshMsg(mCurrentPageno, mNovelBean.novelinfodata.novel_pagenum);
        }else{
            mInterface.PackageDownRefreshMsg(mCurrentChapterBean.no, mNovelBean.novelinfodata.chapteritems.size());
        }
        NovelDownloadManager mDownloadManager =
                new NovelDownloadManager(mActivity,
                        mNovelBean,
                        mCurrentChapterBean,
                        this,
                        false,
                        mCurrentPageno);
        mDownloadManager.DownloadStart();
    }
    //找到开始下载的地方
    void setFirstDownloadChapter(){
        for(NovelInfoChapterdata item : mNovelBean.getChapterItems()){
            if(item.chapter_url!=null && item.chapter_url.length()>0){
                item.currentpageno = item.chapter_pagestart;
                mCurrentChapterBean = item;
                mCurrentPageno = item.chapter_pagestart;
                return;
            }
        }
        mCurrentChapterBean = null;
    }
    // 下载下一页
    private void downloadNextPage(){
        if(stopflg){
            if(mInterface!=null){
                mInterface.PackageDwonloadStoped();
            }
            return ;
        }
        NovelInfoChapterdata item = ReaderCom.getNextChapterPage(this.mNovelBean,mCurrentChapterBean, mCurrentPageno);
        if(item!=null ){
            mCurrentChapterBean = item;
            mCurrentPageno = item.currentpageno;
            packagedownloadStart();
        }else{
            mInterface.PackageDownloadOk();
        }
    }
    //下载的接口
    public void downloadStart(boolean openTxtReader){
    }
    public void downloadOk(String filepath,
                    NovelInfoBean mNovelBean,
                    NovelInfoChapterdata mChapterBean,
                    String chapterurl,
                    int pageno,
                    boolean openTxtReader){
        mInterface.PackageDownRefreshTable(mChapterBean.no);
        downloadNextPage();
    }
    public void downloadFail(){
        mInterface.PackageDownloadFail();
    }
}
