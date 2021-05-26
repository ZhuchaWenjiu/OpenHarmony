package com.example.jltfmoban8.view;

import ohos.agp.components.AttrSet;
import ohos.agp.components.ComponentContainer;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.utils.Point;
import ohos.app.Context;
import ohos.multimodalinput.event.TouchEvent;

/**
 * Vertical RulerView
 */
public class VerticalRulerView extends BaseRulerView {
    private static final float HALF_DIVIDER = 2.0f;
    private static final float DOUBLE_MULTIPLIER = 2.0f;
    private static final int MAX_TEXT_LENGTH = 3;
    private static final int NUMBER_SHOW_SCALES = 10; // Number show at every ten scales

    /**
     * scroll end Y
     */
    private int mScrollEndY;
    /**
     * scroll start Y
     */
    private int mScrollStartY;

    /**
     * constructor
     *
     * @param context Context
     */
    public VerticalRulerView(Context context) {
        this(context, null);
    }

    /**
     * constructor
     *
     * @param context Context
     * @param attrs   AttributeSet
     */
    public VerticalRulerView(Context context, AttrSet attrs) {
        this(context, attrs, "");
    }

    public VerticalRulerView(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
        init();
    }

    private void init() {
        this.setLayoutRefreshedListener(component -> {
            rulerViewRange = component.getHeight();
            halfRangeCount = (rulerViewRange / (lineMargin * 1.0f)) / HALF_DIVIDER;
        });
        this.setScrolledListener((component, i0, i1, i2, i3) -> {
            mScrollEndY = i3;
            mScrollStartY = i1;
            invalidate();
        });
        this.setTouchEventListener((component, event) -> {
            int fy = (int) event.getPointerPosition(event.getIndex()).getY();
            switch (event.getAction()) {
                case TouchEvent.PRIMARY_POINT_DOWN: {
                    scrollLastX = fy;
                    return true;
                }
                case TouchEvent.POINT_MOVE: {
                    int dataY = fy - scrollLastX;
                    if (countLine <= userScrollStart && dataY >= 0) {
                        return true;
                    }
                    if (countLine >= userScrollEnd && dataY <= 0) {
                        return true;
                    }
                    scrollBy(0, dataY);
                    scrollLastX = fy;
                    return true;
                }
                default: {
                    return true;
                }
            }
        });
    }

    @Override
    protected void initLayoutParam() {
        totalHeight = (displayRulerEnd - displayRulerStart) * lineMargin;
        totalWidth = rulerViewHeight;
        ComponentContainer.LayoutConfig layoutConfig = new ComponentContainer.LayoutConfig(totalWidth, totalHeight);
        this.setLayoutConfig(layoutConfig);
    }

    @Override
    protected void drawRuler(Canvas canvas, Paint paint, int delta) {
        paint.setTextSize(numberFontSize);
        paint.setStyle(Paint.Style.FILL_STYLE);
        paint.setStrokeWidth(strokeWidth);
        paint.setStrokeCap(Paint.StrokeCap.ROUND_CAP);
        int iterator = 0;
        int rulerValue = displayRulerStart;
        int startX = totalWidth - shortLineToBottomLength;
        for (iterator = 0; iterator <= displayRulerEnd - displayRulerStart; iterator++) {
            setPaintColor(paint, iterator + displayRulerStart);
            if (iterator % NUMBER_SHOW_SCALES == 0) {
                canvas.drawLine(new Point(startX, iterator * lineMargin - delta), new Point(startX - tenLineLength,
                        iterator * lineMargin - delta), paint);
                canvas.drawText(paint, String.valueOf(rulerValue),
                        startX - tenLineLength - tenLineToNumberLength - numberFontSize,
                        iterator * lineMargin + paint.getTextSize() / MAX_TEXT_LENGTH - delta);
                rulerValue += NUMBER_SHOW_SCALES;
            } else {
                canvas.drawLine(new Point(startX, iterator * lineMargin - delta), new Point(startX - shortLineLength,
                        iterator * lineMargin - delta), paint);
            }
        }
    }

    @Override
    protected void updateMiddleLineDegree(Canvas canvas, Paint paint) {
        // calc num of mCountLine
        float tmpCountScale = ((mScrollEndY - mScrollStartY) * 1.0f) / lineMargin;
        mScrollEndY = 0;
        mScrollStartY = 0;
        countLine += tmpCountScale;
        if (countLine < userScrollStart) {
            countLine = userScrollStart;
        } else if (countLine > userScrollEnd) {
            countLine = userScrollEnd;
        }
        if (rotateChangeListener != null) {
            rotateChangeListener.onAngleChanged(countLine);
        }
        // drawMiddleLine
        paint.setStrokeWidth(strokeWidth * DOUBLE_MULTIPLIER);
        paint.setAlpha(1);
        paint.setStyle(Paint.Style.FILL_STYLE);
        paint.setStrokeCap(Paint.StrokeCap.ROUND_CAP);
        float middleY = rulerViewRange / HALF_DIVIDER;
        float middleEndY = rulerViewHeight - middleLineToEnd;
        canvas.drawLine(new Point(middleEndY, middleY), new Point(middleEndY - middleLineLength, middleY), paint);
    }

    @Override
    public void scrollToLine(float targetScale) {
        // can not scroll if limits
        if (targetScale < userScrollStart || targetScale > userScrollEnd) {
            return;
        }
        float dx = (countLine - targetScale) * lineMargin;
        scrollBy(0, (int) dx);
    }
}