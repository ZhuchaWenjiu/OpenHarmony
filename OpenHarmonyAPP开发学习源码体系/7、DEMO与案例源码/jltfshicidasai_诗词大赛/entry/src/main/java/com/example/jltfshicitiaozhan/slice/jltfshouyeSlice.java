package com.example.jltfshicitiaozhan.slice;

import com.example.jltfshicitiaozhan.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.Text;

public class jltfshouyeSlice extends AbilitySlice {


    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_jltf_shouye);

        Button button = (Button) findComponentById(ResourceTable.Id_jltf_shouye);
        button.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                present(new MainAbilitySlice(),new Intent());
            }
        });

        Text text4 = (Text) findComponentById(ResourceTable.Id_jltf_juansi);
        text4.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                present(new jltfjieshuyeSlice(),new Intent());
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
