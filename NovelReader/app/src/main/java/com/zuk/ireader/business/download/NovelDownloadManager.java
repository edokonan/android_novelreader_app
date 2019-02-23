package com.zuk.ireader.business.download;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.zuk.ireader.model.bean.novel.NovelInfoChapterdata;
import com.zuk.ireader.model.bean.novel.NovelInfoBean;
import com.zuk.ireader.realm.DBManagerBookChapter;
import com.zuk.ireader.business.config.ComCode;
import com.zuk.ireader.ui.adapter.novel.NovelChapterWebPageHolder;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import static java.lang.Thread.sleep;


/**
 * Created by ksymac on 2018/12/25.
 */
public class NovelDownloadManager {

    Context mContext;
    NovelInfoBean mNovelBean;
    NovelInfoChapterdata mChapterBean;

    public String siteno;
    public String chapter_no;
    public int page_no;
    public String downloadpageurl;
    public String novel_original_no;
    Document mDoc;
    NovelDownloadManagerInterface impl;
    MyTask task;
    public File txtfile;
    public Boolean mDownloadOK = false;
    public Boolean mOpenReader = false;


    public NovelDownloadManager(Context context,
                                NovelInfoBean novelBean,
                                NovelInfoChapterdata chapterbean,
                                NovelDownloadManagerInterface impl,
                                Boolean openReader,
                                int pageno){
        this.mContext = context;

        this.mNovelBean = novelBean;
        this.mChapterBean = chapterbean;

        this.siteno = novelBean.novelinfodata.siteno;
        this.novel_original_no = novelBean.novelinfodata.novel_original_no;
        this.chapter_no = chapterbean.chapter_no;
        this.downloadpageurl = chapterbean.chapter_url;
        this.page_no = pageno;
//        this.mChapterBean.currentpageno = pageno;
        if(this.siteno.equalsIgnoreCase(ComCode.siteno_estar) ){
            this.downloadpageurl = String.format("https://estar.jp/_work_viewer?p=%d&w=%s",
                    this.page_no,novelBean.novelinfodata.novel_original_no);
        }
        if(this.siteno.equalsIgnoreCase(ComCode.siteno_estar_r) ){
            this.downloadpageurl = String.format("https://r.estar.jp/_work_viewer?p=%d&w=%s",
                    this.page_no,novelBean.novelinfodata.novel_original_no);
        }
        if(this.siteno.equalsIgnoreCase(ComCode.siteno_otona_novel) ){
            this.downloadpageurl = String.format("https://otona-novel.jp/viewstory/page/%s/%d/?guid=ON",
                    novelBean.novelinfodata.novel_original_no,this.page_no);
        }
        if(this.siteno.equalsIgnoreCase(ComCode.siteno_maho) ){
            this.downloadpageurl = String.format("https://s.maho.jp/book/%s%d/",
                    novelBean.novelinfodata.novel_original_no.replace("_","/"),this.page_no);
        }
        if(this.siteno.equalsIgnoreCase(ComCode.siteno_noichigo) ){
            this.downloadpageurl = String.format("https://www.no-ichigo.jp/read/page/book_id/%s/page/%d",
                    novelBean.novelinfodata.novel_original_no,this.page_no);
        }
        if(siteno.equalsIgnoreCase(ComCode.siteno_berryscafe)) {
            this.downloadpageurl = String.format("https://www.berrys-cafe.jp/spn/book/%s/%d/",
                    novelBean.novelinfodata.novel_original_no,this.page_no);
        }
        this.impl = impl;
        this.mDoc = null;
        this.mOpenReader = openReader;
    }
    public void DownloadStart(){
        DBManagerBookChapter.insertChapterInfo(mNovelBean,mChapterBean,this.page_no);
        if(hadDownloaded(mContext)){
            if(impl != null){
                impl.downloadOk(txtfile.getPath(),
                        this.mNovelBean,
                        this.mChapterBean,
                        this.mChapterBean.chapter_url,
                        this.page_no,
                        this.mOpenReader);
            }
        }else{
            if(task == null){
                if(impl != null){
                    impl.downloadStart(this.mOpenReader);
                }
                task = new MyTask();
                task.execute();
            }
        }
    }

