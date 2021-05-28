package com.example.jltfmoban8.view;

import ohos.agp.components.AttrHelper;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.utils.Color;
import ohos.agp.utils.TextAlignment;
import ohos.app.Context;
import ohos.multimodalinput.event.TouchEvent;

/**
 * Slide Step Bar Land
 */
public class SlideStepBarLand extends SlideStepBar {
    private static final float HORIZONTAL_PADDING = 20;
    private static final float TEXT_GAP = 5;

    /**
     * horizontal padding
     */
    private float horizontalPadding;

    /**
     * text gap
     */
    private float textGap;

    /**
     * point's X position
     */
    private float pointX;

    /**
     * number's X position
     */
    private float textX;

    /**
     * first point position
     */
    private float pointStartY;

    /**
     * last point position
     */
    private float pointEndY;


    /**
     * constructor
     *
     * @param context Context
     */
    public SlideStepBarLand(Context context) {
        this(context, null);
    }

    /**
     * constructor
     *
     * @param context Context
     * @param attrs   AttributeSet
     */
    public SlideStepBarLand(Context context, AttrSet attrs) {
        this(context, attrs, "");
    }

    /**
     * constructor
     *
     * @param context   Context
     * @param attrSet   AttrSet
     * @param styleName styleName
     */
    public SlideStepBarLand(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
        initDpParams(context);
        initEvent();
    }

    private void initDpParams(Context context) {
        horizontalPadding = AttrHelper.vp2px(HORIZONTAL_PADDING, context);
        textGap = AttrHelper.vp2px(TEXT_GAP, context);

        width = viewHeightAll;
        height = viewWidthAll;
        pointStartY = leftOrRightPadding + specialPointRadius;
        pointEndY = height - leftOrRightPadding - specialPointRadius;
        pointX = width - horizontalPadding;
        textX = pointX - leftOrRightPadding;
    }

    private void initEvent() {
        this.setTouchEventListener((component, event) -> {
            if (event == null) {
                return true;
            }
            float positionY = event.getPointerPosition(event.getIndex()).getY();

            switch (event.getAction()) {
                case TouchEvent.PRIMARY_POINT_UP: {
                    changePressedState(false);
                    if (onSlideBarChangeListener != null) {
                        onSlideBarChangeListener.onStopTrackingTouch(SlideStepBarLand.this);
                    }
                    break;
                }
                case TouchEvent.PRIMARY_POINT_DOWN: {
                    updatePosition(positionY);
                    break;
                }
                case TouchEvent.POINT_MOVE: {
                    if (onSlideBarChangeListener != null) {
                        onSlideBarChangeListener.onStartTrackingTouch(SlideStepBarLand.this);
                    }
                    changePressedState(true);
                    updatePosition(positionY);
                    break;
                }
                default: {
                    break;
                }
            }
            callOnClick();
            return true;
        });
    }

    /**
     * draw points
     *
     * @param canvas canvas
     */
    @Override
    public void drawPoint(Canvas canvas) {
        if (canvas == null) {
            return;
        }
        pointPosition.clear();
        pointPosition.add(pointStartY);

        initPaint();

        /* End Point */
        canvas.drawColor(Color.BLACK.getValue(), Canvas.PorterDuffMode.SRC);
        canvas.drawCircle(pointX, pointStartY, specialPointRadius, paint);
        canvas.drawCircle(pointX, pointEndY, specialPointRadius, paint);

        int pointNum;
        int decorativePointNum;
        if (mode == Mode.ADJUST_MODE) {
            pointNum = ADJUST_VALID_POINT_NUM;
            decorativePointNum = 0;
        } else {
            pointNum = NORMAL_VALID_POINT_NUM;
            decorativePointNum = DECORATIVE_POINT_NUM;
        }

        /* Distance between two real points = gapDistance / (num - 1) */
        float pointGap = (pointEndY - pointStartY) / (pointNum - 1);
        /* Distance between two decorated points = gap / (num + 1) */
        float decorativePointGap = (decorativePointNum != 0) ? pointGap / (decorativePointNum + 1) : 0;

        float pointY = pointStartY;
        for (int i = 1; i < pointNum; i++) {
            for (int j = 1; j <= decorativePointNum; j++) {
                pointY = pointY + decorativePointGap;
                paint.setAlpha(ALPHA_DECORATIVE);
                canvas.drawCircle(pointX, pointY, circleStrokeWidth / HALF_DIVIDER, paint);
            }

            if (i < pointNum - 1) {
                paint.setAlpha(ALPHA_NORMAL);
                if (decorativePointNum > 0) {
                    pointY = pointY + decorativePointGap;
                } else {
                    pointY = pointY + pointGap;
                }
                canvas.drawCircle(pointX, pointY, circleStrokeWidth / HALF_DIVIDER, paint);
                pointPosition.add(pointY);
            }
        }
        pointPosition.add(pointEndY);
    }

