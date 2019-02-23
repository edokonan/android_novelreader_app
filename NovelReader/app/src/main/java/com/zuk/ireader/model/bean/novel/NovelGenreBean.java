package com.zuk.ireader.model.bean.novel;

import java.util.List;

/**
 * Created by ksymac on 2018/12/10.
 */

public class NovelGenreBean {
    public boolean err;
    public NovelGenreData genredata;
    public List<NovelGenreDataBookData> getBookLists() {
        return genredata.itemlist;
    }
}
