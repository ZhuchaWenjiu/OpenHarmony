package com.example.rdbdemo.data;

import com.example.rdbdemo.ResourceTable;
import com.example.rdbdemo.db.Note;
import ohos.aafwk.ability.AbilitySlice;

import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.RecycleItemProvider;
import ohos.agp.components.Text;

import java.util.List;

public class RecycleSimpleItemProvider extends RecycleItemProvider {
    private List<Note> mListInfo;
    private AbilitySlice mSlice;
    private LayoutScatter mLayoutScatter;

    public RecycleSimpleItemProvider(List<Note> mListInfo, AbilitySlice slice) {
        this.mListInfo = mListInfo;
        this.mSlice = slice;
        this.mLayoutScatter = LayoutScatter.getInstance(mSlice);
    }
    @Override
    public int getCount() {
        return mListInfo.size();
    }

    public Note getItem(int position) {
        return mListInfo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Component getComponent(int position, Component component, ComponentContainer componentContainer) {
        Note info = (Note) getItem(position);

        if (component != null) {
            return component;
        }

        Component newComponent = mLayoutScatter.parse(ResourceTable.Layout_actionbar_item, null, false);
        Text leftText = (Text) newComponent.findComponentById(ResourceTable.Id_list_text_left);
        Text rightText = (Text) newComponent.findComponentById(ResourceTable.Id_list_text_right);
        leftText.setText(info.getTitle());
        rightText.setText(info.getDateStr());
        return newComponent;
    }

    public void setmListInfo(List<Note> mListInfo) {
        this.mListInfo = mListInfo;

    }
}
