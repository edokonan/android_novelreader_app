package com.zuk.ireader.business.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ksymac on 2018/12/14.
 */

public class NovelConfigRankingClass {
    public class NovelConfigRankingItem {
        public int id;
        public String title;
        public String siteno;
        public String genreurl;

        NovelConfigRankingItem( int id, String siteno,  String title, String genreurl){
            this.id = id;
            this.siteno = siteno;
            this.title = title;
            this.genreurl = genreurl;
        }
    };


    public List<NovelConfigRankingItem> get_tab0_rangkinglist(){
        List<NovelConfigRankingItem> list = new ArrayList<NovelConfigRankingItem>();
        list.add(new NovelConfigRankingItem(0, "2","Best300", "/rank/list/type/total_total/"));
        list.add(new NovelConfigRankingItem(1, "2","今日総合", "/rank/list/type/daily_total/"));
        list.add(new NovelConfigRankingItem(2, "2","異世界",  "/rank/genrelist/type/daily_101/"));
        list.add(new NovelConfigRankingItem(3, "2","現実世界",  "/rank/genrelist/type/daily_102/"));
        list.add(new NovelConfigRankingItem(4, "2","ハイファンタジー",  "/rank/genrelist/type/daily_201/"));
        list.add(new NovelConfigRankingItem(5, "2","ローファンタジー",  "/rank/genrelist/type/daily_202/"));

        return list;
    }

    public List<NovelConfigRankingItem> get_tab1_rangkinglist(){
        List<NovelConfigRankingItem> list = new ArrayList<NovelConfigRankingItem>();
        list.add(new NovelConfigRankingItem(200, ComCode.siteno_estar,"estar", "default"));
        list.add(new NovelConfigRankingItem(201, ComCode.siteno_estar,"恋愛", "/rank/list/type/total_total/"));
        list.add(new NovelConfigRankingItem(202, ComCode.siteno_estar,"今日総合", "/rank/list/type/daily_total/"));
        return list;
    }

    public List<NovelConfigRankingItem> get_other_rangkinglist(){
        List<NovelConfigRankingItem> list = new ArrayList<NovelConfigRankingItem>();
        list.add(new NovelConfigRankingItem(701, ComCode.siteno_kakuyomu,"カクヨム", "weekly"));
        list.add(new NovelConfigRankingItem(702, ComCode.siteno_kakuyomu,"カクヨム", "monthly"));
        list.add(new NovelConfigRankingItem(703, ComCode.siteno_kakuyomu,"カクヨム", "all"));

        list.add(new NovelConfigRankingItem(401, ComCode.siteno_maho,"iらんど", "daily"));
        list.add(new NovelConfigRankingItem(402, ComCode.siteno_maho,"iらんど", "weekly"));
        list.add(new NovelConfigRankingItem(403, ComCode.siteno_maho,"iらんど", "monthly"));
        list.add(new NovelConfigRankingItem(404, ComCode.siteno_maho,"iらんど", "complete"));

        list.add(new NovelConfigRankingItem(501, ComCode.siteno_noichigo,"野いちご", "frame"));
        list.add(new NovelConfigRankingItem(502, ComCode.siteno_noichigo,"野いちご", "ranking"));

        list.add(new NovelConfigRankingItem(601, ComCode.siteno_berryscafe,"ベリーズカフェ", "default"));

        list.add(new NovelConfigRankingItem(101, ComCode.siteno_alphapolis,"アルファポリス", "default"));
        return list;
    }


    //
    public NovelConfigRankingItem getItem(int id){
        for (NovelConfigRankingItem item : get_tab0_rangkinglist()){
            if (item.id ==id){
                return item;
            }
        }
        for (NovelConfigRankingItem item : get_tab1_rangkinglist()){
            if (item.id ==id){
                return item;
            }
        }
        for (NovelConfigRankingItem item : get_other_rangkinglist()){
            if (item.id ==id){
                return item;
            }
        }
        return null;
    }
}
