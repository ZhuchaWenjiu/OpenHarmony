package com.example.myapplication.slice;

import com.example.myapplication.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;

import ohos.aafwk.content.Operation;
import ohos.agp.components.*;
import ohos.agp.components.DirectionalLayout.LayoutConfig;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.agp.utils.TextAlignment;

public class MainAbilitySlice extends AbilitySlice {

    TextField content;
    Button send;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_main);//Ctrl + Alt +空格 提示

        //通过id找到UI组件
        content = (TextField) findComponentById(ResourceTable.Id_content);
        send = (Button) findComponentById(ResourceTable.Id_send);

        //给Button添加，注册点击事件，
        send.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                //我们所在点击以后想完成 的功能 都可以放到这里，
                //1、创建 Intent对象，
                Intent intent1 = new Intent();
                //2、创建Operation对象
                Operation operation = new Intent.OperationBuilder()
                        .withBundleName("com.example.myapplication")
                        .withAbilityName("com.example.myapplication.ResultAbility")
                        .build();

                intent1.setOperation(operation);
                //3.添加数据
                intent1.setParam("content",content.getText());
                //4、startAbility方法启动Ability
                startAbility(intent1);
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
