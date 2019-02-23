package com.zuk.ireader.presenter.novel;

import com.google.gson.Gson;
import com.zuk.ireader.model.bean.BookChapterBean;
import com.zuk.ireader.model.bean.CollBookBean;
import com.zuk.ireader.model.bean.novel.NovelInfoBean;
import com.zuk.ireader.model.local.BookRepository;
import com.zuk.ireader.model.remote.RemoteRepository;
import com.zuk.ireader.business.config.ComCode;
import com.zuk.ireader.realm.DBManagerApiCache;
import com.zuk.ireader.ui.base.RxPresenter;
import com.zuk.ireader.utils.LogUtils;
import com.zuk.ireader.utils.MD5Utils;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ksymac on 2018/12/2.
 */
public class NovelBookDetailPresenter extends RxPresenter<NovelBookDetailContract.View>
        implements NovelBookDetailContract.Presenter{
    private static final String TAG = "NovelBookDetailPresenter";
//    private String bookId;
//    private NovelGenreDataBookData mbookdata;

    private String mSiteNo;
    private String mNovelUrl;

    @Override
    public void refreshNovelInfoFromNet(String siteno , String novelurl, int hoursago) {
        this.mSiteNo = siteno;
        this.mNovelUrl = novelurl;
        getNovelInfoFromNet(siteno, novelurl, hoursago);
    }

    @Override
    public void refreshNovelInfoFromLocalDB(String siteno , String novelurl) {
        this.mSiteNo = siteno;
        this.mNovelUrl = novelurl;
        getNovelInfoFromLocalDB(siteno, novelurl);
    }

    //获取网络信息
    private void getNovelInfoFromLocalDB(String siteno, String novelurl) {
        //首先从DB中获取
        String jsonstr = DBManagerApiCache.getJsouFromCache(novelurl, ComCode.apiver);
        if (jsonstr != null) {
            NovelInfoBean bean = new Gson().fromJson(jsonstr, NovelInfoBean.class);
            mView.finishRefresh(bean);
            mView.complete();
            return;
        }else{
            mView.complete();
        }
    }

    //获取网络信息
    private void getNovelInfoFromNet(String siteno, String novelurl, int hoursago){
        //首先从DB中获取
        String jsonstr = DBManagerApiCache.getJsouFromCache(novelurl,ComCode.apiver, hoursago);
        if(jsonstr != null){
            NovelInfoBean bean = new Gson().fromJson(jsonstr,NovelInfoBean.class);
            mView.finishRefresh(bean);
            mView.complete();
            return;
        }
        RemoteRepository
                .getInstance()
                .getNovelDetail(siteno, novelurl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<NovelInfoBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }
                    @Override
                    public void onSuccess(NovelInfoBean bean){
                        String json = new Gson().toJson(bean);
                        DBManagerApiCache.saveToCache(novelurl,ComCode.apiver, json);
                        mView.finishRefresh(bean);
                        mView.complete();
                    }
                    @Override
                    public void onError(Throwable e) {
                        mView.showError();
                    }
                });
    }
    @Override
    public void addToBookShelf(CollBookBean collBookBean)  {
        Disposable disposable = RemoteRepository.getInstance()
                .getBookChapters(collBookBean.get_id())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(
                        (d) -> mView.waitToBookShelf() //等待加载
                )
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        beans -> {
                            //设置 id
                            for(BookChapterBean bean :beans){
                                bean.setId(MD5Utils.strToMd5By16(bean.getLink()));
                            }
                            //设置目录
                            collBookBean.setBookChapters(beans);
                            //存储收藏
                            BookRepository.getInstance()
                                    .saveCollBookWithAsync(collBookBean);

                            mView.succeedToBookShelf();
                        }
                        ,
                        e -> {
                            mView.errorToBookShelf();
                            LogUtils.e(e);
                        }
                );
        addDisposable(disposable);
    }


//    private void refreshBook(){
//        RemoteRepository
//                .getInstance()
//                .getBookDetail(bookId)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new SingleObserver<BookDetailBean>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        addDisposable(d);
//                    }
//
//                    @Override
//                    public void onSuccess(BookDetailBean value){
//                        mView.finishRefresh(value);
//                        mView.complete();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        mView.showError();
//                    }
//                });
//    }

//    private void refreshComment(){
//        Disposable disposable = RemoteRepository
//                .getInstance()
//                .getHotComments(bookId)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        (value) -> mView.finishHotComment(value)
//                );
//        addDisposable(disposable);
//    }
//
//    private void refreshRecommend(){
//        Disposable disposable = RemoteRepository
//                .getInstance()
//                .getRecommendBookList(bookId,3)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        (value) -> mView.finishRecommendBookList(value)
//                );
//        addDisposable(disposable);
//    }
}
