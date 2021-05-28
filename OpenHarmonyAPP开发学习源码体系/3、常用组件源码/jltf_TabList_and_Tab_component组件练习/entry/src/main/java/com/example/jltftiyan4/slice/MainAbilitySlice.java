package com.example.jltftiyan4.slice;

import com.example.jltftiyan4.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.*;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.TextAlignment;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

import static ohos.agp.components.ComponentContainer.LayoutConfig.MATCH_CONTENT;
import static ohos.agp.components.ComponentContainer.LayoutConfig.MATCH_PARENT;

public class MainAbilitySlice extends AbilitySlice {

    static final HiLogLabel label = new HiLogLabel(HiLog.LOG_APP, 0x00201, "MY_TAG");

    private TableLayout imageGrid = null;

    private TableLayout videoGrid = null;

    private DirectionalLayout audioList = null;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        initImageGrid();
        initVideoGrid();
        //列表项
        TabList tabList = (TabList) findComponentById(ResourceTable.Id_jltftab_list);
        //内容项
        ScrollView scrollView = (ScrollView) findComponentById(ResourceTable.Id_tab_jltfcontent);

        TabList.Tab imagejltfTab = tabList.new Tab(getContext());
        imagejltfTab.setText("jltfImage");
        tabList.addTab(imagejltfTab, true);
        scrollView.addComponent(imageGrid);

        TabList.Tab videojltfTab = tabList.new Tab(getContext());
        videojltfTab.setText("jltfVideo");
        tabList.addTab(videojltfTab);

        TabList.Tab audiojltfTab = tabList.new Tab(getContext());
        audiojltfTab.setText("jltfAudio");
        tabList.addTab(audiojltfTab);

        tabList.addTabSelectedListener(new TabList.TabSelectedListener() {
            @Override
            public void onSelected(TabList.Tab tab) {
                if (tab.getPosition() == 0) {
                    scrollView.addComponent(imageGrid);
                } else if (tab.getPosition() == 1) {
                    scrollView.addComponent(videoGrid);
                } else {
                    scrollView.addComponent(new DirectionalLayout(getContext()));
                }
                HiLog.info(label, "onSelected：" + tab.getPosition());
            }

            @Override
            public void onUnselected(TabList.Tab tab) {
                if (tab.getPosition() == 0) {
                    scrollView.removeComponent(imageGrid);
                } else if (tab.getPosition() == 1) {
                    scrollView.removeComponent(videoGrid);
                } else {
                    scrollView.removeComponent(new DirectionalLayout(getContext()));
                }
                HiLog.info(label, "onUnselected：" + tab.getText());
            }

            @Override
            public void onReselected(TabList.Tab tab) {
                HiLog.info(label, "onReselected：" + tab.getText());
            }
        });
    }

    private void initVideoGrid() {
        videoGrid = new TableLayout(this);
        videoGrid.setWidth(MATCH_PARENT);
        videoGrid.setHeight(MATCH_CONTENT);
        videoGrid.setColumnCount(1);
        videoGrid.setRowCount(2);
        for (int i = 1; i < 4; i++) {
            Text text = new Text(this);
            text.setWidth(MATCH_PARENT);
            text.setHeight(800);
            text.setTextAlignment(TextAlignment.CENTER);
            text.setText("第" + i + "块视频");
            ShapeElement shapeElement = new ShapeElement();
            shapeElement.setCornerRadius(20);
            shapeElement.setRgbColor(new RgbColor(192,0,255));
            text.setBackground(shapeElement);
            text.setPadding(10,10,10,10);
            text.setMarginsTopAndBottom(10,10);
            text.setMarginsLeftAndRight(20,20);
            text.setTextSize(50);
            videoGrid.addComponent(text);
        }
    }
    private void initImageGrid() {
        imageGrid = new TableLayout(this);
        imageGrid.setWidth(MATCH_PARENT);
        imageGrid.setHeight(MATCH_CONTENT);
        imageGrid.setColumnCount(2);
        imageGrid.setRowCount(4);
        for (int i = 1; i <= 6; i++) {
            Text text = new Text(this);
            text.setWidth(420);
            text.setHeight(420);
            text.setTextAlignment(TextAlignment.BOTTOM);
            text.setText("第" + i + "块图片");
            ShapeElement shapeElement = new ShapeElement();
            shapeElement.setCornerRadius(20);
            shapeElement.setRgbColor(new RgbColor(0,192,255));
            text.setBackground(shapeElement);
            text.setPadding(10,10,10,10);
            text.setMarginsTopAndBottom(10,10);
            text.setMarginsLeftAndRight(20,20);
            text.setTextSize(50);
            imageGrid.addComponent(text);
        }
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
