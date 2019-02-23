package com.zuk.ireader.ui.activity.novel;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;

import com.zuk.ireader.R;
import com.zuk.ireader.RxBus;
import com.zuk.ireader.event.BookSubSortEvent;
import com.zuk.ireader.model.bean.BookTagBean;
import com.zuk.ireader.model.flag.BookListType;
import com.zuk.ireader.business.config.NovelConfigClass;
import com.zuk.ireader.business.config.ComCode;
import com.zuk.ireader.ui.adapter.HorizonTagAdapter;
import com.zuk.ireader.ui.adapter.TagGroupAdapter;
import com.zuk.ireader.ui.base.BaseTabActivity;
import com.zuk.ireader.ui.fragment.novel.BookListNovelFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by ksymac on 2018/12/2.
 */

public class NovelBookListActivity extends BaseTabActivity {
    private static final int RANDOM_COUNT = 5;
    @BindView(R.id.book_list_rv_tag_horizon)
    RecyclerView mRvTag;
    @BindView(R.id.book_list_cb_filter)
    CheckBox mCbFilter;
    @BindView(R.id.book_list_rv_tag_filter)
    RecyclerView mRvFilter;
    /*************************************/
    private HorizonTagAdapter mHorizonTagAdapter;
    private TagGroupAdapter mTagGroupAdapter;
    private Animation mTopInAnim;
    private Animation mTopOutAnim;
    /************Params*******************/
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    @Override
    protected int getContentId() {
        return R.layout.activity_book_list;
    }

    @Override
    protected List<Fragment> createTabFragments() {
        List<Fragment> fragments = new ArrayList<>(BookListType.values().length);

        NovelConfigClass config = new NovelConfigClass();
        List<NovelConfigClass.NovelConfigItem> list = config.get_yomou_syosetu_com_List();
        for (NovelConfigClass.NovelConfigItem item : list){
            fragments.add(BookListNovelFragment.newInstance(BookListType.HOT,item.type, ComCode.siteno_ncode_syosetu,item.genreurl));
        }
//        for (BookListType type : BookListType.values()){
//                fragments.add(BookListFragment.newInstance(type));
//        }
//        fragments.add(BookListNovelFragment.newInstance(BookListType.HOT,"2","/rank/list/type/yearly_total/"));
//        fragments.add(BookListNovelFragment.newInstance(BookListType.HOT,"2","/rank/list/type/weekly_total/"));
//        fragments.add(BookListNovelFragment.newInstance(BookListType.HOT,"2","/rank/list/type/daily_total/"));
//        fragments.add(BookListFragment.newInstance(BookListType.COLLECT));
        return fragments;
    }

    @Override
    protected List<String> createTabTitles() {
//        List<String> titles = new ArrayList<>(BookListType.values().length);
//        for (BookListType type : BookListType.values()){
//            titles.add(type.getTypeName());
//        }
        List<String> titles = new ArrayList<>();
        NovelConfigClass config = new NovelConfigClass();
        List<NovelConfigClass.NovelConfigItem> list = config.get_yomou_syosetu_com_List();
        for (NovelConfigClass.NovelConfigItem item : list){
            titles.add(item.title);
        }
        return titles;
    }

    @Override
    protected void setUpToolbar(Toolbar toolbar) {
        super.setUpToolbar(toolbar);
        getSupportActionBar().setTitle("小説を読もう");
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        initTag();
    }

    private void initTag(){
        //横向的
        mHorizonTagAdapter = new HorizonTagAdapter();
        LinearLayoutManager tagManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        mRvTag.setLayoutManager(tagManager);
        mRvTag.setAdapter(mHorizonTagAdapter);

        //筛选框
        mTagGroupAdapter = new TagGroupAdapter(mRvFilter,2);
        mRvFilter.setAdapter(mTagGroupAdapter);
    }

