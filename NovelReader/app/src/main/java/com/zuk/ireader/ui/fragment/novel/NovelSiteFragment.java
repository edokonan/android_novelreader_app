package com.zuk.ireader.ui.fragment.novel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zuk.ireader.R;
import com.zuk.ireader.business.config.ComNovelSiteConfig;
import com.zuk.ireader.business.config.NovelSiteTagclass;
import com.zuk.ireader.ui.activity.novel.NovelBookListActivity;
import com.zuk.ireader.ui.activity.web.NovelSiteWebActivity;
import com.zuk.ireader.ui.base.BaseFragment;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.next.tagview.TagCloudView;

/**
 * Created by newbiechen on 17-4-15.
 */

public class NovelSiteFragment extends BaseFragment {
    /******************view************************/
//    @BindView(R.id.find_rv_content)
//    RecyclerView mRvContent;

    @BindView(R.id.tag_cloud_view_yomou_syosetu_1)
    TagCloudView tag_cloud_view_yomou_syosetu_1;
    @BindView(R.id.tag_cloud_view_yomou_syosetu_2)
    TagCloudView tag_cloud_view_yomou_syosetu_2;

    @BindView(R.id.tag_cloud_view_alphapolis_1)
    TagCloudView tag_cloud_view_alphapolis_1;
    @BindView(R.id.tag_cloud_view_alphapolis_2)
    TagCloudView tag_cloud_view_alphapolis_2;

    @BindView(R.id.tag_cloud_view_mnlt_syosetu_1)
    TagCloudView tag_cloud_view_mnlt_syosetu_1;
    @BindView(R.id.tag_cloud_view_doc_syosetu_1)
    TagCloudView tag_cloud_view_doc_syosetu_1;

    @BindView(R.id.tag_cloud_view_estar_jp_1)
    TagCloudView tag_cloud_view_estar_jp_1;
    @BindView(R.id.tag_cloud_view_otona_novel_1)
    TagCloudView tag_cloud_view_otona_novel_1;

    @BindView(R.id.title_yomou_syosetu_1)
    TextView title_yomou_syosetu_1;
    @BindView(R.id.title_mnlt_syosetu_1)
    TextView title_mnlt_syosetu_1;
    @BindView(R.id.titledoc_syosetu_1)
    TextView titledoc_syosetu_1;
    @BindView(R.id.titleestar_jp__1)
    TextView titleestar_jp__1;
    @BindView(R.id.titleotona_novel_1)
    TextView titleotona_novel_1;
    @BindView(R.id.title_alphapolis_1)
    TextView title_alphapolis_1;


    @BindView(R.id.subtitle_yomou_syosetu_1)
    TextView subtitle_yomou_syosetu_1;
    @BindView(R.id.subtitlemnlt_syosetu_1)
    TextView subtitlemnlt_syosetu_1;
    @BindView(R.id.subtitledoc_syosetu_1)
    TextView subtitledoc_syosetu_1;

    @BindView(R.id.subtitle_alphapolis_1)
    TextView subtitle_alphapolis_1;
    @BindView(R.id.subtitleestar_jp_1)
    TextView subtitleestar_jp_1;
    @BindView(R.id.subtitleotona_novel_1)
    TextView subtitleotona_novel_1;

    @BindView(R.id.title_maho)
    TextView title_maho;
    @BindView(R.id.subtitle_maho)
    TextView subtitle_maho;
    @BindView(R.id.tag_cloud_view_maho)
    TagCloudView tag_cloud_view_maho;


    @BindView(R.id.title_noichigo)
    TextView title_noichigo;
    @BindView(R.id.subtitle_noichigo)
    TextView subtitle_noichigo;
    @BindView(R.id.tag_cloud_view_noichigo)
    TagCloudView tag_cloud_view_noichigo;


    @BindView(R.id.title_berryscafe)
    TextView title_berryscafe;
    @BindView(R.id.subtitle_berryscafe)
    TextView subtitle_berryscafe;
    @BindView(R.id.tag_cloud_view_berryscafe)
    TagCloudView tag_cloud_view_berryscafe;

    @BindView(R.id.title_kakuyomu)
    TextView title_kakuyomu;
    @BindView(R.id.subtitle_kakuyomu)
    TextView subtitle_kakuyomu;
    @BindView(R.id.tag_cloud_view_kakuyomu)
    TagCloudView tag_cloud_view_kakuyomu;

