package com.example.jltfmoban8.view;

import com.example.jltfmoban8.ResourceTable;
import com.example.jltfmoban8.core.exceptions.CropPickerException;
import com.example.jltfmoban8.core.utils.ImageEditUtils;
import com.example.jltfmoban8.core.utils.RectCompute;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.utils.Color;
import ohos.agp.utils.Point;
import ohos.agp.utils.Rect;
import ohos.agp.utils.RectFloat;
import ohos.app.Context;
import ohos.global.resource.NotExistException;
import ohos.global.resource.WrongTypeException;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.multimodalinput.event.TouchEvent;

import java.io.IOException;

/**
 * Crop Picker
 */
public class CropPicker extends ImagePicker {
    private static final int MOVE_LEFT = 1;
    private static final int MOVE_TOP = 2;
    private static final int MOVE_RIGHT = 4;
    private static final int MOVE_BOTTOM = 8;
    private static final int MOVE_BLOCK = 16;
    private static final int TOUCH_TOLERANCE = 20;
    private static final int RULER_LINE_COUNTS = 3;
    private static final int COLOR_SHADOW = 0x66000000;
    private static final float OUTLINE_WIDTH = 3f;
    private static final float HALF_DIVIDER = 2.0f;
    private static final float RECT_STROKE_WIDTH = 3.0f;
    private static final float MIN_SELECTION_LENGTH = 50f;
    private static final float CORNER_THICKNESS = 3f;
    private static final float BOARDER_THICKNESS = 3f;
    private static final float CORNER_LENGTH = 15f;
    private static final float STROKE_WIDTH = 1f;
    private static final float UNSPECIFIED = -1f;
    private static final float RATIO_RECT_EDGE = 1f;
    private static final HiLogLabel CROP_PICKER_EXCEPTIONS = new HiLogLabel(1, 1, "Element Loader");

    private int touchTolerance;
    private float minSelectionLength;
    private float cornerThickness;
    private float borderThickness;
    private float cornerLength;
    private RectFloat highlightRect = new RectFloat(0f, 0f, RATIO_RECT_EDGE, RATIO_RECT_EDGE);
    private RectFloat baseHighlightRect = new RectFloat(0f, 0f, RATIO_RECT_EDGE, RATIO_RECT_EDGE);
    private RectFloat tempRect = new RectFloat();
    private int movingEdges = 0;
    private float referenceX;
    private float referenceY;
    private Paint gridPaint = new Paint();
    private float aspectRatio = UNSPECIFIED;

    public CropPicker(Context context) {
        super(context);
        initView(context);
    }

    public CropPicker(Context context, AttrSet attrSet) {
        super(context, attrSet);
        initView(context);
    }

    public CropPicker(Context context, AttrSet attrSet, int resId) {
        super(context, attrSet, resId);
        initView(context);
    }

    private void initView(Context context) {
        initConstants(context);
        try {
            gridPaint.setColor(new Color(getResourceManager()
                    .getElement(ResourceTable.Color_crop_bolder_selected).getColor()));
            gridPaint.setStrokeWidth(STROKE_WIDTH);
        } catch (IOException | NotExistException | WrongTypeException e) {
            HiLog.error(CROP_PICKER_EXCEPTIONS, "Element Loader: ", " Failed");
        }
        this.setTouchEventListener((component, touchEvent) -> {
            if (touchEvent == null) {
                return false;
            }

            switch (touchEvent.getAction()) {
                case TouchEvent.PRIMARY_POINT_DOWN: {
                    return dealWithActionDown(touchEvent);
                }
                case TouchEvent.POINT_MOVE: {
                    return dealWithActionMove(touchEvent);
                }
                case TouchEvent.CANCEL:
                case TouchEvent.PRIMARY_POINT_UP: {
                    return dealWithActionUpOrCancel(touchEvent);
                }
                default:
                    return false;
            }
        });
        this.addDrawTask(new CropDrawTask());
    }

