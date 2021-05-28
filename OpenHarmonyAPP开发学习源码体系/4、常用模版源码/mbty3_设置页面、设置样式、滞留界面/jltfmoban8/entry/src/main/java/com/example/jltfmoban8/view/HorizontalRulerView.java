package com.example.jltfmoban8.view;

import ohos.agp.components.AttrSet;
import ohos.agp.components.ComponentContainer;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.utils.Point;
import ohos.app.Context;
import ohos.multimodalinput.event.TouchEvent;

/**
 * Horizontal Ruler View
 */
public class HorizontalRulerView extends BaseRulerView {
    private static final float HALF_DIVIDER = 2.0f;
    private static final float DOUBLE_MULTIPLIER = 2.0f;
    private static final int NUMBER_SHOW_SCALES = 10; // Number show at every ten scales


    /**
     * scroll end x
     */
    private int mScrollEndX;
    /**
     * scroll start x
     */
    private int mScrollStartX;

    /**
     * constructor
     *
     * @param context Context
     */
    public HorizontalRulerView(Context context) {
        this(context, null);
    }

    /**
     * constructor
     *
     * @param context Context
     * @param attrs   AttributeSet
     */
    public HorizontalRulerView(Context context, AttrSet attrs) {
        this(context, attrs, "");
    }

    /**
     * Constructor
     *
     * @param context   Context
     * @param attrSet   AttributeSet
     * @param styleName StyleName
     */
    public HorizontalRulerView(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
        init();
    }

    private void init() {
        this.setLayoutRefreshedListener(component -> {
            rulerViewRange = component.getWidth();
            halfRangeCount = (rulerViewRange / (lineMargin * 1.0f)) / HALF_DIVIDER;
        });
        this.setScrolledListener((component, i0, i1, i2, i3) -> {
            mScrollEndX = i2;
            mScrollStartX = i0;
            invalidate();
        });
        this.setTouchEventListener((component, event) -> {
            int fx = (int) event.getPointerPosition(event.getIndex()).getX();
            switch (event.getAction()) {
                case TouchEvent.PRIMARY_POINT_DOWN: {
                    scrollLastX = fx;
                    return true;
                }
                case TouchEvent.POINT_MOVE: {
                    int dataX = fx - scrollLastX;
                    if (countLine <= userScrollStart && dataX >= 0) {
                        return true;
                    }
                    if (countLine >= userScrollEnd && dataX <= 0) {
                        return true;
                    }
                    scrollBy(dataX, 0);
                    scrollLastX = fx;
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
        totalWidth = (displayRulerEnd - displayRulerStart) * lineMargin;
        totalHeight = rulerViewHeight;
        ComponentContainer.LayoutConfig layoutConfig = new ComponentContainer.LayoutConfig(totalWidth, totalHeight);
        this.setLayoutConfig(layoutConfig);
    }

    @Override
    protected void drawRuler(Canvas canvas, Paint paint, int delta) {
        paint.setTextSize(numberFontSize);
        paint.setStyle(Paint.Style.FILL_STYLE);
        paint.setStrokeWidth(strokeWidth);
        paint.setStrokeCap(Paint.StrokeCap.ROUND_CAP);
        int iterator;
        int rulerValue = displayRulerStart;
        int startY = totalHeight - shortLineToBottomLength;
        for (iterator = 0; iterator <= displayRulerEnd - displayRulerStart; iterator++) {
            setPaintColor(paint, iterator + displayRulerStart);
            if (iterator % NUMBER_SHOW_SCALES == 0) {
                // draw long line
                canvas.drawLine(new Point(iterator * lineMargin - delta, startY),
                        new Point(iterator * lineMargin - delta,
                                startY - tenLineLength), paint);
                // draw number
                canvas.drawText(paint, String.valueOf(rulerValue), iterator * lineMargin - delta,
                        startY - tenLineLength - tenLineToNumberLength);
                rulerValue += NUMBER_SHOW_SCALES;
            } else {
                // draw normal line
                canvas.drawLine(new Point(iterator * lineMargin - delta, startY),
                        new Point(iterator * lineMargin - delta,
                                startY - shortLineLength), paint);
            }
        }
    }

    @Override
    protected void updateMiddleLineDegree(Canvas canvas, Paint paint) {
        // calc num of mCountLine
        float tmpCountScale = ((mScrollEndX - mScrollStartX) * 1.0f) / lineMargin;
        mScrollEndX = 0;
        mScrollStartX = 0;
        countLine += tmpCountScale;
        if (countLine < userScrollStart) {
            countLine = userScrollStart;
        } else if (countLine > userScrollEnd) {
            countLine = userScrollEnd;
        }
        if (rotateChangeListener != null) {
            rotateChangeListener.onAngleChanged(countLine);
        }

        // DrawMiddleLine
        paint.setStrokeWidth(strokeWidth * DOUBLE_MULTIPLIER);
        paint.setAlpha(1);
        paint.setStyle(Paint.Style.FILL_STYLE);
        paint.setStrokeCap(Paint.StrokeCap.ROUND_CAP);
        float middleX = rulerViewRange / HALF_DIVIDER;
        float middleEndY = rulerViewHeight - middleLineToEnd;
        canvas.drawLine(new Point(middleX, middleEndY), new Point(middleX, middleEndY - middleLineLength), paint);
    }

    @Override
    public void scrollToLine(float targetScale) {
        // can not scroll if limits
        if (targetScale < userScrollStart || targetScale > userScrollEnd) {
            return;
        }
        float dx = (countLine - targetScale) * lineMargin;
        scrollBy((int) dx, 0);
    }
}