package com.example.jltfmoban8.core.utils;

import ohos.agp.utils.Matrix;
import ohos.agp.utils.Rect;
import ohos.agp.utils.RectFloat;

import java.util.Optional;

/**
 * Crop Rect Compute
 */
public class RectCompute {
    private static final Rect DEFAULT_RECT = new Rect(0, 0, 0, 0);

    private static final Matrix DEFAULT_MATRIX = new Matrix();
    private static final int MATRIX_LENGTH = 9;
    private static final int MATRIX_X_SCALE = 0;
    private static final int MATRIX_Y_SCALE = 4;
    private static final int MATRIX_X_TRANSLATE = 2;
    private static final int MATRIX_Y_TRANSLATE = 5;
    private static final int ADJUST_HEIGHT = 132;
    private static final int EDITOR_HEAD_HIGHT = 243;
    private static final float HALF_DIVIDED_FLOAT = 2F;
    private static Rect displayRect = new Rect();
    private static RectFloat displayRectF = new RectFloat();
    private static Rect renderRect = new Rect();
    private static RectFloat renderRectF = new RectFloat();
    private static Rect tempRect = new Rect();
    private static RectFloat tempResultRectF = new RectFloat();
    private static RectFloat tempArg1RectF = new RectFloat();
    private static RectFloat tempArg2RectF = new RectFloat();
    private static Rect margins = new Rect();
    private static Rect paddings = new Rect();
    private static Matrix matrix = new Matrix();
    private static float[] sValues = new float[MATRIX_LENGTH];

    /**
     * Computer Rect source -> PixelMap target --> render
     *
     * @param delegate compute delegate
     * @param source   compute source Rect
     * @param target   compute target Rect
     */
    public static void computerRect(RenderDelegate delegate, Rect source, Rect target) {
        computerRect(delegate, source, target, true);
    }

    /**
     * Computer Rect
     *
     * @param delegate compute delegate
     * @param source   compute source RectFloat
     * @param target   compute target RectFloat
     */
    public static void computerRect(RenderDelegate delegate, RectFloat source, RectFloat target) {
        computerRect(delegate, source, target, true);
    }

    /**
     * Computer Rect
     *
     * @param delegate   compute delegate
     * @param source     compute source RectFloat
     * @param target     compute target RectFloat
     * @param isNeedOver is need over
     */
    public static void computerRect(RenderDelegate delegate, RectFloat source, RectFloat target, boolean isNeedOver) {
        if (delegate == null) {
            return;
        }
        initParamRect(delegate);

        ComputeInfo computeInfo = new ComputeInfo(
                delegate.getPixelMapWidth(),
                delegate.getPixelMapHeight(),
                paddings,
                matrix,
                delegate.isLongEdgeFull());
        computerRect(computeInfo, source, target, isNeedOver);
    }

    /**
     * Computer Rect
     *
     * @param delegate   compute delegate
     * @param source     compute source Rect
     * @param target     compute target Rect
     * @param isNeedOver is need over
     */
    public static void computerRect(RenderDelegate delegate, Rect source, Rect target, boolean isNeedOver) {
        initParamRect(delegate);

        ComputeInfo computeInfo = new ComputeInfo(
                delegate.getPixelMapWidth(),
                delegate.getPixelMapHeight(),
                paddings,
                matrix,
                delegate.isLongEdgeFull());
        computerRect(computeInfo, source, target, isNeedOver);
    }

    /**
     * Computer Rect
     *
     * @param delegate compute delegate
     * @param bw       compute width
     * @param bh       compute height
     * @param source   compute source
     * @param target   compute target
     */
    public static void computerRect(RenderDelegate delegate, int bw, int bh, Rect source, Rect target) {
        initParamRect(delegate);
        computerRect(new ComputeInfo(bw, bh, paddings, matrix, delegate.isLongEdgeFull()),
                source, target, true);
    }

    private static void computerRect(ComputeInfo info, RectFloat source, RectFloat target, boolean isNeedOver) {
        compute(info);

        if (target != null) {
            target.modify(isNeedOver ? getOverRect(displayRectF, renderRectF) : renderRectF);
        }

        if ((source != null) && (target != null)) {
            tempRect.set(0, 0, info.mBoundsWidth, info.mBoundsHeight);
            RectFloat rectFloat = getRatioTargetRect(tempRect, getRatioRect(target, renderRectF)).get();
            if (rectFloat != null) {
                source.modify(rectFloat);
            }
        }
    }

    private static void computerRect(ComputeInfo info, Rect source, Rect target, boolean isNeedOver) {
        compute(info);

        if (target != null) {
            target.modify(isNeedOver ? getOverRect(displayRect, renderRect) : renderRect);
        }

        if ((source != null) && (target != null)) {
            tempRect.set(0, 0, info.mBoundsWidth, info.mBoundsHeight);
            RectFloat rectFloat = getRatioTargetRect(tempRect, getRatioRect(target, renderRect)).orElse(null);
            if (rectFloat != null) {
                source.set((int) rectFloat.left, (int) rectFloat.top, (int) rectFloat.right, (int) rectFloat.bottom);
            }
        }
    }