    private void initConstants(Context context) {
        touchTolerance = ImageEditUtils.vpToPixel(context, TOUCH_TOLERANCE);
        minSelectionLength = ImageEditUtils.vpToPixel(context, MIN_SELECTION_LENGTH);
        borderThickness = ImageEditUtils.vpToPixel(context, BOARDER_THICKNESS);
        cornerThickness = ImageEditUtils.vpToPixel(context, CORNER_THICKNESS);
        cornerLength = ImageEditUtils.vpToPixel(context, CORNER_LENGTH);
    }

    /**
     * Set CropPicker AspectRatio
     *
     * @param ratio      AspectRatio
     * @param boundRatio Specifying a Rectangle Range, Can be null
     * @throws CropPickerException CropPickerException
     */
    public void setAspectRatio(float ratio, RectFloat boundRatio) throws CropPickerException {
        if (baseLineRect == null) {
            throw new CropPickerException("Baseline not initialized yet");
        }

        aspectRatio = ratio;
        setHighlightRectangle(boundRatio);
    }

    /**
     * Obtains RatioRect
     *
     * @return RatioRect
     */
    public RectFloat getPickRatioRect() {
        return highlightRect;
    }

    /**
     * Update CropPicker BaseLine Rect.
     *
     * @param baseLineRect CropPicker BaseLine Rect
     * @throws CropPickerException CropPickerException
     */
    public void update(Rect baseLineRect) throws CropPickerException {
        if (baseLineRect.getWidth() < minSelectionLength || baseLineRect.getHeight() < minSelectionLength) {
            throw new CropPickerException("baseline size MUST > " + minSelectionLength);
        }
        if (this.baseLineRect == null) {
            this.baseLineRect = new RectFloat(baseLineRect);
        } else {
            this.baseLineRect.modify(baseLineRect);
        }
        if (aspectRatio == UNSPECIFIED) {
            setHighlightRectangle(baseHighlightRect);
        } else {
            setHighlightRectangle(null);
        }
    }

    /**
     * Set Highlight rect
     *
     * @param destRatioRect HighlightRect
     */
    private void setHighlightRectangle(RectFloat destRatioRect) {
        if (destRatioRect == null) {
            RectFloat src = new RectFloat(highlightRect);
            float ratio = aspectRatio == UNSPECIFIED ? RATIO_RECT_EDGE : aspectRatio;
            RectFloat highlightArea = getHighlightRect(ratio);
            this.highlightRect.modify(highlightArea);
        } else {
            highlightRect.modify(destRatioRect);
        }
        invalidate();
    }

    /**
     * Reset Highlight Rect to baseHighlightRect
     */
    public void resetHighlightRectangle() {
        aspectRatio = UNSPECIFIED;
        setHighlightRectangle(baseHighlightRect);
    }

    private RectFloat getHighlightRect(float aspectRatio) {
        RectFloat highlightArea = new RectFloat();
        float baselineAspectRatio = baseLineRect.getWidth() / baseLineRect.getHeight();
        boolean scaleByWidth = aspectRatio >= baselineAspectRatio;
        if (scaleByWidth) {
            float width = baseLineRect.getWidth();
            float height = width / aspectRatio;
            float left = 0f;
            float right = RATIO_RECT_EDGE;
            float top = (baseLineRect.getHeight() - height) / HALF_DIVIDER / baseLineRect.getHeight();
            float bottom = RATIO_RECT_EDGE - top;
            highlightArea.modify(left, top, right, bottom);
        } else {
            float height = baseLineRect.getHeight();
            float width = height * aspectRatio;
            float top = 0f;
            float bottom = RATIO_RECT_EDGE;
            float left = (baseLineRect.getWidth() - width) / HALF_DIVIDER / baseLineRect.getWidth();
            float right = RATIO_RECT_EDGE - left;
            highlightArea.modify(left, top, right, bottom);
        }
        return highlightArea;
    }

