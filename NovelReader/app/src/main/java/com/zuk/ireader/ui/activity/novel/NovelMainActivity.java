package com.zuk.ireader.ui.activity.novel;

import android.Manifest;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.zuk.ireader.ui.fragment.BookShelfFragment;
import com.zuk.ireader.ui.fragment.CommunityFragment;
import com.zuk.ireader.ui.fragment.FindFragment;
import com.zuk.ireader.ui.fragment.novel.NovelBillBookFragment;
import com.zuk.ireader.ui.fragment.novel.NovelBookShelfFragment;
import com.zuk.ireader.ui.fragment.novel.NovelFindFragment;
import com.zuk.ireader.ui.fragment.novel.NovelGenreFragment;
import com.zuk.ireader.ui.fragment.novel.NovelHistoryFragment;
import com.zuk.ireader.ui.fragment.novel.NovelHomeMainFragment;
import com.zuk.ireader.ui.fragment.novel.NovelSiteFragment;
import com.zuk.ireader.ui.fragment.novel.NovelTab1Fragment;
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

public class NovelMainActivity extends BaseTabActivity{
    /*************Constant**********/
    private static final int WAIT_INTERVAL = 2000;
    private static final int PERMISSIONS_REQUEST_STORAGE = 1;

    static final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    /***************Object*********************/
    private final ArrayList<Fragment> mFragmentList = new ArrayList<>();
    private PermissionsChecker mPermissionsChecker;
    /*****************Params*********************/
    private boolean isPrepareFinish = false;

    @BindView(R.id.bottomBar)
    BottomBar bottomBar;
    @Override
    protected int getContentId() {
        return R.layout.activity_base_tab;
    }

    /**************init method***********************/
    @Override
    protected void setUpToolbar(Toolbar toolbar) {
        super.setUpToolbar(toolbar);
//        toolbar.setLogo(R.mipmap.logo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("ケイタイ小説");
        toolbar.setVisibility(View.GONE);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected( int tabId) {
                if (tabId == R.id.tab_tab0){
                    TabLayout.Tab tab = mTlIndicator.getTabAt(0);
                    tab.select();
                }
                if (tabId == R.id.tab_tab1){
//                    fragment1.initUI();
                    TabLayout.Tab tab = mTlIndicator.getTabAt(1);
                    tab.select();
                }
                if (tabId == R.id.tab_tab2){
                    TabLayout.Tab tab = mTlIndicator.getTabAt(2);
                    tab.select();
                }
                if (tabId == R.id.tab_tab3){
                    TabLayout.Tab tab = mTlIndicator.getTabAt(3);
                    tab.select();
                }
                if (tabId == R.id.tab_tab4){
                    TabLayout.Tab tab = mTlIndicator.getTabAt(4);
                    tab.select();
                }

            }
        });
        mVp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                bottomBar.selectTabAtPosition(position);
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
        mTlIndicator.setVisibility(View.GONE);

        AppRate.with(this)
                .setInstallDays(0) // default 10, 0 means install day.
                .setLaunchTimes(2) // default 10
                .setRemindInterval(1) // default 1
                .setShowLaterButton(true) // default true
                .setDebug(false) // default false
                .setOnClickButtonListener(new OnClickButtonListener() { // callback listener.
                    @Override
                    public void onClickButton(int which) {
                        //Log.d(MainActivity.class.getName(), Integer.toString(which));
                    }
                })
                .monitor();
        // Show a dialog if meets conditions
        // AppRate.showRateDialogIfMeetsConditions(this);

        //检测是否可以接受通知
        NotificationsUtils.isNotificationEnabled(getApplicationContext());
        // 起動時にサーバー送信
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
        // 更新本棚

        DownloadNovelInfoIntent = new Intent(this, DownloadNovelInfoService.class);
        startService(DownloadNovelInfoIntent);
    }
    Intent DownloadNovelInfoIntent;
    @Override
    public void onDestroy() {
        stopService(DownloadNovelInfoIntent);
        super.onDestroy();
    }


    @Override
    protected List<Fragment> createTabFragments() {
        initFragment();
        return mFragmentList;
    }
    NovelHomeMainFragment fragment0 = new NovelHomeMainFragment();
    Fragment novelsiteFragment = new NovelSiteFragment();
    Fragment bookShelfFragment = new NovelBookShelfFragment();
    Fragment historyFragment = new NovelHistoryFragment();
    Fragment moreFragment = new NovelTabMore();
