package com.zuk.ireader.realm;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by ksymac on 2018/12/23.
 */
public class TableReadHistory  extends RealmObject {
    public String novelurl;
    public String chapterurl;
    public int pageno;
    public String type;
    public Date updatetime;


    //ver 1 to 2
    public String siteno;
    public String title;
    public String author;
    public String summary;
    public String time;
    public String novel_original_no;//所在网站的no

    public String novelimgurl;
    public String chaptertitle;//read
    public String chaptertime;//read
    public String chapterno;//read



}
