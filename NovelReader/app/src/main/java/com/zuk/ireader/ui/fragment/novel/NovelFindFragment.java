package com.zuk.ireader.ui.fragment.novel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zuk.ireader.R;
import com.zuk.ireader.model.bean.SectionBean;
import com.zuk.ireader.model.flag.FindType;
import com.zuk.ireader.ui.activity.BillboardActivity;
import com.zuk.ireader.ui.activity.BookListActivity;
import com.zuk.ireader.ui.activity.BookSortActivity;
import com.zuk.ireader.ui.activity.SearchActivity;
import com.zuk.ireader.ui.activity.novel.NovelBookListActivity;
import com.zuk.ireader.ui.activity.web.NovelSiteWebActivity;
import com.zuk.ireader.ui.adapter.SectionAdapter;
import com.zuk.ireader.ui.base.BaseFragment;
import com.zuk.ireader.widget.itemdecoration.DividerItemDecoration;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by newbiechen on 17-4-15.
 */

public class NovelFindFragment extends BaseFragment {
    /******************view************************/
    @BindView(R.id.find_rv_content)
    RecyclerView mRvContent;
    /*******************Object***********************/
    SectionAdapter mAdapter;

    @Override
    protected int getContentId() {
        return R.layout.fragment_find;
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        setUpAdapter();
    }

    private void setUpAdapter(){
        ArrayList<SectionBean> sections = new ArrayList<>();
        for (FindType type : FindType.values()){
            sections.add(new SectionBean(type.getTypeName(),type.getIconId()));
        }
//        sections.add(new SectionBean(FindType.JPNovel.getTypeName(),FindType.JPNovel.getIconId()));
        mAdapter = new SectionAdapter();
        mRvContent.setHasFixedSize(true);
        mRvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvContent.addItemDecoration(new DividerItemDecoration(getContext()));
        mRvContent.setAdapter(mAdapter);
        mAdapter.addItems(sections);
    }


    @Override
    protected void initClick() {
        mAdapter.setOnItemClickListener(
                (view,pos) -> {
                    FindType type = FindType.values()[pos];
                    Intent intent;
                    //跳转
                    switch (type){
                        case syosetu_data:
                            intent = new Intent(getContext(), NovelBookListActivity.class);
                            startActivity(intent);
                            break;
                        case syosetu_web:
                            NovelSiteWebActivity.startActivity(this.getContext(), "https://yomou.syosetu.com");
                            break;
                        case syosetu_ranking:
                            NovelSiteWebActivity.startActivity(this.getContext(), "https://yomou.syosetu.com/rank/genretop/");
                            break;
                        case syosetu_search://https://noc.syosetu.com/top/top/
                            NovelSiteWebActivity.startActivity(this.getContext(), "https://yomou.syosetu.com/search.php");
                            break;

                        case alphapolis_web:
                            NovelSiteWebActivity.startActivity(this.getContext(), "https://www.alphapolis.co.jp/novel");
                            break;
                        case alphapolis_ranking:
                            NovelSiteWebActivity.startActivity(this.getContext(), "https://www.alphapolis.co.jp/novel/ranking/hot");
                            break;
                        case alphapolis_search://https://noc.syosetu.com/top/top/
                            NovelSiteWebActivity.startActivity(this.getContext(), "https://www.alphapolis.co.jp/novel/index?sort=24hpt");
                            break;

//                        case JPNovel2:
//                            NovelMyWebActivity.startActivity(this.getContext(), "https://mnlt.syosetu.com/top/top/");
//                            break;
//                        case JPNovel3://https://noc.syosetu.com/top/top/
//                            NovelMyWebActivity.startActivity(this.getContext(), "https://noc.syosetu.com/top/top/");
//                            break;
                        case TOP:
                            intent = new Intent(getContext(),BillboardActivity.class);
                            startActivity(intent);
                            break;
                        case SORT:
                            intent = new Intent(getContext(), BookSortActivity.class);
                            startActivity(intent);
                            break;
                        case TOPIC:
                            intent = new Intent(getContext(), BookListActivity.class);
                            startActivity(intent);
                            break;
                        case LISTEN:
                            intent = new Intent(getContext(), SearchActivity.class);
                            startActivity(intent);
                            break;
//                        case JPNovel5://    <string name="nb.fragment.find.novel5">ベリーズカフェ</string>
//                            NovelMyWebActivity.startActivity(this.getContext(), "https://www.berrys-cafe.jp/pc/");
//                            break;
//                        case JPNovel6://    <string name="nb.fragment.find.novel6">野いちご</string>
//                            NovelMyWebActivity.startActivity(this.getContext(), "https://novel.maho.jp/");
//                            break;
//                        case JPNovel7://    <string name="nb.fragment.find.novel7">魔法のiらんど</string>
//                            NovelMyWebActivity.startActivity(this.getContext(), "https://maho.jp/");
//                            break;
//                        case JPNovel8://    <string name="nb.fragment.find.novel8">無料小説ならエブリスタ</string>
//                            NovelMyWebActivity.startActivity(this.getContext(), "https://estar.jp/");
//                            break;
//                        case JPNovel9://    <string name="nb.fragment.find.novel9">ちょっと大人のケータイノベル</string>
//                            NovelMyWebActivity.startActivity(this.getContext(), "https://otona-novel.jp/search/index/?guid=ON");
//                            break;
//                        case JPNovel10://    <string name="nb.fragment.find.novel10">携帯小説 トルタ</string>
//                            NovelMyWebActivity.startActivity(this.getContext(), "https://trte.jp/");
//                            break;
//                        case JPNovel11://    <string name="nb.fragment.find.novel11">無料で読める大人のケータイ官能小説</string>
//                            NovelMyWebActivity.startActivity(this.getContext(), "https://kanno-novel.jp/");
//                            break;
                    }
                }
        );

    }
}
