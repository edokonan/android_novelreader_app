package com.zuk.ireader.business.config;

/**
 * Created by ksymac on 2019/1/5.
 */

public class ComCode {

    public static final String ColorBackground1 = "#ebf0f2";

    //api version
    public static final String apiver = "3";
    public static final String apiver_genre = "3";

    public static final int apicache_novelinfo_hoursago_0 = 0;
    public static final int apicache_novelinfo_hoursago_1 = 1;//１時間以内のデータを更新しないように
    public static final int apicache_novelinfo_hoursago_2 = 2;//2時間以内のデータを更新しないように

    //siteno
    public static final String siteno_ncode_syosetu = "2";
    public static final String siteno_novel18_syosetu = "3";
    public static final String siteno_alphapolis = "10";
    public static final String siteno_estar = "20";
    public static final String siteno_estar_r = "21";
    public static final String siteno_otona_novel = "30";
    public static final String siteno_maho = "40";
    public static final String siteno_noichigo = "50";
    public static final String siteno_berryscafe = "60";
    public static final String siteno_kakuyomu = "70";
    public static final String siteno_aozora = "80";

//    public static final String siteno_kanno_novel = "40";

    public static final String sitenoname_ncode_syosetu = "小説を読もう！";
    public static final String sitenoname_novel18_syosetu = "ムーンライトノベルズ";
    public static final String sitenoname_alphapolis = "アルファポリス";
    public static final String sitenoname_estar = "エブリスタ";
    public static final String sitenoname_estar_r = "エブリスタ";
    public static final String sitenoname_otona_novel = "ちょっと大人のケータイ小説";
    public static final String sitenoname_maho = "魔法のiらんど";
    public static final String sitenoname_noichigo = "野いちご";
    public static final String sitenoname_berryscafe = "ベリーズカフェ";
    public static final String sitenoname_kakuyomu = "カクヨム";
    public static final String sitenoname_aozora = "青空文庫";


    public static  String getBaseUrl(String Code){
        if(Code.equalsIgnoreCase(ComCode.siteno_maho)){
            return "https://s.maho.jp/";
        }else if(Code.equalsIgnoreCase(ComCode.siteno_aozora)){
            return "https://www.aozora.gr.jp/";
        }else if(Code.equalsIgnoreCase(ComCode.siteno_alphapolis)){
            return "https://www.alphapolis.co.jp/";
        }else if(Code.equalsIgnoreCase(ComCode.sitenoname_estar)){
            return "https://estar.jp/";
        }else if(Code.equalsIgnoreCase(ComCode.sitenoname_noichigo)){
            return "https://www.no-ichigo.jp/";
        }else if(Code.equalsIgnoreCase(ComCode.sitenoname_berryscafe)){
            return "https://www.berrys-cafe.jp/";
        }else if(Code.equalsIgnoreCase(ComCode.sitenoname_kakuyomu)){
            return "https://kakuyomu.jp/";
        }
        return "";
    }

    public static Boolean isMultWebPages(String Code){
        if(Code.equalsIgnoreCase(ComCode.siteno_otona_novel) ||
                Code.equalsIgnoreCase(ComCode.siteno_estar) ||
                Code.equalsIgnoreCase(ComCode.siteno_estar_r) ||
                Code.equalsIgnoreCase(ComCode.siteno_maho)||
                Code.equalsIgnoreCase(ComCode.siteno_noichigo)||
                Code.equalsIgnoreCase(ComCode.siteno_berryscafe)
                ){
            return true;
        }
        return false;
    }

    public static Boolean isSingleWebPage(String Code){
        if(Code.equalsIgnoreCase(ComCode.siteno_kakuyomu) ||
                Code.equalsIgnoreCase(ComCode.siteno_aozora) ||
                Code.equalsIgnoreCase(ComCode.siteno_alphapolis)
                ){
            return true;
        }
        return false;
    }

//	http://localhost:8080/novelinfo?link=https://ncode.syosetu.com/n3191eh/
//	http://localhost:8080/novelinfo?link=https://novel18.syosetu.com/n1309fe/
//	http://localhost:8080/novelinfo?link=https://www.alphapolis.co.jp/novel/241244548/554156165
//	http://localhost:8080/novelinfo?link=https://estar.jp/_novel_view?w=24985865
//	http://localhost:8080/novelinfo?link=https://otona-novel.jp/viewstory/index/29066/?guid=ON

