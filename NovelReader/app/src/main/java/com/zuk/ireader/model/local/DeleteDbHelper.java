package com.zuk.ireader.model.local;

import com.zuk.ireader.model.bean.AuthorBean;
import com.zuk.ireader.model.bean.ReviewBookBean;
import com.zuk.ireader.model.bean.BookCommentBean;
import com.zuk.ireader.model.bean.BookHelpfulBean;
import com.zuk.ireader.model.bean.BookHelpsBean;
import com.zuk.ireader.model.bean.BookReviewBean;

import java.util.List;

/**
 * Created by newbiechen on 17-4-28.
 */

public interface DeleteDbHelper {
    void deleteBookComments(List<BookCommentBean> beans);
    void deleteBookReviews(List<BookReviewBean> beans);
    void deleteBookHelps(List<BookHelpsBean> beans);
    void deleteAuthors(List<AuthorBean> beans);
    void deleteBooks(List<ReviewBookBean> beans);
    void deleteBookHelpful(List<BookHelpfulBean> beans);
    void deleteAll();
}
