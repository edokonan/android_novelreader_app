package com.zuk.ireader.realm;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.RealmList;
import io.realm.annotations.Ignore;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ksymac on 2018/12/15.
 */

public class TableReadBookShelf extends RealmObject {
    public String novelurl;
    public String novelimgurl;

    public String chapterurl;//read
    public String chaptertitle;//read
    public String chaptertime;//read
    public String chapterno;//read
    public int pageno;//read

    public int chaptersnum;

    public String lastestchapterno;
    public String lastestchapterurl;
    public String lastestchaptertitle;
    public String lastestchaptertime;

    public String siteno;
    public String title;
    public String author;
//    public String author_link;
    public String summary;
    public String time;
    public String novel_original_no;//所在网站的no

    public boolean isUpdate;

    public String original_no;//自己的跟踪方式
    public String booklist_no;//自己的收藏方式

    public String ver;
    public String bak1;
    public String bak2;
//    public String link;
//    public String NovelGenreDataBookDataJson;
    public Date updatetime;

    // You can instruct Realm to ignore a field and not persist it.
    @Ignore
    public int tempReference;
}