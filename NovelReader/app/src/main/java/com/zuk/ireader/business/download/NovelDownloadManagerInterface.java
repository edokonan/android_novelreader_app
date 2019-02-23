package com.zuk.ireader.business.download;

import com.zuk.ireader.model.bean.novel.NovelInfoChapterdata;
import com.zuk.ireader.model.bean.novel.NovelInfoBean;

/**
 * Created by ksymac on 2018/12/25.
 */

public interface NovelDownloadManagerInterface {
    void downloadStart(boolean openTxtReader);
    void downloadOk(String filepath,
                    NovelInfoBean mNovelBean,
                    NovelInfoChapterdata mChapterBean,
                    String chapterurl,
                    int pageno,
                    boolean openTxtReader);
    void downloadFail();
}