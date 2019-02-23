package com.zuk.ireader.ui.activity.web;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.zuk.ireader.business.download.NovelDownloadManager;
import com.zuk.ireader.business.download.NovelDownloadManagerInterface;
import com.zuk.ireader.R;
import com.zuk.ireader.business.config.ReaderCom;
import com.zuk.ireader.model.bean.novel.NovelInfoChapterdata;
import com.zuk.ireader.model.bean.novel.NovelInfoBean;
import com.zuk.ireader.business.config.ComCode;
import com.zuk.ireader.business.parse.NovelParseAlphapolis;
import com.zuk.ireader.presenter.novel.NovelBookDetailContract;
import com.zuk.ireader.presenter.novel.NovelBookDetailPresenter;
import com.zuk.ireader.realm.DBManagerReadBookShelf;
import com.zuk.ireader.ui.base.BaseMVPActivity;

/**
 * Created by ksymac on 2018/12/23.
 */
public class NovelSiteWebActivity extends BaseMVPActivity<NovelBookDetailContract.Presenter>
        implements  NovelBookDetailContract.View, NovelDownloadManagerInterface {

    WebView webView1;
    NovelInfoBean mNovelBean;
    String mInitURL="https://yomou.syosetu.com";
    private static final String EXTRA_INIT_URL = "EXTRA_INIT_URL";

    NovelDownloadManager mDownloadManager;
    SVProgressHUD mSVProgressHUD;
    FloatingActionButton mfloatingactionButton;
    LinearLayout infoview;
    TextView txtviewtitle;
    Button btnReadNow;
    Button btnAddBookShelf;
    Button btnCancel;
    ImageButton btninfoviewclose;
    ImageButton btnBack;
    ImageButton btnForward;
    ImageButton btnRefresh;
    ImageButton btnClose;

    String mParseUrl;
//    String mNovelChapterUrl;
//    String mNovelUrl;
//    NovelChapterListBean mNovelBean;
//    NovelChapterItemdata mChapterItem;
//    public static void startActivity(Context context, String novelurl, String chapterurl) {
//        Intent intent = new Intent(context, com.zuk.ireader.ui.activity.novel.NovelWebViewActivity.class);
//        intent.putExtra(extra_novel_url, novelurl);
//        intent.putExtra(extra_novel_chapter_url, chapterurl);
//        context.startActivity(intent);
//    }
    public static void startActivity(Context context,String initurl){
        Intent intent = new Intent(context,NovelSiteWebActivity.class);
        intent.putExtra(EXTRA_INIT_URL,initurl);
        context.startActivity(intent);
    }
    @Override
    protected void setUpToolbar(Toolbar toolbar) {
        super.setUpToolbar(toolbar);
        getSupportActionBar().hide();
    }

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null){
            mInitURL = savedInstanceState.getString(EXTRA_INIT_URL);
        }
        else {
            mInitURL = getIntent().getStringExtra(EXTRA_INIT_URL);
        }
//        setContentView(R.layout.activity_novel_webview);
//        mPresenter = new NovelBookDetailPresenter();
        webView1 = (WebView)findViewById(R.id.webview1);

        mfloatingactionButton = (FloatingActionButton) findViewById(R.id.floatingactionButton);
        infoview = (LinearLayout) findViewById(R.id.infoview);
        txtviewtitle = (TextView) findViewById(R.id.txtviewtitle);
        btnReadNow = (Button) findViewById(R.id.btnReadNow);
        btnAddBookShelf = (Button) findViewById(R.id.btnAddBookShelf);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btninfoviewclose = (ImageButton) findViewById(R.id.btninfoviewclose);
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnForward = (ImageButton) findViewById(R.id.btnForward);
        btnRefresh = (ImageButton) findViewById(R.id.btnRefresh);
        btnClose = (ImageButton) findViewById(R.id.btnClose);

