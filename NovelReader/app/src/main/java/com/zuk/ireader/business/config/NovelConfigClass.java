package com.zuk.ireader.business.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ksymac on 2018/12/14.
 */

public class NovelConfigClass {
    public class NovelConfigItem {
        public String type;
        public String title;
        public String tag;
        public String genreurl;
        NovelConfigItem( String type, String title, String tag, String genreurl){
            this.type = type;
            this.title = title;
            this.tag = tag;
            this.genreurl = genreurl;
        }
    };

    public List<NovelConfigItem> get_yomou_syosetu_com_List(){
        List<NovelConfigItem> list = new ArrayList<NovelConfigItem>();
        list.add(new NovelConfigItem("daily", "日間", "総合ランキング", "/rank/list/type/daily_total/"));
        list.add(new NovelConfigItem("weekly","週間", "総合ランキング",   "/rank/list/type/weekly_total/"));
        list.add(new NovelConfigItem("monthly", "月間", "総合ランキング",  "/rank/list/type/monthly_total/"));
        list.add(new NovelConfigItem("yearly", "年間", "総合ランキング",  "/rank/list/type/yearly_total/"));
        return list;
    }

    public List<NovelConfigItem> get_yomou_syosetu_com_NovelGenreList_daily(){
        List<NovelConfigItem> list = new ArrayList<NovelConfigItem>();
        list.add(new NovelConfigItem("daily", "日間", "総合ランキング", "/rank/list/type/daily_total/"));
        list.add(new NovelConfigItem("daily", "日間", "異世界",  "/rank/genrelist/type/daily_101/"));
        list.add(new NovelConfigItem("daily", "日間", "現実世界",  "/rank/genrelist/type/daily_102/"));
        list.add(new NovelConfigItem("daily", "日間", "ハイファンタジー",  "/rank/genrelist/type/daily_201/"));
        list.add(new NovelConfigItem("daily", "日間", "ローファンタジー",  "/rank/genrelist/type/daily_202/"));
        list.add(new NovelConfigItem("daily", "日間", "純文学",  "/rank/genrelist/type/daily_301/"));
        list.add(new NovelConfigItem("daily", "日間", "ヒューマンドラマ",  "/rank/genrelist/type/daily_302/"));
        list.add(new NovelConfigItem("daily", "日間", "歴史",  "/rank/genrelist/type/daily_303/"));
        list.add(new NovelConfigItem("daily", "日間", "推理",  "/rank/genrelist/type/daily_304/"));
        list.add(new NovelConfigItem("daily", "日間", "ホラー",  "/rank/genrelist/type/daily_305/"));
        list.add(new NovelConfigItem("daily", "日間", "アクション",  "/rank/genrelist/type/daily_306/"));
        list.add(new NovelConfigItem("daily", "日間", "コメディー",  "/rank/genrelist/type/daily_307/"));
        return list;
    }

    public List<NovelConfigItem> get_yomou_syosetu_com_NovelGenreList_weekly(){
        List<NovelConfigItem> list = new ArrayList<NovelConfigItem>();
        list.add(new NovelConfigItem("weekly","週間", "総合ランキング",  "/rank/list/type/weekly_total/"));
        list.add(new NovelConfigItem("weekly","週間", "異世界",  "/rank/genrelist/type/weekly_101/"));
        list.add(new NovelConfigItem("weekly","週間", "現実世界",  "/rank/genrelist/type/weekly_102/"));
        list.add(new NovelConfigItem("weekly","週間", "ハイファンタジー",  "/rank/genrelist/type/weekly_201/"));
        list.add(new NovelConfigItem("weekly","週間", "ローファンタジー",  "/rank/genrelist/type/weekly_202/"));
        list.add(new NovelConfigItem("weekly","週間", "純文学",  "/rank/genrelist/type/weekly_301/"));
        list.add(new NovelConfigItem("weekly","週間", "ヒューマンドラマ",  "/rank/genrelist/type/weekly_302/"));
        list.add(new NovelConfigItem("weekly","週間", "歴史",  "/rank/genrelist/type/weekly_303/"));
        list.add(new NovelConfigItem("weekly","週間", "推理",  "/rank/genrelist/type/weekly_304/"));
        list.add(new NovelConfigItem("weekly","週間", "ホラー",  "/rank/genrelist/type/weekly_305/"));
        list.add(new NovelConfigItem("weekly","週間", "アクション",  "/rank/genrelist/type/weekly_306/"));
        list.add(new NovelConfigItem("weekly","週間", "コメディー",  "/rank/genrelist/type/weekly_307/"));
        return list;
    }

