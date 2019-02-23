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

/**
 * Created by ksymac on 2019/2/2.
 */
public class NovelHomeMainFragment  extends BaseFragment {
    TabLayout viewPagerTab;
    ViewPager viewPager;

    OriginalFragmentPagerAdapter adapter;
    int pagePosition = 0;

    Fragment fragment0 = NovelSiteDataFragment.newInstance(0);
    Fragment fragment1 = NovelSiteDataFragment.newInstance(701);
    Fragment fragment2 = NovelSiteDataFragment.newInstance(200);
//    Fragment fragment3 = NovelSiteDataFragment.newInstance(1);
//    Fragment fragment4 = NovelSiteDataFragment.newInstance(2);
//    Fragment fragment5 = NovelSiteDataFragment.newInstance(3);
//    Fragment fragment6 = NovelSiteDataFragment.newInstance(4);
    Fragment fragment3 = NovelSiteDataFragment.newInstance(401);
    Fragment fragment4 = NovelSiteDataFragment.newInstance(501);
    Fragment fragment5 = NovelSiteDataFragment.newInstance(601);
    Fragment fragment6 = NovelSiteDataFragment.newInstance(101);

    //from api get data
    //NovelSiteRangkDataFragment
    @Override
    protected int getContentId() {
        return R.layout.novel_genre_tab;
    }
    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if(viewPager!=null){
//                viewPager.setOffscreenPageLimit(20);
//                if(viewPager.getAdapter()==null){
//                }
//                viewPagerTab.setupWithViewPager(viewPager);
                viewPager.setAdapter(adapter);
                viewPager.setCurrentItem(pagePosition);
            }
//            if(adapter!=null){
//                adapter.notifyDataSetChanged();
//                if(viewPager!=null){
//                    viewPager.invalidate();
//                }
//            }
        } else {
        }
    }

    @Override
    protected void initClick() {
        super.initClick();
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPagerTab = (TabLayout) view.findViewById(R.id.viewPagerTab);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);

        adapter = new NovelHomeMainFragment.OriginalFragmentPagerAdapter(
                this.getActivity().getSupportFragmentManager()
        );
        viewPager.setOffscreenPageLimit(20);
        viewPager.setAdapter(adapter);
        viewPagerTab.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {
                pagePosition = position;
            }
        });
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

        private CharSequence[] tabTitles = {rangkinglist.getItem(0).title,
                rangkinglist.getItem(701).title,
                rangkinglist.getItem(200).title,
                rangkinglist.getItem(401).title,
                rangkinglist.getItem(501).title,
                rangkinglist.getItem(601).title,
                rangkinglist.getItem(101).title
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
                    return fragment0;
                case 1:
                    return fragment1;
                case 2:
                    return fragment2;
                case 3:
                    return fragment3;
                case 4:
                    return fragment4;
                case 5:
                    return fragment5;
                case 6:
                    return fragment6;
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
