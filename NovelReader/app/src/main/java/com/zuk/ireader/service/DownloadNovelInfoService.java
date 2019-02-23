package com.zuk.ireader.service;

import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.zuk.ireader.R;
import com.zuk.ireader.RxBus;
import com.zuk.ireader.business.config.ComCode;
import com.zuk.ireader.event.DeleteResponseEvent;
import com.zuk.ireader.event.DeleteTaskEvent;
import com.zuk.ireader.event.DownloadMessage;
import com.zuk.ireader.model.bean.BookChapterBean;
import com.zuk.ireader.model.bean.DownloadTaskBean;
import com.zuk.ireader.model.bean.novel.NovelInfoBean;
import com.zuk.ireader.model.local.BookRepository;
import com.zuk.ireader.model.local.LocalRepository;
import com.zuk.ireader.model.remote.RemoteRepository;
import com.zuk.ireader.presenter.novel.NovelBookDetailContract;
import com.zuk.ireader.presenter.novel.NovelBookDetailPresenter;
import com.zuk.ireader.realm.DBManagerReadBookShelf;
import com.zuk.ireader.realm.TableReadBookShelf;
import com.zuk.ireader.ui.base.BaseContract;
import com.zuk.ireader.ui.base.BaseService;
import com.zuk.ireader.utils.BookManager;
import com.zuk.ireader.utils.LogUtils;
import com.zuk.ireader.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by newbiechen on 17-5-10.
 */

public class DownloadNovelInfoService extends BaseService implements NovelBookDetailContract.View{
    private static final String TAG = "NovelInfoService";

    NovelBookDetailPresenter mPresenter;
    List<TableReadBookShelf> list;
    int index = 0;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPresenter = new NovelBookDetailPresenter();
        mPresenter.attachView(this);
        Log.d(TAG, "onCreate");

        list  = DBManagerReadBookShelf.getBooklist();
        index = 0;
        sendRequest();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        return START_STICKY;
    }
    public void sendRequest(){
        if (list!=null && list.size() > index){
            if(list.get(index).siteno.equals(ComCode.siteno_alphapolis)){
                sendNextRequest();
            }else{
                mPresenter.refreshNovelInfoFromNet(list.get(index).siteno,list.get(index).novelurl,
                        ComCode.apicache_novelinfo_hoursago_0);
            }
        }else{
            stopSelf();
//            Toast.makeText(this , "トーストメッセージ", Toast.LENGTH_SHORT).show();
        }
    }
    //检查是否更新
    @Override
    public void finishRefresh(NovelInfoBean bean){
        DBManagerReadBookShelf.checkIsUpdate(bean);
        sendNextRequest();
    }
    public void sendNextRequest(){
        index += 1;
        sendRequest();
    }


    @Override
    public void waitToBookShelf(){

    }

    @Override
    public void errorToBookShelf(){

    }

    @Override
    public void succeedToBookShelf(){
    }

    @Override
    public void showError() {
    }

    @Override
    public void complete() {
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
;        Log.d(TAG, "onDestroy");
    }
}
