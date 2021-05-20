package com.example.rdbdemo;

import com.example.rdbdemo.slice.NoteInfoAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class NoteInfoAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(NoteInfoAbilitySlice.class.getName());
    }
}
