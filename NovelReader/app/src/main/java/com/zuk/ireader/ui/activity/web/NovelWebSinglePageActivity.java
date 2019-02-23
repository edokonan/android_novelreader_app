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
import com.zuk.ireader.business.config.ComWebReaderSet;
import com.zuk.ireader.business.download.NovelDownloadManager;
import com.zuk.ireader.business.download.NovelDownloadManagerInterface;
import com.zuk.ireader.R;
import com.zuk.ireader.business.config.ReaderCom;
import com.zuk.ireader.model.bean.novel.NovelInfoChapterdata;
import com.zuk.ireader.model.bean.novel.NovelInfoBean;
import com.zuk.ireader.business.config.ComCode;
import com.zuk.ireader.realm.DBManagerBookChapter;
import com.zuk.ireader.realm.TableNovelChapter;
import com.zuk.ireader.ui.adapter.novel.NovelChapterItemAdapter;
import com.zuk.ireader.ui.base.BaseActivity;
import com.zuk.ireader.widget.itemdecoration.DividerItemDecoration;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by ksymac on 2018/12/17.
 */

public class NovelWebSinglePageActivity extends BaseActivity  implements NovelDownloadManagerInterface {
    private static final String extra_novel_chapter_url = "extra_novel_chapter_url";
    private static final String extra_novel_url = "extra_novel_url";
    private static final String extra_novelbean = "extra_novelbean";
    private static final String extra_chapterbean = "extra_chapterbean";
    private static final String extra_chapter_pageno = "extra_chapter_pageno";
    WebView webView1;
    Document doc;

    String mNovelChapterUrl;
    String mNovelUrl;
    NovelInfoBean mNovelBean;
    NovelInfoChapterdata mChapterBean;
    public int mCurrentPageno;
    public int mNextPageno;

    NovelDownloadManager mDownloadManager;
    SVProgressHUD mSVProgressHUD;

    FloatingActionButton mfloatingactionButton;
//    LinearLayout infoview;
//    TextView txtviewtitle;
//    Button btnReadNow;
//    Button btnAddBookShelf;
//    Button btnCancel;

    private NovelChapterItemAdapter mHotCommentAdapter;

    @BindView(R.id.mChaptersView)
    RecyclerView mChaptersView;
    @BindView(R.id.mDrawerLayout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.cover_layout)
    RelativeLayout mCoverLayout;

    @BindView(R.id.settingBar)
    LinearLayout settingBar;
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


    public static void StartActivity(Context context,
                                     String novelurl,
                                     String chapterurl,
                                     NovelInfoBean mNovelBean,
                                     NovelInfoChapterdata mChapterBean,
                                     int pageno) {
        Intent intent = new Intent(context, NovelWebSinglePageActivity.class);
        intent.putExtra(extra_novel_url, novelurl);
        intent.putExtra(extra_novel_chapter_url, chapterurl);
        intent.putExtra(extra_novelbean,  new Gson().toJson(mNovelBean));
        intent.putExtra(extra_chapterbean, new Gson().toJson(mChapterBean));
        intent.putExtra(extra_chapter_pageno, pageno);
        context.startActivity(intent);
    }
    protected void getIntentData() {
        mNovelUrl = getIntent().getStringExtra(extra_novel_url);
        mNovelChapterUrl = getIntent().getStringExtra(extra_novel_chapter_url);
        String novelbeanstr = getIntent().getStringExtra(extra_novelbean);
        if (novelbeanstr != null) {
            mNovelBean = new Gson().fromJson(novelbeanstr,NovelInfoBean.class);
        }
        String chapterbeanstr = getIntent().getStringExtra(extra_chapterbean);
        if (chapterbeanstr != null) {
            mChapterBean = new Gson().fromJson(chapterbeanstr,NovelInfoChapterdata.class);
        }
        mCurrentPageno = getIntent().getIntExtra(extra_chapter_pageno,
                mChapterBean.chapter_pagestart);
    }