//    NovelTab1Fragment fragment1 = new NovelTab1Fragment();//第二个有tab的view，没有实现
//    Fragment communityFragment = new NovelBillBookFragment();//best300

    private void initFragment(){
        mFragmentList.add(fragment0);
        mFragmentList.add(novelsiteFragment);
        mFragmentList.add(bookShelfFragment);
        mFragmentList.add(historyFragment);
        mFragmentList.add(moreFragment);
//        mFragmentList.add(communityFragment);
//        Fragment communityFragment = new NovelBillBookFragment();
//        mFragmentList.add(communityFragment);
//        Fragment discoveryFragment = new NovelFindFragment();
//        mFragmentList.add(discoveryFragment);
//        Fragment bookShelfFragment = new FindFragment();
//        Fragment communityFragment = new CommunityFragment();
//        mFragmentList.add(discoveryFragment);
//        mFragmentList.add(discoveryFragment);
    }

    public void moveToTab(int i){
        TabLayout.Tab tab = mTlIndicator.getTabAt(i);
        tab.select();
    }

    @Override
    protected List<String> createTabTitles() {
        String [] titles = getResources().getStringArray(R.array.novel_home_fragment_title);
        return Arrays.asList(titles);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        //性别选择框
        //showSexChooseDialog();
    }

    private void showSexChooseDialog(){
        String sex = SharedPreUtils.getInstance()
                .getString(Constant.SHARED_SEX);
        if (sex.equals("")){
            mVp.postDelayed(()-> {
                Dialog dialog = new SexChooseDialog(this);
                dialog.show();
            },500);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Class<?> activityCls = null;
        switch (id) {
            case R.id.action_search:
                activityCls = SearchActivity.class;
                break;
            case R.id.action_login:
                break;
            case R.id.action_my_message:
                break;
            case R.id.action_download:
                activityCls = DownloadActivity.class;
                break;
            case R.id.action_sync_bookshelf:
                break;
            case R.id.action_scan_local_book:

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
                    if (mPermissionsChecker == null){
                        mPermissionsChecker = new PermissionsChecker(this);
                    }
                    //获取读取和写入SD卡的权限
                    if (mPermissionsChecker.lacksPermissions(PERMISSIONS)){
                        //请求权限
                        ActivityCompat.requestPermissions(this, PERMISSIONS,PERMISSIONS_REQUEST_STORAGE);
                        return super.onOptionsItemSelected(item);
                    }
                }
                activityCls = FileSystemActivity.class;
                break;
            case R.id.action_wifi_book:
                break;
            case R.id.action_feedback:
                break;
            case R.id.action_night_mode:
                break;
            case R.id.action_settings:
                break;
            default:
                break;
        }
        if (activityCls != null){
            Intent intent = new Intent(this, activityCls);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPreparePanel(int featureId, View view, Menu menu) {
        if (menu != null && menu instanceof MenuBuilder){
            try {
                Method method = menu.getClass().
                        getDeclaredMethod("setOptionalIconsVisible",Boolean.TYPE);
                method.setAccessible(true);
                method.invoke(menu,true);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return super.onPreparePanel(featureId, view, menu);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSIONS_REQUEST_STORAGE: {
                // 如果取消权限，则返回的值为0
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //跳转到 FileSystemActivity
                    Intent intent = new Intent(this, FileSystemActivity.class);
                    startActivity(intent);
                } else {
                    ToastUtils.show("ストレージへのアクセス権限が必要です");
                }
                return;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(!isPrepareFinish){
            mVp.postDelayed(
                    () -> isPrepareFinish = false,WAIT_INTERVAL
            );
            isPrepareFinish = true;
            Toast.makeText(this, "もう一度押すとアプリが終了します", Toast.LENGTH_SHORT).show();
        }
        else {
            super.onBackPressed();
        }
    }
}
