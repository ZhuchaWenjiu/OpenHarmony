package com.example.myapplication;

import com.example.myapplication.slice.ResultAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class ResultAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(ResultAbilitySlice.class.getName());
    }
}
