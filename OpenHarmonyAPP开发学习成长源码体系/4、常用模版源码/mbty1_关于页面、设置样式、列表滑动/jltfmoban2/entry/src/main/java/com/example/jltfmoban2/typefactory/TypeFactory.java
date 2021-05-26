package com.example.jltfmoban2.typefactory;

import com.example.jltfmoban2.datamodel.DefaultDoubleLineListItemInfo;
import com.example.jltfmoban2.datamodel.SingleButtonDoubleLineListItemInfo;
import com.example.jltfmoban2.viewholder.ViewHolder;

import ohos.agp.components.Component;

/**
 * Type Factory
 */
public interface TypeFactory {
    /**
     * Get resource ID of DefaultDoubleLineList
     *
     * @param itemInfo Item model
     * @return Corresponding resource ID
     */
    int type(DefaultDoubleLineListItemInfo itemInfo);

    int type(SingleButtonDoubleLineListItemInfo itemInfo);

    /**
     * Get view holder corresponding to itemComponent
     *
     * @param type          resource ID
     * @param itemComponent itemComponent
     * @return View holder
     */
    ViewHolder getViewHolder(int type, Component itemComponent);
}
