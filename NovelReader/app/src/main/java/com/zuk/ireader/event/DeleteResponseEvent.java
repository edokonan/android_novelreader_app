package com.zuk.ireader.event;

import com.zuk.ireader.model.bean.CollBookBean;

/**
 * Created by newbiechen on 17-5-27.
 */

public class DeleteResponseEvent {
    public boolean isDelete;
    public CollBookBean collBook;
    public DeleteResponseEvent(boolean isDelete,CollBookBean collBook){
        this.isDelete = isDelete;
        this.collBook = collBook;
    }
}