    private static void initParamRect(RenderDelegate delegate) {
        int viewWidth = delegate.getViewWidth();
        int viewHeight = delegate.getViewHeight();
        int pixelMapWidth = delegate.getPixelMapWidth();
        int pixelMapHeight = delegate.getPixelMapHeight();
        if ((viewWidth == 0) || (viewHeight == 0) || (pixelMapWidth == 0) || (pixelMapHeight == 0)) {
            return;
        }

        margins.modify(delegate.getViewMargins() == null ? DEFAULT_RECT : delegate.getViewMargins());
        paddings.modify(delegate.getDisplayPaddings() == null ? DEFAULT_RECT : delegate.getDisplayPaddings());
        matrix.setMatrix(delegate.getScaleMatrix() == null ? DEFAULT_MATRIX : delegate.getScaleMatrix());
        int displayTop =
                margins.top - ADJUST_HEIGHT < EDITOR_HEAD_HIGHT ? margins.top : margins.top - ADJUST_HEIGHT;
        int displayBottom = viewHeight - margins.bottom;
        displayBottom =
                margins.top - ADJUST_HEIGHT < EDITOR_HEAD_HIGHT ? displayBottom : displayBottom - ADJUST_HEIGHT;
        displayRect.set(margins.left, displayTop, viewWidth - margins.right, displayBottom);
        displayRectF.modify(margins.left, displayTop, viewWidth - margins.right, displayBottom);
    }

    /**
     * Compute Crop Mask Rect
     *
     * @param delegate compute delegate
     * @param target   compute target
     */
    public static void computeCropMaskRect(RenderDelegate delegate, Rect target) {
        int viewWidth = delegate.getViewWidth();
        int viewHeight = delegate.getViewHeight();
        if ((viewWidth == 0) || (viewHeight == 0)) {
            return;
        }

        Rect margin = delegate.getViewMargins() == null ? DEFAULT_RECT : delegate.getViewMargins();
        Rect padding = delegate.getDisplayPaddings() == null ? DEFAULT_RECT : delegate.getDisplayPaddings();
        target.set(margin.left + padding.left,
                margin.top + padding.top,
                viewWidth - margin.right - padding.right,
                viewHeight - margin.bottom - padding.bottom);
    }

    private static void compute(ComputeInfo info) {
        int displayWidth = displayRect.getWidth();
        int displayHeight = displayRect.getHeight();
        float scale;
        if (info.mIsLongEdgeFull) {
            scale = Math.min(
                    (displayWidth - info.mPaddings.left - info.mPaddings.right) / (float) info.mBoundsWidth,
                    (displayHeight - info.mPaddings.top - info.mPaddings.bottom) / (float) info.mBoundsHeight);
        } else {
            scale = Math.max(
                    (displayWidth - info.mPaddings.left - info.mPaddings.right) / (float) info.mBoundsWidth,
                    (displayHeight - info.mPaddings.top - info.mPaddings.bottom) / (float) info.mBoundsHeight);
        }
        float renderWidth = info.mBoundsWidth * scale;
        float renderHeight = info.mBoundsHeight * scale;
        info.mMatrix.getElements(sValues);
        renderWidth = renderWidth * sValues[MATRIX_X_SCALE];
        renderHeight = renderHeight * sValues[MATRIX_Y_SCALE];
        float displayCenterX = displayWidth / HALF_DIVIDED_FLOAT + displayRect.left;
        float displayCenterY = displayHeight / HALF_DIVIDED_FLOAT + displayRect.top;
        displayCenterX = displayCenterX + sValues[MATRIX_X_TRANSLATE];
        displayCenterY = displayCenterY + sValues[MATRIX_Y_TRANSLATE];
        renderRectF.modify(displayCenterX - renderWidth / HALF_DIVIDED_FLOAT,
                displayCenterY - renderHeight / HALF_DIVIDED_FLOAT,
                displayCenterX + renderWidth / HALF_DIVIDED_FLOAT,
                displayCenterY + renderHeight / HALF_DIVIDED_FLOAT);
        renderRect.set(Math.round(displayCenterX - renderWidth / HALF_DIVIDED_FLOAT),
                Math.round(displayCenterY - renderHeight / HALF_DIVIDED_FLOAT),
                Math.round(displayCenterX + renderWidth / HALF_DIVIDED_FLOAT),
                Math.round(displayCenterY + renderHeight / HALF_DIVIDED_FLOAT));
    }

    /**
     * Get Ratio Rect
     *
     * @param rect1 get ratio rect1
     * @param rect2 get ratio rect2
     * @return RectFloat
     */
    public static RectFloat getRatioRect(final Rect rect1, final Rect rect2) {
        tempArg1RectF.modify(rect1);
        tempArg2RectF.modify(rect2);
        return getRatioRect(tempArg1RectF, tempArg2RectF);
    }

