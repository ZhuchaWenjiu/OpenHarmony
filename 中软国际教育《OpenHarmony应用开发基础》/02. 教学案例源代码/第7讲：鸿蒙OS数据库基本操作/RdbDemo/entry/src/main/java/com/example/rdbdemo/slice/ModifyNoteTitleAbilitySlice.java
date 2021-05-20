package com.example.rdbdemo.slice;

import com.example.rdbdemo.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.TextField;

public class ModifyNoteTitleAbilitySlice extends AbilitySlice {

    TextField name;
    Button next;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);

        super.setUIContent(ResourceTable.Layout_add_note_title);
        name = (TextField) findComponentById(ResourceTable.Id_notetitle);
        next = (Button) findComponentById(ResourceTable.Id_next);

        name.setText(intent.getStringParam("title"));
        next.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                intent.setParam("title", name.getText());
                present(new ModifyNoteContentAbilitySlice(), intent);
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