    @Override
    protected void setUpToolbar(Toolbar toolbar) {
        super.setUpToolbar(toolbar);
        getSupportActionBar().hide();
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_novel_chapter_singlewebpage;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntentData();
        webView1 = (WebView)findViewById(R.id.webview1);
        WebSettings settings = webView1.getSettings();
        settings.setDefaultFontSize(20);
//        webView1.loadUrl("file://"+filepath);
//        webView1.loadUrl("javascript:(document.body.style.backgroundColor ='red');");
//        webView1.loadUrl("javascript:(document.body.style.color ='yellow');");
//        webView1.loadUrl("javascript:(document.body.style.fontSize ='20pt');");
        mfloatingactionButton = (FloatingActionButton) findViewById(R.id.floatingactionButton);
        hideInfoView();
        closeChapterListView();
        mCoverLayout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDrawerLayout.setVisibility(View.INVISIBLE);
                        mCoverLayout.setVisibility(View.INVISIBLE);
                    }
                });
        if(mNovelBean != null){
            finishChapterList(mNovelBean.novelinfodata.chapteritems);
        }
        bottomBar.setBackgroundColor(Color.parseColor(ComCode.ColorBackground1));
        intUI();
        loadhtml();
    }
    void loadhtml(){
        TableNovelChapter chapter =  DBManagerBookChapter.getChapterInfo(mNovelChapterUrl, mCurrentPageno);
        if(chapter != null && chapter.strhtml!=null ){
            loadDoc(null,chapter.strhtml);
        }
        if(ComCode.siteno_aozora.equals(mNovelBean.novelinfodata.siteno)){
            Toast toast = Toast.makeText(this, String.format("%s",mNovelBean.novelinfodata.novel_title),  Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            if(chapter != null && chapter.chaptertitle!=null ){
                Toast toast = Toast.makeText(this, String.format("%s",chapter.chaptertitle),  Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
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
//        webView1.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (dy > 0) {
//                    // Scrolling up
//                    hideToolBar();
//                } else {
//                    // Scrolling down
//                    showToolBar();
//                }
//            }
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
//                    // Do something
//                } else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
//                    // Do something
//                } else {
//                    // Do something
//                }
//            }
//        });

        btnColor1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ComWebReaderSet.setWebBackGroundColor(1)){
                    setBackgroundColor();
                }
            }
        });
        btnColor2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ComWebReaderSet.setWebBackGroundColor(2)){
                    setBackgroundColor();
                }
            }
        });
        btnColor3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ComWebReaderSet.setWebBackGroundColor(3)){
                    setBackgroundColor();
                }
            }
        });
        btnColor4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ComWebReaderSet.setWebBackGroundColor(4)){
                    setBackgroundColor();
                }
            }
        });
        btnColor5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ComWebReaderSet.setWebBackGroundColor(5)){
                    setBackgroundColor();
                }
            }
        });
        btnFont1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ComWebReaderSet.setWebFontSize(false)){
//                    mWebPagesAdapter.notifyDataSetChanged();
                    setBackgroundColor();
                }
            }
        });
        btnFont2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ComWebReaderSet.setWebFontSize(true)){
//                    mWebPagesAdapter.notifyDataSetChanged();
                    setBackgroundColor();
                }
            }
        });
        setBackgroundColor();
    }

    public void setBackgroundColor(){
        String Colorstr = ComWebReaderSet.getWebBackGroundColor();
        loadhtml();
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
                    downloadChapterAt(pos,0);
                }
        );
    }

    public void closeChapterListView(){
        mCoverLayout.setVisibility(View.INVISIBLE);
        mDrawerLayout.setVisibility(View.INVISIBLE);
    }

    public void hideInfoView(){
        this.mfloatingactionButton.setVisibility(View.INVISIBLE);
    }

    public void loadDoc(Document doc,String inhtm){
//        String strhtml = "データありません";
//        if(doc!=null){
//            Element element = getData_alphapolis_novel(doc);
//            strhtml = element.html();
//        }else{
//            strhtml = inhtm;
//        }
        String strhtml = inhtm;
        String fontsize = String.format("font-size:%dpx;",ComWebReaderSet.getWebFontSize());
        String bgcolor = String.format("background-color:%s;",ComWebReaderSet.getWebBackGroundColor());
        //Connect to website
        String imagejavascript =                         "     window.onload = function(){\n" +
                "     var $img = document.getElementsByTagName('img');\n" +
                "     for(var p in  $img){\n" +
                "      $img[p].style.width = '100%';\n" +
                "      $img[p].style.height ='auto';\n" +
                "     }\n" +
                "     }" ;
        if (ComCode.siteno_aozora.equals(mNovelBean.novelinfodata.siteno)){
//            imagejavascript = "";
        }
        String str1 = "     <html> \n" +
                        "     <head> \n" +
                        "     <style type='text/css'> \n" +
                        "     body {\n         " +
                        "           font-family: -apple-system, BlinkMacSystemFont," +
                        "           'Hiragino Sans', 'Hiragino Kaku Gothic ProN', " +
                        "           '游ゴシック  Medium', meiryo, sans-serif;" +
                        "           " + fontsize + "  \n "+
                        "           " + bgcolor + "  \n "+
                        "     }\n" +
                        "     </style> \n" +
                        "     </head> \n" +
                        "     <body>\n" +
                        "     <script type='text/javascript'>\n" +
                        imagejavascript +
                        "   \n" +
                        "   function setbackgroundColor(color) {\n" +
                        "       document.body.style.backgroundColor = color; \n" +
                        "   }\n" +
                        "   function fontsize(size) {\n" +
                        "       document.body.style.fontSize = size; \n" +
                        "   }\n" +
                        "     </script>";
        String str2 ="\n</body></html>";
        String novelTitle = String.format("<h3> %s </h3>",mNovelBean.novelinfodata.novel_title);
        String chapterTitle = String.format("<B> %s </B>",mChapterBean.chapter_title);
//        String nowpage = "";
        String hrstr = "<hr width=\"100%\">";

        String htm = str1 + novelTitle + chapterTitle + hrstr + strhtml + str2;
        if(this.mNovelBean.novelinfodata.siteno.equals(ComCode.siteno_aozora)){
             htm = str1 + strhtml + str2;
        }
        webView1.loadDataWithBaseURL(ComCode.getBaseUrl(mNovelBean.novelinfodata.siteno),
                htm,"text/html",
                "UTF-8",null);
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

    public  Element getData_alphapolis_novel(Document document) {
        try{
            Element novel_body = document.select("div#novelBoby").last();
            return novel_body;
        }catch (Exception e) {
            return null;
        }
    }

    private class MyTask extends AsyncTask<Void, Void, String> {
        public String urllink = "";
        Document doc;
        NovelWebSinglePageActivity activity;
        @Override
        protected String doInBackground(Void... params) {
            String title ="";
            try {
                String url = "https://www.alphapolis.co.jp/novel/";
                Connection.Response res = Jsoup.connect(url)
                        .method(Connection.Method.GET)
                        .timeout(10000)
                        .execute();
//                        sleep(100);
                Map<String, String> cookies = res.cookies();
                doc = Jsoup.connect(mNovelChapterUrl)
                        .cookies(cookies)
                        .get();
                title = doc.title();
                System.out.print(title);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return title;
        }
        @Override
        protected void onPostExecute(String result) {
            activity.loadDoc(doc,null);
        }
    }

    private void jumpToPreChapter(){
        NovelInfoChapterdata item = ReaderCom.getPreChapterPage(this.mNovelBean,
                this.mChapterBean,
                mCurrentPageno);
        if(item!=null && item.chapter_url != null && item.chapter_url.length()>0 ){
            downloadChapterAt(item.no, item.currentpageno);
        }else{
            Toast toast = Toast.makeText(this, "最初の話です", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }
    private void jumpToNextChapter(){
        NovelInfoChapterdata item = ReaderCom.getNextChapterPage(this.mNovelBean,
                mChapterBean,
                mCurrentPageno);
        if(item!=null && item.chapter_url != null && item.chapter_url.length()>0 ){
            downloadChapterAt(item.no, item.currentpageno);
        }else{
            Toast toast = Toast.makeText(this, "最後の話です", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }
    public void downloadChapterAt(int pos,int nextpageno){
        int pageno = nextpageno;
        if(mDownloadManager == null){
            if(ComCode.isMultWebPages(this.mNovelBean.novelinfodata.siteno)){
                if(pageno == 0){
                    pageno = this.mNovelBean.novelinfodata.chapteritems.get(pos).chapter_pagestart;
                }
            }
            mNextPageno = pageno;
            DBManagerBookChapter.insertChapterInfo(this.mNovelBean,
                    this.mNovelBean.novelinfodata.chapteritems.get(pos),
                    mNextPageno);
            mDownloadManager = new NovelDownloadManager(NovelWebSinglePageActivity.this,
                    this.mNovelBean,
                    this.mNovelBean.novelinfodata.chapteritems.get(pos),
                    NovelWebSinglePageActivity.this,
                    true,
                    mNextPageno);
            mDownloadManager.DownloadStart();
        }else{
            return;
        }
    }
    public void downloadStart(boolean openTxtReader){
        mSVProgressHUD = new SVProgressHUD(this);
        mSVProgressHUD.show();
    }
    public void downloadFail(){
        if(mSVProgressHUD!=null && mSVProgressHUD.isShowing()){
            mSVProgressHUD.showErrorWithStatus("ファイルダウンロード失敗");
        }
    }
    public void downloadOk(String filepath,
                           NovelInfoBean mNovelBean,
                           NovelInfoChapterdata mChapterBean,
                           String pageurl,int pageno, boolean openTxtReader){
        if(mSVProgressHUD!=null && mSVProgressHUD.isShowing()){
            mSVProgressHUD.dismiss();
        }
//        FilePath = filepath;
        mDownloadManager = null;
//        finish();
        ReaderCom.openSingleHtmlReader(filepath,
                mNovelBean,
                mChapterBean,
                pageno);
    }
}
