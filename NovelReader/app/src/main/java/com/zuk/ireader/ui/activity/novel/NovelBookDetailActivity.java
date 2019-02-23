package com.zuk.ireader.ui.activity.novel;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.bumptech.glide.Glide;
import com.zuk.ireader.business.config.ComRate;
import com.zuk.ireader.business.download.NovelDownloadManager;
import com.zuk.ireader.business.download.NovelDownloadManagerInterface;
import com.zuk.ireader.R;
import com.zuk.ireader.business.download.PackageDownload;
import com.zuk.ireader.business.config.ReaderCom;
import com.zuk.ireader.business.download.PackageDownloadInterface;
import com.zuk.ireader.model.bean.novel.NovelInfoChapterdata;
import com.zuk.ireader.model.bean.novel.NovelInfoBean;
import com.zuk.ireader.business.config.ComCode;
import com.zuk.ireader.business.parse.NovelParseAlphapolis;
import com.zuk.ireader.presenter.novel.NovelBookDetailContract;
import com.zuk.ireader.presenter.novel.NovelBookDetailPresenter;
import com.zuk.ireader.realm.DBManagerBookChapter;
import com.zuk.ireader.realm.DBManagerReadBookShelf;
import com.zuk.ireader.realm.DBManagerReadHistory;
import com.zuk.ireader.realm.TableNovelChapter;
import com.zuk.ireader.ui.adapter.novel.NovelChapterItemAdapter;
import com.zuk.ireader.ui.base.BaseMVPActivity;
import com.zuk.ireader.ui.base.adapter.BaseListAdapter;
import com.zuk.ireader.utils.ToastUtils;
import com.zuk.ireader.widget.RefreshLayout;
import com.zuk.ireader.widget.itemdecoration.DividerItemDecoration;

import java.util.List;

import butterknife.BindView;
import hotchemi.android.rate.AppRate;

/**
 * Created by ksymac on 2018/12/2.
 */
