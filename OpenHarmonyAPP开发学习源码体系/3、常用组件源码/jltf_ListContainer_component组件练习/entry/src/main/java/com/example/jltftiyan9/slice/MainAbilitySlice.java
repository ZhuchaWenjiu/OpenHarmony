package com.example.jltftiyan9.slice;

import com.example.jltftiyan9.ResourceTable;
import com.example.jltftiyan9.SampleItem;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.ListContainer;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.ToastDialog;

import java.util.ArrayList;
import java.util.List;

public class MainAbilitySlice extends AbilitySlice {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_jltfpage_listcontainer);
        initListContainer();


    }
    private void initListContainer() {
        ListContainer listContainer = (ListContainer) findComponentById(ResourceTable.Id_list_container);
        List<SampleItem> list = getData();
        SampleItemProvider sampleItemProvider = new SampleItemProvider(list, this);
        listContainer.setItemProvider(sampleItemProvider);

        listContainer.setItemClickedListener((container, component, position, id) -> {
            SampleItem item = (SampleItem) listContainer.getItemProvider().getItem(position);
            new ToastDialog(this)
                    .setText("you clicked:" + item.getName())
                    // Toast显示在界面中间
                    .setAlignment(LayoutAlignment.CENTER)
                    .show();
        });
    }
    private ArrayList<SampleItem> getData() {
        ArrayList<SampleItem> list = new ArrayList<>();
        for (int i = 0; i <= 8; i++) {
            list.add(new SampleItem("Item" + i));
        }
        return list;


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
