package com.zuk.ireader.ui.activity.web;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.Gson;
import com.zuk.ireader.R;
import com.zuk.ireader.business.config.ComCode;
import com.zuk.ireader.business.config.ComWebReaderSet;
import com.zuk.ireader.business.config.ReaderCom;
import com.zuk.ireader.business.download.NovelDownloadManager;
import com.zuk.ireader.business.download.NovelDownloadManagerInterface;
import com.zuk.ireader.model.bean.novel.NovelChapterPageForWeb;
import com.zuk.ireader.model.bean.novel.NovelInfoBean;
import com.zuk.ireader.model.bean.novel.NovelInfoChapterdata;
import com.zuk.ireader.realm.DBManagerApiCache;
import com.zuk.ireader.realm.DBManagerBookChapter;
import com.zuk.ireader.realm.TableNovelChapter;
import com.zuk.ireader.ui.adapter.novel.NovelChapterItemAdapter;
import com.zuk.ireader.ui.adapter.novel.NovelChapterWebPageAdapter;
import com.zuk.ireader.ui.adapter.novel.NovelChapterWebPageHolder;
import com.zuk.ireader.ui.base.BaseActivity;
import com.zuk.ireader.widget.itemdecoration.DividerItemDecoration;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.realm.RealmResults;

/**
 * Created by ksymac on 2018/12/17.
 */

public class NovelWebPagesActivity extends BaseActivity  {
    private static final String extra_novel_chapter_url = "extra_novel_chapter_url";
    private static final String extra_novel_url = "extra_novel_url";
    private static final String extra_novelbean = "extra_novelbean";
    private static final String extra_chapterbean = "extra_chapterbean";



    @BindView(R.id.bottomBar)
    LinearLayout bottomBar;
    @BindView(R.id.btnPreChapter)
    ImageButton btnPreChapter;
    @BindView(R.id.btnNextChapter)
    ImageButton btnNextChapter;
    @BindView(R.id.btnChapters)
    ImageButton btnChapters;
    @BindView(R.id.btnSetting)
    ImageButton btnSetting;
    @BindView(R.id.btnClose)
    ImageButton btnClose;


    @BindView(R.id.settingBar)
    LinearLayout settingBar;
    @BindView(R.id.btnColor1)
    Button btnColor1;
    @BindView(R.id.btnColor2)
    Button btnColor2;
    @BindView(R.id.btnColor3)
    Button btnColor3;
    @BindView(R.id.btnColor4)
    Button btnColor4;
    @BindView(R.id.btnColor5)
    Button btnColor5;
    @BindView(R.id.btnFont1)
    Button btnFont1;
    @BindView(R.id.btnFont2)
    Button btnFont2;

    @BindView(R.id.mChaptersView)
    RecyclerView mChaptersView;
    @BindView(R.id.mDrawerLayout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.cover_layout)
    RelativeLayout mCoverLayout;

    RecyclerView mWebPages;
    private NovelChapterItemAdapter mHotCommentAdapter;

    FloatingActionButton mfloatingactionButton;
    NovelInfoBean mNovelBean;
    NovelInfoChapterdata mChapterBean;
    NovelChapterWebPageAdapter mWebPagesAdapter;

    List<NovelChapterPageForWeb> list;
    public static void StartActivity(Context context,
                                     NovelInfoBean mNovelBean,
                                     NovelInfoChapterdata mChapterBean) {
        Intent intent = new Intent(context, NovelWebPagesActivity.class);
        intent.putExtra(extra_novelbean,  new Gson().toJson(mNovelBean));
        intent.putExtra(extra_chapterbean, new Gson().toJson(mChapterBean));
        context.startActivity(intent);
    }
    protected void getIntentData() {
        String novelbeanstr = getIntent().getStringExtra(extra_novelbean);
        if (novelbeanstr != null) {
            mNovelBean = new Gson().fromJson(novelbeanstr,NovelInfoBean.class);
        }
        String chapterbeanstr = getIntent().getStringExtra(extra_chapterbean);
        if (chapterbeanstr != null) {
            mChapterBean = new Gson().fromJson(chapterbeanstr,NovelInfoChapterdata.class);
        }
    }


