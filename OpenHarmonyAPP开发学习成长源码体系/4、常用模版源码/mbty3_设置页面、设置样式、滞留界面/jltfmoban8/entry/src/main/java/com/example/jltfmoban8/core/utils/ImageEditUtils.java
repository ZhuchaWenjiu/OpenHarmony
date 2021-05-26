package com.example.jltfmoban8.core.utils;

import ohos.agp.components.AttrHelper;
import ohos.agp.utils.Rect;

import ohos.app.Context;
import ohos.media.image.PixelMap;

/**
 * Utils Class
 */
public class ImageEditUtils {
    /**
     * Zoom the image in the middle proportion.
     *
     * @param srcPixelMap Source PixelMap
     * @param boundRect   ViewBound
     * @param contentRect PixelMap Bound
     * @return ScaleFactor
     */
    public static float fitSrcImageToBounds(PixelMap srcPixelMap, Rect boundRect, Rect contentRect) {
        int imgWidth = srcPixelMap.getImageInfo().size.width;
        int imgHeight = srcPixelMap.getImageInfo().size.height;
        int boundWidth = boundRect.getWidth();
        int boundHeight = boundRect.getHeight();

        contentRect.modify(boundRect);

        float imgRatio = (float) imgWidth / (float) imgHeight;
        float boundRatio = (float) boundWidth / (float) boundHeight;
        boolean scaleByWidth = imgRatio >= boundRatio;
        if (scaleByWidth) {
            return scaleContentRectByWidth(contentRect, imgWidth, imgHeight, boundWidth);
        } else {
            return scaleContentRectByHeight(contentRect, imgWidth, imgHeight, boundHeight);
        }
    }

    private static float scaleContentRectByHeight(Rect contentRect, int imgWidth, int imgHeight, int boundHeight) {
        int contentWidth = boundHeight * imgWidth / imgHeight;
        updateContentRect(contentRect, contentWidth, boundHeight);
        return (float) boundHeight / (float) imgHeight;
    }

    private static float scaleContentRectByWidth(Rect contentRect, int imgWidth, int imgHeight, int boundWidth) {
        int contentHeight = boundWidth * imgHeight / imgWidth;
        updateContentRect(contentRect, boundWidth, contentHeight);
        return (float) boundWidth / (float) imgWidth;
    }

    private static void updateContentRect(Rect contentRect, int width, int height) {
        int centerX = contentRect.getCenterX();
        int centerY = contentRect.getCenterY();
        int halfW = width >> 1;
        int halfH = height >> 1;
        contentRect.set(centerX - halfW, centerY - halfH, centerX + halfW, centerY + halfH);
    }

    /**
     * Returns the input value x clamped to the range [min, max].
     *
     * @param value Value
     * @param min   min
     * @param max   max
     * @return clamped value
     */
    public static float clamp(float value, float min, float max) {
        if (value > max) {
            return max;
        }
        if (value < min) {
            return min;
        }
        return value;
    }

    /**
     * VP to Pixel Int
     *
     * @param ctx Context
     * @param vp  Vp Value
     * @return Pixel Value
     */
    public static int vpToPixel(Context ctx, int vp) {
        return Math.round(AttrHelper.getDensity(ctx) * vp);
    }

    /**
     * VP to Pixel Float
     *
     * @param ctx Context
     * @param vp  Vp Value
     * @return Pixel Value
     */
    public static float vpToPixel(Context ctx, float vp) {
        return AttrHelper.getDensity(ctx) * vp;
    }
}