    public void DownloadFail(){
        if(impl != null){
            impl.downloadFail();
        }
    }
    public void DownloadOK(){
        if(this.siteno.equalsIgnoreCase(ComCode.siteno_ncode_syosetu)
                || this.siteno.equalsIgnoreCase(ComCode.siteno_novel18_syosetu)){
            if(mDoc != null){
                Element element_title = mDoc.select(".novel_subtitle").first();
                Element element = mDoc.select("#novel_honbun").first();

                String newstr = cleanPreserveLineBreaks(element_title.html() + element.html());
                writeToFile(newstr, mContext);
            }
        }
        if(this.siteno.equalsIgnoreCase(ComCode.siteno_alphapolis)){
            Element novel_body = mDoc.select("div#novelBoby").last();
            if(novel_body!=null){
                String newstr = cleanPreserveLineBreaks(novel_body.html());
                writeToFile(newstr, mContext);
                DBManagerBookChapter.updateContentHtml(mChapterBean.chapter_url,
                        this.downloadpageurl,
                        this.page_no,
                        novel_body.html());
            }
        }
        if(this.siteno.equalsIgnoreCase(ComCode.siteno_kakuyomu)){
            Element novel_body = mDoc.select("div#contentMain-inner").last();
            if(novel_body!=null){
                String newstr = cleanPreserveLineBreaks(novel_body.html());
                writeToFile(newstr, mContext);
                DBManagerBookChapter.updateContentHtml(mChapterBean.chapter_url,
                        this.downloadpageurl,
                        this.page_no,
                        novel_body.html());
            }
        }
        if(this.siteno.equalsIgnoreCase(ComCode.siteno_aozora)){
            if(mDoc!=null){
                String newstr = cleanPreserveLineBreaks(mDoc.html());
                writeToFile(newstr, mContext);
                DBManagerBookChapter.updateContentHtml(mChapterBean.chapter_url,
                        this.downloadpageurl,
                        this.page_no,
                        mDoc.html());
            }
        }

        if(siteno.equalsIgnoreCase(ComCode.siteno_estar) || siteno.equalsIgnoreCase(ComCode.siteno_estar_r)){
            Element novel_content = mDoc.select("div.m-viewer__pageContent").first();
            if(novel_content!=null){
                Element novel_body = novel_content.select("div.m-viewer__body").last();
                if(novel_body!=null){
//                    String newstr = cleanPreserveLineBreaks(data);
                    String str = novel_content.text();
                    writeToFile(str, mContext);
                    DBManagerBookChapter.updateContentHtml(mChapterBean.chapter_url,
                            this.downloadpageurl,
                            this.page_no,
                            novel_body.html());
                }
            }
        }
        if(siteno.equalsIgnoreCase(ComCode.siteno_otona_novel)){
            Element novel_body = mDoc.select("div#changeArea").first();
            if(novel_body!=null){
                String str = novel_body.text();
                writeToFile(str, mContext);
                DBManagerBookChapter.updateContentHtml(mChapterBean.chapter_url,
                        this.downloadpageurl,
                        this.page_no,
                        novel_body.html());
            }
        }
        if(siteno.equalsIgnoreCase(ComCode.siteno_maho)){
            Element novel_body = mDoc.select("div#book-main").first();
            if(novel_body!=null){
                Element novel_ex =novel_body.select("div.inner").first();
                if(novel_ex!=null) {
                    String str = novel_ex.text();
                    writeToFile(str, mContext);
                    DBManagerBookChapter.updateContentHtml(mChapterBean.chapter_url,
                            this.downloadpageurl,
                            this.page_no,
                            novel_ex.html());
                }
            }
        }
        if(siteno.equalsIgnoreCase(ComCode.siteno_noichigo)){
            Element novel_ex = mDoc.select("div#content-maincol").first();
            if(novel_ex!=null){
                String str = novel_ex.text();
                writeToFile(str, mContext);
                DBManagerBookChapter.updateContentHtml(mChapterBean.chapter_url,
                        this.downloadpageurl,
                        this.page_no,
                        novel_ex.html());
            }
        }

        if(siteno.equalsIgnoreCase(ComCode.siteno_berryscafe)){
            Element novel_ex = mDoc.select("div#content > p").first();
            if(novel_ex!=null){
                String str = novel_ex.text();
                writeToFile(str, mContext);
                DBManagerBookChapter.updateContentHtml(mChapterBean.chapter_url,
                        this.downloadpageurl,
                        this.page_no,
                        novel_ex.html());
            }
        }

        mDownloadOK = true;
        if(impl != null){
            impl.downloadOk(this.txtfile.getPath(),
                    this.mNovelBean,
                    this.mChapterBean,
                    this.downloadpageurl,
                    this.page_no,
                    this.mOpenReader);
        }
    }
    public  String cleanPreserveLineBreaks(String bodyHtml) {
        // get pretty printed html with preserved br and p tags
        String prettyPrintedBodyFragment = Jsoup.clean(bodyHtml, "", Whitelist.none().addTags("br", "p"), new Document.OutputSettings().prettyPrint(true));
        // get plain text with preserved line breaks by disabled prettyPrint
        return Jsoup.clean(prettyPrintedBodyFragment, "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));
    }

    private String getFilename(){
        String filename = String.format("noveldata/%s/%s_%s.txt", siteno, novel_original_no,chapter_no);
        if(ComCode.isMultWebPages(this.siteno)){
            filename = String.format("noveldata/%s/%s_page_%d.txt", siteno, novel_original_no,this.page_no);
        }
        if(ComCode.isSingleWebPage(this.siteno)){
            filename = String.format("noveldata/%s/%s_page_%d.txt", siteno, novel_original_no,this.page_no);
        }
        return filename;
    }
    private boolean hadDownloaded(Context context){
        String filename = getFilename();
        txtfile = new File(context.getExternalFilesDir(null), filename);
        boolean ret = DBManagerBookChapter.haveTxtFile(this.mChapterBean.chapter_url,this.page_no);
        if(ret){
            return true;
        }else{
            return false;
        }
    }

    private void writeToFile(String str,Context context) {
        try {
            String filename = getFilename();

            txtfile = new File(context.getExternalFilesDir(null), filename);
            if (!txtfile.exists()){
                txtfile.getParentFile().mkdirs();
                txtfile.createNewFile();
            }else{
                txtfile.delete();
                txtfile.createNewFile();
            }
//            str += " /n  ";
            FileOutputStream stream = new FileOutputStream(txtfile,false);
            try {
                stream.write(str.getBytes());
            } finally {
                stream.close();
            }
            DBManagerBookChapter.updateTxtFilepath(mChapterBean.chapter_url,
                    this.downloadpageurl,
                    this.page_no,
                    txtfile.getPath());
            Log.i("info", "File write to: " + txtfile.getPath());
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
        //            OutputStreamWriter outputStreamWriter =
        //                    new OutputStreamWriter(context.openFileOutput("novel.txt", Context.MODE_PRIVATE));
        //            outputStreamWriter.write(data);
        //            outputStreamWriter.close();
    }
    private class MyTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String title ="";
            try {
                if(siteno.equalsIgnoreCase(ComCode.siteno_ncode_syosetu)
                        || siteno.equalsIgnoreCase(ComCode.siteno_estar)
                        || siteno.equalsIgnoreCase(ComCode.siteno_estar_r)
                        || siteno.equalsIgnoreCase(ComCode.siteno_maho)
                        || siteno.equalsIgnoreCase(ComCode.siteno_kakuyomu)
                        || siteno.equalsIgnoreCase(ComCode.siteno_aozora)
                        || siteno.equalsIgnoreCase(ComCode.siteno_noichigo)
                        || siteno.equalsIgnoreCase(ComCode.siteno_berryscafe)
                        ){
                    mDoc = Jsoup.connect(downloadpageurl).get();
                }
                if(siteno.equalsIgnoreCase(ComCode.siteno_novel18_syosetu)
                        || siteno.equalsIgnoreCase(ComCode.siteno_otona_novel)){
//                    String url = "https://www.alphapolis.co.jp/novel/";
//                    Connection.Response res = Jsoup.connect(url)
//                            .method(Connection.Method.GET)
//                            .timeout(10000)
//                            .execute();
//                    Map<String, String> cookies = res.cookies(); .timeout(5*1000)
                    mDoc = Jsoup.connect(downloadpageurl).cookie("over18","yes").get();
                }
                if(siteno.equalsIgnoreCase(ComCode.siteno_alphapolis)){
                    String url = "https://www.alphapolis.co.jp/novel/";
                    Connection.Response res = Jsoup.connect(url)
                                .method(Connection.Method.GET)
                                .timeout(10000)
                                .execute();
//                        sleep(100);
                    Map<String, String> cookies = res.cookies();
                    mDoc = Jsoup.connect(downloadpageurl)
                                .cookies(cookies)
                                .get();
                }

                title = mDoc.title();
                System.out.print(title);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return title;
        }
        @Override
        protected void onPostExecute(String result) {
            if(mDoc==null){
                DownloadFail();
            }else{
                DownloadOK();
            }
        }
    }
}
