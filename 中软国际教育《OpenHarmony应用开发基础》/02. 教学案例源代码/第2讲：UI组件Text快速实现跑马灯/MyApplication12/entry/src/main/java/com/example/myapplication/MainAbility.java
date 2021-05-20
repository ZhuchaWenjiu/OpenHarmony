package com.example.myapplication;

import com.example.myapplication.slice.MainAbilitySlice;
import com.example.myapplication.slice.MainAbilitySlice2;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class MainAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(MainAbilitySlice2.class.getName());
    }
}
