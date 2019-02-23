package com.zuk.ireader.business.parse;

import com.google.gson.Gson;
import com.zuk.ireader.business.config.ComCode;
import com.zuk.ireader.model.bean.novel.NovelInfoChapterdata;
import com.zuk.ireader.model.bean.novel.NovelInfoBean;
import com.zuk.ireader.model.bean.novel.NovelInfoData;
import com.zuk.ireader.realm.DBManagerApiCache;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

/**
 * Created by ksymac on 2019/1/5.
 */

public class NovelParseAlphapolis {

    public static NovelInfoBean parse_novelinfo_html(String html, String novelurl){
        Document doc = Jsoup.parse(html);
        NovelInfoData data = getData_alphapolis_novel(doc,novelurl);
        if(data!= null && data.err == false && data.novel_url !=null && data.novel_url.length() >0) {
            NovelInfoBean bean = new NovelInfoBean();
            bean.novelinfodata = data;
            bean.err = false;

            String json = new Gson().toJson(bean);
            DBManagerApiCache.saveToCache(novelurl, ComCode.apiver, json);
            return bean;
        }else{
            return null;
        }
    }


    //https://www.alphapolis.co.jp/novel/417374462/957210370
    public static NovelInfoData getData_alphapolis_novel(Document document, String str_link) {
        NovelInfoData data = new NovelInfoData();
        data.siteno = ComCode.siteno_alphapolis;
        data.sitename = "alphapolis";
        data.novel_url = str_link;
        data.novel_original_no = str_link.replace("https://www.alphapolis.co.jp/novel/","").replace("/","_");
        if(document == null) {
            return null;
        }
        try{
            Element novel_abstract = document.select("div.abstract").first();
            if(novel_abstract != null){
                data.novel_ex = novel_abstract.text();
            }else{
                data.err = true;
                return data;
            }
            Element novel_writername = document.select("div.author").first();
            if(novel_writername != null){
                Element item_a = novel_writername.getElementsByTag("a").first();
                if(item_a != null){
                    data.novel_writername = item_a.text();
                    data.novel_writername_link = "https://www.alphapolis.co.jp" + item_a.attr("href");
                }
            }else{
                data.err = true;
                return data;
            }
            Element novel_content_main = document.select("div.content-main").first();
            if(novel_content_main != null){
                Element item_a = novel_content_main.getElementsByClass("title").first();
                if(item_a != null){
                    data.novel_title = item_a.text();
                }
            }
            Element novel_tags = document.select("div.content-tags").first();
            if(novel_tags != null){
                data.novel_tags = novel_tags.text();
            }
//            data.novel_tags = document.html();
            Element episodes = document.getElementsByClass("episodes").first();
            int no = 0;
            data.chapteritems = new ArrayList<NovelInfoChapterdata>();
            for(int i=0;i<episodes.children().size();i++){
                Element item = episodes.child(i);
                if(item.tagName() == "h3"){
                    String temp = item.text().replace(" ", "");
                    if(temp.length() >0){
                        NovelInfoChapterdata bean = new NovelInfoChapterdata();
                        bean.siteno = ComCode.siteno_alphapolis;
                        bean.chapter_title= item.text();
                        bean.no = no++;
                        data.chapteritems.add(bean);
                    }
                }else if(item.className().contains("episode")){
                    NovelInfoChapterdata bean = new NovelInfoChapterdata();
                    bean.siteno = ComCode.siteno_alphapolis;
                    bean.no = no++;
                    Element span_title = item.getElementsByClass("title").first();
                    bean.chapter_title= span_title.text();

                    Element link_a = item.getElementsByTag("a").first();
                    String  link_relHref = link_a.attr("href");
                    link_relHref = "https://www.alphapolis.co.jp" + link_relHref;
                    bean.chapter_url= link_relHref;

                    Element link_open_date = item.getElementsByClass("open-date").first();
                    bean.chapter_time= link_open_date.text();

                    bean.chapter_no= link_relHref.replace(str_link,"").replace("/episode/","");

                    Element counter = item.getElementsByClass("counter").first();
                    bean.chapter_otherinfo= counter.text();

                    data.chapteritems.add(bean);
                }
            }
            data.err = false;
        }catch (Exception e) {
            e.printStackTrace();
            data.err = true;
        }
        return data;
    }

}