    private void moveEdges(TouchEvent event, RectFloat highlightRect) {
        float dx = (event.getPointerPosition(event.getIndex()).getX() - referenceX) / baseLineRect.getWidth();
        float dy = (event.getPointerPosition(event.getIndex()).getY() - referenceY) / baseLineRect.getHeight();
        referenceX = event.getPointerPosition(event.getIndex()).getX();
        referenceY = event.getPointerPosition(event.getIndex()).getY();

        if ((movingEdges & MOVE_BLOCK) != 0) {
            dx = ImageEditUtils.clamp(dx, -highlightRect.left, 1 - highlightRect.right);
            dy = ImageEditUtils.clamp(dy, -highlightRect.top, 1 - highlightRect.bottom);

            highlightRect.left += dx;
            highlightRect.top += dy;
            highlightRect.right += dx;
            highlightRect.bottom += dy;

        } else {
            float ratioX = (referenceX - baseLineRect.left) / baseLineRect.getWidth();
            float ratioY = (referenceY - baseLineRect.top) / baseLineRect.getHeight();
            float left = ImageEditUtils.clamp(highlightRect.left + minSelectionLength /
                    baseLineRect.getWidth(), 0.0f, 1.0f);
            float right = ImageEditUtils.clamp(highlightRect.right - minSelectionLength /
                    baseLineRect.getWidth(), 0.0f, 1.0f);
            float top = ImageEditUtils.clamp(highlightRect.top + minSelectionLength /
                    baseLineRect.getHeight(), 0.0f, 1.0f);
            float bottom = ImageEditUtils.clamp(highlightRect.bottom - minSelectionLength /
                    baseLineRect.getHeight(), 0.0f, 1.0f);
            if ((movingEdges & MOVE_RIGHT) != 0) {
                highlightRect.right = ImageEditUtils.clamp(ratioX, left, 1f);
            }
            if ((movingEdges & MOVE_LEFT) != 0) {
                highlightRect.left = ImageEditUtils.clamp(ratioX, 0, right);
            }
            if ((movingEdges & MOVE_TOP) != 0) {
                highlightRect.top = ImageEditUtils.clamp(ratioY, 0, bottom);
            }
            if ((movingEdges & MOVE_BOTTOM) != 0) {
                highlightRect.bottom = ImageEditUtils.clamp(ratioY, top, 1f);
            }
            if (aspectRatio != UNSPECIFIED) {
                moveEdgesWithSpecifiedRatio(highlightRect, left, top, right, bottom, aspectRatio);
            }
        }
    }

    private void moveEdgesWithSpecifiedRatio(RectFloat highlightRect, float left, float top,
                                             float right, float bottom, float specRatio) {
        float maxThreshHold = 1f;
        float minThreshHold = 0f;
        float targetRatio = getTargetRatio(specRatio);
        boolean isGreaterThanTargetRatio = highlightRect.getWidth() / highlightRect.getHeight() > targetRatio;
        if (isGreaterThanTargetRatio) {
            float height = highlightRect.getWidth() / targetRatio;
            if ((movingEdges & MOVE_BOTTOM) != 0) {
                highlightRect.bottom = ImageEditUtils.clamp(highlightRect.top + height, top, maxThreshHold);
            } else {
                highlightRect.top = ImageEditUtils.clamp(highlightRect.bottom - height, minThreshHold, bottom);
            }
        } else {
            float width = highlightRect.getHeight() * targetRatio;
            if ((movingEdges & MOVE_LEFT) != 0) {
                highlightRect.left = ImageEditUtils.clamp(highlightRect.right - width, minThreshHold, right);
            } else {
                highlightRect.right = ImageEditUtils.clamp(highlightRect.left + width, left, maxThreshHold);
            }
        }
        if (isGreaterThanTargetRatio) {
            float width = highlightRect.getHeight() * targetRatio;
            if ((movingEdges & MOVE_LEFT) != 0) {
                highlightRect.left = ImageEditUtils.clamp(highlightRect.right - width, minThreshHold, right);
            } else {
                highlightRect.right = ImageEditUtils.clamp(highlightRect.left + width, left, maxThreshHold);
            }
        } else {
            float height = highlightRect.getWidth() / targetRatio;
            if ((movingEdges & MOVE_BOTTOM) != 0) {
                highlightRect.bottom = ImageEditUtils.clamp(highlightRect.top + height, top, maxThreshHold);
            } else {
                highlightRect.top = ImageEditUtils.clamp(highlightRect.bottom - height, minThreshHold, bottom);
            }
        }
    }

    private float getTargetRatio(float specRatio) {
        return specRatio * baseLineRect.getHeight() / baseLineRect.getWidth();
    }

