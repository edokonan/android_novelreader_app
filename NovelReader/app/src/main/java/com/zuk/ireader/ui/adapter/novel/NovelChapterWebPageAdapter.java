package com.zuk.ireader.ui.adapter.novel;

import com.zuk.ireader.model.bean.novel.NovelChapterPageForWeb;
import com.zuk.ireader.model.bean.novel.NovelInfoChapterdata;
import com.zuk.ireader.realm.TableNovelChapter;
import com.zuk.ireader.ui.base.adapter.BaseListAdapter;
import com.zuk.ireader.ui.base.adapter.IViewHolder;

/**
 * Created by ksymac on 2018/12/9.
 */
public class NovelChapterWebPageAdapter extends BaseListAdapter<NovelChapterPageForWeb> {
    @Override
    protected IViewHolder<NovelChapterPageForWeb> createViewHolder(int viewType) {
        return new NovelChapterWebPageHolder();
    }
}
