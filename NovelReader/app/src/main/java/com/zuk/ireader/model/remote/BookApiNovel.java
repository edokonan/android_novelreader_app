package com.zuk.ireader.model.remote;

import com.zuk.ireader.model.bean.novel.NovelInfoBean;
import com.zuk.ireader.model.bean.novel.NovelGenreBean;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ksymac on 2018/12/1.
 */

public interface BookApiNovel {

    /********************************主题书单**************************************8*/

    /**
     * 获取主题书单列表
     * 本周最热：duration=last-seven-days&sort=collectorCount
     * 最新发布：duration=all&sort=created
     * 最多收藏：duration=all&sort=collectorCount
     *
     * 如:http://*.appspot.com/novelgenre?site=2&genre=/rank/list/type/daily_total/
     * * @param site    都市、古代、架空、重生、玄幻、网游
     * @param genre male、female
     * @return
     */
    @GET("/novelgenre")
    Single<NovelGenreBean> getBookListPackage(@Query("site") String site, @Query("genre") String genre);

    /**
     * 书籍详情
     * @param
     * @return
     */
    @GET("/novelinfo")
    Single<NovelInfoBean> getBookDetail(@Query("site") String site, @Query("link") String link);
}