public class NovelBookDetailActivity extends BaseMVPActivity<NovelBookDetailContract.Presenter>
        implements NovelBookDetailContract.View, NovelDownloadManagerInterface, PackageDownloadInterface {
    public static final String RESULT_IS_COLLECTED = "result_is_collected";
    private static final String TAG = "NovelBookDetailActivity";

//    private static final String EXTRA_Book_Url = "extra_book_url";
    private static final String EXTRA_Site_No = "extra_site_no";
    private static final String EXTRA_Novel_URL = "extra_novel_url";
//    private static final String EXTRA_Book_Data = "extra_book_data";

    private long downloadID ;
    private String mDownloadFilename;
    private SVProgressHUD mSVProgressHUD;
    private static final int REQUEST_READ = 1;

    @BindView(R.id.refresh_layout)
    RefreshLayout mRefreshLayout;
    @BindView(R.id.book_detail_iv_cover)
    ImageView mIvCover;
    @BindView(R.id.book_detail_tv_title)
    TextView mTvTitle;
    @BindView(R.id.book_detail_tv_author)
    TextView mTvAuthor;
    @BindView(R.id.book_detail_tv_type)
    TextView mTvType;
//    @BindView(R.id.book_detail_tv_word_count)
//    TextView mTvWordCount;
    @BindView(R.id.book_detail_tv_lately_update)
    TextView mTvLatelyUpdate;
    @BindView(R.id.book_list_tv_chase)
    TextView mTvChase;
    @BindView(R.id.book_detail_download)
    TextView book_detail_download;


    @BindView(R.id.book_detail_tv_read)
    TextView mTvRead;
//    @BindView(R.id.book_detail_tv_follower_count)
//    TextView mTvFollowerCount;
//    @BindView(R.id.book_detail_tv_retention)
//    TextView mTvRetention;
//    @BindView(R.id.book_detail_tv_day_word_count)
//    TextView mTvDayWordCount;
    @BindView(R.id.book_detail_tv_brief)
    TextView mTvBrief;
//    @BindView(R.id.book_detail_tv_more_comment)
//    TextView mTvMoreComment;
    @BindView(R.id.book_detail_rv_hot_comment)
    RecyclerView mRvHotComment;
//    @BindView(R.id.book_detail_rv_community)
//    RelativeLayout mRvCommunity;
//    @BindView(R.id.book_detail_tv_community)
//    TextView mTvCommunity;
//    @BindView(R.id.book_detail_tv_posts_count)
//    TextView mTvPostsCount;
//    @BindView(R.id.book_list_tv_recommend_book_list)
//    TextView mTvRecommendBookList;
//    @BindView(R.id.book_detail_rv_recommend_book_list)
//    RecyclerView mRvRecommendBookList;

    @BindView(R.id.webview_scrollview)
    ScrollView webview_scrollview;

    @BindView(R.id.webview1)
    WebView novelInfoWebView;

    @BindView(R.id.floatingactionButton)
    FloatingActionButton mfloatingactionButton;

    String mParseNovelUrl;

    /************************************/
    private NovelChapterItemAdapter mHotCommentAdapter;
//    private BookListAdapter mBookListAdapter;
//    private CollBookBean mCollBookBean;
    private ProgressDialog mProgressDialog;
    private ProgressDialog mPackageDownloadProgressDialog;
    private PackageDownload mPackageDownload;
    /*******************************************/
    private String mSiteNo;
    private String novelurl;
    int i=0;
//    private NovelGenreDataBookData mBookData;
    private NovelInfoBean mNovelBean;

    private boolean isBriefOpen = false;
    private boolean isCollected = false;

    public static void startActivity(Context context, String siteno, String  novelurl) {
        Intent intent = new Intent(context, com.zuk.ireader.ui.activity.novel.NovelBookDetailActivity.class);
        intent.putExtra(EXTRA_Site_No, siteno);
        intent.putExtra(EXTRA_Novel_URL,novelurl);
//        intent.putExtra(EXTRA_Book_Data,new Gson().toJson(bookData));
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerReceiver(onDownloadComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        mTvRead.setText("最初から読む");
        mTvRead.setVisibility(View.GONE);
        mTvChase.setVisibility(View.GONE);
        book_detail_download.setVisibility(View.GONE);
        book_detail_download.setEnabled(false);
        initWebView();
        novelInfoWebView.loadUrl(novelurl);
    }
    @Override
    public void onResume() {
        super.onResume();
        i += 1;
        mPresenter.refreshNovelInfoFromLocalDB(mSiteNo,novelurl);//mBookId,
        if (ComRate.checkCanPopRate()){
            if(i>1 && AppRate.with(this).shouldShowRateDialog()){
                AppRate.showRateDialogIfMeetsConditions(this);
                ComRate.addEventIsZero();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onDownloadComplete);
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_novel_chapter_list;
    }

    @Override
    protected NovelBookDetailContract.Presenter bindPresenter() {
        return new NovelBookDetailPresenter();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (savedInstanceState != null) {
            mSiteNo = savedInstanceState.getString(EXTRA_Site_No);
        } else {
            mSiteNo = getIntent().getStringExtra(EXTRA_Site_No);
        }
//        String bookJson=getIntent().getStringExtra(EXTRA_Book_Data);
//        mBookData = new Gson().fromJson(bookJson,NovelGenreDataBookData.class);
        novelurl = getIntent().getStringExtra(EXTRA_Novel_URL);
    }

    @Override
    protected void setUpToolbar(Toolbar toolbar) {
        super.setUpToolbar(toolbar);
        getSupportActionBar().setTitle("詳細");
    }

    @Override
    protected void initClick() {
        super.initClick();
        //可伸缩的TextView
        mTvBrief.setOnClickListener(
                (view) -> {
                    if (isBriefOpen) {
                        mTvBrief.setMaxLines(3);
                        isBriefOpen = false;
                    } else {
                        mTvBrief.setMaxLines(9);
                        isBriefOpen = true;
                    }
                }
        );

        mTvChase.setOnClickListener(
                (V) -> {
                    //点击存储
                    if (isCollected) {
                        DBManagerReadBookShelf.removeFromBookShelf(novelurl);
                        //BookRepository.getInstance() .deleteCollBookInRx(mCollBookBean);
                        mTvChase.setText("お気に入りを追加");
                        Drawable drawable = getResources().getDrawable(R.drawable.selector_btn_book_list);
                        mTvChase.setBackground(drawable);
                        //设置图片
                        mTvChase.setCompoundDrawables(ContextCompat.getDrawable(this, R.drawable.ic_book_list_add), null,
                                null, null);
                        isCollected = false;
                    } else {
                        DBManagerReadBookShelf.addToBookShelf(getApplicationContext(),mNovelBean);
                        //mPresenter.addToBookShelf(mCollBookBean);
                        mTvChase.setText("追加済み");
                        Drawable drawable = getResources().getDrawable(R.drawable.shape_common_gray_corner);
                        mTvChase.setBackground(drawable);
                        mTvChase.setCompoundDrawables(ContextCompat.getDrawable(this, R.drawable.ic_book_list_delete), null,
                                null, null);
                        isCollected = true;
                    }
                }
        );
        mTvRead.setOnClickListener(
                (view) -> {
                    openRead();
                }
        );
    }
    public void openRead(){
        TableNovelChapter tbchapter = ReaderCom.getLastReadFromHistory(this.mNovelBean.novelinfodata.novel_url);
        if(tbchapter!=null && tbchapter.txtfilepath != null && tbchapter.txtfilepath.length()>0){
            if(ComCode.isSingleWebPage(this.mNovelBean.novelinfodata.siteno)){
                ReaderCom.openSingleHtmlReader(null,
                        mNovelBean,
                        mNovelBean.novelinfodata.chapteritems.get(tbchapter.no),
                        0);
            }else if(ComCode.isMultWebPages(this.mNovelBean.novelinfodata.siteno)){
                ReaderCom.openWebReader(
                        mNovelBean,
                        mNovelBean.novelinfodata.chapteritems.get(tbchapter.no));
            }else{
                ReaderCom.openTxtReader(tbchapter.txtfilepath,
                        mNovelBean,
                        mNovelBean.novelinfodata.chapteritems.get(tbchapter.no),
                        mNovelBean.novelinfodata.chapteritems.get(tbchapter.no).chapter_pagestart,
                        false);
            }
        }else{
            //开始下载
            for(NovelInfoChapterdata item:mNovelBean.getChapterItems()){
                if(item.chapter_url != null && item.chapter_url.length()>0){
                    this.downloadChapterAt(item,true);
                    return;
                }
            }
        }
    }


    public void downloadStart(boolean openTxtReader){
        if(openTxtReader) {
            if(mSVProgressHUD==null){
                mSVProgressHUD = new SVProgressHUD(this);
            }
            mSVProgressHUD.show();
        }
    }
    public void downloadFail(){
        if(mSVProgressHUD!=null && mSVProgressHUD.isShowing()){
            mSVProgressHUD.showErrorWithStatus("ファイルダウンロード失敗");
        }
    }
    public void downloadOk(String filepath ,NovelInfoBean mNovelBean,
                           NovelInfoChapterdata mChapterBean,
                           String chapterurl, int pageno,
                           boolean openReader){
//        mDownloadManager=null;
        if(mSVProgressHUD!=null)
            mSVProgressHUD.dismissImmediately();
        if(openReader){
            mHotCommentAdapter.notifyItemChanged(mChapterBean.no);
            if(ComCode.isSingleWebPage(this.mNovelBean.novelinfodata.siteno)){
                ReaderCom.openSingleHtmlReader(null,
                        mNovelBean,
                        mChapterBean,
                        pageno);
            }else if(ComCode.isMultWebPages(this.mNovelBean.novelinfodata.siteno)){
                ReaderCom.openWebReader(mNovelBean,mChapterBean);
            }else{
                ReaderCom.openTxtReader(filepath,
                        mNovelBean,mChapterBean,
                        pageno,
                        true);
            }
        }else{
            mHotCommentAdapter.notifyDataSetChanged();
        }
    }
    public void downloadChapterAt(NovelInfoChapterdata item, Boolean openReader){
//        if(mDownloadManager == null){
//        }
        int pageno = 0;
        if(ComCode.isMultWebPages(mNovelBean.novelinfodata.siteno)){
            pageno =  item.chapter_pagestart;
        }
        NovelDownloadManager mDownloadManager =
                new NovelDownloadManager(NovelBookDetailActivity.this,
                mNovelBean,
                item,
                NovelBookDetailActivity.this,
                        openReader,
                pageno);
        mDownloadManager.DownloadStart();
    }

    @Override
    protected void processLogic() {
        super.processLogic();
        mRefreshLayout.showLoading();
        if(mSiteNo.equalsIgnoreCase(ComCode.siteno_alphapolis)){
            mPresenter.refreshNovelInfoFromLocalDB(mSiteNo, novelurl);//mBookId,
        }else{
            mPresenter.refreshNovelInfoFromNet(mSiteNo, novelurl, ComCode.apicache_novelinfo_hoursago_0);//mBookId,
        }
    }

    @Override
    public void finishRefresh(NovelInfoBean bean) {
        mTvRead.setVisibility(View.VISIBLE);
        mTvChase.setVisibility(View.VISIBLE);

        mNovelBean = bean;
        //封面
        if (this.mNovelBean.novelinfodata.novel_imgurl != null && this.mNovelBean.novelinfodata.novel_imgurl.length()>0){
            Glide.with(this)
                    .load(this.mNovelBean.novelinfodata.novel_imgurl)
                    .placeholder(R.drawable.icons8_book_80)
                    .error(R.drawable.icons8_book_80)
                    .centerCrop()
                    .into(mIvCover);
        }else{
            Glide.with(this)
                    .load(R.drawable.icons8_book_80)
                    .placeholder(R.drawable.icons8_book_80)
                    .error(R.drawable.icons8_book_80)
                    .centerCrop()
                    .into(mIvCover);
        }
        //书名
        mTvTitle.setText(mNovelBean.novelinfodata.novel_title);
        //作者
        mTvAuthor.setText(mNovelBean.novelinfodata.novel_writername);
        //类型
        mTvType.setText("");
        //总字数
//        mTvWordCount.setText(getResources().getString(R.string.nb_book_word, bean.getWordCount() / 10000));
        //更新时间
//        mTvLatelyUpdate.setText(StringUtils.dateConvert(bean.getUpdated(), Constant.FORMAT_BOOK_DATE));
        mTvLatelyUpdate.setText(mNovelBean.novelinfodata.getLastUpdateTime());
        //追书人数
//        mTvFollowerCount.setText(bean.getFollowerCount() + "");
        //存留率
//        mTvRetention.setText(bean.getRetentionRatio() + "%");
        //日更字数
//        mTvDayWordCount.setText(bean.getSerializeWordCount() + "");
        //简介
        mTvBrief.setText(mNovelBean.novelinfodata.novel_ex);
        //社区
//        mTvCommunity.setText(getResources().getString(R.string.nb_book_detail_community, bean.getTitle()));
        //帖子数
//        mTvPostsCount.setText(getResources().getString(R.string.nb_book_detail_posts_count, bean.getPostCount()));
//        mCollBookBean = BookRepository.getInstance().getCollBook(bean.get_id());

        isCollected = DBManagerReadBookShelf.hadAddToBookShelf(novelurl);
        if (isCollected){
            mTvChase.setText("追加済み");
            DBManagerReadBookShelf.removeIsUpdate(mNovelBean);

            Drawable drawable = getResources().getDrawable(R.drawable.shape_common_gray_corner);
            mTvChase.setBackground(drawable);
            mTvChase.setCompoundDrawables(ContextCompat.getDrawable(this, R.drawable.ic_book_list_delete),
                    null,
                    null,
                    null);
        }else{
            mTvChase.setText("お気に入りを追加");
            Drawable drawable = getResources().getDrawable(R.drawable.selector_btn_book_list);
            mTvChase.setBackground(drawable);
            //设置图片
            mTvChase.setCompoundDrawables(ContextCompat.getDrawable(this, R.drawable.ic_book_list_add), null,
                    null, null);
        }
        if(DBManagerReadHistory.haveHistoryWithNovelUrl(mNovelBean.novelinfodata.novel_url)){
            mTvRead.setText("続きから読む");
        }else{
            mTvRead.setText("最初から読む");
        }
        finishChapterList(mNovelBean.getChapterItems());
    }



    public void finishChapterList(List<NovelInfoChapterdata> beans) {
        if (beans.isEmpty()) {
            return;
        }
        mHotCommentAdapter = new NovelChapterItemAdapter();
        mRvHotComment.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                //与外部ScrollView滑动冲突
                return true;
            }
        });
        mRvHotComment.addItemDecoration(new DividerItemDecoration(this));
        mRvHotComment.setAdapter(mHotCommentAdapter);
        mHotCommentAdapter.addItems(beans);
        mHotCommentAdapter.setOnItemClickListener(
                (view,pos) -> {
                    NovelInfoChapterdata item = mHotCommentAdapter.getItem(pos);
                    if(item.chapter_url != null && item.chapter_url.length()>0){
                        downloadChapterAt(item,true);
                    }
                }
        );
        mHotCommentAdapter.setOnItemLongClickListener(
                new BaseListAdapter.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(View view, int pos) {
//                        NovelInfoChapterdata item = mHotCommentAdapter.getItem(pos);
//                        if(item.chapter_url != null && item.chapter_url.length()>0){
//                            downloadChapterAt(item,true);
//                        }
                        popdeleteFile(pos);
                        return false;
                    }
                }
        );
        book_detail_download.setVisibility(View.VISIBLE);
        book_detail_download.setEnabled(true);
        book_detail_download.setOnClickListener(
                (view) -> {
                    PackageDownloadStart();
                }
        );