    public List<NovelConfigItem> get_yomou_syosetu_com_NovelGenreList_monthly(){
        List<NovelConfigItem> list = new ArrayList<NovelConfigItem>();
        list.add(new NovelConfigItem("monthly", "月間", "総合ランキング",  "/rank/list/type/monthly_total/"));
        list.add(new NovelConfigItem("monthly", "月間", "異世界",  "/rank/genrelist/type/monthly_101/"));
        list.add(new NovelConfigItem("monthly", "月間", "現実世界",  "/rank/genrelist/type/monthly_102/"));
        list.add(new NovelConfigItem("monthly", "月間", "ハイファンタジー",  "/rank/genrelist/type/monthly_201/"));
        list.add(new NovelConfigItem("monthly", "月間", "ローファンタジー",  "/rank/genrelist/type/monthly_202/"));
        list.add(new NovelConfigItem("monthly", "月間", "純文学",  "/rank/genrelist/type/monthly_301/"));
        list.add(new NovelConfigItem("monthly", "月間", "ヒューマンドラマ",  "/rank/genrelist/type/monthly_302/"));
        list.add(new NovelConfigItem("monthly", "月間", "歴史",  "/rank/genrelist/type/monthly_303/"));
        list.add(new NovelConfigItem("monthly", "月間", "推理",  "/rank/genrelist/type/monthly_304/"));
        list.add(new NovelConfigItem("monthly", "月間", "ホラー",  "/rank/genrelist/type/monthly_305/"));
        list.add(new NovelConfigItem("monthly", "月間", "アクション",  "/rank/genrelist/type/monthly_306/"));
        list.add(new NovelConfigItem("monthly", "月間", "コメディー",  "/rank/genrelist/type/monthly_307/"));
        return list;
    }

    public List<NovelConfigItem> get_yomou_syosetu_com_NovelGenreList_yearly(){
        List<NovelConfigItem> list = new ArrayList<NovelConfigItem>();
        list.add(new NovelConfigItem("yearly", "年間", "総合ランキング",  "/rank/list/type/yearly_total/"));
        list.add(new NovelConfigItem("yearly", "年間", "異世界",  "/rank/genrelist/type/yearly_101/"));
        list.add(new NovelConfigItem("yearly", "年間", "現実世界",  "/rank/genrelist/type/yearly_102/"));
        list.add(new NovelConfigItem("yearly", "年間", "ハイファンタジー",  "/rank/genrelist/type/yearly_201/"));
        list.add(new NovelConfigItem("yearly", "年間", "ローファンタジー",  "/rank/genrelist/type/yearly_202/"));
        list.add(new NovelConfigItem("yearly", "年間", "純文学",  "/rank/genrelist/type/yearly_301/"));
        list.add(new NovelConfigItem("yearly", "年間", "ヒューマンドラマ",  "/rank/genrelist/type/yearly_302/"));
        list.add(new NovelConfigItem("yearly", "年間", "歴史",  "/rank/genrelist/type/yearly_303/"));
        list.add(new NovelConfigItem("yearly", "年間", "推理",  "/rank/genrelist/type/yearly_304/"));
        list.add(new NovelConfigItem("yearly", "年間", "ホラー",  "/rank/genrelist/type/yearly_305/"));
        list.add(new NovelConfigItem("yearly", "年間", "アクション",  "/rank/genrelist/type/yearly_306/"));
        list.add(new NovelConfigItem("yearly", "年間", "コメディー",  "/rank/genrelist/type/yearly_307/"));
        return list;
    }

    public List<NovelConfigItem> get_yomou_syosetu_com_GenreList(){
        List<NovelConfigItem> list = new ArrayList<NovelConfigItem>();
        list.addAll(get_yomou_syosetu_com_NovelGenreList_daily());
        list.addAll(get_yomou_syosetu_com_NovelGenreList_weekly());
        list.addAll(get_yomou_syosetu_com_NovelGenreList_monthly());
        list.addAll(get_yomou_syosetu_com_NovelGenreList_yearly());
        return list;
    }
    //
    public NovelConfigItem getItem(String type, String tag){
        for (NovelConfigClass.NovelConfigItem item : get_yomou_syosetu_com_GenreList()){
            if (item.type.equals(type) && item.tag.equals(tag)){
                return item;
            }
        }
        return null;
    }
}
