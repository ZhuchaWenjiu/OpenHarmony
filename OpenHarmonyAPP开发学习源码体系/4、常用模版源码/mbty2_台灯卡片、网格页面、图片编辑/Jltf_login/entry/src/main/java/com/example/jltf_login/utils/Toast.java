package com.example.jltf_login.utils;

import ohos.agp.colors.RgbColor;
import ohos.agp.components.AttrHelper;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.Text;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.TextAlignment;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.Context;

/**
 * The Toast
 */
public class Toast {
    /**
     * 1000ms
     */
    public static final int TOAST_SHORT = 1000;

    /**
     * 2000ms
     */
    public static final int TOAST_LONG = 2000;

    // Toast offset
    private static final int TOAST_OFFSETX = 0;
    private static final int TOAST_OFFSETY = 180;

    // Shape arg
    private static final int SHAPE_CORNER_RADIO = 18;
    private static final int SHAPE_RGB_COLOR_RED = 188;
    private static final int SHAPE_RGB_COLOR_GREEN = 188;
    private static final int SHAPE_RGB_COLOR_BLUE = 188;

    // Text arg
    private static final int TEXT_PADDING_LEFT = 8;
    private static final int TEXT_PADDING_TOP = 4;
    private static final int TEXT_PADDING_RIGHT = 8;
    private static final int TEXT_PADDING_BOTTOM = 4;
    private static final int TEXT_SIZE = 16;

    /**
     * Get ItemList
     *
     * @param context  context
     * @param text     text
     * @param duration duration
     * @return toastDialog
     */
    public static ToastDialog makeToast(Context context, String text, int duration) {
        Text toastText = new Text(context);

        ShapeElement shapeElement = new ShapeElement();
        shapeElement.setShape(ShapeElement.RECTANGLE);
        shapeElement.setCornerRadius(AttrHelper.vp2px(SHAPE_CORNER_RADIO, context));
        shapeElement.setRgbColor(new RgbColor(SHAPE_RGB_COLOR_RED, SHAPE_RGB_COLOR_GREEN, SHAPE_RGB_COLOR_BLUE));

        toastText.setComponentSize(
                DirectionalLayout.LayoutConfig.MATCH_CONTENT, DirectionalLayout.LayoutConfig.MATCH_CONTENT);
        toastText.setPadding(
                AttrHelper.vp2px(TEXT_PADDING_LEFT, context),
                AttrHelper.vp2px(TEXT_PADDING_TOP, context),
                AttrHelper.vp2px(TEXT_PADDING_RIGHT, context),
                AttrHelper.vp2px(TEXT_PADDING_BOTTOM, context));
        toastText.setTextAlignment(TextAlignment.CENTER);
        toastText.setTextSize(AttrHelper.vp2px(TEXT_SIZE, context));
        toastText.setBackground(shapeElement);
        toastText.setText(text);

        ToastDialog toastDialog = new ToastDialog(context);
        toastDialog
                .setContentCustomComponent(toastText)
                .setDuration(duration)
                .setTransparent(true)
                .setOffset(TOAST_OFFSETX, TOAST_OFFSETY)
                .setSize(DirectionalLayout.LayoutConfig.MATCH_CONTENT, DirectionalLayout.LayoutConfig.MATCH_CONTENT);

        return toastDialog;
    }
}
