package com.example.jltfmoban2.datamodel;

import com.example.jltfmoban2.typefactory.TypeFactory;
import ohos.agp.components.element.Element;

/**
 * SingleButtonDoubleLineList Item model
 */
public class SingleButtonDoubleLineListItemInfo implements ItemInfo {
    private String firstLineText;
    private String secondLineText;
    private Element image;

    /**
     * ListContent Constructor
     *
     * @param firstLineText  First line text
     * @param secondLineText second line text
     * @param image          Image resource ID
     */
    public SingleButtonDoubleLineListItemInfo(String firstLineText, String secondLineText, Element image) {
        this.firstLineText = firstLineText;
        this.secondLineText = secondLineText;
        this.image = image;
    }

    public String getFirstLineText() {
        return firstLineText;
    }

    public String getSecondLineText() {
        return secondLineText;
    }

    public Element getImage() {
        return image;
    }

    @Override
    public int getType(TypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
