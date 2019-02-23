package com.zuk.ireader.ui.fragment.novel;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;

import com.bifan.txtreaderlib.main.TxtConfig;
//import com.bifan.txtreaderlib.ui.HwTxtPlayActivity;
import com.google.gson.Gson;
import com.zuk.ireader.R;
import com.zuk.ireader.model.bean.CollBookBean;
import com.zuk.ireader.presenter.BookShelfPresenter;
import com.zuk.ireader.presenter.contract.BookShelfContract;
import com.zuk.ireader.realm.DBManagerBookChapter;
import com.zuk.ireader.realm.DBManagerReadBookShelf;
import com.zuk.ireader.realm.DBManagerReadHistory;
import com.zuk.ireader.realm.TableReadBookShelf;
import com.zuk.ireader.realm.TableReadHistory;
import com.zuk.ireader.ui.activity.ReadActivity;
import com.zuk.ireader.ui.activity.novel.NovelBookDetailActivity;
import com.zuk.ireader.ui.activity.novel.NovelMainActivity;
import com.zuk.ireader.ui.adapter.CollBookAdapter;
import com.zuk.ireader.ui.adapter.novel.NovelBookShelfAdapter;
import com.zuk.ireader.ui.base.BaseMVPFragment;
import com.zuk.ireader.ui.reader.TxtPlayActivity;
import com.zuk.ireader.widget.adapter.WholeAdapter;
import com.zuk.ireader.widget.itemdecoration.DividerItemDecoration;
import com.zuk.ireader.widget.refresh.ScrollRefreshRecyclerView;

import java.io.File;
import java.util.List;

import butterknife.BindView;

/**
 * Created by zhukui on 19-1-1.
 */
