package com.zuk.ireader.ui.adapter.novel;

import android.content.Context;

import com.zuk.ireader.model.bean.novel.NovelGenreDataBookData;
import com.zuk.ireader.ui.base.adapter.IViewHolder;
import com.zuk.ireader.widget.adapter.WholeAdapter;

/**
 * Created by ksymac on 2018/12/1.
 */
public class NovelListAdapter extends WholeAdapter<NovelGenreDataBookData> {
    public NovelListAdapter() {
    }

    public NovelListAdapter(Context context, Options options) {
        super(context, options);
    }

    @Override
    protected IViewHolder<NovelGenreDataBookData> createViewHolder(int viewType) {
        return new NovelBookListHolder();
    }
}
