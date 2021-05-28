package com.example.jltftiyan3.slice;

import com.example.jltftiyan3.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.Text;
import ohos.agp.components.TextField;
import ohos.agp.components.element.ShapeElement;

public class MainAbilitySlice extends AbilitySlice {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);

        // 当点击登录，改变相应组件的样式
        Button button = (Button) findComponentById(ResourceTable.Id_ensure_button);
        button.setClickedListener((component -> {
            // 显示错误提示的Text
            Text text = (Text) findComponentById(ResourceTable.Id_error_tip_text);
            text.setVisibility(Component.VISIBLE);

            // 显示TextField错误状态下的样式
            ShapeElement errorElement = new ShapeElement(this, ResourceTable.Graphic_jltfbackground_text_field_error);
            TextField textField = (TextField) findComponentById(ResourceTable.Id_password_text_field);
            textField.setBackground(errorElement);
            // TextField失去焦点
            textField.clearFocus();

        }));
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
