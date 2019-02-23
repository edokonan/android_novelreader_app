package com.zuk.ireader.model.bean.novel;

import java.util.List;

/**
 * Created by ksymac on 2019/1/5.
 */

public class NovelInfoData {
    public Boolean err;
    public String errmsg;

    public String siteno;
    public String sitename;

    public String novel_url;
    public String novel_imgurl;
    public String novel_title;
    public String novel_writername;
    public String novel_writername_link;
    public String novel_ex;
    public String novel_original_no;
    public String novel_labelinfo;

    public String novel_tags;
    public String novel_keywords;
    public String novel_lastupdatetime;

    public int novel_pagenum;

//    public String ncode_syosetu_txtdownloadlink;
    public List<NovelInfoChapterdata> chapteritems;


    public String getLastUpdateTime(){
        if(chapteritems!=null && chapteritems.size()>0){
            return chapteritems.get(chapteritems.size()-1).chapter_time;
        }else{
            return "";
        }
    }
}