    @Override
    protected void setUpToolbar(Toolbar toolbar) {
        super.setUpToolbar(toolbar);
        getSupportActionBar().hide();
    }
    @Override
    protected int getContentId() {
        return R.layout.activity_novel_chapter_webpage;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntentData();
        //setContentView(R.layout.activity_novel_chapterwebview);
        mWebPages = (RecyclerView)findViewById(R.id.chapter_web_recyclerview);
        mfloatingactionButton = (FloatingActionButton) findViewById(R.id.floatingactionButton);
        if(mChapterBean != null){
            list = new ArrayList<NovelChapterPageForWeb>();
            int pos = 0;

            for(int i = mChapterBean.chapter_pagestart;i<=mChapterBean.chapter_pageend; i++){
                NovelChapterPageForWeb item = new NovelChapterPageForWeb();
                item.mNovelBean = mNovelBean;
                item.mChapterBean = mChapterBean;
                item.pageno = i;
                item.pos = pos;
                pos++;
                list.add(item);
            }
//            list = DBManagerBookChapter.getChapterPages(mChapterBean.chapter_url) ;
            LoadPages();
            intUI();
//            if(ComCode.isEstarOrOtonaNovel(mNovelBean.novelinfodata.siteno)){
//                btnPreChapter.setText("前の頁");
//                btnNextChapter.setText("次の頁");
//            }else{
//                btnPreChapter.setText("前の話");
//                btnNextChapter.setText("次の話");
//            }
        }
    }


