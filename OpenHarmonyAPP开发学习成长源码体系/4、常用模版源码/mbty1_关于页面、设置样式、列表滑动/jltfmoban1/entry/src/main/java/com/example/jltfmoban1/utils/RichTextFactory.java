package com.example.jltfmoban1.utils;

import ohos.agp.colors.RgbColor;
import ohos.agp.components.AttrHelper;
import ohos.agp.text.RichText;
import ohos.agp.text.RichTextBuilder;
import ohos.agp.text.TextForm;
import ohos.app.Context;

/**
 * RichTextFactory
 */
public class RichTextFactory {
    private static final int COLOR_R_CLICKABLE_TEXT = 0;
    private static final int COLOR_G_CLICKABLE_TEXT = 125;
    private static final int COLOR_B_CLICKABLE_TEXT = 255;
    private static final int FP_TEXT_SIZE = 12;
    private RichTextBuilder richTextBuilder;
    private TextForm textFormClickable;
    private TextForm textFormNormal;

    /**
     * RichTextFactory Constructor
     *
     * @param context context
     */
    public RichTextFactory(Context context) {
        this.richTextBuilder = new RichTextBuilder();
        this.textFormClickable = new TextForm();
        this.textFormNormal = new TextForm();
        RgbColor color = new RgbColor(COLOR_R_CLICKABLE_TEXT, COLOR_G_CLICKABLE_TEXT, COLOR_B_CLICKABLE_TEXT);
        textFormClickable.setTextColor(color.asArgbInt());
        textFormClickable.setTextSize(AttrHelper.fp2px(FP_TEXT_SIZE, context));
        textFormNormal.setTextSize(AttrHelper.fp2px(FP_TEXT_SIZE, context));
    }

    /**
     * Add default clickable style text to RichTextBuilder
     *
     * @param text text string
     */
    public void addClickableText(String text) {
        richTextBuilder.mergeForm(textFormClickable);
        richTextBuilder.addText(text);
        richTextBuilder.revertForm();
    }

    /**
     * Add normal style text to RichTextBuilder
     *
     * @param text text string
     */
    public void addNormalText(String text) {
        richTextBuilder.mergeForm(textFormNormal);
        richTextBuilder.addText(text);
        richTextBuilder.revertForm();
    }

    /**
     * Reset RichTextBuilder
     */
    public void clean() {
        richTextBuilder = new RichTextBuilder();
    }

    /**
     * Return a rich text object based on the latest configurations
     *
     * @return RichText object
     */
    public RichText getRichText() {
        return richTextBuilder.build();
    }

}
