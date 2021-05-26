package com.example.bailian_shop.shenqing;

import com.example.bailian_shop.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;

public class TijiaoAbility extends AbilitySlice {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_layout_shentijiao);
    }
}
