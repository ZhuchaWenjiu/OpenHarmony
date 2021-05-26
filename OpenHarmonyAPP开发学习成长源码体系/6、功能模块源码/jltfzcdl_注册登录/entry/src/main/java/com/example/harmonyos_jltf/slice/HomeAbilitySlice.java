package com.example.harmonyos_jltf.slice;

import com.example.harmonyos_jltf.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;

public class HomeAbilitySlice extends AbilitySlice {

    @Override
    public void onStart(Intent intent) {
         super.onStart(intent);
         super.setUIContent(ResourceTable.Layout_ability_Login);
    }
}
