package com.zuk.ireader.ui.fragment.novel;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.zuk.ireader.R;
import com.zuk.ireader.model.bean.CollBookBean;
import com.zuk.ireader.presenter.BookShelfPresenter;
import com.zuk.ireader.presenter.contract.BookShelfContract;
import com.zuk.ireader.realm.DBManagerReadBookShelf;
import com.zuk.ireader.realm.DBManagerReadHistory;
import com.zuk.ireader.realm.TableReadBookShelf;
import com.zuk.ireader.realm.TableReadHistory;
import com.zuk.ireader.ui.activity.novel.NovelBookDetailActivity;
import com.zuk.ireader.ui.activity.novel.NovelMainActivity;
import com.zuk.ireader.ui.adapter.novel.NovelBookShelfAdapter;
import com.zuk.ireader.ui.adapter.novel.NovelReadHistoryAdapter;
import com.zuk.ireader.ui.base.BaseMVPFragment;
import com.zuk.ireader.widget.adapter.WholeAdapter;
import com.zuk.ireader.widget.itemdecoration.DividerItemDecoration;
import com.zuk.ireader.widget.refresh.ScrollRefreshRecyclerView;

import java.io.File;
import java.util.List;

import butterknife.BindView;

//import com.bifan.txtreaderlib.ui.HwTxtPlayActivity;

/**
 * Created by zhukui on 19-1-19.
 */
public class NovelHistoryFragment extends BaseMVPFragment<BookShelfContract.Presenter>
        implements BookShelfContract.View {
    private static final String TAG = "NovelHistoryFragment";
    @BindView(R.id.book_shelf_rv_content)
    ScrollRefreshRecyclerView mRvContent;


    @BindView(R.id.book_shelf_tv_add)
    Button book_shelf_tv_add;

    List<TableReadHistory> mBooklist;
    /************************************/
    private NovelReadHistoryAdapter mCollBookAdapter;
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
        mCollBookAdapter = new NovelReadHistoryAdapter();
        mRvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvContent.addItemDecoration(new DividerItemDecoration(getContext()));
        mRvContent.setAdapter(mCollBookAdapter);
    }

    @Override
    protected void initClick() {
        super.initClick();

        mRvContent.setOnRefreshListener(() -> finishViewWithRealmDB());
        mCollBookAdapter.setOnItemClickListener(
                (view, pos) -> {
                    //如果是本地文件，首先判断这个文件是否存在
                    TableReadHistory tbBook = mCollBookAdapter.getItem(pos);
                    String bookid = tbBook.siteno;
//                    String lastTxtFilePath = DBManagerReadHistory.getLastestTxtPath(tbBook.chapterurl);
                    NovelBookDetailActivity.startActivity(this.getActivity(), bookid, tbBook.novelurl);
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

    @Override
    protected void processLogic() {
        super.processLogic();
//        mRvContent.startRefresh();
    }

    private void openItemDialog(TableReadHistory collBook) {
        String[] menus;
        menus = getResources().getStringArray(R.array.nb_menu_local_book_novel);
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

    private void onItemMenuClick(String which, TableReadHistory collBook) {
        switch (which) {
//            //置顶
//            case "置顶":
//                break;
//            //缓存
//            case "缓存":
//                //2. 进行判断，如果CollBean中状态为未更新。那么就创建Task，加入到Service中去。
//                //3. 如果状态为finish，并且isUpdate为true，那么就根据chapter创建状态
//                //4. 如果状态为finish，并且isUpdate为false。
//                downloadBook(collBook);
//                break;
//            //删除
            case "削除":
                deleteBook(collBook);
                break;
//            case "download":
//                download();
//                break;
//            case "read":
//                read();
//                break;
//            //批量管理
//            case "批量管理":
//                break;
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
    private void deleteBook(TableReadHistory collBook) {
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
                            DBManagerReadHistory.removeHistoryWithnovelurl(collBook.novelurl);
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
        mBooklist = DBManagerReadHistory.getBooklist();

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
        mBooklist = DBManagerReadHistory.getBooklist();
        mCollBookAdapter.refreshItems(mBooklist);
    }
}
