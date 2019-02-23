package com.zuk.ireader.ui.adapter.novel;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zuk.ireader.R;
import com.zuk.ireader.business.config.ComCode;
import com.zuk.ireader.model.bean.novel.NovelGenreDataBookData;
import com.zuk.ireader.ui.base.adapter.ViewHolderImpl;

/**
 * Created by ksymac on 2018/12/1.
 */

public class NovelBookListHolder extends ViewHolderImpl<NovelGenreDataBookData> {

    private ImageView mIvPortrait;
    private TextView mTvTitle;
    private TextView mTvAuthor;
    private TextView mTvBrief;
    private TextView mTvMsg;

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_book_brief;
    }

    @Override
    public void initView() {
        mIvPortrait = findById(R.id.book_brief_iv_portrait);
        mTvTitle = findById(R.id.book_brief_tv_title);
        mTvAuthor = findById(R.id.book_brief_tv_author);
        mTvBrief = findById(R.id.book_brief_tv_brief);
        mTvMsg = findById(R.id.book_brief_tv_msg);
    }

    @Override
    public void onBind(NovelGenreDataBookData value, int pos) {
        int imgresourceid = R.drawable.icons8_bookcover_default;
        if (value.siteno.equals(ComCode.siteno_berryscafe) ||
                value.siteno.equals(ComCode.siteno_estar)  ||
                value.siteno.equals(ComCode.siteno_noichigo)  ||
                value.siteno.equals(ComCode.siteno_maho)  ) {
            imgresourceid =  R.drawable.icons8_book_pink;
        }

        if (value.imglink != null && value.imglink.length() >0){
            Glide.with(getContext())
                    .load(value.imglink)
                    .placeholder(imgresourceid)
                    .error(imgresourceid)
                    .fitCenter()
                    .into(mIvPortrait);
        }else{
            mIvPortrait.setImageResource(imgresourceid);
        }
        //书单名
        mTvTitle.setText(value.title);
        //作者
        mTvAuthor.setText(value.author);
        //简介
        mTvBrief.setText(value.summary);
        if (value.siteno.equals(ComCode.siteno_estar)){
            mTvMsg.setText(value.lableinfo + " " + value.time);
        }else{
            mTvMsg.setText(value.time);
        }
        //信息
    }
}