//        for(NovelInfoChapterdata item:beans){
//            if(item.chapter_url != null && item.chapter_url.length()>0){
//                this.downloadChapterAt(item,false);
//                return;
//            }
//        }
    }

    void PackageDownloadStart(){
        if (mPackageDownloadProgressDialog == null) {
            mPackageDownloadProgressDialog = new ProgressDialog(this);
            mPackageDownloadProgressDialog.setTitle("ダウンロード");
            mPackageDownloadProgressDialog.setMessage("downloading");
            mPackageDownloadProgressDialog.setCancelable(false);
            mPackageDownloadProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                    "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    PackageDownloadEnd();
                }
            });
            mPackageDownloadProgressDialog.show();

            if(mPackageDownload == null){
                mPackageDownload = new PackageDownload(this,this,this.mNovelBean);
                mPackageDownload.packagedownloadStart();
            }
        }
    }

    //停止下载
    void PackageDownloadEnd(){
        if(mPackageDownload != null){
            mPackageDownload.stopflg = true;
        }
    }
    //接口：停止下载
    public void PackageDwonloadStoped(){
        if(mPackageDownloadProgressDialog!=null ){
            mPackageDownloadProgressDialog.dismiss();
            mPackageDownloadProgressDialog = null;
        }
        if(mPackageDownload != null){
            mPackageDownload = null;
        }
    }
    public void PackageDownloadOk(){
        if(mPackageDownloadProgressDialog!=null ){
            mPackageDownloadProgressDialog.dismiss();
            mPackageDownloadProgressDialog = null;
        }

        Toast toast = Toast.makeText(this, "ダウンロード完了",  Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
    public void PackageDownRefreshMsg(int current, int total){
        String msg = String.format("ダウンロード中（%d/%d）", current,total);
        if(mPackageDownloadProgressDialog != null){
            mPackageDownloadProgressDialog.setMessage(msg);
        }
    }
    public void PackageDownRefreshTable(int pos){
        mHotCommentAdapter.notifyItemChanged(pos);
    }
    public void PackageDownloadFail(){
        if(mPackageDownloadProgressDialog!=null ){
            mPackageDownloadProgressDialog.dismiss();
            mPackageDownloadProgressDialog = null;
        }
        Toast toast = Toast.makeText(this, "ダウンロード失敗",  Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


    public void popdeleteFile(int pos){
        new AlertDialog.Builder(this)
                .setTitle("削除")
                .setMessage("ダウンロードファイルを削除する")
                .setPositiveButton(getResources().getString(R.string.nb_common_sure), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBManagerBookChapter.delChapterInfo(mNovelBean.novelinfodata.chapteritems.get(pos).chapter_url);
                        finishRefresh(mNovelBean);
                    }
                })
                .setNegativeButton(getResources().getString(R.string.nb_common_cancel), null)
                .show();
    }




//    public void openurl(NovelChapterItemdata item){
//        NovelWebViewActivity.startActivity(this,
//                this.mNovelBean.novelinfodata.novel_url,
//                item.chapter_url );
//    }

    @Override
    public void waitToBookShelf() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setTitle("正在添加到书架中");
        }
        mProgressDialog.show();
    }

    @Override
    public void errorToBookShelf() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        ToastUtils.show("加入书架失败，请检查网络");
    }

    @Override
    public void succeedToBookShelf() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        ToastUtils.show("加入书架成功");
    }

    @Override
    public void showError() {
        mRefreshLayout.showError();
    }

    @Override
    public void complete() {
        mRefreshLayout.showFinish();
    }

    /*******************************************************/
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_Site_No, mSiteNo);
        outState.putString(EXTRA_Novel_URL, novelurl);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //如果进入阅读页面收藏了，页面结束的时候，就需要返回改变收藏按钮
        if (requestCode == REQUEST_READ) {
            if (data == null) {
                return;
            }

            isCollected = data.getBooleanExtra(RESULT_IS_COLLECTED, false);
//            isCollected = DBManagerBookShelf.hadAddToBookShelf(mBookData);
            if (isCollected) {
                mTvChase.setText(getResources().getString(R.string.nb_book_detail_give_up));
                Drawable drawable = getResources().getDrawable(R.drawable.shape_common_gray_corner);
                mTvChase.setBackground(drawable);
                //设置图片
                mTvChase.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(this, R.drawable.ic_book_list_delete), null,
                        null, null);
            }
        }
    }
