package com.zuk.ireader.ui.adapter;

import com.zuk.ireader.model.bean.CollBookBean;
import com.zuk.ireader.ui.adapter.view.CollBookHolder;
import com.zuk.ireader.ui.base.adapter.IViewHolder;
import com.zuk.ireader.widget.adapter.WholeAdapter;

/**
 * Created by newbiechen on 17-5-8.
 */

public class CollBookAdapter extends WholeAdapter<CollBookBean> {

    @Override
    protected IViewHolder<CollBookBean> createViewHolder(int viewType) {
        return new CollBookHolder();
    }

}
