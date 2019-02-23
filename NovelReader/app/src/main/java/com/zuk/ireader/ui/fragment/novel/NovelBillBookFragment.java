package com.zuk.ireader.ui.fragment.novel;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zuk.ireader.R;
import com.zuk.ireader.model.bean.novel.NovelGenreDataBookData;
import com.zuk.ireader.presenter.novel.BookListContractNovel;
import com.zuk.ireader.presenter.novel.BookListPresenterNovel;
import com.zuk.ireader.business.config.ComCode;
import com.zuk.ireader.ui.activity.novel.NovelBookDetailActivity;
import com.zuk.ireader.ui.adapter.novel.NovelListAdapter;
import com.zuk.ireader.ui.base.BaseMVPFragment;
import com.zuk.ireader.widget.RefreshLayout;
import com.zuk.ireader.widget.itemdecoration.DividerItemDecoration;

import java.util.List;

import butterknife.BindView;

/**
 * Created by newbiechen on 17-5-3.
 */

public class NovelBillBookFragment extends BaseMVPFragment<BookListContractNovel.Presenter>
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
//    private String mBillId;

    public static Fragment newInstance(String billId){
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_BILL_ID,billId);
        Fragment fragment = new NovelBillBookFragment();
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
//        mBillId = getArguments().getString(EXTRA_BILL_ID);
    }

    @Override
    protected void initClick() {
        super.initClick();
        mBillBookAdapter.setOnItemClickListener(
                (view, pos) -> {
//                    String bookId = mBillBookAdapter.getItem(pos).get_id();
//                    BookDetailActivity.startActivity(getContext(),bookId);
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
        mPresenter.refreshBookList(ComCode.siteno_ncode_syosetu, "/rank/list/type/total_total/");
    }
//    NovelGenreConfigModel.init(title: "累計ランキングBEST300", c: "/rank/list/type/total_total/"),


    private void setUpAdapter(){
        mRvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvContent.addItemDecoration(new DividerItemDecoration(getContext()));
        mBillBookAdapter = new NovelListAdapter();
        mRvContent.setAdapter(mBillBookAdapter);
    }

    @Override
    public void finishRefresh(List<NovelGenreDataBookData> beans){
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
