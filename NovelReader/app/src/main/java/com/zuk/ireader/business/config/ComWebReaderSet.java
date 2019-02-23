package com.zuk.ireader.business.config;

import com.zuk.ireader.realm.DBManagerApiCache;

/**
 * Created by ksymac on 2019/1/27.
 */

public class ComWebReaderSet {
    //    255
    //    237 255 208
    //    224 205 182
    //    183 207 194
    //    209 209 209
    public static String webreader_setting_ver = "webreader_setting_ver";
    public static final String webreader_background_color1 = "#FFFFFF";
    public static final String webreader_background_color2 = "#EDFFD0";
    public static final String webreader_background_color3 = "#E0CDB6";
    public static final String webreader_background_color4 = "#B7CFC2";
    public static final String webreader_background_color5 = "#D1D1D1";
    public static final String webreader_background_color_default = webreader_background_color3;

    public static boolean setWebBackGroundColor(int index){
        String nowcolor = getWebBackGroundColor();
        String newcolor = webreader_background_color_default;
        if(index == 2){
            newcolor = webreader_background_color2;
        }else if(index == 3){
            newcolor = webreader_background_color3;
        }else if(index == 4){
            newcolor = webreader_background_color4;
        }else if(index == 5){
            newcolor = webreader_background_color5;
        }else{
            newcolor = webreader_background_color1;
        }
        DBManagerApiCache.saveToCache("webreader_background_color",
                webreader_setting_ver, newcolor);
        if (nowcolor.equals(newcolor)){
            return false;
        }
        return true;
    }
    public static String getWebBackGroundColor(){
        String ret = DBManagerApiCache.getJsouFromCache("webreader_background_color",
                webreader_setting_ver);
        if(ret == null){
            return webreader_background_color_default;
        }else{
            return ret ;
        }
    }

    public static boolean setWebFontSize(boolean bigger){
        int nowSize = getWebFontSize();
        int newsize = nowSize;
        if(bigger){
            newsize += 2;
        }else{
            newsize -= 2;
        }
        if (newsize<45 & newsize>10){
            DBManagerApiCache.saveToCache("webreader_fontsize",
                    webreader_setting_ver, String.format("%d",newsize));
            return true;
        }else{
            return false;
        }
    }
    public static int getWebFontSize(){
        String ret = DBManagerApiCache.getJsouFromCache("webreader_fontsize",
                webreader_setting_ver);
        if(ret == null){
            return 20;
        }else{
            return Integer.valueOf(ret) ;
        }
    }
}
