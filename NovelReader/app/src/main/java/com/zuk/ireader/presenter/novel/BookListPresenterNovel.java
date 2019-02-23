package com.zuk.ireader.presenter.novel;

import com.google.gson.Gson;
import com.zuk.ireader.model.bean.novel.NovelGenreBean;
import com.zuk.ireader.model.remote.RemoteRepository;
import com.zuk.ireader.business.config.ComCode;
import com.zuk.ireader.realm.DBManagerApiCache;
import com.zuk.ireader.ui.base.RxPresenter;
import com.zuk.ireader.utils.LogUtils;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ksymac on 2018/12/1.
 */
public class BookListPresenterNovel extends RxPresenter<BookListContractNovel.View> implements BookListContractNovel.Presenter {

    String createkey(String siteno, String genrelink){
        return String.format("genreapi:site-%s,genre-%s,",siteno,genrelink);
    }
    @Override
    public void refreshBookList(String siteno, String genrelink) {
        //"2","/rank/list/type/daily_total/"
//        if (tag.equals("全本")){
//            tag = "";
//        }
        String key = createkey(siteno,genrelink);
        //首先从DB中获取
        String jsonstr = DBManagerApiCache.getJsouFromCache(key,
                ComCode.apiver_genre,
                ComCode.apicache_novelinfo_hoursago_1);
        if(jsonstr != null){
            NovelGenreBean beans = new Gson().fromJson(jsonstr,NovelGenreBean.class);
            mView.finishRefresh(beans.getBookLists());
            mView.complete();
            return;
        }

        Disposable refreshDispo = getBookListSingle(siteno, genrelink)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (bean)-> {
                            String json = new Gson().toJson(bean);
                            DBManagerApiCache.saveToCache(key, ComCode.apiver_genre, json);
                            mView.finishRefresh(bean.getBookLists());
                            mView.complete();
                        }
                        ,
                        (e) ->{
                            mView.complete();
                            mView.showError();
                            LogUtils.e(e);
                        }
                );
        addDisposable(refreshDispo);
    }
    @Override
    public void loadBookList(String siteno, String genrelink) {

        String key = createkey(siteno,genrelink);
        //"2","/rank/list/type/daily_total/"
        Disposable refreshDispo = getBookListSingle(siteno, genrelink)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (bean)-> {
                            String json = new Gson().toJson(bean);
                            DBManagerApiCache.saveToCache(key,ComCode.apiver_genre,json);
                            mView.finishLoading(bean.getBookLists());
                        }
                        ,
                        (e) ->{
                            mView.showLoadError();
                            LogUtils.e(e);
                        }
                );
        addDisposable(refreshDispo);
    }

    private Single<NovelGenreBean> getBookListSingle(String siteno, String genrelink){
        Single<NovelGenreBean> bookListSingle = null;
        bookListSingle = RemoteRepository.getInstance().getBookListsNovel(siteno,genrelink);
        return bookListSingle;
    }
}
