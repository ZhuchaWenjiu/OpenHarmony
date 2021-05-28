package com.example.jltfshujuku;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.DataAbilityHelper;
import ohos.aafwk.content.Intent;
import ohos.data.DatabaseHelper;
import ohos.data.dataability.DataAbilityUtils;
import ohos.data.rdb.*;
import ohos.data.resultset.ResultSet;
import ohos.data.dataability.DataAbilityPredicates;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.utils.net.Uri;
import ohos.utils.PacMap;

import java.io.FileDescriptor;

public class PersonDataAbility extends Ability {

    /*定义Data Ability数据库相关常量*/
    private static final String DB_NAME = "persondataability.db";

    private static final String DB_TAB_NAME = "person";

    private static final String DB_COLUMN_PERSON_ID = "id";

    private static final String DB_COLUMN_NAME = "name";

    private static final String DB_COLUMN_GENDER = "gender";

    private static final String DB_COLUMN_AGE = "age";

    private static final int DB_VERSION = 1;

  /*  创建关系型数据库*/
    private StoreConfig config = StoreConfig.newDefaultConfig(DB_NAME);

    private RdbStore rdbStore;

    private RdbOpenCallback rdbOpenCallback = new RdbOpenCallback() {
        @Override
        public void onCreate(RdbStore store) {
            store.executeSql("create table if not exists "
                    + DB_TAB_NAME + " ("
                    + DB_COLUMN_PERSON_ID + " integer primary key, "
                    + DB_COLUMN_NAME + " text not null, "
                    + DB_COLUMN_GENDER + " text not null, "
                    + DB_COLUMN_AGE + " integer)");
        }

        @Override
        public void onUpgrade(RdbStore store, int oldVersion, int newVersion) {
        }
    };


    private static final HiLogLabel LABEL_LOG = new HiLogLabel(3, 0xD001100, "Demo");

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        HiLog.info(LABEL_LOG, "PersonDataAbility onStart");
        /*初始化数据库连接*/
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        rdbStore = databaseHelper.getRdbStore(config, DB_VERSION, rdbOpenCallback, null);
    }
    /*重写数据库操作方法*/
    @Override
    public ResultSet query(Uri uri, String[] columns, DataAbilityPredicates predicates) {
        /*return null;*/
        RdbPredicates rdbPredicates = DataAbilityUtils.createRdbPredicates(predicates, DB_TAB_NAME);
        ResultSet resultSet = rdbStore.query(rdbPredicates, columns);
        if (resultSet == null) {
            HiLog.info(LABEL_LOG, "resultSet is null");
        }
        return resultSet;
    }

    @Override
    public int insert(Uri uri, ValuesBucket value) {
        HiLog.info(LABEL_LOG, "PersonDataAbility insert");
        /*return 999;*/
        String path = uri.getLastPath();
        if (!"person".equals(path)) {
            HiLog.info(LABEL_LOG, "DataAbility insert path is not matched");
            return -1;
        }
        ValuesBucket values = new ValuesBucket();
        values.putInteger(DB_COLUMN_PERSON_ID, value.getInteger(DB_COLUMN_PERSON_ID));
        values.putString(DB_COLUMN_NAME, value.getString(DB_COLUMN_NAME));
        values.putString(DB_COLUMN_GENDER, value.getString(DB_COLUMN_GENDER));
        values.putInteger(DB_COLUMN_AGE, value.getInteger(DB_COLUMN_AGE));
        int index = (int) rdbStore.insert(DB_TAB_NAME, values);
        DataAbilityHelper.creator(this, uri).notifyChange(uri);
        return index;
    }

    @Override
    public int delete(Uri uri, DataAbilityPredicates predicates) {
        /*return 0;*/
        RdbPredicates rdbPredicates = DataAbilityUtils.createRdbPredicates(predicates, DB_TAB_NAME);
        int index = rdbStore.delete(rdbPredicates);
        HiLog.info(LABEL_LOG, "delete: " + index);
        DataAbilityHelper.creator(this, uri).notifyChange(uri);
        return index;
    }

    @Override
    public int update(Uri uri, ValuesBucket value, DataAbilityPredicates predicates) {
      /*  return 0;*/
        RdbPredicates rdbPredicates = DataAbilityUtils.createRdbPredicates(predicates, DB_TAB_NAME);
        int index = rdbStore.update(value, rdbPredicates);
        HiLog.info(LABEL_LOG, "update: " + index);
        DataAbilityHelper.creator(this, uri).notifyChange(uri);
        return index;
    }

    @Override
    public FileDescriptor openFile(Uri uri, String mode) {
        return null;
    }

    @Override
    public String[] getFileTypes(Uri uri, String mimeTypeFilter) {
        return new String[0];
    }

    @Override
    public PacMap call(String method, String arg, PacMap extras) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }
}