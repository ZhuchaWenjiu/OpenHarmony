package com.example.pastedemo.slice;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.Component;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.DirectionalLayout.LayoutConfig;
import ohos.agp.components.Text;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.agp.utils.TextAlignment;
import ohos.miscservices.pasteboard.PasteData;
import ohos.miscservices.pasteboard.SystemPasteboard;

public class MainAbilitySlice2 extends AbilitySlice {

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
        text.setText("Hello 1111111111111");
        text.setTextColor(new Color(0xFF000000));
        text.setTextSize(50);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                SystemPasteboard pasteboard = SystemPasteboard.getSystemPasteboard(MainAbilitySlice2.this);
                PasteData pasteData = pasteboard.getPasteData();
                if (pasteData == null) {
                    return;
                }
                PasteData.DataProperty dataProperty = pasteData.getProperty();
                boolean hasText = dataProperty.hasMimeType(PasteData.MIMETYPE_TEXT_PLAIN);
                if (hasText) {
                    for (int i = 0; i < pasteData.getRecordCount(); i++) {
                        PasteData.Record record = pasteData.getRecordAt(i);
                        String mimeType = record.getMimeType();
                        if (mimeType.equals(PasteData.MIMETYPE_TEXT_PLAIN)) {
                            text.setText(record.getPlainText().toString());
                            break;
                        }
                    }
                }

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