    @BindView(R.id.title_aozora)
    TextView title_aozora;
    @BindView(R.id.subtitle_aozora)
    TextView subtitle_aozora;
    @BindView(R.id.tag_cloud_view_aozora)
    TagCloudView tag_cloud_view_aozora;


    @Override
    protected int getContentId() {
        return R.layout.novel_fragment_site;
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        setUpAdapter();
    }

    List<NovelSiteTagclass>  YomouSyosetuTaglist1;
    List<NovelSiteTagclass>  YomouSyosetuTaglist2;
    List<NovelSiteTagclass>  alphapolisTaglist1;
    List<NovelSiteTagclass>  alphapolisTaglist2;

    List<NovelSiteTagclass>  MnltSyosetuTaglist1;
    List<NovelSiteTagclass>  DocSyosetuTaglist1;
    List<NovelSiteTagclass>  EstarTaglist1;
    List<NovelSiteTagclass>  OtonaNovelTaglist1;



    List<NovelSiteTagclass>  MahoTaglist1;
    List<NovelSiteTagclass>  NoichigoTaglist1;
    List<NovelSiteTagclass>  BerryscafeTaglist1;
    List<NovelSiteTagclass>  KakuyomuTaglist1;
    List<NovelSiteTagclass>  AozoraTaglist1;

