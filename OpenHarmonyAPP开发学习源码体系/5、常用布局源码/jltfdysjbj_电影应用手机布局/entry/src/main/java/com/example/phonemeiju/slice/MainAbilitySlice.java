package com.example.phonemeiju.slice;

import com.example.phonemeiju.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;

public class MainAbilitySlice extends AbilitySlice {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_hot);

        //热门分类
        Button reMen = (Button)findComponentById(ResourceTable.Id_reMen);
        reMen.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                present(new reMenAbilitySlice(),new Intent());
            }
        });

        //最新分类
        Button zuiXin = (Button)findComponentById(ResourceTable.Id_zuiXin);
        zuiXin.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                present(new zuiXinAbilitySlice(),new Intent());
            }
        });

        //专题分类
        Button zhuanTi = (Button)findComponentById(ResourceTable.Id_zhuanTi);
        zhuanTi.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                present(new zhuanTiAbilitySlice(),new Intent());
            }
        });

        //一周追剧表分类
        Button table = (Button)findComponentById(ResourceTable.Id_table);
        table.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                present(new tableAbilitySlice(),new Intent());
            }
        });
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
