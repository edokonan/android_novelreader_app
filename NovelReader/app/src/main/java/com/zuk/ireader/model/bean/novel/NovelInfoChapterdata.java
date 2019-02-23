package com.zuk.ireader.model.bean.novel;

/**
 * Created by ksymac on 2018/12/9.
 */

public class NovelInfoChapterdata {
    public String siteno;
    public int no;
    public String chapter_no;
    public String chapter_title;
    public String chapter_time;
    public String chapter_url;
    public String chapter_otherinfo = "";
    public int chapter_pagestart;
    public int chapter_pageend;

    public int currentpageno;

    public int getPageNo(){
        if(currentpageno>=chapter_pagestart && currentpageno<=chapter_pageend){
            return currentpageno;
        }else{
            return chapter_pagestart;
        }
    }
}

