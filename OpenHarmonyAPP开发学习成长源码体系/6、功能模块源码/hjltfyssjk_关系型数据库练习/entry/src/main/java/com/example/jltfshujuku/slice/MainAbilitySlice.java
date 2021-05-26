package com.example.jltfshujuku.slice;

import com.example.jltfshujuku.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.DataAbilityHelper;
import ohos.aafwk.ability.DataAbilityRemoteException;
import ohos.aafwk.ability.IDataAbilityObserver;
import ohos.aafwk.content.Intent;
import ohos.data.dataability.DataAbilityPredicates;
import ohos.data.rdb.ValuesBucket;
import ohos.data.resultset.ResultSet;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.utils.net.Uri;

public class MainAbilitySlice extends AbilitySlice {
     /*创建DataAbilityHelper*/
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(3, 0xD001100, "MainAbilitySlice");

    private DataAbilityHelper databaseHelper;
    /*客户端访问数据库常量定义 */
    private static final String BASE_URI = "dataability:///com.example.jltfshujuku.PersonDataAbility";

    private static final String DATA_PATH = "/person";

    private static final String DB_COLUMN_PERSON_ID = "id";

    private static final String DB_COLUMN_NAME = "name";

    private static final String DB_COLUMN_GENDER = "gender";

    private static final String DB_COLUMN_AGE = "age";
/*查询方法*/
    private void query() {
        String[] columns = new String[] {DB_COLUMN_PERSON_ID,
                DB_COLUMN_NAME, DB_COLUMN_GENDER, DB_COLUMN_AGE};
        // 构造查询条件
        DataAbilityPredicates predicates = new DataAbilityPredicates();
        predicates.between(DB_COLUMN_AGE, 15, 40);
        try {
            ResultSet resultSet = databaseHelper.query(Uri.parse(BASE_URI + DATA_PATH),
                    columns, predicates);
            if (resultSet == null || resultSet.getRowCount() == 0) {
                HiLog.info(LABEL_LOG, "query: resultSet is null or no result found");
                return;
            }
            resultSet.goToFirstRow();
            do {
                int id = resultSet.getInt(resultSet.getColumnIndexForName(DB_COLUMN_PERSON_ID));
                String name = resultSet.getString(resultSet.getColumnIndexForName(DB_COLUMN_NAME));
                String gender = resultSet.getString(resultSet.getColumnIndexForName(DB_COLUMN_GENDER));
                int age = resultSet.getInt(resultSet.getColumnIndexForName(DB_COLUMN_AGE));
                HiLog.info(LABEL_LOG, "query: Id :" + id + " Name :" + name + " Gender :" + gender + " Age :" + age);
            } while (resultSet.goToNextRow());
        } catch (DataAbilityRemoteException | IllegalStateException exception) {
            HiLog.error(LABEL_LOG, "query: dataRemote exception | illegalStateException");
        }
    }
    /*新增方法*/
    private void insert(int id, String name, String gender, int age) {
        ValuesBucket valuesBucket = new ValuesBucket();
        valuesBucket.putInteger(DB_COLUMN_PERSON_ID, id);
        valuesBucket.putString(DB_COLUMN_NAME, name);
        valuesBucket.putString(DB_COLUMN_GENDER, gender);
        valuesBucket.putInteger(DB_COLUMN_AGE, age);
        try {
            if (databaseHelper.insert(Uri.parse(BASE_URI + DATA_PATH), valuesBucket) != -1) {
                HiLog.info(LABEL_LOG, "insert successful");
            }
        } catch (DataAbilityRemoteException | IllegalStateException exception) {
            HiLog.error(LABEL_LOG, "insert: dataRemote exception|illegalStateException");
        }
    }
    /*更新方法*/
    private void update() {
        DataAbilityPredicates predicates = new DataAbilityPredicates();
        predicates.equalTo(DB_COLUMN_PERSON_ID, 102);
        ValuesBucket valuesBucket = new ValuesBucket();
        valuesBucket.putString(DB_COLUMN_NAME, "ZhangSanPlus");
        valuesBucket.putInteger(DB_COLUMN_AGE, 28);
        try {
            if (databaseHelper.update(Uri.parse(BASE_URI + DATA_PATH), valuesBucket, predicates) != -1) {
                HiLog.info(LABEL_LOG, "update successful");
            }
        } catch (DataAbilityRemoteException | IllegalStateException exception) {
            HiLog.error(LABEL_LOG, "update: dataRemote exception | illegalStateException");
        }
    }
    /*删除方法*/
    private void delete() {
        DataAbilityPredicates predicates = new DataAbilityPredicates();
        predicates.equalTo(DB_COLUMN_PERSON_ID, 100);
        try {
            if (databaseHelper.delete(Uri.parse(BASE_URI + DATA_PATH), predicates) != -1) {
                HiLog.info(LABEL_LOG, "delete successful");
            }
        } catch (DataAbilityRemoteException | IllegalStateException exception) {
            HiLog.error(LABEL_LOG, "delete: dataRemote exception | illegalStateException");
        }
    }
    /*订阅数据变化*/
    IDataAbilityObserver dataAbilityObserver;

    private void personDatabaseObserver() {
        dataAbilityObserver = new IDataAbilityObserver() {
            @Override
            public void onChange() {
                // 订阅者接收目标数据表格产生变化的通知，通过查询获取最新的数据
                query();
            }
        };
        // 根据uri指定订阅的数据表
        databaseHelper.registerObserver(Uri.parse(BASE_URI + DATA_PATH), dataAbilityObserver);
    }

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        /*创建其实例*/
        databaseHelper = DataAbilityHelper.creator(this);
        query();
        insert(100, "Tom", "male", 20);
        insert(101, "Jerry", "female", 21);
        insert(102, "Bob", "male", 22);
        query(); // 查看插入后的结果
        update();
        query(); // 查看更新后的结果
        delete();
        query(); // 查看删除后的结果

    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
