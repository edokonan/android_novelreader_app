package com.zuk.ireader.model.bean.novel;

import java.util.List;

/**
 * Created by ksymac on 2018/12/9.
 */

public class NovelInfoBean {
    public Boolean err;
    public String errmsg;
    public String link;
    public NovelInfoData novelinfodata;
    public List<NovelInfoChapterdata> getChapterItems() {
        return novelinfodata.chapteritems;
    }
}
