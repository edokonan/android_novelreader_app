package com.zuk.ireader.ui.adapter;

import com.zuk.ireader.model.bean.HotCommentBean;
import com.zuk.ireader.ui.adapter.view.HotCommentHolder;
import com.zuk.ireader.ui.base.adapter.BaseListAdapter;
import com.zuk.ireader.ui.base.adapter.IViewHolder;

/**
 * Created by newbiechen on 17-5-4.
 */

public class HotCommentAdapter extends BaseListAdapter<HotCommentBean>{
    @Override
    protected IViewHolder<HotCommentBean> createViewHolder(int viewType) {
        return new HotCommentHolder();
    }
}