    public static boolean is_ncode_syosetu_novelurl( String url){
        //https://ncode.syosetu.com/n2107ff/
        if(url.startsWith("https://ncode.syosetu.com/n")){
            String tempstr = url.replace("https://ncode.syosetu.com/","");
            String[] list = tempstr.split("/",2);
            if(list.length<2){
                return true;
            }else{
                if(list[1]!=null && list[1].length()>0 ){
                    return false;
                }else{
                    return true;
                }
            }
        }
        return false;
    }
    public static boolean is_estar_novelurl( String url){
        //	https://estar.jp/_novel_view?w=24985865
        if(url.startsWith("https://estar.jp/_novel_view?w=")){
            String tempstr = url.replace("https://estar.jp/_novel_view?w=","");
            return true;
        }
        return false;
    }
    public static boolean is_estar_r_novelurl( String url){
        //	https://r.estar.jp/_novel_view?w=25310791
        if(url.startsWith("https://r.estar.jp/_novel_view?w=")){
            String tempstr = url.replace("https://r.estar.jp/_novel_view?w=","");
            return true;
        }
        return false;
    }
    public static boolean is_otona_novel_novelurl( String url){
        //	https://otona-novel.jp/viewstory/index/29066/?guid=ON
        if(url.startsWith("https://otona-novel.jp/viewstory/index/")){
            String tempstr = url.replace("https://otona-novel.jp/viewstory/index/","");
            return true;
        }
        return false;
    }
    public static boolean is_maho_novel_novelurl( String url){
        //https://s.maho.jp/book/11ec5cj743f98fdc/6960577035/#_ga=2.89072987.1566361251.1549330115-430419340.1545657471
        if(url.startsWith("https://s.maho.jp/book/")){
//            String tempstr = url.replace("https://otona-novel.jp/viewstory/index/","");
            return true;
        }
        return false;
    }

    public static boolean is_noichigo_novel_novelurl( String url){
        //https://www.no-ichigo.jp/read/book?book_id=1534173
        if(url.startsWith("https://www.no-ichigo.jp/read/book")){
//            String tempstr = url.replace("https://otona-novel.jp/viewstory/index/","");
            return true;
        }
        return false;
    }
    public static boolean is_berryscafe_novel_novelurl( String url){
        //
        if(url.startsWith("https://www.berrys-cafe.jp/pc/book/") || url.startsWith("https://www.berrys-cafe.jp/spn/book/")){
//            String tempstr = url.replace("https://otona-novel.jp/viewstory/index/","");
            return true;
        }
        return false;
    }

    public static boolean is_kakuyomu_novel_novelurl( String url){
        //https://kakuyomu.jp/works/1177354054888009404
        if(url.startsWith("https://kakuyomu.jp/works/")){
//            String tempstr = url.replace("https://otona-novel.jp/viewstory/index/","");
            return true;
        }
        return false;
    }

    public static boolean is_aozora_novel_novelurl( String url){
        //https://www.aozora.gr.jp/cards/000020/card2569.html
        if(url.startsWith("https://www.aozora.gr.jp/cards/")  && !url.contains("files") ){
//            String tempstr = url.replace("https://otona-novel.jp/viewstory/index/","");
            return true;
        }
        return false;
    }


    public static boolean is_novel18_syosetu_novelurl( String url){
        //	http://localhost:8080/novelinfo?link=https://novel18.syosetu.com/n1309fe/
        if(url.startsWith("https://novel18.syosetu.com/n")){
            String tempstr = url.replace("https://novel18.syosetu.com/","");
            String[] list = tempstr.split("/",2);
            if(list.length<2){
                return true;
            }else{
                if(list[1]!=null && list[1].length()>0 ){
                    return false;
                }else{
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean is_Alphapolis_novelurl( String url){
        if(url.startsWith("https://www.alphapolis.co.jp/novel/")){
            String tempstr = url.replace("https://www.alphapolis.co.jp/novel/","");
            String[] list = tempstr.split("/",3);
            if(list.length<3){
                return true;
            }
        }
        return false;
    }
}
