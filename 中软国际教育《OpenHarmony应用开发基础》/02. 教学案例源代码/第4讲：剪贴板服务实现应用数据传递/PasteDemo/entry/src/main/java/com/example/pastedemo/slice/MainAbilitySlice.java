package com.example.pastedemo.slice;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;

import ohos.aafwk.content.Operation;
import ohos.agp.components.Component;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.DirectionalLayout.LayoutConfig;
import ohos.agp.components.Text;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.agp.utils.TextAlignment;
import ohos.miscservices.pasteboard.PasteData;
import ohos.miscservices.pasteboard.SystemPasteboard;

public class MainAbilitySlice extends AbilitySlice {

    private DirectionalLayout myLayout = new DirectionalLayout(this);

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        LayoutConfig config = new LayoutConfig(LayoutConfig.MATCH_PARENT, LayoutConfig.MATCH_PARENT);
        myLayout.setLayoutConfig(config);
        ShapeElement element = new ShapeElement();
        element.setRgbColor(new RgbColor(255, 255, 255));
        myLayout.setBackground(element);

        Text text = new Text(this);
        text.setLayoutConfig(config);
        text.setText("复制内容，跳转到新的Ability");
        text.setTextColor(new Color(0xFF000000));
        text.setTextSize(50);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                // 获取系统剪贴板对象
                SystemPasteboard pasteboard = SystemPasteboard.getSystemPasteboard(MainAbilitySlice.this);
                if (pasteboard != null) {
                    pasteboard.setPasteData(PasteData.creatPlainTextData(text.getText()));
                }
//                PasteData pasteData = pasteboard.getPasteData();

//                present(new MainAbilitySlice2(), new Intent());

                Operation operation = new Intent.OperationBuilder()
                        .withBundleName("com.example.pastedemo")
                        .withAbilityName(".SecondAbility")
                        .build();
                Intent secondIntent = new Intent();
                secondIntent.setOperation(operation);
                startAbility(secondIntent); // 通过AbilitySlice的startAbility接口实现启动另一个页面
            }
        });
        myLayout.addComponent(text);
        super.setUIContent(myLayout);
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
