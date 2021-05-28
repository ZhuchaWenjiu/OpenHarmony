package com.example.myhomeworkproject.slice;

import com.example.myhomeworkproject.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;

public class MainAbility2Slice extends AbilitySlice {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main2);

        Button button2 = (Button) findComponentById(ResourceTable.Id_fanweiButton);
        button2.setClickedListener(new Component.ClickedListener(){
            @Override
            public void onClick(Component component){
                present(new MainAbilitySlice(),new Intent());
            }
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
