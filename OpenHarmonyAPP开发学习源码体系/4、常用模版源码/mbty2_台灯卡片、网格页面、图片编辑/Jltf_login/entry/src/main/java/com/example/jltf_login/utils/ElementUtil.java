package com.example.jltf_login.utils;

import ohos.agp.colors.RgbColor;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.app.Context;
import ohos.global.resource.NotExistException;
import ohos.global.resource.WrongTypeException;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The ElementUtil
 */
public class ElementUtil {
    /**
     * The getShapeElement6
     *
     * @param bgColor bgColor
     * @return shapeElement
     */
    public static ShapeElement getShapeElement6(int bgColor) {
        int bgColorTmp = bgColor;
        if (bgColor < 0 && bgColor >= -0xffffff) {
            bgColorTmp = bgColor + 0xffffff + 1;
        }
        if (bgColor > 0 && bgColor <= 0xffffff) {
            bgColorTmp = alphaColor(bgColor, 1.0f);
        }
        ShapeElement shapeElement = new ShapeElement();
        shapeElement.setRgbColor(new RgbColor(bgColorTmp));
        return shapeElement;
    }

    /**
     * The getShapeElementWith8
     *
     * @param bgColor bgColor
     * @return shapeElement
     */
    public static ShapeElement getShapeElementWith8(int bgColor) {
        ShapeElement shapeElement = new ShapeElement();
        shapeElement.setRgbColor(new RgbColor(bgColor));
        return shapeElement;
    }

    /**
     * The getColor
     *
     * @param context    context
     * @param resColorId resColorId
     * @return color
     */
    public static int getColor(Context context, int resColorId) {
        try {
            String strColor = context.getResourceManager().getElement(resColorId).getString();
            if (strColor.length() == 7) {
                return context.getResourceManager().getElement(resColorId).getColor();
            } else if (strColor.length() == 9) {
                return Color.getIntColor(strColor);
            } else {
                return 0x000000;
            }
        } catch (WrongTypeException | NotExistException | IOException e) {
            Logger.getLogger(ElementUtil.class.getName()).log(Level.SEVERE, e.getMessage());
        }
        return 0x000000;
    }

    /**
     * The alphaColor
     *
     * @param rgbColor rgbColor
     * @param alpha    alpha
     * @return rgba
     */
    public static int alphaColor(int rgbColor, float alpha) {
        int bgColorTmp = rgbColor;
        if (rgbColor < 0) {
            bgColorTmp = rgbColor + 0xffffff + 1;
        }
        int color = Math.max(0, bgColorTmp) << 8;
        int alpha255 = (int) (alpha * 255);
        return color + alpha255;
    }

    /**
     * The createDrawable
     *
     * @param color  color
     * @param radius radius
     * @return drawable
     */
    public static ShapeElement createDrawable(int color, float radius) {
        ShapeElement drawable = new ShapeElement();
        drawable.setShape(ShapeElement.RECTANGLE);
        drawable.setRgbColor(new RgbColor(color));
        drawable.setCornerRadius(radius);
        return drawable;
    }

    /**
     * The createDrawable
     *
     * @param color    color
     * @param tlRadius tlRadius
     * @param trRadius trRadius
     * @param brRadius brRadius
     * @param blRadius blRadius
     * @return drawable
     */
    public static ShapeElement createDrawable(
            int color, float tlRadius, float trRadius, float brRadius, float blRadius) {
        ShapeElement drawable = new ShapeElement();
        drawable.setShape(ShapeElement.RECTANGLE);
        drawable.setRgbColor(new RgbColor(color));
        drawable.setCornerRadiiArray(
                new float[]{
                        tlRadius, tlRadius,
                        trRadius, trRadius,
                        brRadius, brRadius,
                        blRadius, blRadius
                });
        return drawable;
    }
}