    /**
     * Update HighlightRectangle moving states
     *
     * @param event TouchEvent
     */
    private void setMovingEdges(TouchEvent event) {
        RectFloat rectF = tempRect;
        rectF.modify(RectCompute.getRatioTargetRect(baseLineRect, highlightRect).get());
        float posX = event.getPointerPosition(event.getIndex()).getX();
        float posY = event.getPointerPosition(event.getIndex()).getY();

        if (isInsideMovingBlock(rectF, posX, posY)) {
            movingEdges = MOVE_BLOCK;
        } else {
            movingEdges = handleTouchOnEdge(rectF, posX, posY);
        }
    }

    private int handleTouchOnEdge(RectFloat rectF, float posX, float posY) {
        int movingEdge = 0;
        boolean inVerticalRange = (rectF.top - touchTolerance) <= posY
                && posY <= (rectF.bottom + touchTolerance);
        boolean inHorizontalRange = (rectF.left - touchTolerance) <= posX
                && posX <= (rectF.right + touchTolerance);

        if (inVerticalRange) {
            boolean isLeft = Math.abs(posX - rectF.left) <= touchTolerance;
            boolean isRight = Math.abs(posX - rectF.right) <= touchTolerance;
            if (isLeft && isRight) {
                isLeft = Math.abs(posX - rectF.left) < Math.abs(posX - rectF.right);
                isRight = !isLeft;
            }
            if (isLeft) {
                movingEdge |= MOVE_LEFT;
            }
            if (isRight) {
                movingEdge |= MOVE_RIGHT;
            }
            if (aspectRatio != UNSPECIFIED && inHorizontalRange) {
                movingEdge |= (posY > (rectF.top + rectF.bottom) / HALF_DIVIDER) ? MOVE_BOTTOM : MOVE_TOP;
            }
        }
        if (inHorizontalRange) {
            boolean isTop = Math.abs(posY - rectF.top) <= touchTolerance;
            boolean isBottom = Math.abs(posY - rectF.bottom) <= touchTolerance;
            if (isTop && isBottom) {
                isTop = Math.abs(posY - rectF.top) < Math.abs(posY - rectF.bottom);
                isBottom = !isTop;
            }
            if (isTop) {
                movingEdge |= MOVE_TOP;
            }
            if (isBottom) {
                movingEdge |= MOVE_BOTTOM;
            }
            if (aspectRatio != UNSPECIFIED && inVerticalRange) {
                movingEdge |= (posX > (rectF.left + rectF.right) / HALF_DIVIDER) ? MOVE_RIGHT : MOVE_LEFT;
            }
        }
        return movingEdge;
    }

    private boolean isInsideMovingBlock(RectFloat rectF, float posX, float posY) {
        return posX > rectF.left + touchTolerance && posX < rectF.right - touchTolerance
                && posY > rectF.top + touchTolerance && posY < rectF.bottom - touchTolerance;
    }

    private boolean dealWithActionMove(TouchEvent event) {
        if (event.getPointerCount() > 1) {
            return false;
        }
        moveEdges(event, highlightRect);
        invalidate();
        return true;
    }

    private boolean dealWithActionDown(TouchEvent event) {
        referenceX = event.getPointerPosition(event.getIndex()).getX();
        referenceY = event.getPointerPosition(event.getIndex()).getY();
        setMovingEdges(event);
        invalidate();
        return true;
    }

    private boolean dealWithActionUpOrCancel(TouchEvent event) {
        movingEdges = 0;
        invalidate();
        return true;
    }

    private void drawRuleOfThird(Canvas canvas, RectFloat hlightRectF) {
        float stepX = hlightRectF.getWidth() / RULER_LINE_COUNTS;
        float stepY = hlightRectF.getHeight() / RULER_LINE_COUNTS;
        float lineX = hlightRectF.left + stepX;
        float lineY = hlightRectF.top + stepY;
        for (int i = 0; i < RULER_LINE_COUNTS - 1; i++) {
            canvas.drawLine(new Point(lineX, hlightRectF.top), new Point(lineX, hlightRectF.bottom), gridPaint);
            lineX += stepX;
        }
        for (int j = 0; j < RULER_LINE_COUNTS - 1; j++) {
            canvas.drawLine(new Point(hlightRectF.left, lineY), new Point(hlightRectF.right, lineY), gridPaint);
            lineY += stepY;
        }
    }

