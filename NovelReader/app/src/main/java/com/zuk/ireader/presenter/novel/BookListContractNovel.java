package com.zuk.ireader.presenter.novel;

import com.zuk.ireader.model.bean.novel.NovelGenreDataBookData;
import com.zuk.ireader.ui.base.BaseContract;

import java.util.List;

/**
 * Created by ksymac on 2018/12/1.
 */

public interface BookListContractNovel {
    interface View extends BaseContract.BaseView{
        void finishRefresh(List<NovelGenreDataBookData> beans);
        void finishLoading(List<NovelGenreDataBookData> beans);
        void showLoadError();
    }

    interface Presenter extends BaseContract.BasePresenter<View>{
        void refreshBookList(String siteno, String genrelink);
        void loadBookList(String siteno, String genrelink);
    }
}
