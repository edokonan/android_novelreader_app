package com.zuk.ireader.realm;

import java.util.Date;
import io.realm.RealmObject;

/**
 * Created by ksymac on 2018/12/23.
 */
public class TableNovelChapter extends RealmObject {
    public String novelurl;
    public String chapterurl;
    public int pageno;

    public String siteno;

    public int no;
    public String chapterno;
    public String chaptertitle;
    public String chaptertime;
    public String chapterother;
    public int chapterpagestart;
    public int chapterpageend;

    public String pageurl;
    public String txtfilepath;
    public String strhtml;

    public String dochtml;
    public String pdffilepath;
    public String epubfilepath;
    public String readertype;

    public String ver;
    public String bak1;
    public String bak2;

    public Date updatetime;
    //ver. 2
    //ver. 3
//    //ver. 4
//    public int testno;
}