    private void drawAspectRatioArrow(Canvas canvas, RectFloat hlightRectF) {
        drawCorners(canvas, hlightRectF);
    }

    private void drawCorners(Canvas canvas, RectFloat hlightRectF) {
        final Paint cornerPaint = new Paint();
        cornerPaint.setStyle(Paint.Style.STROKE_STYLE);
        cornerPaint.setStrokeWidth(cornerThickness);
        cornerPaint.setColor(Color.WHITE);
        float centerY = (hlightRectF.top + hlightRectF.bottom) / HALF_DIVIDER;
        float centerX = (hlightRectF.left + hlightRectF.right) / HALF_DIVIDER;
        // Absolute value of the offset by which to draw the corner line
        // Such that its inner edge is flush with the border's inner edge.
        final float lateralOffset = (cornerThickness - borderThickness) / HALF_DIVIDER;
        // Absolute value of the offset by which to start the corner line
        // Such that the line is drawn all the way to form a corner edge with the adjacent side.
        final float cornerStartOffset = cornerThickness - (borderThickness / HALF_DIVIDER);

        // Top-left corner: left side
        Point topLeftToLeftStart = new Point(hlightRectF.left - lateralOffset,
                hlightRectF.top - cornerStartOffset);
        Point topLeftToLeftEnd = new Point(hlightRectF.left - lateralOffset,
                hlightRectF.top + cornerLength);
        canvas.drawLine(topLeftToLeftStart, topLeftToLeftEnd, cornerPaint);
        // Top-left corner: top side
        Point topLeftToTopStart = new Point(hlightRectF.left - cornerStartOffset,
                hlightRectF.top - lateralOffset);
        Point topLeftToTopEnd = new Point(hlightRectF.left + cornerLength,
                hlightRectF.top - lateralOffset);
        canvas.drawLine(topLeftToTopStart, topLeftToTopEnd, cornerPaint);

        // Top-right corner: right side
        Point topRightToRightStart = new Point(hlightRectF.right + lateralOffset,
                hlightRectF.top - cornerStartOffset);
        Point topRightToRightEnd = new Point(hlightRectF.right + lateralOffset,
                hlightRectF.top + cornerLength);
        canvas.drawLine(topRightToRightStart, topRightToRightEnd, cornerPaint);
        // Top-right corner: top side
        Point topRightToTopStart = new Point(hlightRectF.right + cornerStartOffset,
                hlightRectF.top - lateralOffset);
        Point topRightToTopEnd = new Point(hlightRectF.right - cornerLength,
                hlightRectF.top - lateralOffset);
        canvas.drawLine(topRightToTopStart, topRightToTopEnd, cornerPaint);

        // Bottom-left corner: left side
        Point bottomLeftToLeftStart = new Point(hlightRectF.left - lateralOffset,
                hlightRectF.bottom + cornerStartOffset);
        Point bottomLeftToLeftEnd = new Point(hlightRectF.left - lateralOffset,
                hlightRectF.bottom - cornerLength);
        canvas.drawLine(bottomLeftToLeftStart, bottomLeftToLeftEnd, cornerPaint);
        // Bottom-left corner: bottom side
        Point bottomLeftToBottomStart = new Point(hlightRectF.left - cornerStartOffset,
                hlightRectF.bottom + lateralOffset);
        Point bottomLeftToBottomEnd = new Point(hlightRectF.left + cornerLength,
                hlightRectF.bottom + lateralOffset);
        canvas.drawLine(bottomLeftToBottomStart, bottomLeftToBottomEnd, cornerPaint);

        // Bottom-right corner: right side
        Point bottomRightToRightStart = new Point(hlightRectF.right + lateralOffset,
                hlightRectF.bottom + cornerStartOffset);
        Point bottomRightToRightEnd = new Point(hlightRectF.right + lateralOffset,
                hlightRectF.bottom - cornerLength);
        canvas.drawLine(bottomRightToRightStart, bottomRightToRightEnd, cornerPaint);
        // Bottom-right corner: bottom side
        Point bottomRightToBottomStart = new Point(hlightRectF.right + cornerStartOffset,
                hlightRectF.bottom + lateralOffset);
        Point bottomRightToBottomEnd = new Point(hlightRectF.right - cornerLength,
                hlightRectF.bottom + lateralOffset);
        canvas.drawLine(bottomRightToBottomStart, bottomRightToBottomEnd, cornerPaint);

        Point leftToStart = new Point(hlightRectF.left - lateralOffset,
                centerY - (cornerLength / HALF_DIVIDER));
        Point leftToEnd = new Point(hlightRectF.left - lateralOffset,
                centerY + (cornerLength / HALF_DIVIDER));
        canvas.drawLine(leftToStart, leftToEnd, cornerPaint);

        Point rightToStart = new Point(hlightRectF.right + lateralOffset,
                centerY - (cornerLength / HALF_DIVIDER));
        Point rightToEnd = new Point(hlightRectF.right + lateralOffset,
                centerY + (cornerLength / HALF_DIVIDER));
        canvas.drawLine(rightToStart, rightToEnd, cornerPaint);

        Point topToStart = new Point(centerX - (cornerLength / HALF_DIVIDER),
                hlightRectF.top - lateralOffset);
        Point topToEnd = new Point(centerX + (cornerLength / HALF_DIVIDER),
                hlightRectF.top - lateralOffset);
        canvas.drawLine(topToStart, topToEnd, cornerPaint);

        Point bottomToStart = new Point(centerX - (cornerLength / HALF_DIVIDER),
                hlightRectF.bottom + lateralOffset);
        Point bottomToEnd = new Point(centerX + (cornerLength / HALF_DIVIDER),
                hlightRectF.bottom + lateralOffset);
        canvas.drawLine(bottomToStart, bottomToEnd, cornerPaint);
    }

