package com.example.rdbdemo.slice;

import com.example.rdbdemo.ResourceTable;
import com.example.rdbdemo.data.RecycleSimpleItemProvider;
import com.example.rdbdemo.db.Note;
import com.example.rdbdemo.utils.DataUtil;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.*;

import java.util.ArrayList;
import java.util.List;

public class MainAbilitySlice extends AbilitySlice {
    private ComponentContainer mRootLayout;
    private ListContainer mListContainer;
    RecycleSimpleItemProvider itemProvider;
    private List<Note> mListInfo = new ArrayList<>();
    Button btnAddnote;
    Button btnExite;

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        mRootLayout = initSliceLayout();
        super.setUIContent(mRootLayout);
    }

    @Override
    protected void onActive() {
        super.onActive();
        mListInfo = DataUtil.getAll();
        itemProvider.setmListInfo(mListInfo);
        itemProvider.notifyDataChanged();
        if (mListInfo == null || mListInfo.size() == 0) {
            mListContainer.setVisibility(Component.HIDE);
        } else {
            mListContainer.setVisibility(Component.VISIBLE);
        }
    }

    @Override
    protected void onInactive() {
        super.onInactive();
    }

    @Override
    protected void onForeground(Intent intent) {
        super.onForeground(intent);
        mListInfo.clear();
        itemProvider.setmListInfo(mListInfo);
        mListContainer.setItemProvider(itemProvider);
        itemProvider.notifyDataChanged();
    }

    @Override
    protected void onBackground() {
        super.onBackground();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private ComponentContainer initSliceLayout() {
        ComponentContainer rootLayout = (ComponentContainer) LayoutScatter.getInstance(getContext())
                .parse(ResourceTable.Layout_actionbar, null, false);

        mListContainer = (ListContainer) rootLayout.findComponentById(ResourceTable.Id_list_container);
        btnAddnote = (Button) rootLayout.findComponentById(ResourceTable.Id_btn_addnote);
        btnExite = (Button) rootLayout.findComponentById(ResourceTable.Id_btn_exit);

        itemProvider = new RecycleSimpleItemProvider(mListInfo, this);
        itemProvider.setCacheSize(20);
        mListContainer.setItemProvider(itemProvider);

        mListContainer.setItemClickedListener(new ListContainer.ItemClickedListener() {
            @Override
            public void onItemClicked(ListContainer listContainer, Component component, int i, long l) {
                Note note = itemProvider.getItem(i);
                Operation operation = new Intent.OperationBuilder().
                        withBundleName("com.example.rdbdemo").withAbilityName("com.example.rdbdemo.NoteInfoAbility").build();
                Intent intent1 = new Intent();
                intent1.setOperation(operation);
                intent1.setParam("title", note.getTitle());
                intent1.setParam("content", note.getContent());
                intent1.setParam("data", note.getDateStr());
                intent1.setParam("id", note.getId());
                startAbility(intent1);
            }
        });
        btnAddnote.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                Operation operation = new Intent.OperationBuilder().
                        withBundleName("com.example.rdbdemo").withAbilityName("com.example.rdbdemo.AddNoteAbility").build();
                Intent intent1 = new Intent();
                intent1.setOperation(operation);
                startAbility(intent1);
            }
        });
        btnExite.setClickedListener(listener -> terminateAbility());
        return rootLayout;
    }
}
