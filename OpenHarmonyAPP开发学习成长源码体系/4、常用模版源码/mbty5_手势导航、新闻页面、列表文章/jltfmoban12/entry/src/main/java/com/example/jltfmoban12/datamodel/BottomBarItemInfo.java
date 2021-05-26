package com.example.jltfmoban12.datamodel;

/**
 * Bottom bar item data setting.
 */
public class BottomBarItemInfo {
    // Text of bottom bar item.
    private String barText;

    // Original image of bottom bar item.
    private int barOriImageId;

    // Activated image of bottom bar item.
    private int barActivatedImageId;

    /**
     * Constructor for this class.
     *
     * @param barText             text of bottom bar item.
     * @param barOriImageId       original image of bottom bar item.
     * @param barActivatedImageId activated image of bottom bar item.
     */
    BottomBarItemInfo(String barText, int barOriImageId, int barActivatedImageId) {
        this.barText = barText;
        this.barOriImageId = barOriImageId;
        this.barActivatedImageId = barActivatedImageId;
    }

    public String getBarText() {
        return barText;
    }

    public int getBarOriImageId() {
        return barOriImageId;
    }

    public int getBarActivatedImageId() {
        return barActivatedImageId;
    }
}
