package com.example.jltfmoban9.slice;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;

/**
 * Slice with MainAbility
 */
public class MainAbilitySlice extends AbilitySlice {
    private ViewCreateHelper viewCreateHelper;

    @Override
    public void onStart(Intent intent) {
        viewCreateHelper = new ViewCreateHelper(this);
        setUIContent(viewCreateHelper.createComponent());
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
