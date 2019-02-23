package com.zuk.ireader.realm;

import java.util.Date;
import io.realm.RealmObject;

/**
 * Created by ksymac on 2018/12/15.
 */
public class TableApiCache extends RealmObject {
    public String key;
    public String ver;
    public String jsondata;
    public Date updatetime;
}
