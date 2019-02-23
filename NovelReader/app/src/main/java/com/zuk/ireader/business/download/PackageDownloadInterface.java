package com.zuk.ireader.business.download;

import com.zuk.ireader.model.bean.novel.NovelInfoBean;
import com.zuk.ireader.model.bean.novel.NovelInfoChapterdata;

/**
 * Created by ksymac on 2019/1/16.
 */

public interface PackageDownloadInterface {
    void PackageDownloadOk();
    void PackageDownRefreshTable(int pos);
    void PackageDownRefreshMsg(int currentpage, int totalpage);
    void PackageDownloadFail();
    void PackageDwonloadStoped();
}
