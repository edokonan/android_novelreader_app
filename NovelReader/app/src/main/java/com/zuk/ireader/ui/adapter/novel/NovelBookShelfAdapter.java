package com.zuk.ireader.ui.adapter.novel;

import com.zuk.ireader.realm.TableReadBookShelf;
import com.zuk.ireader.ui.base.adapter.IViewHolder;
import com.zuk.ireader.widget.adapter.WholeAdapter;

/**
 * Created by newbiechen on 17-5-8.
 */

public class NovelBookShelfAdapter extends WholeAdapter<TableReadBookShelf> {

    @Override
    protected IViewHolder<TableReadBookShelf> createViewHolder(int viewType) {
        return new NovelBookShelfItemHolder();
    }

}
