package com.zuk.ireader.presenter.novel;
import com.zuk.ireader.model.bean.CollBookBean;
import com.zuk.ireader.model.bean.novel.NovelInfoBean;
import com.zuk.ireader.ui.base.BaseContract;

import java.util.List;

/**
 * Created by ksymac on 2018/12/2.
 */
public interface NovelBookDetailContract {
    interface View extends BaseContract.BaseView{
        void finishRefresh(NovelInfoBean bean);
//        void finishHotComment(List<HotCommentBean> beans);
//        void finishRecommendBookList(List<BookListBean> beans);
        void waitToBookShelf();
        void errorToBookShelf();
        void succeedToBookShelf();
    }

    interface Presenter extends BaseContract.BasePresenter<com.zuk.ireader.presenter.novel.NovelBookDetailContract.View>{

        void refreshNovelInfoFromNet(String siteno , String novelurl, int hoursago);//String bookId,
        void refreshNovelInfoFromLocalDB(String siteno , String novelurl);//String bookId,
//        //添加到书架上
        void addToBookShelf(CollBookBean collBook);
    }
}