package com.zuk.ireader.ui.adapter;

import com.zuk.ireader.model.bean.SectionBean;
import com.zuk.ireader.ui.adapter.view.SectionHolder;
import com.zuk.ireader.ui.base.adapter.BaseListAdapter;
import com.zuk.ireader.ui.base.adapter.IViewHolder;

/**
 * Created by newbiechen on 17-4-16.
 */

public class SectionAdapter extends BaseListAdapter<SectionBean> {
    @Override
    protected IViewHolder<SectionBean> createViewHolder(int viewType) {
        return new SectionHolder();
    }
}
