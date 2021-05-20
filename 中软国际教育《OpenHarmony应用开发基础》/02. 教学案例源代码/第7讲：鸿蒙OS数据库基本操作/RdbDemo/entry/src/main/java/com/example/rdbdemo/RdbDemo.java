package com.example.rdbdemo;

import com.example.rdbdemo.utils.DataUtil;
import ohos.aafwk.ability.AbilityPackage;

public class RdbDemo extends AbilityPackage {
    @Override
    public void onInitialize() {
        super.onInitialize();
        DataUtil.onInitialize(this);
    }
}