//        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.first_coordinator_layout);
        // Enable javascript
        webView1.getSettings().setJavaScriptEnabled(true);
        //        webView1.getSettings().setBuiltInZoomControls(true);
        webView1.addJavascriptInterface(new MyJavaScriptInterface(this), "HtmlViewer");

        //webView1.setWebChromeClient(new WebChromeClient());
        webView1.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mNovelBean = null;
                hideInfoView();
                System.out.print(url);
                if(ComCode.is_ncode_syosetu_novelurl(url)){
                    mPresenter.refreshNovelInfoFromNet( ComCode.siteno_ncode_syosetu , url, ComCode.apicache_novelinfo_hoursago_0);
                }else if(ComCode.is_novel18_syosetu_novelurl(url)){
                    mPresenter.refreshNovelInfoFromNet( ComCode.siteno_novel18_syosetu , url, ComCode.apicache_novelinfo_hoursago_0);
                }else if(ComCode.is_estar_novelurl(url)){
                    mPresenter.refreshNovelInfoFromNet( ComCode.siteno_estar , url, ComCode.apicache_novelinfo_hoursago_0);
                }else if(ComCode.is_estar_r_novelurl(url)){
                    mPresenter.refreshNovelInfoFromNet( ComCode.siteno_estar_r , url, ComCode.apicache_novelinfo_hoursago_0);
                }else if(ComCode.is_otona_novel_novelurl(url)){
                    mPresenter.refreshNovelInfoFromNet( ComCode.siteno_otona_novel , url, ComCode.apicache_novelinfo_hoursago_0);
                }
                else if(ComCode.is_maho_novel_novelurl(url)){
                    String[] list = url.split("#");
                    if (list.length>0){
                        mPresenter.refreshNovelInfoFromNet( ComCode.siteno_maho , list[0], ComCode.apicache_novelinfo_hoursago_0);
                    }
                }
                else if(ComCode.is_noichigo_novel_novelurl(url)){
                    mPresenter.refreshNovelInfoFromNet( ComCode.siteno_noichigo , url, ComCode.apicache_novelinfo_hoursago_0);
//                    Snackbar.make(mfloatingactionButton, url, Snackbar.LENGTH_LONG).show();
                }
                else if(ComCode.is_aozora_novel_novelurl(url)){
                    mPresenter.refreshNovelInfoFromNet( ComCode.siteno_aozora , url, ComCode.apicache_novelinfo_hoursago_0);
                }
                else if(ComCode.is_berryscafe_novel_novelurl(url)){
                    mPresenter.refreshNovelInfoFromNet( ComCode.siteno_berryscafe, url, ComCode.apicache_novelinfo_hoursago_0);
//                    Snackbar.make(mfloatingactionButton, url, Snackbar.LENGTH_LONG).show();
                }
                else if(ComCode.is_kakuyomu_novel_novelurl(url)){
                    mPresenter.refreshNovelInfoFromNet( ComCode.siteno_kakuyomu, url, ComCode.apicache_novelinfo_hoursago_0);
                }
                else if(ComCode.is_Alphapolis_novelurl(url)){
//                    mPresenter.refreshBookDetail( ComCode.siteno_alphapolis , url);
                }else{
//                    Snackbar.make(mfloatingactionButton, url, Snackbar.LENGTH_LONG).show();
                }
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                Toast t = Toast.makeText(NovelMyWebActivity.this, url, Toast.LENGTH_LONG);
//                t.show();
//                txtviewtitle.setText(url);
                if(ComCode.is_Alphapolis_novelurl(url)){
//                    Snackbar.make(mfloatingactionButton, url, Snackbar.LENGTH_SHORT).show();
                    if(webView1!=null){
                        mParseUrl = url;
                        webView1.loadUrl("javascript:window.HtmlViewer.showHTML" +
                                "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                    }
                }
                if(webView1==null){
                    return;
                }
                if(webView1.canGoBack()){
                    btnBack.setEnabled(true);
                }else{
                    btnBack.setEnabled(false);
                }
                if(webView1.canGoForward()){
                    btnForward.setEnabled(true);
                }else{
                    btnForward.setEnabled(false);
                }
            }
        });

        // Load the webpage
        webView1.loadUrl(this.mInitURL);
        mfloatingactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfoView();
            }
        });
