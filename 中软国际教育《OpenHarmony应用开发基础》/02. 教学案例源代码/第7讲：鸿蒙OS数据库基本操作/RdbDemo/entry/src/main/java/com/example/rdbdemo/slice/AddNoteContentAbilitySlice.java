package com.example.rdbdemo.slice;

import com.example.rdbdemo.ResourceTable;
import com.example.rdbdemo.db.Note;
import com.example.rdbdemo.utils.DataUtil;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.TextField;

public class AddNoteContentAbilitySlice extends AbilitySlice {

    TextField content;
    Button next;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);

        super.setUIContent(ResourceTable.Layout_add_note_content);
        content = (TextField) findComponentById(ResourceTable.Id_addnotecontent);
        next = (Button) findComponentById(ResourceTable.Id_next);
        String title = intent.getStringParam("title");

        next.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                String content2 = content.getText();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Note note = new Note(title, content2);
                        DataUtil.addNote(note);
                        getAbility().terminateAbility();
                    }
                }).start();
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
