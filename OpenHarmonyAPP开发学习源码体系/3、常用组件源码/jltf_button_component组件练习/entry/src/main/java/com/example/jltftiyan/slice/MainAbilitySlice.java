package com.example.jltftiyan.slice;

import com.example.jltftiyan.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;

public class MainAbilitySlice extends AbilitySlice {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);

        Button button = (Button) findComponentById(ResourceTable.Id_jltfbtn);
        button.setClickedListener(l -> {
            //更改Button组件的内容
            button.setText("我被点击了~");
        });
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
