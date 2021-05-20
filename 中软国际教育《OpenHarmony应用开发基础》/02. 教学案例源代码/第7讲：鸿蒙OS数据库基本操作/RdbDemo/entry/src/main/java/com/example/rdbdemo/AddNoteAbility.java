package com.example.rdbdemo;

import com.example.rdbdemo.slice.AddNoteContentAbilitySlice;
import com.example.rdbdemo.slice.AddNoteTitleAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class AddNoteAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(AddNoteTitleAbilitySlice.class.getName());
        addActionRoute("action_add_content", AddNoteContentAbilitySlice.class.getName());
    }
}
