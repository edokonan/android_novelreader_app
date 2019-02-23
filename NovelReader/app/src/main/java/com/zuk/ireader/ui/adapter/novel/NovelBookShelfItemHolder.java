package com.zuk.ireader.ui.adapter.novel;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zuk.ireader.R;
import com.zuk.ireader.business.config.ComCode;
import com.zuk.ireader.realm.TableReadBookShelf;
import com.zuk.ireader.ui.base.adapter.ViewHolderImpl;

/**
 * Created by newbiechen on 17-5-8.
 * CollectionBookView
 */

public class NovelBookShelfItemHolder extends ViewHolderImpl<TableReadBookShelf>{

    private static final String TAG = "CollBookView";
    private ImageView mIvCover;
    private TextView mTvName;
    private TextView mTvChapter;
    private TextView mTvTime;
    private CheckBox mCbSelected;
    private ImageView mIvRedDot;
    private ImageView mIvTop;


    @Override
    public void initView() {
        mIvCover = findById(R.id.coll_book_iv_cover);
        mTvName = findById(R.id.coll_book_tv_name);
        mTvChapter = findById(R.id.coll_book_tv_chapter);
        mTvTime = findById(R.id.coll_book_tv_lately_update);
        mCbSelected = findById(R.id.coll_book_cb_selected);
        mIvRedDot = findById(R.id.coll_book_iv_red_rot);
        mIvTop = findById(R.id.coll_book_iv_top);
    }

    @Override
    public void onBind(TableReadBookShelf value, int pos) {
//        if (value.isLocal()){
            //本地文件的图片

            if(value.novelimgurl != null && value.novelimgurl.length() > 0){
                Glide.with(getContext())
                        .load(value.novelimgurl)
                        .placeholder(R.drawable.icons8_book_80)
                        .fitCenter()
                        .into(mIvCover);
            }else{
                Glide.with(getContext())
                        .load(R.drawable.icons8_book_80)
                        .fitCenter()
                        .into(mIvCover);
            }
//        }
//        else {
//            //书的图片
//            Glide.with(getContext())
//                    .load(Constant.IMG_BASE_URL+value.getCover())
//                    .placeholder(R.drawable.ic_book_loading)
//                    .error(R.drawable.ic_load_error)
//                    .fitCenter()
//                    .into(mIvCover);
//        }
        //书名
        mTvName.setText(value.title);
//        if(value.summary!=null){
//            mTvChapter.setText(value.summary);
//            mTvChapter.setVisibility(View.VISIBLE);
//        }else{
            mTvChapter.setVisibility(View.GONE);
//        }

        String tempstr = "";

        if (value.siteno!=null){
            if(value.siteno.equalsIgnoreCase(ComCode.siteno_alphapolis)){
                tempstr = " "+ComCode.sitenoname_alphapolis;
            }else if(value.siteno.equalsIgnoreCase(ComCode.siteno_ncode_syosetu)){
                tempstr = " "+ComCode.sitenoname_ncode_syosetu;
            }else if(value.siteno.equalsIgnoreCase(ComCode.siteno_novel18_syosetu)){
                tempstr = " "+ComCode.sitenoname_novel18_syosetu;
            }else if(value.siteno.equalsIgnoreCase(ComCode.siteno_estar) ||
                    value.siteno.equalsIgnoreCase(ComCode.siteno_estar_r) ){
                tempstr = " "+ComCode.sitenoname_estar;
            }else if(value.siteno.equalsIgnoreCase(ComCode.siteno_otona_novel)  ){
                tempstr = " "+ComCode.sitenoname_otona_novel;
            }else if(value.siteno.equalsIgnoreCase(ComCode.siteno_maho)  ){
                tempstr = " "+ComCode.sitenoname_maho;
            }else if(value.siteno.equalsIgnoreCase(ComCode.siteno_noichigo)  ){
                tempstr = " "+ComCode.sitenoname_noichigo;
            }else if(value.siteno.equalsIgnoreCase(ComCode.siteno_berryscafe)  ){
                tempstr = " "+ComCode.sitenoname_berryscafe;
            }else if(value.siteno.equalsIgnoreCase(ComCode.siteno_kakuyomu)  ){
                tempstr = " "+ComCode.sitenoname_kakuyomu;
            }else if(value.siteno.equalsIgnoreCase(ComCode.siteno_aozora)  ){
                tempstr = " "+ComCode.sitenoname_aozora;
            }
        }
        tempstr += " 最新: ";
        tempstr += value.lastestchaptertitle + " " + value.lastestchaptertime;
        mTvTime.setText(tempstr);
        mTvTime.setVisibility(View.VISIBLE);

//        //我的想法是，在Collection中加一个字段，当追更的时候设置为true。当点击的时候设置为false。
//        //当更新的时候，最新数据跟旧数据进行比较，如果更新的话，设置为true。
        if (value.isUpdate ==true){
            mIvRedDot.setVisibility(View.VISIBLE);
        }else {
            mIvRedDot.setVisibility(View.GONE);
        }
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_coll_book;
    }
}
