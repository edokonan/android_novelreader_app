package com.zuk.ireader.ui.activity.novel;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.fcm.NotificationsUtils;
import com.fcm.RegistrationIntentService;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.zuk.ireader.R;
import com.zuk.ireader.service.DownloadNovelInfoService;
import com.zuk.ireader.ui.activity.DownloadActivity;
import com.zuk.ireader.ui.activity.FileSystemActivity;
import com.zuk.ireader.ui.activity.SearchActivity;
import com.zuk.ireader.ui.base.BaseTabActivity;
import com.zuk.ireader.ui.dialog.SexChooseDialog;
import com.zuk.ireader.ui.fragment.novel.NovelBookShelfFragment;
import com.zuk.ireader.ui.fragment.novel.NovelGenreFragment;
import com.zuk.ireader.ui.fragment.novel.NovelHistoryFragment;
import com.zuk.ireader.ui.fragment.novel.NovelSiteFragment;
import com.zuk.ireader.ui.fragment.novel.NovelTabMore;
import com.zuk.ireader.utils.Constant;
import com.zuk.ireader.utils.PermissionsChecker;
import com.zuk.ireader.utils.SharedPreUtils;
import com.zuk.ireader.utils.ToastUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import hotchemi.android.rate.AppRate;
import hotchemi.android.rate.OnClickButtonListener;

public class NovelDownloadActivity extends BaseTabActivity{
    private final ArrayList<Fragment> mFragmentList = new ArrayList<>();


    @Override
    protected int getContentId() {
        return R.layout.activity_novel_download;
    }

    /**************init method***********************/
    @Override
    protected void setUpToolbar(Toolbar toolbar) {
        super.setUpToolbar(toolbar);
//        toolbar.setLogo(R.mipmap.logo);
        getSupportActionBar().setTitle("ケイタイ小説");
//        toolbar.setVisibility(View.GONE);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mVp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {

            }
            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected List<Fragment> createTabFragments() {
        initFragment();
        return mFragmentList;
    }
    Fragment bookShelfFragment = new NovelBookShelfFragment();
    Fragment historyFragment = new NovelHistoryFragment();
    private void initFragment(){
        mFragmentList.add(bookShelfFragment);
        mFragmentList.add(historyFragment);
    }

    @Override
    protected List<String> createTabTitles() {
        String [] titles = getResources().getStringArray(R.array.novel_download_fragment_title);
        return Arrays.asList(titles);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
    }

}
