package com.zuk.ireader.ui.adapter.novel;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zuk.ireader.R;
import com.zuk.ireader.model.bean.novel.NovelInfoChapterdata;
import com.zuk.ireader.business.config.ComCode;
import com.zuk.ireader.realm.DBManagerBookChapter;
import com.zuk.ireader.realm.DBManagerReadHistory;
import com.zuk.ireader.ui.base.adapter.ViewHolderImpl;

/**
 * Created by ksymac on 2018/12/9.
 */

public class NovelChapterItemHolder extends ViewHolderImpl<NovelInfoChapterdata> {

    private RelativeLayout mContainerView;
    private ImageView mIvPortrait;
    private TextView mTvNo;
    private TextView mTvTitle;
    private TextView mTvTime;

    @Override
    public void initView() {
        mContainerView = findById(R.id.container_view);
        mIvPortrait = findById(R.id.item_chapter_image);
        mTvNo = findById(R.id.item_chapter_no);
        mTvTitle = findById(R.id.item_chapter_title);
        mTvTime = findById(R.id.item_chapter_time);
    }

    @Override
    public void onBind(NovelInfoChapterdata value, int pos) {
        if (value.chapter_url != null && value.chapter_url.length() >0){
            mTvNo.setVisibility(View.VISIBLE);
            mTvTime.setVisibility(View.VISIBLE);
//            mTvNo.setText(value.chapter_no);
            mTvNo.setText(String.valueOf(value.no));
            if(ComCode.isMultWebPages(value.siteno)){
                String pagestr = String.format(" %d ~ %d ",value.chapter_pagestart,value.chapter_pageend);
                mTvNo.setText(pagestr);
            }

            mTvTitle.setText(value.chapter_title);
            mTvTime.setText(value.chapter_time);

            if(DBManagerBookChapter.haveTxtFile(value.chapter_url,value.chapter_pagestart)){
                mIvPortrait.setImageResource(R.drawable.ic_document);
            }else{
                mIvPortrait.setImageResource(R.drawable.ic_document_white);
            }
            mIvPortrait.setVisibility(View.VISIBLE);
            if(DBManagerReadHistory.haveHistory(value.chapter_url)){
                this.mContainerView.setBackgroundColor(Color.rgb(240, 240, 240));
            }else{
                this.mContainerView.setBackgroundColor(Color.WHITE);
            }
        }else{
            mTvTitle.setText(value.chapter_title);
            mTvNo.setVisibility(View.GONE);
            mTvTime.setVisibility(View.GONE);
            mIvPortrait.setVisibility(View.GONE);
            mContainerView.setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_chapter;
    }
}
