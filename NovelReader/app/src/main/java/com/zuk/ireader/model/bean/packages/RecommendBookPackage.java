package com.zuk.ireader.model.bean.packages;

import com.zuk.ireader.model.bean.BaseBean;
import com.zuk.ireader.model.bean.CollBookBean;

import java.util.List;

/**
 * Created by newbiechen on 17-5-8.
 */

public class RecommendBookPackage extends BaseBean {

    private List<CollBookBean> books;

    public List<CollBookBean> getBooks() {
        return books;
    }

    public void setBooks(List<CollBookBean> books) {
        this.books = books;
    }
}
