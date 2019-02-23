package com.zuk.ireader.ui.fragment.novel;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.zuk.ireader.R;
import com.zuk.ireader.business.config.NovelConfigRankingClass;
import com.zuk.ireader.ui.base.BaseFragment;

import java.util.ArrayList;

/**
 * Created by newbiechen on 17-5-3.
 */

public class NovelTab1Fragment extends BaseFragment{

    TabLayout viewPagerTab;
    ViewPager viewPager;

    private final ArrayList<Fragment> mFragmentList = new ArrayList<>();
    @Override
    protected int getContentId() {
        return R.layout.novel_genre_tab1;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
    }

    @Override
    protected void initClick() {
        super.initClick();
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        viewPagerTab = (TabLayout) view.findViewById(R.id.viewPagerTab);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        super.onViewCreated(view, savedInstanceState);
    }
    @Override
    public void onStart() {
        super.onStart();

    }
    @Override
    public void onResume(){
        super.onResume();
    }
    public void initUI(){
        OriginalFragmentPagerAdapter adapter = new OriginalFragmentPagerAdapter(
                this.getActivity().getSupportFragmentManager());
        viewPager.setOffscreenPageLimit(100);
        viewPager.setAdapter(adapter);
        viewPagerTab.setupWithViewPager(viewPager);
    }
     @Override
    public void onHiddenChanged(boolean hidden) {
         if (hidden) {
         } else {
         }
     }
    @Override
    protected void initWidget(Bundle savedInstanceState) {
        super.initWidget(savedInstanceState);
    }

    @Override
    protected void processLogic() {
        super.processLogic();
    }


    public class OriginalFragmentPagerAdapter extends FragmentPagerAdapter {

        NovelConfigRankingClass rangkinglist = new NovelConfigRankingClass();

        private CharSequence[] tabTitles = {rangkinglist.getItem(200).title,
                rangkinglist.getItem(201).title,
                rangkinglist.getItem(202).title,
                rangkinglist.getItem(203).title,
//                rangkinglist.getItem(4).title,
//                rangkinglist.getItem(5).title
        };

        public OriginalFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    Fragment fragment0 = NovelSiteRangkDataFragment.newInstance(200);
                    return fragment0;
                case 1:
                    return NovelSiteRangkDataFragment.newInstance(201);
                case 2:
                    return NovelSiteRangkDataFragment.newInstance(202);
                case 3:
                    return NovelSiteRangkDataFragment.newInstance(203);
//                case 4:
//                    return NovelSiteRangkDataFragment.newInstance(4);
//                case 5:
//                    return NovelSiteRangkDataFragment.newInstance(5);
                default:
                    return null;
            }
        }
        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }
}