//    public void  download(NovelInfoChapterdata chapteritem){
//        mSVProgressHUD = new SVProgressHUD(this);
//        mSVProgressHUD.show();
//        DownloadManager downloadmanager = (DownloadManager)this.getSystemService(Context.DOWNLOAD_SERVICE);
//
//        mDownloadFilename  = String.format("%s_%s.txt",mNovelBean.novelinfodata.siteno, chapteritem.chapter_no);
//        File file = new File(this.getExternalFilesDir(null), mDownloadFilename);
//        if(file.exists()){
////            read();
//            return;
//        }
//        // /sdcard/Android/data/com.your.app/files/test
//        String str = String.format("%s?no=%s&hankaku=0&code=utf-8&kaigyo=crlf",
//                mNovelBean.novelinfodata.ncode_syosetu_txtdownloadlink, chapteritem.no);
////        Uri uri = Uri.parse("https://ncode.syosetu.com/txtdownload/dlstart/ncode/1319853/?no=2&hankaku=0&code=utf-8&kaigyo=crlf");
//        Uri uri = Uri.parse(str);
//        DownloadManager.Request request = new DownloadManager.Request(uri);
//        request.setTitle(chapteritem.chapter_title);
//        request.setDescription("Downloading");
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//        request.setVisibleInDownloadsUi(false);
//        Uri targetfile = Uri.fromFile(file);
////        request.setDestinationUri(Uri.parse("file://" + folderName + "/myfile.mp3"));
//        request.setDestinationUri(targetfile);
////        File file = new File(getActivity().getExternalFilesDir(null), "test"); // /sdcard/Android/data/com.your.app/files/test
////        long downloadId = downloadmanager.enqueue(new DownloadManager.Request(Uri.parse(content.url))
////                .setDestinationUri(Uri.fromFile(file));
//        downloadID = downloadmanager.enqueue(request);
//    }
    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (downloadID == id) {
                Toast.makeText(NovelBookDetailActivity.this, "Download Completed", Toast.LENGTH_SHORT).show();
//                read();
            }
        }
    };