    @Override
    protected void initClick() {
        super.initClick();
        //滑动的Tag
        mHorizonTagAdapter.setOnItemClickListener(
                (view,pos) -> {
                    String bookSort = mHorizonTagAdapter.getItem(pos);
                    RxBus.getInstance().post(new BookSubSortEvent(bookSort));
                }
        );

        //筛选
        mCbFilter.setOnCheckedChangeListener(
                (btn,checked)->{
                    if (mTopInAnim == null || mTopOutAnim == null){
                        mTopInAnim = AnimationUtils.loadAnimation(this,R.anim.slide_top_in);
                        mTopOutAnim = AnimationUtils.loadAnimation(this,R.anim.slide_top_out);
                    }

                    if (checked){
                        mRvFilter.setVisibility(View.VISIBLE);
                        mRvFilter.startAnimation(mTopInAnim);
                    }
                    else {
                        mRvFilter.startAnimation(mTopOutAnim);
                        mRvFilter.setVisibility(View.GONE);
                    }
                }
        );



        //筛选列表
        mTagGroupAdapter.setOnChildItemListener(
                (view, groupPos, childPos) -> {
                    String bean = mTagGroupAdapter.getChildItem(groupPos, childPos);
                    //是否已存在
                    List<String> tags =  mHorizonTagAdapter.getItems();
                    boolean isExist = false;
                    for (int i=0; i<tags.size(); ++i){
                        if (bean.equals(tags.get(i))){
                            mHorizonTagAdapter.setCurrentSelected(i);
                            mRvTag.getLayoutManager().scrollToPosition(i);
                            isExist = true;
                        }
                    }
                    if (!isExist){
                        //添加到1的位置,保证全本的位置
                        mHorizonTagAdapter.addItem(1,bean);
                        mHorizonTagAdapter.setCurrentSelected(1);
                        mRvTag.getLayoutManager().scrollToPosition(1);

                        //移除最后一个，并且更新列表
                        mHorizonTagAdapter.removeItem(mHorizonTagAdapter.getItem(mHorizonTagAdapter.getItemCount()-1));
                        String bookSort = mHorizonTagAdapter.getItem(1);
                        RxBus.getInstance().post(new BookSubSortEvent(bookSort));
                    }
                    mCbFilter.setChecked(false);
                }
        );
    }


    @Override
    protected void processLogic() {
        super.processLogic();
        refreshTag();
    }
    //设置标签
    private void refreshTag(){
        //不从服务器获取标签列表
//        Disposable refreshDispo = RemoteRepository.getInstance()
//                .getBookTags()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        (tagBeans)-> {
//                            refreshHorizonTag(tagBeans);
//                            refreshGroupTag(tagBeans);
//                        },
//                        (e) ->{
//                            LogUtils.e(e);
//                        }
//                );
//        mDisposable.add(refreshDispo);
        List<BookTagBean> tagBeans = new ArrayList<BookTagBean>();
        refreshGroupTag(tagBeans);
        refreshHorizonTag(tagBeans);
    }

    private void refreshHorizonTag(List<BookTagBean> tagBeans){
        List<String> randomTag = new ArrayList<>(RANDOM_COUNT);
        randomTag.add("総合ランキング");
        randomTag.add("異世界");
        randomTag.add("現実世界");
        randomTag.add("ハイファンタジー");
//        int caculator = 0;
//        //随机获取4,5个。
//        final int tagBeanCount = tagBeans.size();
//        for (int i=0; i<tagBeanCount; ++i){
//            List<String> tags = tagBeans.get(i).getTags();
//            final int tagCount = tags.size();
//            for (int j=0; j<tagCount; ++j){
//                if (caculator < RANDOM_COUNT){
//                    randomTag.add(tags.get(j));
//                    ++caculator;
//                }
//                else {
//                    break;
//                }
//            }
//            if (caculator >= RANDOM_COUNT){
//                break;
//            }
//        }
        mHorizonTagAdapter.addItems(randomTag);
    }

    private void refreshGroupTag(List<BookTagBean> tagBeans){
        //由于数据还有根据性别分配，所以需要加上去
        BookTagBean bean = new BookTagBean();
//        bean.setName(getResources().getString(R.string.nb_tag_sex));
//        bean.setTags(Arrays.asList(getResources().getString(R.string.nb_tag_boy),getResources().getString(R.string.nb_tag_girl)));
//        tagBeans.add(0,bean);
        bean.setName("ジャンル");
        bean.setTags(Arrays.asList("異世界","現実世界","ハイファンタジー","ローファンタジー", "純文学","ヒューマンドラマ", "歴史", "推理", "ホラー","アクション", "コメディー"));
        tagBeans.add(0,bean);

        mTagGroupAdapter.refreshItems(tagBeans);
    }
}
