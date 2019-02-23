package com.zuk.ireader.model.flag;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.zuk.ireader.App;
import com.zuk.ireader.R;

/**
 * Created by newbiechen on 17-4-25.
 */

public enum FindType {
    syosetu_data(R.string.nb_fragment_find_syosetu_data,R.drawable.ic_section_top),
    syosetu_web(R.string.nb_fragment_find_syosetu_web,R.drawable.ic_section_topic),
    syosetu_ranking(R.string.nb_fragment_find_syosetu_ranking,R.drawable.ic_section_top),
    syosetu_search(R.string.nb_fragment_find_syosetu_search,R.drawable.ic_section_sort),

    alphapolis_web(R.string.nb_fragment_find_alphapolis_web,R.drawable.ic_section_topic),
    alphapolis_ranking(R.string.nb_fragment_find_alphapolis_ranking,R.drawable.ic_section_top),
    alphapolis_search(R.string.nb_fragment_find_alphapolis_search,R.drawable.ic_section_sort),

//    JPNovel1(R.string.nb_fragment_find_novel1,R.drawable.ic_section_top),
//    JPNovel2(R.string.nb_fragment_find_novel2,R.drawable.ic_section_top),
//    JPNovel3(R.string.nb_fragment_find_novel3,R.drawable.ic_section_top),
//    JPNovel4(R.string.nb_fragment_find_novel4,R.drawable.ic_section_top),
//    JPNovel5(R.string.nb_fragment_find_novel5,R.drawable.ic_section_top),
//    JPNovel6(R.string.nb_fragment_find_novel6,R.drawable.ic_section_top),
//    JPNovel7(R.string.nb_fragment_find_novel7,R.drawable.ic_section_top),
//    JPNovel8(R.string.nb_fragment_find_novel8,R.drawable.ic_section_top),
//    JPNovel9(R.string.nb_fragment_find_novel9,R.drawable.ic_section_top),
//    JPNovel10(R.string.nb_fragment_find_novel10,R.drawable.ic_section_top),
//    JPNovel11(R.string.nb_fragment_find_novel11,R.drawable.ic_section_top),
//
    TOP(R.string.nb_fragment_find_top,R.drawable.ic_section_top),
    TOPIC(R.string.nb_fragment_find_topic,R.drawable.ic_section_topic),
    SORT(R.string.nb_fragment_find_sort,R.drawable.ic_section_sort),
    LISTEN(R.string.nb_fragment_find_listen,R.drawable.ic_section_listen);
    ;
    private String typeName;
    private int iconId;

    private FindType(@StringRes int typeNameId,@DrawableRes int iconId){
        this.typeName = App.getContext().getResources().getString(typeNameId);
        this.iconId = iconId;
    }

    public String getTypeName(){
        return typeName;
    }

    public int getIconId(){
        return iconId;
    }
}
