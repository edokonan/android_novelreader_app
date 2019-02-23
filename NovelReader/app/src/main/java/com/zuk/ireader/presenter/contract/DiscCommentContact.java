package com.zuk.ireader.presenter.contract;

import com.zuk.ireader.model.bean.BookCommentBean;
import com.zuk.ireader.model.flag.BookDistillate;
import com.zuk.ireader.model.flag.BookSort;
import com.zuk.ireader.model.flag.CommunityType;
import com.zuk.ireader.ui.base.BaseContract;

import java.util.List;

/**
 * Created by newbiechen on 17-4-20.
 */

public interface DiscCommentContact {

    interface View extends BaseContract.BaseView{
        void finishRefresh(List<BookCommentBean> beans);
        void finishLoading(List<BookCommentBean> beans);
        void showErrorTip();
    }

    interface Presenter extends BaseContract.BasePresenter<View>{
        void firstLoading(CommunityType block, BookSort sort, int start, int limited, BookDistillate distillate);
        void refreshComment(CommunityType block, BookSort sort, int start, int limited, BookDistillate distillate);
        void loadingComment(CommunityType block, BookSort sort, int start, int limited, BookDistillate distillate);
        void saveComment(List<BookCommentBean> beans);
    }
}