public class NovelBookShelfFragment extends BaseMVPFragment<BookShelfContract.Presenter>
        implements BookShelfContract.View {
    private static final String TAG = "BookShelfFragment";
    @BindView(R.id.book_shelf_rv_content)
    ScrollRefreshRecyclerView mRvContent;


    @BindView(R.id.book_shelf_tv_add)
    Button book_shelf_tv_add;

    List<TableReadBookShelf> mBooklist;
    /************************************/
    private NovelBookShelfAdapter mCollBookAdapter;
//    CollBookAdapter
    private FooterItemView mFooterItem;
    //是否是第一次进入
    private boolean isInit = true;

    @Override
    protected int getContentId() {
        return R.layout.fragment_bookshelf;
    }

    @Override
    protected BookShelfContract.Presenter bindPresenter() {
        return new BookShelfPresenter();
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        super.initWidget(savedInstanceState);
        setUpAdapter();
    }

    private void setUpAdapter() {
        //添加Footer
        mCollBookAdapter = new NovelBookShelfAdapter();
        mRvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvContent.addItemDecoration(new DividerItemDecoration(getContext()));
        mRvContent.setAdapter(mCollBookAdapter);
    }

    @Override
    protected void initClick() {
        super.initClick();
//        //推荐书籍
//        Disposable recommendDisp = RxBus.getInstance()
//                .toObservable(RecommendBookEvent.class)
//                .subscribe(
//                        event -> {
//                            mRvContent.startRefresh();
//                            mPresenter.loadRecommendBooks(event.sex);
//                        }
//                );
//        addDisposable(recommendDisp);
//
//        Disposable donwloadDisp = RxBus.getInstance()
//                .toObservable(DownloadMessage.class)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        event -> {
//                            //使用Toast提示
//                            ToastUtils.show(event.message);
//                        }
//                );
//        addDisposable(donwloadDisp);
//        //删除书籍 (写的丑了点)
//        Disposable deleteDisp = RxBus.getInstance()
//                .toObservable(DeleteResponseEvent.class)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        event -> {
//                            if (event.isDelete) {
//                                ProgressDialog progressDialog = new ProgressDialog(getContext());
//                                progressDialog.setMessage("正在删除中");
//                                progressDialog.show();
//                                BookRepository.getInstance().deleteCollBookInRx(event.collBook)
//                                        .compose(RxUtils::toSimpleSingle)
//                                        .subscribe(
//                                                (Void) -> {
//                                                    mCollBookAdapter.removeItem(event.collBook);
//                                                    progressDialog.dismiss();
//                                                }
//                                        );
//                            } else {
//                                //弹出一个Dialog
//                                AlertDialog tipDialog = new AlertDialog.Builder(getContext())
//                                        .setTitle("您的任务正在加载")
//                                        .setMessage("先请暂停任务再进行删除")
//                                        .setPositiveButton("确定", (dialog, which) -> {
//                                            dialog.dismiss();
//                                        }).create();
//                                tipDialog.show();
//                            }
//                        }
//                );
//        addDisposable(deleteDisp);

//        mRvContent.setOnRefreshListener(
//                () -> mPresenter.updateCollBooks(mCollBookAdapter.getItems())
//        );

        mRvContent.setOnRefreshListener(() -> finishViewWithRealmDB());
        mCollBookAdapter.setOnItemClickListener(
                (view, pos) -> {
                    //如果是本地文件，首先判断这个文件是否存在
                    TableReadBookShelf tbBook = mCollBookAdapter.getItem(pos);
                    String bookid = tbBook.siteno;
                    String lastTxtFilePath = DBManagerReadHistory.getLastestTxtPath(tbBook.chapterurl);
//                    if(lastTxtFilePath!=null && lastTxtFilePath.length() > 5){
//                        DBManagerReadHistory.insertHistory(tbBook.novelurl,tbBook.chapterurl);
//                        openTxtReader(lastTxtFilePath,tbBook.novelurl,tbBook.chapterurl);
//                        return;
//                    }
                    NovelBookDetailActivity.startActivity(this.getActivity(), bookid, tbBook.novelurl);
//                    if (collBook.isLocal()) {
//                        //id表示本地文件的路径
//                        String path = collBook.getCover();
//                        File file = new File(path);
//                        //判断这个本地文件是否存在
//                        if (file.exists() && file.length() != 0) {
//                            CollBookBean bean = mCollBookAdapter.getItem(pos);
//                            ReadActivity.startActivity(getContext(), bean, true);
//                        } else {
//                            String tip = getContext().getString(R.string.nb_bookshelf_book_not_exist);
//                            //提示(从目录中移除这个文件)
//                            new AlertDialog.Builder(getContext())
//                                    .setTitle(getResources().getString(R.string.nb_common_tip))
//                                    .setMessage(tip)
//                                    .setPositiveButton(getResources().getString(R.string.nb_common_sure),
//                                            new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    deleteBook(collBook);
//                                                }
//                                            })
//                                    .setNegativeButton(getResources().getString(R.string.nb_common_cancel), null)
//                                    .show();
//                        }
//                    } else {
//                        ReadActivity.startActivity(getContext(),
//                                mCollBookAdapter.getItem(pos), true);
//                    }
                }
        );
        mCollBookAdapter.setOnItemLongClickListener(
                (v, pos) -> {
                    openItemDialog(mCollBookAdapter.getItem(pos));
                    return true;
                }
        );
        book_shelf_tv_add.setOnClickListener(
                (view) -> {
                    NovelMainActivity activity =  (NovelMainActivity)getActivity();
                    activity.moveToTab(1);
                });
    }
//    public void openTxtReader(String filepath,String novelurl, String chapterurl){
//        TxtConfig.saveIsOnVerticalPageMode(this.getActivity(),false);
//        TxtConfig.saveTextSize(this.getActivity(), 45);
////        HwTxtPlayActivity.loadTxtFile(this.getActivity(), filepath);
//        TxtPlayActivity.StartActivity(this.getActivity(), filepath,null, novelurl, chapterurl);
//    }
    @Override
    protected void processLogic() {
        super.processLogic();
//        mRvContent.startRefresh();
    }

    private void openItemDialog(TableReadBookShelf collBook) {
        String[] menus;
//        if (collBook.isLocal()) {
            menus = getResources().getStringArray(R.array.nb_menu_local_book_novel);
//        } else {
//            menus = getResources().getStringArray(R.array.nb_menu_net_book);
//        }
        AlertDialog collBookDialog = new AlertDialog.Builder(getContext())
                .setTitle(collBook.title)
                .setAdapter(new ArrayAdapter<String>(getContext(),
                                android.R.layout.simple_list_item_1, menus),
                        (dialog, which) -> onItemMenuClick(menus[which], collBook))
                .setNegativeButton(null, null)
                .setPositiveButton(null, null)
                .create();
        collBookDialog.show();
    }

    private void onItemMenuClick(String which, TableReadBookShelf collBook) {
        switch (which) {
            //置顶
            case "置顶":
                break;
            //缓存
            case "缓存":
                //2. 进行判断，如果CollBean中状态为未更新。那么就创建Task，加入到Service中去。
                //3. 如果状态为finish，并且isUpdate为true，那么就根据chapter创建状态
                //4. 如果状态为finish，并且isUpdate为false。
                downloadBook(collBook);
                break;
            //删除
            case "削除":
                deleteBook(collBook);
                break;
            case "download":
                download();
                break;
            case "read":
                read();
                break;
            //批量管理
            case "批量管理":
                break;
            default:
                break;
        }
    }

    public void  download(){
        DownloadManager downloadmanager = (DownloadManager) getActivity()
                .getSystemService(Context.DOWNLOAD_SERVICE);

        File file = new File(getActivity().getExternalFilesDir(null), "test.txt"); // /sdcard/Android/data/com.your.app/files/test
        Uri uri = Uri.parse("https://ncode.syosetu.com/txtdownload/dlstart/ncode/1319853/?no=2&hankaku=0&code=utf-8&kaigyo=crlf");
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle("My File");
        request.setDescription("Downloading");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setVisibleInDownloadsUi(false);
        Uri targetfile = Uri.fromFile(file);
//        request.setDestinationUri(Uri.parse("file://" + folderName + "/myfile.mp3"));
        request.setDestinationUri(targetfile);
//        File file = new File(getActivity().getExternalFilesDir(null), "test"); // /sdcard/Android/data/com.your.app/files/test
//        long downloadId = downloadmanager.enqueue(new DownloadManager.Request(Uri.parse(content.url))
//                .setDestinationUri(Uri.fromFile(file));
        downloadmanager.enqueue(request);
    }
    public void read(){
//        CollBookBean book = mCollBookAdapter.getItem(1);
//        File file = new File(getActivity().getExternalFilesDir(null), "test.txt");
//        book.setCover(file.getPath());
//        ReadActivity.startActivity(getContext(), book, true);
    }
    private void downloadBook(TableReadBookShelf collBook) {
        //创建任务
//        mPresenter.createDownloadTask(collBook);
    }

    /**
     * 默认删除本地文件
     *
     * @param collBook
     */
    private void deleteBook(TableReadBookShelf collBook) {
//        if (collBook.isLocal()) {
            View view = LayoutInflater.from(getContext())
                    .inflate(R.layout.dialog_bookshelf_delete, null);
//            CheckBox cb = (CheckBox) view.findViewById(R.id.delete_cb_select);
            new AlertDialog.Builder(getContext())
                    .setTitle("削除する")
                    .setView(view)
                    .setPositiveButton(getResources().getString(R.string.nb_common_sure), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            boolean isSelected = cb.isSelected();
//                            if (isSelected) {
//                                ProgressDialog progressDialog = new ProgressDialog(getContext());
//                                progressDialog.setMessage("正在删除中");
//                                progressDialog.show();
//                                //删除
//                                File file = new File(collBook.getCover());
//                                if (file.exists()) file.delete();
//                                BookRepository.getInstance().deleteCollBook(collBook);
//                                BookRepository.getInstance().deleteBookRecord(collBook.get_id());
//                                //从Adapter中删除
//                                mCollBookAdapter.removeItem(collBook);
//                                progressDialog.dismiss();
//                            } else {
//                                BookRepository.getInstance().deleteCollBook(collBook);
//                                BookRepository.getInstance().deleteBookRecord(collBook.get_id());
//                                //从Adapter中删除
//                                mCollBookAdapter.removeItem(collBook);
//                            }
                            DBManagerReadBookShelf.removeFromBookShelf(collBook.novelurl);
                            finishViewWithRealmDB();
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.nb_common_cancel), null)
                    .show();
//        } else {
//            RxBus.getInstance().post(new DeleteTaskEvent(collBook));
//        }
    }

    /*******************************************************************8*/

    @Override
    public void showError() {

    }

    @Override
    public void complete() {
        mBooklist = DBManagerReadBookShelf.getBooklist();

        if (mCollBookAdapter.getItemCount() > 0 && mFooterItem == null) {
            mFooterItem = new FooterItemView();
            mCollBookAdapter.addFooterView(mFooterItem);
        }

        if (mRvContent.isRefreshing()) {
            mRvContent.finishRefresh();
        }
    }

    @Override
    public void finishRefresh(List<CollBookBean> collBookBeans) {
//        mCollBookAdapter.refreshItems(collBookBeans);
//        //如果是初次进入，则更新书籍信息
//        if (isInit) {
//            isInit = false;
//            mRvContent.post(
//                    () -> mPresenter.updateCollBooks(mCollBookAdapter.getItems())
//            );
//        }
        finishViewWithRealmDB();
    }


    @Override
    public void finishUpdate() {
        //重新从数据库中获取数据
//        mCollBookAdapter.refreshItems(BookRepository
//                .getInstance().getCollBooks());
    }

    @Override
    public void showErrorTip(String error) {
        mRvContent.setTip(error);
        mRvContent.showTip();
    }

    /*****************************************************************/
    class FooterItemView implements WholeAdapter.ItemView {
        @Override
        public View onCreateView(ViewGroup parent) {
            View view = LayoutInflater.from(getContext())
                    .inflate(R.layout.footer_book_shelf, parent, false);
            view.setOnClickListener(
                    (v) -> {
                        //设置RxBus回调
                    }
            );
            return view;
        }
        @Override
        public void onBindView(View view) {
        }
    }
    @Override
    public void onResume() {
        super.onResume();
//        mPresenter.refreshCollBooks();
        finishViewWithRealmDB();
    }
    public void finishViewWithRealmDB() {
        mRvContent.finishRefresh();
        mBooklist = DBManagerReadBookShelf.getBooklist();
        mCollBookAdapter.refreshItems(mBooklist);
    }
}
