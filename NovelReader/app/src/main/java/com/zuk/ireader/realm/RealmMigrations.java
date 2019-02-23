package com.zuk.ireader.realm;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

/**
 * Created by ksymac on 2019/1/10.
 */

public class RealmMigrations implements RealmMigration {

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        final RealmSchema schema = realm.getSchema();

        if (oldVersion <= 1) {
            final RealmObjectSchema userSchema = schema.get("TableReadHistory");
            userSchema.addField("siteno", String.class);
            userSchema.addField("title", String.class);
            userSchema.addField("author", String.class);
            userSchema.addField("summary", String.class);
            userSchema.addField("time", String.class);
            userSchema.addField("novel_original_no", String.class);

            userSchema.addField("novelimgurl", String.class);
            userSchema.addField("chaptertitle", String.class);
            userSchema.addField("chaptertime", String.class);
            userSchema.addField("chapterno", String.class);
        }
//        if (oldVersion <= 2) {
//            final RealmObjectSchema userSchema = schema.get("TableBookChapter");
//            userSchema.addField("pagerange", int.class);
//        }
    }
}