    void intUI(){
        btnChapters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDrawerLayout.getVisibility() == View.VISIBLE) {
                    mDrawerLayout.setVisibility(View.INVISIBLE);
                    mCoverLayout.setVisibility(View.INVISIBLE);
                } else {
                    mDrawerLayout.setVisibility(View.VISIBLE);
                    mCoverLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        btnPreChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpToPreChapter();
            }
        });
        btnNextChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpToNextChapter();
            }
        });
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSettingView();
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mCoverLayout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDrawerLayout.setVisibility(View.INVISIBLE);
                        mCoverLayout.setVisibility(View.INVISIBLE);
                        settingBar.setVisibility(View.INVISIBLE);
                        bottomBar.setVisibility(View.VISIBLE);
                    }
                });
        finishChapterList(this.mNovelBean.getChapterItems());
        closeChapterListView();
        hideSettingView();
        mWebPages.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    // Scrolling up
                    hideToolBar();
                } else {
                    // Scrolling down
                    showToolBar();
                }
            }
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    // Do something
                } else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    // Do something
                } else {
                    // Do something
                }
            }
        });

        btnColor1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ComWebReaderSet.setWebBackGroundColor(1)){
                    mWebPagesAdapter.notifyDataSetChanged();
                    setBackgroundColor();
                }
            }
        });
        btnColor2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ComWebReaderSet.setWebBackGroundColor(2)){
                    mWebPagesAdapter.notifyDataSetChanged();
                    setBackgroundColor();
                }
            }
        });
        btnColor3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ComWebReaderSet.setWebBackGroundColor(3)){
                    mWebPagesAdapter.notifyDataSetChanged();
                    setBackgroundColor();
                }
            }
        });
        btnColor4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ComWebReaderSet.setWebBackGroundColor(4)){
                    mWebPagesAdapter.notifyDataSetChanged();
                    setBackgroundColor();
                }
            }
        });
        btnColor5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ComWebReaderSet.setWebBackGroundColor(5)){
                    mWebPagesAdapter.notifyDataSetChanged();
                    setBackgroundColor();
                }
            }
        });
        btnFont1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ComWebReaderSet.setWebFontSize(false)){
                    mWebPagesAdapter.notifyDataSetChanged();
                }
            }
        });
        btnFont2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ComWebReaderSet.setWebFontSize(true)){
                    mWebPagesAdapter.notifyDataSetChanged();
                }
            }
        });
        setBackgroundColor();
    }

    public void setBackgroundColor(){
        String Colorstr = ComWebReaderSet.getWebBackGroundColor();
        mWebPages.setBackgroundColor(Color.parseColor(Colorstr));
    }


    public void hideToolBar(){
        bottomBar.setVisibility(View.GONE);
    }
    public void showToolBar(){
        bottomBar.setVisibility(View.VISIBLE);
        settingBar.setVisibility(View.INVISIBLE);
    }
    public void hideSettingView(){
        mCoverLayout.setVisibility(View.INVISIBLE);
        settingBar.setVisibility(View.INVISIBLE);
    }
    public void showSettingView(){
        bottomBar.setVisibility(View.GONE);
        mCoverLayout.setVisibility(View.VISIBLE);
        settingBar.setVisibility(View.VISIBLE);
    }
    public void closeChapterListView(){
        mCoverLayout.setVisibility(View.INVISIBLE);
        mDrawerLayout.setVisibility(View.INVISIBLE);
    }
    public void finishChapterList(List<NovelInfoChapterdata> beans) {
        if (beans.isEmpty()) {
            return;
        }
        mHotCommentAdapter = new NovelChapterItemAdapter();
        mChaptersView.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        });
        mChaptersView.addItemDecoration(new DividerItemDecoration(this));
        mChaptersView.setAdapter(mHotCommentAdapter);
        mHotCommentAdapter.addItems(beans);
        mHotCommentAdapter.setOnItemClickListener(
                (view,pos) -> {
                    openChapterAt(pos);
                }
        );
    }

    private void jumpToPreChapter(){
        NovelInfoChapterdata item = getPreChapter(this.mNovelBean,this.mChapterBean);
        if(item!=null && item.chapter_url != null && item.chapter_url.length()>0 ){
            openChapterAt(item.no);
        }else{
            Toast toast = Toast.makeText(this, "最初の話です", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }
    private void jumpToNextChapter(){
        NovelInfoChapterdata item = getNextChapter(this.mNovelBean,this.mChapterBean);
        if(item!=null && item.chapter_url != null && item.chapter_url.length()>0 ){
            openChapterAt(item.no);
        }else{
            Toast toast = Toast.makeText(this, "最後の話です", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }



    public static  NovelInfoChapterdata getPreChapter(NovelInfoBean mNovelBean, NovelInfoChapterdata mChapterBean){
        NovelInfoChapterdata retitem = null;
        for(NovelInfoChapterdata item : mNovelBean.getChapterItems()){
            if(item.no < mChapterBean.no && item.chapter_url!=null && item.chapter_url.length()>0){
                item.currentpageno = item.chapter_pagestart;
                retitem = item;
            }
        }
        return retitem;
    }
    public static NovelInfoChapterdata getNextChapter(NovelInfoBean mNovelBean,
                                                          NovelInfoChapterdata mChapterBean){
        for(NovelInfoChapterdata item : mNovelBean.getChapterItems()){
            if(item.no > mChapterBean.no && item.chapter_url!=null && item.chapter_url.length()>0){
                item.currentpageno = item.chapter_pagestart;
                return item;
            }
        }
        return null;
    }
    public void openChapterAt(int pos){
        openChapterBean(
                mNovelBean,
                mNovelBean.novelinfodata.chapteritems.get(pos));
    }
    public void openChapterBean(NovelInfoBean novelBean,
                              NovelInfoChapterdata chapterBean){
        ReaderCom.openWebReader(novelBean,chapterBean);
        finish();
        return;
    }

    public void LoadPages() {
//        if (beans.isEmpty()) {
//            return;
//        }
        mWebPagesAdapter = new NovelChapterWebPageAdapter();
        mWebPages.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        });
        mWebPages.addItemDecoration(new DividerItemDecoration(this));
        mWebPages.setAdapter(mWebPagesAdapter);
        mWebPages.setVisibility(View.VISIBLE);
        mWebPagesAdapter.addItems(list);
        mWebPagesAdapter.setOnItemClickListener(
                (view,pos) -> {
//                    downloadChapterAt(pos,0);
                    return;
                }
        );
        mWebPages.setClickable(false);
    }
}
