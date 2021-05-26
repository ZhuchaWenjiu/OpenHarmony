package com.example.jltfmoban2.typefactory;

import com.example.jltfmoban2.ResourceTable;
import com.example.jltfmoban2.datamodel.DefaultDoubleLineListItemInfo;
import com.example.jltfmoban2.datamodel.SingleButtonDoubleLineListItemInfo;
import com.example.jltfmoban2.viewholder.DefaultDoubleLineList;
import com.example.jltfmoban2.viewholder.SingleButtonDoubleLineList;
import com.example.jltfmoban2.viewholder.ViewHolder;

import ohos.agp.components.Component;

/**
 * List Type Factory
 * Implement type method to support different list item class
 */
public class ListTypeFactory implements TypeFactory {
    private static final int TYPE_DEFAULT_DOUBLE_LINE_LIST = ResourceTable.Layout_default_doublelinelist;
    private static final int TYPE_SINGLE_BUTTON_DOUBLE_LINE_LIST = ResourceTable.Layout_singlebutton_doublelinelist;

    @Override
    public int type(DefaultDoubleLineListItemInfo itemInfo) {
        return TYPE_DEFAULT_DOUBLE_LINE_LIST;
    }

    @Override
    public int type(SingleButtonDoubleLineListItemInfo itemInfo) {
        return TYPE_SINGLE_BUTTON_DOUBLE_LINE_LIST;
    }

    @Override
    public ViewHolder getViewHolder(int type, Component itemComponent) {
        switch (type) {
            // Add cases here if other view holder added
            case TYPE_DEFAULT_DOUBLE_LINE_LIST:
                return new DefaultDoubleLineList(itemComponent);
            case TYPE_SINGLE_BUTTON_DOUBLE_LINE_LIST:
                return new SingleButtonDoubleLineList(itemComponent);
            default:
                break;
        }
        return new DefaultDoubleLineList(itemComponent);
    }
}
