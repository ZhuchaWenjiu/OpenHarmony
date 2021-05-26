package com.example.jltfmoban9.holder;

import com.example.jltfmoban9.model.Item;

/**
 * BaseItemHolder
 */
public abstract class BaseItemHolder implements ItemHolder {
    /**
     * processView
     *
     * @param item data binding
     */
    public abstract void processItem(Item item);

    /**
     * initViewListener
     */
    public abstract void initItemListener();
}
