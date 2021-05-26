package com.example.jltfmoban2.slice;

import com.example.jltfmoban2.ResourceTable;
import com.example.jltfmoban2.datamodel.DefaultDoubleLineListItemInfo;
import com.example.jltfmoban2.datamodel.ItemInfo;
import com.example.jltfmoban2.datamodel.SingleButtonDoubleLineListItemInfo;
import com.example.jltfmoban2.itemprovider.ListItemProvider;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.AttrHelper;
import ohos.agp.components.Image;
import ohos.agp.components.ListContainer;
import ohos.agp.components.ScrollView;
import ohos.agp.components.Text;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.Component;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.element.Element;
import ohos.agp.components.element.ElementScatter;
import ohos.agp.components.element.ShapeElement;

import java.util.ArrayList;
import java.util.List;

/**
 * MainAbilitySlice
 */
public class MainAbilitySlice extends AbilitySlice {
    private static final int BUTTON_NUM = 4;
    private static final int LIST_LEN = 4;
    private static final int LIST_ITEM_HEIGHT = 65;
    private static final int OVERSCROLL_PERCENT = 20;
    private static final float OVERSCROLL_RATE = 1.0f;
    private static final int REMAIN_VISIBLE_PERCENT = 10;
    private static final int COLOR_CHANGE_RANGE = 108;
    private static final int ORIGINAL_BACKGROUND_COLOR = 220;
    private static final int BACKGROUND_COLOR = 255;
    private static final int LIST_ITEM_TYPE = 2;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        initLists();
        initAppBarButtons();
        initScrollView();
        initBottomTab();
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    private void initLists() {
        List<ItemInfo> contactsDetails = new ArrayList<>();
        Element aboutIcon = ElementScatter.getInstance(getContext()).parse(ResourceTable.Graphic_ic_about);
        Element switchIcon = ElementScatter.getInstance(getContext()).parse(ResourceTable.Graphic_ic_enabled);
        // List item data
        for (int index = 0; index < LIST_LEN; index++) {
            contactsDetails.add(new DefaultDoubleLineListItemInfo("蛟龙腾飞",
                    "Mobile - ShangHai", aboutIcon, aboutIcon));
            contactsDetails.add(new SingleButtonDoubleLineListItemInfo("8:50 AM",
                    "蛟龙腾飞", switchIcon));
        }
        // Set providers to ListContainers
        ListContainer contactsList = (ListContainer) findComponentById(ResourceTable.Id_contacts_detail_upperCard_list);
        if (contactsList != null) {
            contactsList.setItemProvider(new ListItemProvider(contactsDetails, this));
            contactsList.setHeight(AttrHelper.vp2px(LIST_ITEM_HEIGHT, this) * LIST_LEN * LIST_ITEM_TYPE);
        }
    }

    private void initAppBarButtons() {
        Image backButton = (Image) findComponentById(ResourceTable.Id_appBar_backButton);
        if (backButton != null) {
            backButton.setClickedListener(component -> MainAbilitySlice.super.onBackPressed());
        }
    }

    private void initScrollView() {
        ScrollView scrollView = (ScrollView) findComponentById(ResourceTable.Id_contacts_detail_scroll);
        int profileSizePx = AttrHelper.vp2px(COLOR_CHANGE_RANGE, this);
        if (scrollView != null) {
            scrollView.setReboundEffectParams(OVERSCROLL_PERCENT, OVERSCROLL_RATE, REMAIN_VISIBLE_PERCENT);
            scrollView.setReboundEffect(true);
            Text userName = (Text) findComponentById(ResourceTable.Id_appBar_userName);
            DirectionalLayout backGround = (DirectionalLayout) findComponentById(ResourceTable.Id_background);
            ShapeElement shapeElement = new ShapeElement();
            shapeElement.setShape(ShapeElement.RECTANGLE);

            // Set Scrolled listener
            scrollView.getComponentTreeObserver().addScrolledListener(() -> {
                float curY = scrollView.getScrollValue(Component.AXIS_Y);
                int colorChange = (int) ((BACKGROUND_COLOR - ORIGINAL_BACKGROUND_COLOR) * curY / profileSizePx);
                shapeElement.setRgbColor(new RgbColor(ORIGINAL_BACKGROUND_COLOR + colorChange,
                        ORIGINAL_BACKGROUND_COLOR + colorChange, ORIGINAL_BACKGROUND_COLOR + colorChange));
                backGround.setBackground(shapeElement);

                userName.setAlpha(1 * curY / profileSizePx);
            });
        }
    }

    private void initBottomTab() {
        DirectionalLayout bottomTab = (DirectionalLayout) findComponentById(ResourceTable.Id_bottom_tabMenu);
        List<DirectionalLayout> tabList = new ArrayList<>();

        // Cast xml resources to elements
        Element tabActived = ElementScatter.getInstance(getContext()).parse(ResourceTable.Graphic_ic_actived);
        Element tabNormal = ElementScatter.getInstance(getContext()).parse(ResourceTable.Graphic_ic_normal);

        for (int count = 0; count < BUTTON_NUM; count++) {
            // Use LayoutScatter to convert xml file into Component instance
            DirectionalLayout tab = (DirectionalLayout) LayoutScatter.getInstance(getContext())
                    .parse(ResourceTable.Layout_tab, bottomTab, false);
            Image buttonImage = (Image) tab.findComponentById(ResourceTable.Id_bottom_tab_button_image);
            if (buttonImage != null) {
                if (count == BUTTON_NUM - 1) {
                    buttonImage.setImageElement(tabActived);
                } else {
                    buttonImage.setImageElement(tabNormal);
                }
            }
            Text buttonText = (Text) tab.findComponentById(ResourceTable.Id_bottom_tab_button_text);
            // Set Tab Text Here
            if (buttonText != null) {
                buttonText.setText("Tab");
            }
            tab.setClickedListener(component -> {
                // Deselect all tabs in tab menu
                for (DirectionalLayout btn : tabList) {
                    ((Image) btn.findComponentById(ResourceTable.Id_bottom_tab_button_image))
                            .setImageElement(tabNormal);
                }

                // Set Selected state on the clicked tab
                ((Image) component.findComponentById(ResourceTable.Id_bottom_tab_button_image))
                        .setImageElement(tabActived);
            });
            tabList.add(tab);
            bottomTab.addComponent(tab);
        }
    }
}
