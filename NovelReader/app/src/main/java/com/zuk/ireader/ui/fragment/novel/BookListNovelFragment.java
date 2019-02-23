package com.zuk.ireader.ui.fragment.novel;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zuk.ireader.R;
import com.zuk.ireader.RxBus;
import com.zuk.ireader.event.BookSubSortEvent;
import com.zuk.ireader.model.bean.novel.NovelGenreDataBookData;
import com.zuk.ireader.model.flag.BookListType;
import com.zuk.ireader.business.config.NovelConfigClass;
import com.zuk.ireader.presenter.novel.BookListPresenterNovel;
import com.zuk.ireader.presenter.novel.BookListContractNovel;
import com.zuk.ireader.ui.activity.novel.NovelBookDetailActivity;
import com.zuk.ireader.ui.adapter.novel.NovelListAdapter;
import com.zuk.ireader.ui.base.BaseMVPFragment;
import com.zuk.ireader.widget.RefreshLayout;
import com.zuk.ireader.widget.adapter.WholeAdapter;
import com.zuk.ireader.widget.itemdecoration.DividerItemDecoration;

import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by ksymac on 2018/12/1.
 */

public class BookListNovelFragment extends BaseMVPFragment<BookListContractNovel.Presenter>
        implements BookListContractNovel.View{

//    private static final String EXTRA_BOOK_LIST_TYPE = "extra_book_list_type";
    private static final String BUNDLE_BOOK_TAG = "bundle_book_tag";
    private static final String BUNDLE_Genre_Type = "BUNDLE_Genre_Type";
    private static final String BUNDLE_Genre_SiteNo = "BUNDLE_Genre_SiteNo";
    private static final String BUNDLE_Genre_Link = "BUNDLE_Genre_Link";

    @BindView(R.id.refresh_layout)
    RefreshLayout mRefreshLayout;
    @BindView(R.id.refresh_rv_content)
    RecyclerView mRvContent;
    /************************************/
    private NovelListAdapter mBookListAdapter;
    /***************************************/
//    private BookListType mBookListType;
//    private int mStart = 0;
//    private int mLimit = 20;

    private String mGenreType = "";
    private String mTag = "";
    private String mSiteNo = "";
    private String mGenreLink = "";

    public static Fragment newInstance(BookListType bookListType, String genretype, String siteno, String genrelink){
        Bundle bundle = new Bundle();
//        bundle.putSerializable(EXTRA_BOOK_LIST_TYPE,bookListType);
        bundle.putSerializable(BUNDLE_Genre_Type, genretype);
        bundle.putSerializable(BUNDLE_Genre_SiteNo, siteno);
        bundle.putSerializable(BUNDLE_Genre_Link, genrelink);

        Fragment fragment = new BookListNovelFragment();
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
        if(savedInstanceState != null){
//            mBookListType = (BookListType) savedInstanceState.getSerializable(EXTRA_BOOK_LIST_TYPE);
            mTag = savedInstanceState.getString(BUNDLE_BOOK_TAG);
            mSiteNo = savedInstanceState.getString(BUNDLE_Genre_SiteNo);
            mGenreLink = savedInstanceState.getString(BUNDLE_Genre_Link);
            mGenreType = savedInstanceState.getString(BUNDLE_Genre_Type);
        }
        else {
//            mBookListType = (BookListType) getArguments().getSerializable(EXTRA_BOOK_LIST_TYPE);
//            mSiteNo = savedInstanceState.getString(BUNDLE_Genre_SiteNo);
            mGenreType = (String) getArguments().getSerializable(BUNDLE_Genre_Type);
            mSiteNo = (String) getArguments().getSerializable(BUNDLE_Genre_SiteNo);
            mGenreLink = (String)getArguments().getSerializable(BUNDLE_Genre_Link);
        }
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        super.initWidget(savedInstanceState);
        setUpAdapter();
    }


    @Override
    protected void initClick() {
        super.initClick();
        mBookListAdapter.setOnLoadMoreListener(
                () -> {
                    mPresenter.loadBookList(mSiteNo, mGenreLink);
                }
        );
        mBookListAdapter.setOnItemClickListener(
                (view,pos) -> {
                    NovelGenreDataBookData bookdata = mBookListAdapter.getItem(pos);
                    String bookid = "5913f112e0ea5a5e7444fa7b";
                    NovelBookDetailActivity.startActivity(this.getActivity(), bookid, bookdata.link);
                }
        );
        Disposable disposable = RxBus.getInstance()
                .toObservable(BookSubSortEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        event -> {
                            mTag = event.bookSubSort;
                            NovelConfigClass config = new NovelConfigClass();
                            NovelConfigClass.NovelConfigItem item = config.getItem(mGenreType,mTag);
                            mGenreLink = item.genreurl;
                            showRefresh();
                        }
                );
        addDisposable(disposable);
    }

    @Override
    protected void processLogic() {
        super.processLogic();
        showRefresh();
    }

    private void showRefresh(){
//        mStart = 0;
        mRefreshLayout.showLoading();
        mPresenter.refreshBookList(mSiteNo, mGenreLink);
    }

    private void setUpAdapter(){
        mRvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvContent.addItemDecoration(new DividerItemDecoration(getContext()));
        mBookListAdapter = new NovelListAdapter(getContext(),new WholeAdapter.Options());
        mRvContent.setAdapter(mBookListAdapter);
    }

    @Override
    public void finishRefresh(List<NovelGenreDataBookData> beans){
        mBookListAdapter.refreshItems(beans);
//        mStart = beans.size();
    }

    @Override
    public void finishLoading(List<NovelGenreDataBookData> beans) {
        mBookListAdapter.addItems(beans);
//        mStart += beans.size();
    }

    @Override
    public void showLoadError() {
        mBookListAdapter.showLoadError();
    }

    @Override
    public void showError() {
        mRefreshLayout.showError();
    }

    @Override
    public void complete() {
        mRefreshLayout.showFinish();
    }

    /***********************************************************************/
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putSerializable(EXTRA_BOOK_LIST_TYPE, mBookListType);
        outState.putSerializable(BUNDLE_BOOK_TAG, mTag);
        outState.putSerializable(BUNDLE_Genre_SiteNo, mSiteNo);
        outState.putSerializable(BUNDLE_Genre_Link, mGenreLink);
        outState.putSerializable(BUNDLE_Genre_Type, mGenreType);
    }
}