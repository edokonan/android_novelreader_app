package com.zuk.ireader.ui.adapter.novel;

import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zuk.ireader.R;
import com.zuk.ireader.business.config.ComCode;
import com.zuk.ireader.business.config.ComWebReaderSet;
import com.zuk.ireader.business.download.NovelDownloadManager;
import com.zuk.ireader.business.download.NovelDownloadManagerInterface;
import com.zuk.ireader.model.bean.novel.NovelChapterPageForWeb;
import com.zuk.ireader.model.bean.novel.NovelInfoBean;
import com.zuk.ireader.model.bean.novel.NovelInfoChapterdata;
import com.zuk.ireader.realm.DBManagerBookChapter;
import com.zuk.ireader.ui.base.adapter.ViewHolderImpl;

import org.jsoup.nodes.Document;

/**
 * Created by ksymac on 2018/12/9.
 */
public class NovelChapterWebPageHolder extends ViewHolderImpl<NovelChapterPageForWeb> implements NovelDownloadManagerInterface {

    NovelChapterPageForWeb mBean;
//    TableNovelChapter mChapterBean;
    private TextView mItemTitle;
    private WebView mWebPage;

    LinearLayout ll_downloading;
    TextView item_downloading;
    @Override
    public void initView() {
        mItemTitle = findById(R.id.item_title);
        mWebPage = findById(R.id.webpage);
        ll_downloading = findById(R.id.ll_downloading);
        item_downloading = findById(R.id.item_downloading);

    }

    @Override
    public void onBind(NovelChapterPageForWeb value, int pos) {
        mBean = value;
        mItemTitle.setText(String.format("pageno %d", value.pageno));
        mItemTitle.setVisibility(View.GONE);
        mItemTitle.setBackgroundColor(Color.parseColor(ComWebReaderSet.getWebBackGroundColor()));
        mWebPage.setBackgroundColor(Color.parseColor(ComWebReaderSet.getWebBackGroundColor()));
//        loadDoc(value.strhtml);
        if( mBean.tbbean != null){
            loadDoc(mBean.tbbean.strhtml);
        }else{
            downloadThisPage();
        }
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.novel_item_webpage;
    }

    public void loadDoc(String inhtm){
        ll_downloading.setVisibility(View.GONE);

        String strhtml = inhtm;
        String fontsize = String.format("font-size:%dpx;",ComWebReaderSet.getWebFontSize());
        String bgcolor = String.format("background-color:%s;",ComWebReaderSet.getWebBackGroundColor());

        //Connect to website
        String str1 =
                "     <html> \n" +
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
                        "     window.onload = function(){\n" +
                        "     var $img = document.getElementsByTagName('img');\n" +
                        "     for(var p in  $img){\n" +
                        "      $img[p].style.width = '100%';\n" +
                        "      $img[p].style.height ='auto';\n" +
                        "     }\n" +
                        "     }" +
                        "\n" +
                        "   function setbackgroundColor(color) {\n" +
                        "       document.body.style.backgroundColor = color; \n" +
                        "   }\n" +
                        "   function fontsize(size) {\n" +
                        "       document.body.style.fontSize = size; \n" +
                        "   }\n" +
                        "     </script>";

        String str2 ="\n</body></html>";

        String novelTitle = String.format("<h3> %s </h3>",this.mBean.mNovelBean.novelinfodata.novel_title);
        String chapterTitle = String.format("<B> %s </B>",this.mBean.mChapterBean.chapter_title);
//        String nowpage = "";
        String hrstr = "<hr width=\"100%\">";
        String nowpage = String.format("<div align='right'><h6> page.%d </h6></div>",mBean.pageno);
        String htm = str1 + nowpage  + strhtml + str2;

        if(mBean.pos ==0){
            htm = str1  + novelTitle + chapterTitle  + strhtml + str2;
            //+ hrstr
            //+ nowpage
        }
        mWebPage.loadDataWithBaseURL(ComCode.getBaseUrl(mBean.mNovelBean.novelinfodata.siteno),
                htm,"text/html",
                "UTF-8",null);
        WebSettings settings = mWebPage.getSettings();
        settings.setJavaScriptEnabled(true); //Javascript実行可能に設定
        mWebPage.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                //ページ読み込みが完了してからJavascriptが利用可能になる
                onScriptReady();
            }
        });
    }

    private void onScriptReady() {
//        startValidation();
    }
    //関数の存在チェック
    private void startValidation() {
        String setcorlorfunc = "";
        String newcolor =  ComWebReaderSet.getWebBackGroundColor();
        if (newcolor!=null){
            setcorlorfunc = String.format("setbackgroundColor('%s')",newcolor);
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWebPage.evaluateJavascript(setcorlorfunc, null);
//            mWebPage.evaluateJavascript("fontsize(30)", null);
        } else {
            mWebPage.loadUrl(setcorlorfunc);
//            mWebPage.loadUrl("javascript:fontsize(30)");
        }
    }
    //  #ebf0f2
    //    255  #FFFFFF
    //    237 255 208  #EDFFD0
    //    224 205 182  #E0CDB6
    //    183 207 194  #B7CFC2
    //    209 209 209  #D1D1D1
    NovelDownloadManager mDownloadManager;
    public void downloadThisPage(){
        if(mDownloadManager == null){
            DBManagerBookChapter.insertChapterInfo(mBean.mNovelBean,
                    mBean.mChapterBean,
                    mBean.pageno);

            mDownloadManager = new NovelDownloadManager(this.getContext(),
                    mBean.mNovelBean,
                    mBean.mChapterBean,
                    this,
                    true,
                    mBean.pageno);
            mDownloadManager.DownloadStart();
        }else{
            return;
        }
    }
    public void downloadStart(boolean openTxtReader){
//        mSVProgressHUD = new SVProgressHUD(this);
//        mSVProgressHUD.show();
    }
    public void downloadFail(){
//        if(mSVProgressHUD!=null && mSVProgressHUD.isShowing()){
//            mSVProgressHUD.showErrorWithStatus("ファイルダウンロード失敗");
//        }
    }
    public void downloadOk(String filepath,
                           NovelInfoBean mNovelBean,
                           NovelInfoChapterdata mChapterBean,
                           String pageurl,int pageno, boolean openTxtReader){
//        if(mSVProgressHUD!=null && mSVProgressHUD.isShowing()){
//            mSVProgressHUD.dismiss();
//        }
//        FilePath = filepath;
        mDownloadManager = null;
//        finish();
        mBean.tbbean = DBManagerBookChapter.getChapterInfo(mChapterBean.chapter_url,pageno);
        loadDoc(mBean.tbbean.strhtml);
    }
}
