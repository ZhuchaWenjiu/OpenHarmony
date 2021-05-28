package com.example.myhomeworkproject.slice;

import com.example.myhomeworkproject.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;

public class MainAbilitySlice extends AbilitySlice {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);

        Button button1 = (Button) findComponentById(ResourceTable.Id_xinwenButton);
        button1.setClickedListener(new Component.ClickedListener(){
            @Override
            public void onClick(Component component){
                present(new MainAbility2Slice(),new Intent());
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
