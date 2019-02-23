package com.zuk.ireader.event;

import com.zuk.ireader.model.flag.BookDistillate;
import com.zuk.ireader.model.flag.BookSort;
import com.zuk.ireader.model.flag.BookType;
import com.zuk.ireader.utils.Constant;

/**
 * Created by newbiechen on 17-4-21.
 */

public class SelectorEvent {

    public BookDistillate distillate;

    public BookType type;

    public BookSort sort;

    public SelectorEvent(BookDistillate distillate,
                         BookType type,
                         BookSort sort) {
        this.distillate = distillate;
        this.type = type;
        this.sort = sort;
    }
}
