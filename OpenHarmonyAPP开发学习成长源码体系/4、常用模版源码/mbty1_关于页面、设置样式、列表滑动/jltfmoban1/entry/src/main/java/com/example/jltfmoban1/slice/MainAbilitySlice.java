package com.example.jltfmoban1.slice;

import com.example.jltfmoban1.ResourceTable;
import com.example.jltfmoban1.utils.DoubleLineListItemFactory;
import com.example.jltfmoban1.utils.RichTextFactory;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.ScrollView;
import ohos.agp.components.Text;
import ohos.agp.text.RichText;
import ohos.bundle.AbilityInfo;
import ohos.global.configuration.Configuration;

/**
 * MainAbilitySlice
 */
public class MainAbilitySlice extends AbilitySlice {
    private static final int OVER_SCROLL_PERCENT = 20;
    private static final float OVER_SCROLL_RATE = 1.0f;
    private static final int REMAIN_VISIBLE_PERCENT = 20;
    private static final int ITEM_NUM = 3;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        int orientation = getResourceManager().getConfiguration().direction;
        if (orientation == Configuration.DIRECTION_HORIZONTAL) {
            super.setUIContent(ResourceTable.Layout_ability_main_landscape);
        } else {
            super.setUIContent(ResourceTable.Layout_ability_main);
            initScrollView();
            initItems();
        }
        initRichText();
        initAppBar();
    }

    @Override
    protected void onOrientationChanged(AbilityInfo.DisplayOrientation displayOrientation) {
        if (displayOrientation == AbilityInfo.DisplayOrientation.LANDSCAPE) {
            setUIContent(ResourceTable.Layout_ability_main_landscape);
        } else if (displayOrientation == AbilityInfo.DisplayOrientation.PORTRAIT) {
            setUIContent(ResourceTable.Layout_ability_main);
            initScrollView();
            initItems();
        }
        initRichText();
        initAppBar();
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    private void initAppBar() {
        DirectionalLayout backButton = (DirectionalLayout)
                findComponentById(ResourceTable.Id_appBar_backButton_touchTarget);
        backButton.setClickedListener(component -> onBackPressed());
    }

    private void initRichText() {
        RichTextFactory richTextFactory = new RichTextFactory(getContext());
        richTextFactory.addClickableText("蛟龙腾飞软件许可及服务协议");
        RichText openSourceText = richTextFactory.getRichText();
        Text openSourceTextContainer = (Text) findComponentById(ResourceTable.Id_openSourceNoticeText);
        openSourceTextContainer.setRichText(openSourceText);

       /* richTextFactory.clean();
        richTextFactory.addClickableText("XXXXX XXXX XXXXXXXX");
        richTextFactory.addNormalText(" XXX ");
        richTextFactory.addClickableText("XXXXX XXXX XXXXX XXX XXXXXX");*/

        RichText protocolPrivacyText = richTextFactory.getRichText();
        Text protocolPrivacyTextContainer = (Text) findComponentById(ResourceTable.Id_protocolPrivacyText);
        protocolPrivacyTextContainer.setRichText(protocolPrivacyText);
    }

    private void initScrollView() {
        ScrollView scrollView = (ScrollView) findComponentById(ResourceTable.Id_aboutPageScrollView);
        scrollView.setReboundEffectParams(OVER_SCROLL_PERCENT, OVER_SCROLL_RATE, REMAIN_VISIBLE_PERCENT);
        scrollView.setReboundEffect(true);
    }

    private void initItems() {
        DoubleLineListItemFactory doubleLineListItemFactory = new DoubleLineListItemFactory(getContext());
        DirectionalLayout aboutPageList = (DirectionalLayout) findComponentById(ResourceTable.Id_aboutPageLowerPart);
        aboutPageList.removeAllComponents();
        // Add ITEM_NUM - 1 Components, manually hide the last component's divider
        for (int i = 0; i < ITEM_NUM - 1; i++) {
            aboutPageList.addComponent(doubleLineListItemFactory
                    .getDoubleLineList("帮助中心", " www.jltfcloud.com"));
        }
        DirectionalLayout lastItem = doubleLineListItemFactory
                .getDoubleLineList("蛟龙腾飞", " www.jltfcloud.com");
        lastItem.findComponentById(ResourceTable.Id_divider).setVisibility(Component.INVISIBLE);
        aboutPageList.addComponent(lastItem);
    }
}