//    public void read(){
//        if (mSVProgressHUD.isShowing()){
//            mSVProgressHUD.dismiss();
//        }
//    }


    //解析小说信息
   void initWebView(){
        //final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.first_coordinator_layout);
        novelInfoWebView.getSettings().setJavaScriptEnabled(true);
        //        webView1.getSettings().setBuiltInZoomControls(true);
        novelInfoWebView.addJavascriptInterface(new NovelBookDetailActivity.MyJavaScriptInterface(this),
                "HtmlViewer");
        novelInfoWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                System.out.print(url);
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(ComCode.is_Alphapolis_novelurl(url)){
                    if(novelInfoWebView!=null){
                        novelInfoWebView.loadUrl("javascript:window.HtmlViewer.showHTML" +
                                "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                        mParseNovelUrl = url;

                    }
                }
            }
        });
        //初始化关闭
       mfloatingactionButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (webview_scrollview.getVisibility() != View.GONE) {
                   webview_scrollview.setVisibility(View.GONE);
               }else{
                   webview_scrollview.setVisibility(View.VISIBLE);
               }
           }
       });
       webview_scrollview.setVisibility(View.GONE);
    }
    //解析
    class MyJavaScriptInterface {
        private Context ctx;
        MyJavaScriptInterface(Context ctx) {
            this.ctx = ctx;
        }
        @SuppressLint("JavascriptInterface")
        @JavascriptInterface
        public void showHTML(String html) {
            NovelInfoBean bean = NovelParseAlphapolis.parse_novelinfo_html(html,mParseNovelUrl);
            if(bean!= null && bean.novelinfodata.err == false
                    && bean.novelinfodata.novel_url !=null
                    && bean.novelinfodata.novel_url.length() >0
                    && bean.novelinfodata.novel_url.equalsIgnoreCase(novelurl)
                    ){
                if( this!=null){
                    finishRefresh(bean);
                }
            }
        }
    }

}