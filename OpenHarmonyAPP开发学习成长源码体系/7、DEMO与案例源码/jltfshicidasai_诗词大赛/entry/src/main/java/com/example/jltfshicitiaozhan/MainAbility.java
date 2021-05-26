package com.example.jltfshicitiaozhan;

import com.example.jltfshicitiaozhan.slice.MainAbilitySlice;
import com.example.jltfshicitiaozhan.slice.jltfshouyeSlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class MainAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        /*super.setMainRoute(MainAbilitySlice.class.getName());*/
        super.setMainRoute(jltfshouyeSlice.class.getName());
    }
}
