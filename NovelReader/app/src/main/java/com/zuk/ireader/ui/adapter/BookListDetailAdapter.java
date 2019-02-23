package com.zuk.ireader.ui.adapter;

import android.content.Context;

import com.zuk.ireader.model.bean.BookListDetailBean;
import com.zuk.ireader.ui.adapter.view.BookListInfoHolder;
import com.zuk.ireader.ui.base.adapter.IViewHolder;
import com.zuk.ireader.widget.adapter.WholeAdapter;

/**
 * Created by newbiechen on 17-5-2.
 */

public class BookListDetailAdapter extends WholeAdapter<BookListDetailBean.BooksBean.BookBean> {
    public BookListDetailAdapter(Context context, Options options) {
        super(context, options);
    }

    @Override
    protected IViewHolder<BookListDetailBean.BooksBean.BookBean> createViewHolder(int viewType) {
        return new BookListInfoHolder();
    }
}
