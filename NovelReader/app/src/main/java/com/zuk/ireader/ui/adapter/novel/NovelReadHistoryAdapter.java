package com.zuk.ireader.ui.adapter.novel;

import com.zuk.ireader.realm.TableReadBookShelf;
import com.zuk.ireader.realm.TableReadHistory;
import com.zuk.ireader.ui.base.adapter.IViewHolder;
import com.zuk.ireader.widget.adapter.WholeAdapter;

/**
 * Created by newbiechen on 17-5-8.
 */

public class NovelReadHistoryAdapter extends WholeAdapter<TableReadHistory> {

    @Override
    protected IViewHolder<TableReadHistory> createViewHolder(int viewType) {
        return new NovelReadHistoryItemHolder();
    }

}
