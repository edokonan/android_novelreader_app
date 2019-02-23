package com.zuk.ireader.business.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ksymac on 2019/1/8.
 */

public class ComNovelSiteConfig {

    public static List<NovelSiteTagclass>  getYomouSyosetulist1(){
        List<NovelSiteTagclass> list = new ArrayList<>();
        list.add(new NovelSiteTagclass(" ジャンル ", ""));
        return list;
    }
    public static List<NovelSiteTagclass>  getYomouSyosetulist2(){
        List<NovelSiteTagclass> list = new ArrayList<>();
        list.add(new NovelSiteTagclass(" Top ", "https://yomou.syosetu.com"));
        list.add(new NovelSiteTagclass(" ランキング ", "https://yomou.syosetu.com/rank/genretop/"));
        list.add(new NovelSiteTagclass(" 分類検索 ", "https://yomou.syosetu.com/search.php"));
        return list;
    }

    public static List<NovelSiteTagclass>  getAlphapolisLinklist(){
        List<NovelSiteTagclass> list = new ArrayList<>();
        list.add(new NovelSiteTagclass(" Top ", "https://www.alphapolis.co.jp/novel"));
        list.add(new NovelSiteTagclass(" ランキング ", "https://www.alphapolis.co.jp/novel/ranking/hot"));
        list.add(new NovelSiteTagclass(" 分類検索 ", "https://www.alphapolis.co.jp/novel/index?sort=24hpt"));
        return list;
    }


    public static List<NovelSiteTagclass>  getAlphapolisTaglist(){
        List<NovelSiteTagclass> list = new ArrayList<>();
        list.add(new NovelSiteTagclass("ファンタジー", "https://www.alphapolis.co.jp/novel/index/110400"));
        list.add(new NovelSiteTagclass(" 恋愛 ", "https://www.alphapolis.co.jp/novel/index/110500"));
        list.add(new NovelSiteTagclass(" BL ", "https://www.alphapolis.co.jp/novel/index/119000"));
        list.add(new NovelSiteTagclass(" 青春 ", "https://www.alphapolis.co.jp/novel/index/110600"));
        list.add(new NovelSiteTagclass("大衆娯楽", "https://www.alphapolis.co.jp/novel/index/110800"));
        list.add(new NovelSiteTagclass("ミステリー", "https://www.alphapolis.co.jp/novel/index/110100"));
        list.add(new NovelSiteTagclass("ホラー", "https://www.alphapolis.co.jp/novel/index/110200"));
        list.add(new NovelSiteTagclass(" SF ", "https://www.alphapolis.co.jp/novel/index/110300"));
        list.add(new NovelSiteTagclass("キャラ文芸", "https://www.alphapolis.co.jp/novel/index/111400"));
        list.add(new NovelSiteTagclass("ライト文芸", "https://www.alphapolis.co.jp/novel/index/111500"));
        list.add(new NovelSiteTagclass("現代文学", "https://www.alphapolis.co.jp/novel/index/110700"));
        list.add(new NovelSiteTagclass("歴史・時代", "https://www.alphapolis.co.jp/novel/index/111100"));
        list.add(new NovelSiteTagclass("児童書・童話", "https://www.alphapolis.co.jp/novel/index/111200"));
        return list;
    }

    public static List<NovelSiteTagclass>  getMnltSyosetuTagList(){
        List<NovelSiteTagclass> list = new ArrayList<>();
        list.add(new NovelSiteTagclass(" ランキング[女性向け] ", "https://mnlt.syosetu.com/rank/top/"));
        list.add(new NovelSiteTagclass(" ランキング[ＢＬ] ", "https://mnlt.syosetu.com/rank/bltop/"));
        return list;
    }

    public static List<NovelSiteTagclass>  getDocSyosetuTagList(){
        List<NovelSiteTagclass> list = new ArrayList<>();
        list.add(new NovelSiteTagclass(" ランキングTOP ", "https://noc.syosetu.com/rank/top/"));
        return list;
    }

    public static List<NovelSiteTagclass>  getEstarTagList(){
        List<NovelSiteTagclass> list = new ArrayList<>();
        list.add(new NovelSiteTagclass(" Top ", "https://r.estar.jp/"));
        list.add(new NovelSiteTagclass(" ランキング ", "https://r.estar.jp/_common_work_list?key=book_work&o=read_uu&wtn=novel"));
        list.add(new NovelSiteTagclass(" 急上昇 ", "https://r.estar.jp/_common_work_list?key=book_work&wtn=novel&o=trend"));
        list.add(new NovelSiteTagclass(" 分類検索 ", "https://r.estar.jp/_search_novel_top"));
        return list;
    }

    public static List<NovelSiteTagclass>  getOtonaNovelTagList(){
        List<NovelSiteTagclass> list = new ArrayList<>();
        list.add(new NovelSiteTagclass(" ランキング ", "https://otona-novel.jp/search/ranking/2/1/?guid=ON"));
        list.add(new NovelSiteTagclass(" 完結 ", "https://otona-novel.jp/search/searchword/2/1/4/?guid=ON"));
        list.add(new NovelSiteTagclass(" 検索 ", "https://otona-novel.jp/search/index/?guid=ON"));
        return list;
    }

    public static List<NovelSiteTagclass>  getMahoNovelTagList(){
        List<NovelSiteTagclass> list = new ArrayList<>();
        list.add(new NovelSiteTagclass(" TOP ", "https://novel.maho.jp/"));
        list.add(new NovelSiteTagclass(" ランキング ", "https://novel.maho.jp/rank/daily/"));
        list.add(new NovelSiteTagclass(" 検索 ", "https://novel.maho.jp/search/?action=book_search_list&genreCD=0&q=&x=24&y=10"));
        return list;
    }

    public static List<NovelSiteTagclass>  getNoichigoNovelTagList(){
        List<NovelSiteTagclass> list = new ArrayList<>();
        list.add(new NovelSiteTagclass(" TOP ", "https://www.no-ichigo.jp/home/index/type/0"));
        list.add(new NovelSiteTagclass(" 殿堂ランキング ", "https://www.no-ichigo.jp/ranking/fame"));
        list.add(new NovelSiteTagclass(" 検索 ", "https://www.no-ichigo.jp/search"));
        return list;
    }

    public static List<NovelSiteTagclass>  getBerryscafeNovelTagList(){
        List<NovelSiteTagclass> list = new ArrayList<>();
        list.add(new NovelSiteTagclass(" TOP ", "https://www.berrys-cafe.jp"));
        list.add(new NovelSiteTagclass(" ランキング ", "https://www.berrys-cafe.jp/ranking"));
        list.add(new NovelSiteTagclass(" 検索 ", "https://www.berrys-cafe.jp/search"));
        return list;
    }

    public static List<NovelSiteTagclass>  getKakuyomuNovelTagList(){
        List<NovelSiteTagclass> list = new ArrayList<>();
        list.add(new NovelSiteTagclass(" TOP ", "https://kakuyomu.jp/"));
        list.add(new NovelSiteTagclass(" ランキング ", "https://kakuyomu.jp/rankings/all/weekly"));
        list.add(new NovelSiteTagclass(" 検索 ", "https://kakuyomu.jp/explore"));
        return list;
    }

    public static List<NovelSiteTagclass>  getAozoraTagList(){
        List<NovelSiteTagclass> list = new ArrayList<>();
        list.add(new NovelSiteTagclass(" 総合インデックス ", "https://www.aozora.gr.jp/index_pages/index_top.html"));
        list.add(new NovelSiteTagclass(" 分野別リスト ", "http://yozora.main.jp/"));
        return list;
    }

}
