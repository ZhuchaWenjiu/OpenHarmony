package com.example.rdbdemo.slice;

import com.example.rdbdemo.ResourceTable;
import com.example.rdbdemo.utils.DataUtil;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.Text;


public class NoteInfoAbilitySlice extends AbilitySlice {

    Text title, date, content;

    Button back, deletenote,btnModify;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);

        String titles = intent.getStringParam("title");
        String contents = intent.getStringParam("content");
        String datas = intent.getStringParam("data");
        int id = intent.getIntParam("id", -1);
        super.setUIContent(ResourceTable.Layout_note_info);
        title = (Text) findComponentById(ResourceTable.Id_title);
        date = (Text) findComponentById(ResourceTable.Id_date);
        content = (Text) findComponentById(ResourceTable.Id_content);


        back = (Button) findComponentById(ResourceTable.Id_back);
        btnModify = (Button) findComponentById(ResourceTable.Id_btn_modify);
        back.setClickedListener(listener -> getAbility().terminateAbility());

        deletenote = (Button) findComponentById(ResourceTable.Id_deletenote);
        deletenote.setClickedListener(listener -> {

            if(id>-1) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DataUtil.deleteNote(id);
                        getAbility().terminateAbility();
                    }
                }).start();
            }
        });

        btnModify.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                Operation operation = new Intent.OperationBuilder()
                        .withBundleName("com.example.rdbdemo").withAbilityName("com.example.rdbdemo.ModifyNoteAbility").build();
                intent.setOperation(operation);
                startAbility(intent);
                terminateAbility();
            }
        });
        title.setText(titles);
        date.setText(datas);
        content.setText(contents);
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
