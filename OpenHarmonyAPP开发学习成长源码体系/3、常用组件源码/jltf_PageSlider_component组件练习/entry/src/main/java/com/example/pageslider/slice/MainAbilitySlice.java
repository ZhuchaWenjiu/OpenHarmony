package com.example.pageslider.slice;

import com.example.pageslider.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.PageSlider;

import java.util.ArrayList;

public class MainAbilitySlice extends AbilitySlice {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);

        initPageSlider();
    }
    private void initPageSlider() {
        PageSlider pageSlider = (PageSlider) findComponentById(ResourceTable.Id_page_slider);
        pageSlider.setProvider(new TestPagerProvider(getData()));
    }
    private ArrayList<TestPagerProvider.DataItem> getData() {
        ArrayList<TestPagerProvider.DataItem> dataItems = new ArrayList<>();
        dataItems.add(new TestPagerProvider.DataItem("第一个页面"));
        dataItems.add(new TestPagerProvider.DataItem("第二个页面"));
        dataItems.add(new TestPagerProvider.DataItem("第三个页面"));
        dataItems.add(new TestPagerProvider.DataItem("第四个页面"));

        return dataItems;
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