    private void setUpAdapter(){

        YomouSyosetuTaglist1 = ComNovelSiteConfig.getYomouSyosetulist1();
        YomouSyosetuTaglist2 = ComNovelSiteConfig.getYomouSyosetulist2();
        alphapolisTaglist1 = ComNovelSiteConfig.getAlphapolisLinklist();
        alphapolisTaglist2 = ComNovelSiteConfig.getAlphapolisTaglist();

        MnltSyosetuTaglist1 = ComNovelSiteConfig.getMnltSyosetuTagList();
        DocSyosetuTaglist1 = ComNovelSiteConfig.getDocSyosetuTagList();
        EstarTaglist1 = ComNovelSiteConfig.getEstarTagList();
        OtonaNovelTaglist1 = ComNovelSiteConfig.getOtonaNovelTagList();

        MahoTaglist1 = ComNovelSiteConfig.getMahoNovelTagList();
        NoichigoTaglist1 = ComNovelSiteConfig.getNoichigoNovelTagList();
        BerryscafeTaglist1 = ComNovelSiteConfig.getBerryscafeNovelTagList();
        KakuyomuTaglist1 = ComNovelSiteConfig.getKakuyomuNovelTagList();
        AozoraTaglist1 = ComNovelSiteConfig.getAozoraTagList();


        List<String> tags1 = new ArrayList<>();
        for (int i = 0; i < YomouSyosetuTaglist1.size(); i++) {
            tags1.add(YomouSyosetuTaglist1.get(i).title);
        }
        tag_cloud_view_yomou_syosetu_1.setTags(tags1);
        tag_cloud_view_yomou_syosetu_1.setOnTagClickListener(new TagCloudView.OnTagClickListener() {
            @Override
            public void onTagClick(int position) {
//                if (position == -1) {
//                    Toast.makeText(getActivity(), "点击末尾文字",
//                            Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getActivity(), "点击 position : " + position,
//                            Toast.LENGTH_SHORT).show();
//                }
                Intent intent = new Intent(getContext(), NovelBookListActivity.class);
                startActivity(intent);
            }
        });
        setTagsTitle(this.tag_cloud_view_yomou_syosetu_2,YomouSyosetuTaglist2);


        List<String> alphapolistags1 = new ArrayList<>();
        for (int i = 0; i < alphapolisTaglist1.size(); i++) {
            alphapolistags1.add(alphapolisTaglist1.get(i).title);
        }
        tag_cloud_view_alphapolis_1.setTags(alphapolistags1);
        tag_cloud_view_alphapolis_1.setOnTagClickListener(new TagCloudView.OnTagClickListener() {
            @Override
            public void onTagClick(int position) {
                openTag(alphapolisTaglist1.get(position).link);
            }
        });
        List<String> alphapolistags2 = new ArrayList<>();
        for (int i = 0; i < alphapolisTaglist2.size(); i++) {
            alphapolistags2.add(alphapolisTaglist2.get(i).title);
        }
        tag_cloud_view_alphapolis_2.setTags(alphapolistags2);
        tag_cloud_view_alphapolis_2.setOnTagClickListener(new TagCloudView.OnTagClickListener() {
            @Override
            public void onTagClick(int position) {
                    openTag(alphapolisTaglist2.get(position).link);
            }
        });
//        tag_cloud_view_alphapolis_2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getActivity(), "TagView onClick",
//                        Toast.LENGTH_SHORT).show();
//            }
//        });
        setTagsTitle(this.tag_cloud_view_mnlt_syosetu_1,MnltSyosetuTaglist1);
        setTagsTitle(this.tag_cloud_view_doc_syosetu_1,DocSyosetuTaglist1);
        setTagsTitle(this.tag_cloud_view_estar_jp_1,EstarTaglist1);
        setTagsTitle(this.tag_cloud_view_otona_novel_1,OtonaNovelTaglist1);

        setTagsTitle(this.tag_cloud_view_maho,MahoTaglist1);
        setTagsTitle(this.tag_cloud_view_noichigo,NoichigoTaglist1);
        setTagsTitle(this.tag_cloud_view_berryscafe,BerryscafeTaglist1);
        setTagsTitle(this.tag_cloud_view_kakuyomu,KakuyomuTaglist1);
        setTagsTitle(this.tag_cloud_view_aozora,AozoraTaglist1);

        title_yomou_syosetu_1.setText("小説を読もう！");
        subtitle_yomou_syosetu_1.setText("数万作品の小説が無料で読める　ファンタジー");
        title_mnlt_syosetu_1.setText("小説を読もう！- ムーンライトノベルズ");
        subtitlemnlt_syosetu_1.setText("ＢＬ小説やＢＬ以外のソフトな女性向け大人小説サイト。");
        titledoc_syosetu_1.setText("小説を読もう！- ノクターンノベルズ");
        subtitledoc_syosetu_1.setText("男性向け大人小説サイト。");

        titleestar_jp__1.setText("無料小説ならエブリスタ");
        subtitleestar_jp_1.setText("恋愛 ファンタジー等作品が人気");

        titleotona_novel_1.setText("ちょっと大人のケータイ小説");
        subtitleotona_novel_1.setText("恋愛・友情　BL・GL　不倫・禁断の恋等作品多数");

        title_alphapolis_1.setText("アルファポリス");
        subtitle_alphapolis_1.setText("ファンタジー 恋愛 HOTランキング BL SF 異世界等作品多数");

        title_maho.setText("魔法のiらんどで小説を読もう");
        subtitle_maho.setText("あなたの妄想かなえます！女の子のための小説サイト");

        title_noichigo.setText("ケータイ小説サイト「野いちご」");
        subtitle_noichigo.setText("甘々＆胸キュンな恋愛小説から、切なくて泣ける感動小説、話題のホラーなど47万作以上。");

        title_berryscafe.setText("ベリーズカフェ");
        subtitle_berryscafe.setText("女性に人気の小説 恋愛");

        title_kakuyomu.setText("カクヨム");
        subtitle_kakuyomu.setText("ファンタジー、SF、恋愛、ホラー、ミステリーなどがあり、二次創作作品も楽しめます！");

        title_aozora.setText("青空文庫");
        subtitle_aozora.setText("インターネットの電子図書館");

//        titledoc_syosetu_1.setVisibility(View.GONE);
//        subtitledoc_syosetu_1.setVisibility(View.GONE);
//        tag_cloud_view_doc_syosetu_1.setVisibility(View.GONE);
//
//        title_noichigo.setVisibility(View.GONE);
//        subtitle_noichigo.setVisibility(View.GONE);
//        tag_cloud_view_noichigo.setVisibility(View.GONE);
//
//        title_berryscafe.setVisibility(View.GONE);
//        subtitle_berryscafe.setVisibility(View.GONE);
//        tag_cloud_view_berryscafe.setVisibility(View.GONE);
    }
    public void setTagsTitle(TagCloudView tagcloudview ,List<NovelSiteTagclass> listtag){
        List<String> tags = new ArrayList<>();
        for (int i = 0; i < listtag.size(); i++) {
            tags.add(listtag.get(i).title);
        }
        tagcloudview.setTags(tags);

        tagcloudview.setOnTagClickListener(new TagCloudView.OnTagClickListener() {
            @Override
            public void onTagClick(int position) {
                openTag(listtag.get(position).link);
            }
        });
    }

    @Override
    protected void initClick() {
    }

    void openTag(String url){
        NovelSiteWebActivity.startActivity(this.getContext(), url);
    }
}
