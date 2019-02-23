package com.zuk.ireader.ui.activity.web;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import com.google.gson.Gson;
import com.zuk.ireader.R;
import com.zuk.ireader.model.bean.novel.NovelInfoChapterdata;
import com.zuk.ireader.model.bean.novel.NovelInfoBean;
import com.zuk.ireader.business.config.ComCode;
import com.zuk.ireader.realm.DBManagerApiCache;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.bifan.txtreaderlib.main.TxtConfig;
import com.bifan.txtreaderlib.ui.HwTxtPlayActivity;

/**
 * Created by ksymac on 2018/12/17.
 */

public class NovelWebViewActivity extends Activity {
    private static final String extra_novel_chapter_url = "extra_novel_chapter_url";
    private static final String extra_novel_url = "extra_novel_url";
    WebView webView1;
    Document doc;
    File txtfile;
    Button btnReadNow;
    String mNovelChapterUrl;
    String mNovelUrl;
    NovelInfoBean mNovelBean;
    NovelInfoChapterdata mChapterItem;
//    public static void startActivity(Context context, String filepath) {
//        Intent intent = new Intent(context, com.zuk.ireader.ui.activity.novel.NovelWebViewActivity.class);
//        intent.putExtra(EXTRA_BOOK_ID, filepath);
//        context.startActivity(intent);
//    }
    public static void startActivity(Context context, String novelurl, String chapterurl) {
        Intent intent = new Intent(context, NovelWebViewActivity.class);
        intent.putExtra(extra_novel_url, novelurl);
        intent.putExtra(extra_novel_chapter_url, chapterurl);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novel_webview);
        webView1 = (WebView)findViewById(R.id.webview1);

        mNovelUrl = getIntent().getStringExtra(extra_novel_url);
        mNovelChapterUrl = getIntent().getStringExtra(extra_novel_chapter_url);

        String str = DBManagerApiCache.getJsouFromCache(mNovelUrl, ComCode.apiver);
        if(str != null){
            mNovelBean = new Gson().fromJson(str,NovelInfoBean.class);
            for (NovelInfoChapterdata item : mNovelBean.getChapterItems()){
                if(item.chapter_url.equals(mNovelChapterUrl)){
                    mChapterItem = item;
                }
            }
        }

//        DBManagerBookChapter.insertChapterInfo(this.mNovelBean,this.mChapterItem);
//        DBManagerReadBookShelf.updateChapterToBookShelf(this.mNovelBean.novelinfodata.novel_url, this.mChapterItem.chapter_url);
//        DBManagerReadHistory.insertHistory(this.mNovelBean.novelinfodata.novel_url, this.mChapterItem.chapter_url);

        MyTask task = new MyTask();
        task.urllink = mNovelChapterUrl;
        task.activity = this;
        task.execute();

        WebSettings settings = webView1.getSettings();
        settings.setDefaultFontSize(20);
//        webView1.loadUrl("file://"+filepath);
//        webView1.loadUrl("javascript:(document.body.style.backgroundColor ='red');");
//        webView1.loadUrl("javascript:(document.body.style.color ='yellow');");
//        webView1.loadUrl("javascript:(document.body.style.fontSize ='20pt');");
        btnReadNow =  (Button) findViewById(R.id.btnReadNow);
        btnReadNow.setOnClickListener(
                (V) -> {
                    openReader();
        });
    }
    public void loadDoc(Document doc){
        //Connect to website
        Element element = doc.select("#novel_color").first();
        writeToFile(element.html(),this);
        webView1.loadDataWithBaseURL("https://ncode.syosetu.com",element.html(),"text/html", "UTF-8",null);
    }

    private void writeToFile(String data,Context context) {
        try {
            String newstr = cleanPreserveLineBreaks(data);
            String filename = String.format("noveldata/%s/%s", "2", "test.txt");
            if(this.mNovelBean!=null && this.mChapterItem!=null){
                filename = String.format("noveldata/%s/%s_%s.txt", "2", mNovelBean.novelinfodata.novel_original_no,mChapterItem.no);
            }
            txtfile = new File(context.getExternalFilesDir(null), filename);
            if (!txtfile.exists()){
                txtfile.getParentFile().mkdirs();
                txtfile.createNewFile();
            }
            FileOutputStream stream = new FileOutputStream(txtfile,false);
//            OutputStreamWriter outputStreamWriter =
//                    new OutputStreamWriter(context.openFileOutput("novel.txt", Context.MODE_PRIVATE));
//            outputStreamWriter.write(data);
//            outputStreamWriter.close();
            try {
                stream.write(newstr.getBytes());
            } finally {
                stream.close();
            }
//            DBManagerBookChapter.updateTxtFilepath(this.mChapterItem.chapter_url,
//                    this.downloadpageurl,
//                    this.page_no,
//                    txtfile.getPath());
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
    public  String cleanPreserveLineBreaks(String bodyHtml) {
        // get pretty printed html with preserved br and p tags
        String prettyPrintedBodyFragment = Jsoup.clean(bodyHtml, "", Whitelist.none().addTags("br", "p"), new Document.OutputSettings().prettyPrint(true));
        // get plain text with preserved line breaks by disabled prettyPrint
        return Jsoup.clean(prettyPrintedBodyFragment, "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));
    }
    public void openReader(){
        TxtConfig.saveIsOnVerticalPageMode(this,false);
        TxtConfig.saveTextSize(this, 45);
        HwTxtPlayActivity.loadTxtFile(this, txtfile.getPath());
    }

    private void textSmaller() {
        WebSettings settings = webView1.getSettings();
        settings.setTextZoom(settings.getTextZoom() - 10);
    }

    private void textBigger() {
        WebSettings settings = webView1.getSettings();
        settings.setTextZoom(settings.getTextZoom() + 10);
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

    private class MyTask extends AsyncTask<Void, Void, String> {
        public String urllink = "";
        Document doc;
        NovelWebViewActivity activity;
        @Override
        protected String doInBackground(Void... params) {
            String title ="";
            try {
                doc = Jsoup.connect(urllink).get();
                title = doc.title();
                System.out.print(title);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return title;
        }
        @Override
        protected void onPostExecute(String result) {
            activity.loadDoc(doc);
        }
    }


}
