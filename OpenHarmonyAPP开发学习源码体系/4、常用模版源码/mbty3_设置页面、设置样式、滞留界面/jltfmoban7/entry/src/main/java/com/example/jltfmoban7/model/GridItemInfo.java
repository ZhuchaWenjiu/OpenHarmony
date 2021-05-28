package com.example.jltfmoban7.model;

/**
 * Grid item model
 */
public class GridItemInfo {
    private final String itemText;
    private final int iconId;
    private final String tag;

    /**
     * Item data model Constructor
     *
     * @param itemText item text
     * @param iconId   image resource ID
     * @param tag      component tag
     */
    public GridItemInfo(String itemText, int iconId, String tag) {
        this.itemText = itemText;
        this.iconId = iconId;
        this.tag = tag;
    }

    public String getItemText() {
        return itemText;
    }

    public int getIconId() {
        return iconId;
    }

    public String getTag() {
        return tag;
    }
}