//        WebSettings settings = webView1.getSettings();
//        settings.setDefaultFontSize(20);
//        webView1.loadUrl("file://"+filepath);
//        webView1.loadUrl("javascript:(document.body.style.backgroundColor ='red');");
//        webView1.loadUrl("javascript:(document.body.style.color ='yellow');");
//        webView1.loadUrl("javascript:(document.body.style.fontSize ='20pt');");
        btnReadNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readnow();
            }
        });
        btnAddBookShelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addtoBookShelf();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideInfoView();
            }
        });
        btninfoviewclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideInfoView();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(webView1.canGoBack()){
                    webView1.goBack();
                }
            }
        });
        btnForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(webView1.canGoForward()){
                    webView1.goForward();
                }
            }
        });
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView1.reload();
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        hideInfoView();
    }
    class MyJavaScriptInterface {
        private Context ctx;
        MyJavaScriptInterface(Context ctx) {
            this.ctx = ctx;
        }
        @SuppressLint("JavascriptInterface")
        @JavascriptInterface
        public void showHTML(String html) {
//            new AlertDialog.Builder(ctx).setTitle("HTML").setMessage(html)
//                    .setPositiveButton(android.R.string.ok, null).setCancelable(false).create().show();
            NovelInfoBean bean = NovelParseAlphapolis.parse_novelinfo_html(html,mParseUrl);
            if(bean!= null && bean.novelinfodata.err == false
                    && bean.novelinfodata.novel_url !=null
                    && bean.novelinfodata.novel_url.length() >0){
                finishRefresh(bean);
            }
        }
    }

    //----------------- NovelBookDetailContract.View---------------------------------

    public void hideInfoView(){
        infoview.setVisibility(View.INVISIBLE);
        if(mNovelBean!=null){
            this.mfloatingactionButton.setVisibility(View.VISIBLE);
        }else{
            this.mfloatingactionButton.setVisibility(View.INVISIBLE);
        }
    }
    public void showInfoView(){
        resetBtnAddBookShelf();
        infoview.setVisibility(View.VISIBLE);
        this.mfloatingactionButton.setVisibility(View.INVISIBLE);
    }
    public void readnow(){
        if(mNovelBean==null) return;
        for(NovelInfoChapterdata item :this.mNovelBean.novelinfodata.chapteritems){
            if(item.chapter_url !=null && item.chapter_url.length() >0){
                downloadstart(item.no);
                return;
            }
        }
    }
    public void downloadstart(int pos){
        if(mDownloadManager == null){
            int pageno = 0;
            if(ComCode.isMultWebPages(this.mNovelBean.novelinfodata.siteno)){
                pageno = this.mNovelBean.novelinfodata.chapteritems.get(pos).chapter_pagestart;
            }
            mDownloadManager = new NovelDownloadManager(this,
                    mNovelBean,
                    mNovelBean.novelinfodata.chapteritems.get(pos),
                    this,
                    true,
                    pageno
                    );
            mDownloadManager.DownloadStart();
        }else{
            return;
        }
    }
    public void downloadStart(boolean openTxtReader){
        if(mSVProgressHUD==null){
            mSVProgressHUD = new SVProgressHUD(this);
        }
        mSVProgressHUD.show();
    }
    public void downloadFail(){
        if(mSVProgressHUD!=null && mSVProgressHUD.isShowing()){
            mSVProgressHUD.showErrorWithStatus("ファイルダウンロード失敗");
        }
    }
    public void downloadOk(String filepath, NovelInfoBean mNovelBean,NovelInfoChapterdata mChapterBean,
                           String chapterurl,int pageno, boolean openTxtReader){
        if(mSVProgressHUD!=null && mSVProgressHUD.isShowing()){
            mSVProgressHUD.dismiss();
        }
        mDownloadManager = null;
        if(ComCode.isSingleWebPage(this.mNovelBean.novelinfodata.siteno)){
            ReaderCom.openSingleHtmlReader(null,
                    mNovelBean,mChapterBean,pageno);
        }else if(ComCode.isMultWebPages(this.mNovelBean.novelinfodata.siteno) ){
            ReaderCom.openWebReader(mNovelBean,mChapterBean);
        }else{
            ReaderCom.openTxtReader(filepath,
                    mNovelBean,mChapterBean,pageno,true);
        }
    }
    public void addtoBookShelf(){
        if(mNovelBean==null) return;
        DBManagerReadBookShelf.addToBookShelf(getApplicationContext(),mNovelBean);
        resetBtnAddBookShelf();
    }
    public void resetBtnAddBookShelf(){
        if(DBManagerReadBookShelf.hadAddToBookShelf(mNovelBean.novelinfodata.novel_url)){
            this.btnAddBookShelf.setVisibility(View.GONE);
            this.btnAddBookShelf.setEnabled(false);
            this.btnAddBookShelf.setText("追加済み");
        }else{
            this.btnAddBookShelf.setVisibility(View.VISIBLE);
            this.btnAddBookShelf.setEnabled(true);
            this.btnAddBookShelf.setText("本棚に追加");
        }
    }

//----------------- NovelBookDetailContract.View---------------------------------
    @Override
    public void finishRefresh(NovelInfoBean bean){
        if(bean!=null && bean.novelinfodata.novel_url !=null && bean.novelinfodata.novel_url.length() >0){
            mNovelBean = bean;
            String str = String.format("✨小説発見「%s」", bean.novelinfodata.novel_title);
            txtviewtitle.setText(str);
            showInfoView();
        }
//        Snackbar.make(mfloatingactionButton, str, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void waitToBookShelf(){}
    @Override
    public void errorToBookShelf(){}
    @Override
    public void succeedToBookShelf(){}
    @Override
    public void showError() {}
    @Override
    public void complete() {}
    @Override
    protected int getContentId() {
        return R.layout.activity_novel_webview;
    }
    @Override
    protected NovelBookDetailContract.Presenter bindPresenter() {
        return new NovelBookDetailPresenter();
    }
}