    private void drawHighlightRectangle(Canvas canvas, RectFloat hlightRectF) {
        canvas.save();
        Paint tmpPaint = new Paint();
        RectFloat tmpRectF = new RectFloat();

        tmpPaint.setStrokeWidth(RECT_STROKE_WIDTH);
        tmpPaint.setColor(Color.TRANSPARENT);
        tmpPaint.setStyle(Paint.Style.FILL_STYLE);
        tmpRectF.modify(hlightRectF.left, hlightRectF.top, hlightRectF.right, hlightRectF.bottom);
        canvas.drawRect(tmpRectF, tmpPaint);

        // Shadow Area
        canvas.clipRect(tmpRectF, Canvas.ClipOp.DIFFERENCE);
        tmpPaint.setColor(new Color(COLOR_SHADOW));
        tmpRectF.modify(this.getLeft(), this.getTop(), this.getRight(), this.getBottom());
        canvas.drawRect(tmpRectF, tmpPaint);

        // Highlight Area
        canvas.clipRect(tmpRectF, Canvas.ClipOp.DIFFERENCE);
        tmpPaint.setColor(Color.TRANSPARENT);
        tmpRectF.modify(0, 0, getWidth(), getHeight());
        canvas.drawRect(tmpRectF, tmpPaint);

        // Edge
        tmpPaint.setColor(gridPaint.getColor());
        tmpPaint.setStyle(Paint.Style.STROKE_STYLE);
        tmpPaint.setStrokeWidth(OUTLINE_WIDTH);
        canvas.drawRect(tmpRectF, tmpPaint);

        canvas.restore();
    }

    private class CropDrawTask implements DrawTask {
        @Override
        public void onDraw(Component component, Canvas canvas) {
            RectFloat rectF = RectCompute.getRatioTargetRect(baseLineRect, highlightRect).orElse(null);
            if (rectF == null) {
                return;
            }
            tempRect.modify(rectF);
            drawHighlightRectangle(canvas, tempRect);
            drawRuleOfThird(canvas, tempRect);
            drawAspectRatioArrow(canvas, tempRect);
            if (onPickActionListener != null) {
                onPickActionListener.onPickUpdate(highlightRect, null);
            }
        }
    }
}
