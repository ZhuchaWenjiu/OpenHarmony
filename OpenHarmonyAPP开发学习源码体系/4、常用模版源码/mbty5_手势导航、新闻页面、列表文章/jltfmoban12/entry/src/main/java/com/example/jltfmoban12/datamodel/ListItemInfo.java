package com.example.jltfmoban12.datamodel;

/**
 * List item data setting.
 */
public class ListItemInfo {
    // Main text of double line list item.
    private String textMain;

    // Auxiliary text of double line list item.
    private String textAux;

    // Right text of double line list item.
    private String textRight;

    // Left image id of double line list item.
    private int imageIdLeft;

    // Right image id of double line list item.
    private int imageIdRight;

    /**
     * Constructor for this class.
     *
     * @param textMain     main text of double line list item.
     * @param textAux      auxiliary text of double line list item.
     * @param textRight    right text of double line list item.
     * @param imageIdLeft  left image id of double line list item.
     * @param imageIdRight right image id of double line list item.
     */
    ListItemInfo(String textMain, String textAux, String textRight, int imageIdLeft, int imageIdRight) {
        this.textMain = textMain;
        this.textAux = textAux;
        this.textRight = textRight;
        this.imageIdLeft = imageIdLeft;
        this.imageIdRight = imageIdRight;
    }

    public String getTextMain() {
        return textMain;
    }

    public String getTextAux() {
        return textAux;
    }

    public String getTextRight() {
        return this.textRight;
    }

    public int getImageIdLeft() {
        return this.imageIdLeft;
    }

    public int getImageIdRight() {
        return this.imageIdRight;
    }
}
