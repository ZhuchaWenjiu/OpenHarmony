package com.example.jltfmoban7.slice;

import com.example.jltfmoban7.ResourceTable;
import com.example.jltfmoban7.component.DragLayout;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;

/**
 * MainAbility slice
 */
public class MainAbilitySlice extends AbilitySlice {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        DragLayout layout = new DragLayout(this);
        layout.initGridView();
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
