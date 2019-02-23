package com.zuk.ireader.realm;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by ksymac on 2019/1/14.
 */
public class TableNovelInfo extends RealmObject {
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

    public Date updatetime;
}