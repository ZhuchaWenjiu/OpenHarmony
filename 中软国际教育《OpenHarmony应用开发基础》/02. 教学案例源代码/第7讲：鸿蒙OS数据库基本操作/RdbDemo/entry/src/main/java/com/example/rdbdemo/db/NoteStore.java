package com.example.rdbdemo.db;

import ohos.data.orm.OrmDatabase;
import ohos.data.orm.annotation.Database;

/**
 * 数据库类
 */
//@Database(entities = {User.class,Book.class,AllDataType.class},version = 1)
@Database(entities = {Note.class},version = 1)
public abstract class NoteStore extends OrmDatabase {

}