    /**
     * Draw selected Points and Circle
     *
     * @param canvas canvas
     */
    @Override
    public void drawCircle(Canvas canvas) {
        if (canvas == null) {
            return;
        }
        float circleY = getCenterPosition();
        initPaint();
        float pointRadius = isSelected ? centerPointSelectedRadius : specialPointRadius;
        canvas.drawCircle(pointX, circleY, pointRadius, paint);
        paint.setStyle(Paint.Style.STROKE_STYLE);
        paint.setStrokeWidth(circleStrokeWidth);
        if (isSelected) {
            canvas.drawCircle(pointX, circleY, circleSelectedRadius, paint);
        } else {
            canvas.drawCircle(pointX, circleY, circleRadius, paint);
        }
    }

    /**
     * Draw SlideBar Text
     *
     * @param canvas canvas
     */
    @Override
    public void drawText(Canvas canvas) {
        if (canvas == null) {
            return;
        }
        if (isSelected) {
            return;
        }
        String centerText;
        centerText = String.valueOf((mode == Mode.ADJUST_MODE) ? level - MAX_LEVEL : level);

        initPaint();
        paint.setTextAlign(TextAlignment.CENTER);
        paint.setTextSize(subtitleTextSize);
        float centerY = getCenterPosition();
        canvas.drawText(paint, centerText, textX, centerY + textGap);
    }

    /**
     * Obtains Selected Point Y coordinate
     *
     * @return center point position
     */
    @Override
    public float getCenterPosition() {
        float centerPosition = DEFAULT_INVALID_VALUE;
        float fy = 1.0f * level / wholeLevel * (pointEndY - pointStartY) + pointStartY;
        if (fy <= pointPosition.get(0)) {
            return pointPosition.get(0);
        }
        int pointNum = pointPosition.size();
        if (fy >= pointPosition.get(pointNum - 1)) {
            return pointPosition.get(pointNum - 1);
        }
        float lastPosition = pointPosition.get(0);
        for (float position : pointPosition) {
            if ((fy >= lastPosition) && (fy <= position)) {
                centerPosition = ((fy - lastPosition) > (position - fy)) ? position : lastPosition;
                break;
            }
            lastPosition = position;
        }
        return centerPosition;
    }

    /**
     * Update Level Value
     *
     * @param fy Touch Point Y coordinate
     */
    @Override
    public void updatePosition(float fy) {
        int pointSize = pointPosition.size();
        if (pointSize <= 0) {
            return;
        }
        float centerPosition = DEFAULT_INVALID_VALUE;
        float lastPosition = pointPosition.get(0);
        if (fy < lastPosition) {
            centerPosition = lastPosition;
        } else {
            for (float position : pointPosition) {
                if ((fy >= lastPosition) && (fy <= position)) {
                    centerPosition = ((fy - lastPosition) > (position - fy)) ? position : lastPosition;
                    break;
                }
                lastPosition = position;
            }
            if (centerPosition < 0) {
                centerPosition = pointPosition.get(pointSize - 1);
            }
        }
        int level = Math.round((centerPosition - pointStartY) / (pointEndY - pointStartY) * wholeLevel);
        updateLevel(level);
    }
}
