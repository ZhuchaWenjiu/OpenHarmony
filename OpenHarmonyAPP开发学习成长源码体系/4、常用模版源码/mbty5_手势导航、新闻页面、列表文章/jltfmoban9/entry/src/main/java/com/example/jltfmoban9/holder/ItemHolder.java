package com.example.jltfmoban9.holder;

import com.example.jltfmoban9.model.Item;

/**
 * item provider interface
 */
public interface ItemHolder {
    /**
     * process Item with an item
     *
     * @param item data binding
     */
    void processItem(Item item);

    /**
     * initItemListener
     */
    void initItemListener();
}
