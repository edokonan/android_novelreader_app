package com.zuk.ireader.ui.adapter;

import com.zuk.ireader.model.bean.DownloadTaskBean;
import com.zuk.ireader.ui.adapter.view.DownloadHolder;
import com.zuk.ireader.ui.base.adapter.BaseListAdapter;
import com.zuk.ireader.ui.base.adapter.IViewHolder;

/**
 * Created by newbiechen on 17-5-12.
 */

public class DownLoadAdapter extends BaseListAdapter<DownloadTaskBean>{

    @Override
    protected IViewHolder<DownloadTaskBean> createViewHolder(int viewType) {
        return new DownloadHolder();
    }
}
