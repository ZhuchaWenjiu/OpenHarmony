package com.example.jltfmoban1.utils;

import com.example.jltfmoban1.ResourceTable;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.Text;
import ohos.agp.components.element.Element;
import ohos.agp.components.element.ElementScatter;
import ohos.app.Context;
import ohos.multimodalinput.event.TouchEvent;

/**
 * DoubleLineListItemFactory
 */
public class DoubleLineListItemFactory {
    private Context context;

    /**
     * Constructor
     *
     * @param context Context
     */
    public DoubleLineListItemFactory(Context context) {
        this.context = context;
    }

    /**
     * Return a double-line list item based on input texts
     *
     * @param textPrimary   primary text
     * @param textSecondary secondary text
     * @return double-line list item
     */
    public DirectionalLayout getDoubleLineList(String textPrimary, String textSecondary) {
        DirectionalLayout doubleLineList = (DirectionalLayout) LayoutScatter.getInstance(context)
                .parse(ResourceTable.Layout_default_doubleline_list, null, false);
        doubleLineList.setTouchEventListener((component, touchEvent) -> {
            Element touchedBackGround = ElementScatter.getInstance(context)
                    .parse(ResourceTable.Graphic_item_touched_background);
            Element normalBackGround = ElementScatter.getInstance(context)
                    .parse(ResourceTable.Graphic_item_normal_background);
            switch (touchEvent.getAction()) {
                case TouchEvent.PRIMARY_POINT_DOWN:
                    doubleLineList.setBackground(touchedBackGround);
                    break;
                case TouchEvent.CANCEL:
                case TouchEvent.PRIMARY_POINT_UP:
                    doubleLineList.setBackground(normalBackGround);
                    break;
                default:
                    break;
            }
            return true;
        });
        Text primaryText = (Text) doubleLineList.findComponentById(ResourceTable.Id_doubleLineList_text_primary);
        Text secondaryText = (Text) doubleLineList.findComponentById(ResourceTable.Id_doubleLineList_text_secondary);
        primaryText.setText(textPrimary);
        secondaryText.setText(textSecondary);
        return doubleLineList;
    }
}
