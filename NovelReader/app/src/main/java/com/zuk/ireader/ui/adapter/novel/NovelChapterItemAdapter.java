package com.zuk.ireader.ui.adapter.novel;

import com.zuk.ireader.model.bean.novel.NovelInfoChapterdata;
import com.zuk.ireader.ui.base.adapter.BaseListAdapter;
import com.zuk.ireader.ui.base.adapter.IViewHolder;

/**
 * Created by ksymac on 2018/12/9.
 */
public class NovelChapterItemAdapter extends BaseListAdapter<NovelInfoChapterdata> {
    @Override
    protected IViewHolder<NovelInfoChapterdata> createViewHolder(int viewType) {
        return new NovelChapterItemHolder();
    }
}