    /**
     * Get Ratio Rect
     *
     * @param target       get target
     * @param baseLineRect get base line rect
     * @return RectFloat
     */
    public static RectFloat getRatioRect(final RectFloat target, final RectFloat baseLineRect) {
        if ((target == null) || (baseLineRect == null)) {
            return tempResultRectF;
        }
        float left = (target.left - baseLineRect.left) / baseLineRect.getWidth();
        float top = (target.top - baseLineRect.top) / baseLineRect.getHeight();
        float right = 1F - (baseLineRect.right - target.right) / baseLineRect.getWidth();
        float bottom = 1F - (baseLineRect.bottom - target.bottom) / baseLineRect.getHeight();
        tempResultRectF.modify(left, top, right, bottom);
        return tempResultRectF;
    }

    /**
     * Get Over Rect
     * two rect result overflow part
     *
     * @param rect1 get over rect1
     * @param rect2 get over rect2
     * @return RectFloat
     */
    public static Rect getOverRect(final Rect rect1, final Rect rect2) {
        int left = Math.max(rect1.left, rect2.left);
        int right = Math.min(rect1.right, rect2.right);
        int top = Math.max(rect1.top, rect2.top);
        int bottom = Math.min(rect1.bottom, rect2.bottom);
        tempRect.set(left, top, right, bottom);
        return tempRect;
    }

    /**
     * Get Over Rect
     *
     * @param rect1 get over rect1
     * @param rect2 get over rect2
     * @return RectFloat
     */
    public static RectFloat getOverRect(final RectFloat rect1, final RectFloat rect2) {
        float left = Math.max(rect1.left, rect2.left);
        float right = Math.min(rect1.right, rect2.right);
        float top = Math.max(rect1.top, rect2.top);
        float bottom = Math.min(rect1.bottom, rect2.bottom);
        tempResultRectF.modify(left, top, right, bottom);
        return tempResultRectF;
    }

    private static Optional<RectFloat> getRatioTargetRect(Rect baseLineRect, RectFloat ratioRect) {
        tempArg1RectF.modify(baseLineRect);
        return getRatioTargetRect(tempArg1RectF, ratioRect);
    }

    /**
     * Get Ratio Target Rect
     *
     * @param baseLineRect get base line rect
     * @param ratioRect    get ratio rect
     * @return RectFloat
     */
    public static Optional<RectFloat> getRatioTargetRect(RectFloat baseLineRect, RectFloat ratioRect) {
        return getRatioTargetRect(baseLineRect, ratioRect, 1f, 1f);
    }

    /**
     * Get Ratio Target Rect
     *
     * @param baseLineRect      get base line rect
     * @param ratioRect         get ratio rect
     * @param targetWidthRatio  get target width
     * @param targetHeightRatio get target height
     * @return RectFloat
     */
    public static Optional<RectFloat> getRatioTargetRect(RectFloat baseLineRect, RectFloat ratioRect,
                                                         float targetWidthRatio, float targetHeightRatio) {
        if ((baseLineRect == null) || (ratioRect == null)) {
            return Optional.empty();
        }
        float left = (baseLineRect.left + ratioRect.left * baseLineRect.getWidth()) / targetWidthRatio;
        float top = (baseLineRect.top + ratioRect.top * baseLineRect.getHeight()) / targetHeightRatio;
        float right = (baseLineRect.right - (1F - ratioRect.right) * baseLineRect.getWidth()) / targetWidthRatio;
        float bottom = (baseLineRect.bottom - (1F - ratioRect.bottom) * baseLineRect.getHeight()) / targetHeightRatio;
        tempResultRectF.modify(left, top, right, bottom);
        return Optional.of(tempResultRectF);
    }

    /**
     * RenderDelegate
     */
    public interface RenderDelegate {
        /**
         * Get View Width
         *
         * @return Width
         */
        int getViewWidth();

        /**
         * Get View Height
         *
         * @return Height
         */
        int getViewHeight();

        /**
         * Get PixelMap Width
         *
         * @return Width
         */
        int getPixelMapWidth();

        /**
         * Get PixelMap Height
         *
         * @return Height
         */
        int getPixelMapHeight();

        /**
         * Is Long Edge Full
         *
         * @return boolean
         */
        boolean isLongEdgeFull();

        /**
         * Get View Margins
         *
         * @return Rect
         */
        Rect getViewMargins();

        /**
         * Get Display Paddings
         *
         * @return Rect
         */
        Rect getDisplayPaddings();

        /**
         * Get Scale Matrix
         *
         * @return Matrix
         */
        Matrix getScaleMatrix();
    }

    private static class ComputeInfo {
        final Rect mPaddings;
        final Matrix mMatrix;
        int mBoundsWidth;
        int mBoundsHeight;
        boolean mIsLongEdgeFull;

        /**
         * constructor
         *
         * @param bw             bounds width
         * @param bh             bounds height
         * @param paddings       paddings
         * @param matrix         Matrix
         * @param isLongEdgeFull boolean
         */
        ComputeInfo(int bw, int bh, Rect paddings, Matrix matrix, boolean isLongEdgeFull) {
            this.mBoundsWidth = bw;
            this.mBoundsHeight = bh;
            this.mPaddings = paddings;
            this.mMatrix = matrix;
            this.mIsLongEdgeFull = isLongEdgeFull;
        }
    }

}
