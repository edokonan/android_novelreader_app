package com.zuk.ireader.ui.fragment.novel;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.zuk.ireader.R;
import com.zuk.ireader.business.config.NovelConfigRankingClass;
import com.zuk.ireader.business.firebase.ComHomeData;
import com.zuk.ireader.model.bean.novel.NovelGenreBean;
import com.zuk.ireader.model.bean.novel.NovelGenreDataBookData;
import com.zuk.ireader.presenter.novel.BookListContractNovel;
import com.zuk.ireader.presenter.novel.BookListPresenterNovel;
import com.zuk.ireader.ui.activity.novel.NovelBookDetailActivity;
import com.zuk.ireader.ui.adapter.novel.NovelListAdapter;
import com.zuk.ireader.ui.base.BaseMVPFragment;
import com.zuk.ireader.utils.ToastUtils;
import com.zuk.ireader.widget.RefreshLayout;
import com.zuk.ireader.widget.itemdecoration.DividerItemDecoration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import butterknife.BindView;

/**
 * Created by newbiechen on 17-5-3.
 */

public class NovelSiteDataFragment extends BaseMVPFragment<BookListContractNovel.Presenter>
        implements BookListContractNovel.View{

    private static final String EXTRA_BILL_ID = "extra_bill_id";

    /********************/
    @BindView(R.id.refresh_layout)
    RefreshLayout mRefreshLayout;
    @BindView(R.id.refresh_rv_content)
    RecyclerView mRvContent;
    /*******************/
    private NovelListAdapter mBillBookAdapter;
    /*****************/
    private int mSearchId=0;

    ComHomeData homeapi;

    public static Fragment newInstance(int searchId){
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_BILL_ID,searchId);
        Fragment fragment = new NovelSiteDataFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getContentId() {
        return R.layout.fragment_refresh_list;
    }

    @Override
    protected BookListContractNovel.Presenter bindPresenter() {
        return new BookListPresenterNovel();
    }
    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (savedInstanceState != null){
            mSearchId = savedInstanceState.getInt(EXTRA_BILL_ID);
        }else {
            mSearchId = getArguments().getInt(EXTRA_BILL_ID);
        }
    }

    @Override
    protected void initClick() {
        super.initClick();
        mBillBookAdapter.setOnItemClickListener(
                (view, pos) -> {
                    NovelGenreDataBookData bookdata = mBillBookAdapter.getItem(pos);
                    String bookid = "5913f112e0ea5a5e7444fa7b";
                    NovelBookDetailActivity.startActivity(this.getActivity(), bookid, bookdata.link);
                }
        );
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        super.initWidget(savedInstanceState);
        setUpAdapter();
    }

    @Override
    protected void processLogic() {
        super.processLogic();
        mRefreshLayout.showLoading();
        NovelConfigRankingClass rangkinglist = new NovelConfigRankingClass();
        NovelConfigRankingClass.NovelConfigRankingItem item = rangkinglist.getItem(this.mSearchId );
//        //load data with json
//        loadDataWithJsonString(json);
        //get json from gae firestore
        homeapi = new ComHomeData();
        homeapi.getHomeRankingData(this.getActivity(),this, item.id);
//        homeapi.getHomeRankingDataFromGaeFireStore(this.getActivity(),this, item.id);
    }

    public void showInfo(){
//        ToastUtils.show(String.format("Data Loading  ..."));
    }
    //load data with json
    public void loadDataWithJsonString(String json) {
        NovelGenreBean bean = homeapi.JsonToObject(json, NovelGenreBean.class);
        if(bean!=null){
            complete();
            finishRefresh(bean.getBookLists());
        }
    }
    private void setUpAdapter(){
        mRvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvContent.addItemDecoration(new DividerItemDecoration(getContext()));
        mBillBookAdapter = new NovelListAdapter();
        mRvContent.setAdapter(mBillBookAdapter);
    }

    @Override
    public void finishRefresh(List<NovelGenreDataBookData> beans){
        mRefreshLayout.showFinish();
        mBillBookAdapter.refreshItems(beans);
    }
    @Override
    public void finishLoading(List<NovelGenreDataBookData> beans) {
        mBillBookAdapter.addItems(beans);
    }

    @Override
    public void showLoadError() {
        mRefreshLayout.showError();
    }

    @Override
    public void showError() {
        mRefreshLayout.showError();
    }

    @Override
    public void complete() {
        mRefreshLayout.showFinish();
    }
}
