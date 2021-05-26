package com.example.bailian_shop;

import com.example.bailian_shop.move.ShipingAbility;
import com.example.bailian_shop.slice.MainAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class MainAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(MainAbilitySlice.class.getName());

        //创建VideoAbilitySlice对应的Action供跳转使用
        super.addActionRoute("action.layout_shiping", ShipingAbility.class.getName());
    }
}
