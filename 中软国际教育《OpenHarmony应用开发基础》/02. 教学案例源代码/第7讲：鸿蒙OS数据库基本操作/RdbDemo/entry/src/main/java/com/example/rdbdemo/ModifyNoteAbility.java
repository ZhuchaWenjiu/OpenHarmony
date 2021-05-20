package com.example.rdbdemo;

import com.example.rdbdemo.slice.ModifyNoteContentAbilitySlice;
import com.example.rdbdemo.slice.ModifyNoteTitleAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class ModifyNoteAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(ModifyNoteTitleAbilitySlice.class.getName());
        addActionRoute("action_modify_content", ModifyNoteContentAbilitySlice.class.getName());
    }
}
