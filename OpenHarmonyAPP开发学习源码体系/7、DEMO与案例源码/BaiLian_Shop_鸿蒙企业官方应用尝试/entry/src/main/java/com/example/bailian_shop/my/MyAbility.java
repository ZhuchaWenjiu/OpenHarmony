package com.example.bailian_shop.my;

import com.example.bailian_shop.ResourceTable;
import com.example.bailian_shop.shenqing.ShenqingAbility;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;

public class MyAbility extends AbilitySlice {
    private Button button_shengqing_my,button_xieyi,button_shengming;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_layou_my);
        button_shengqing_my = (Button) findComponentById(ResourceTable.Id_button_shengqing_my);
        button_xieyi = (Button) findComponentById(ResourceTable.Id_button_xieyi);
        button_shengming = (Button) findComponentById(ResourceTable.Id_button_shengming);

        shengqing();
        xieyi();
        shengming();
    }


    //跳转到鸿蒙计划页面
    public void shengqing(){
        // 为按钮设置点击回调
        button_shengqing_my.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if(button_shengqing_my!=null){
                    present(new ShenqingAbility(),new Intent());
                }
            }
        });
    }

    //跳转到订单界面
    public void xieyi(){
        // 为按钮设置点击回调
        button_xieyi.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if(button_xieyi!=null){
                    present(new XieyiAbility(),new Intent());
                }
            }
        });
    }

    //跳转到订单界面
    public void shengming(){
        // 为按钮设置点击回调
        button_shengming.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if(button_shengming!=null){
                    present(new XieyiAbility(),new Intent());
                }
            }
        });
    }


}
