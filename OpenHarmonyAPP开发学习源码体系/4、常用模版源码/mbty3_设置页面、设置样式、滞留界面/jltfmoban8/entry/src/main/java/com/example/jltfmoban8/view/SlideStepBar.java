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

import java.util.ArrayList;

/**
 * Slide Step Bar
 */
public class SlideStepBar extends Component {
    /**
     * MAX LEVEL
     */
    public static final int MAX_LEVEL = 10;
    /**
     * valid point alpha 0.9
     */
    protected static final float ALPHA_NORMAL = 0.9f;
    /**
     * decorative point alpha 0.2
     */
    protected static final float ALPHA_DECORATIVE = 0.2f;
    /**
     * NORMAL_MODE，valid point num (0--10)
     */
    protected static final int NORMAL_VALID_POINT_NUM = 11;
    /**
     * ADJUST_MODE，valid point num (-10--10)
     */
    protected static final int ADJUST_VALID_POINT_NUM = 21;
    /**
     * decorative point num between two valid points 3
     */
    protected static final int DECORATIVE_POINT_NUM = 3;
    /**
     * HALF DIVIDER
     */
    protected static final float HALF_DIVIDER = 2.0f;
    /**
     * default invalid value -1
     */
    protected static final int DEFAULT_INVALID_VALUE = -1;
    private static final float CENTER_POINT_SELECTED_RADIUS = 5.0f;
    private static final float SPECIAL_POINT_RADIUS = 3.0f;
    private static final float CIRCLE_RADIUS = 24.0f;
    private static final float CIRCLE_SELECTED_RADIUS = 36.0f;
    private static final float CIRCLE_STROKE_WIDTH = 1.5f;
    private static final int LEFT_OR_RIGHT_PADDING = 24;
    private static final int SUBTITLE1_TEXT_SIZE = 12;
    private static final float VIEW_WIDTH_ALL = 296.0f;
    private static final float VIEW_HEIGHT_ALL = 64.0f;
    private static final float POINT_Y_HEIGHT = 24.0f;
    private static final float TEXT_Y_HEIGHT = 20.0f;
    private static final float DEFAULT_POINT_GAP = 4.0f;
    /**
     * default point gap
     */
    protected static float defaultPointGap;
    /**
     * Text's yHeight
     */
    protected static float textYHeight;
    /**
     * point's yHeight
     */
    protected static float pointYHeight;
    /**
     * view height all
     */
    protected static float viewHeightAll;
    /**
     * view width all
     */
    protected static float viewWidthAll;
    /**
     * SUBTITLE1 text size
     */
    protected static int subtitleTextSize;
    /**
     * left or right padding
     */
    protected static int leftOrRightPadding;
    /**
     * decorative point radius 1.5dp
     */
    protected static float circleStrokeWidth;
    /**
     * circle selected radius 18dp
     */
    protected static float circleSelectedRadius;
    /**
     * circle normal radius 12dp
     */
    protected static float circleRadius;
    /**
     * center point normal radius 1.5dp
     */
    protected static float specialPointRadius;
    /**
     * center point selected radius 2.5dp
     */
    protected static float centerPointSelectedRadius;
    /**
     * seek bar change listener
     */
    protected OnSlideBarChangeListener onSlideBarChangeListener;
    /**
     * gap between adjacent points
     */
    protected float pointGap;
    /**
     * paint of slide bar
     */
    protected Paint paint = new Paint();
    /**
     * points`s X position list
     */
    protected ArrayList<Float> pointPosition = new ArrayList<>();
    /**
     * Present level
     */
    protected int level = MAX_LEVEL / (int) HALF_DIVIDER;
    /**
     * max level
     */
    protected int wholeLevel = MAX_LEVEL;
    /**
     * is selected or not
     */
    protected boolean isSelected = false;
    /**
     * SlideBar Mode
     */
    protected Mode mode = Mode.NORMAL_MODE;
    /**
     * SlideBar width
     */
    protected float width;
    /**
     * SlideBar height
     */
    protected float height;
    /**
     * context
     */
    private Context context;
    /**
     * paint color
     */
    private Color color = Color.WHITE;
    /**
     * point's Y position
     */
    private float pointY;
    /**
     * number's Y position
     */
    private float textY;
    /**
     * first point position
     */
    private float pointStartX;
    /**
     * last point position
     */
    private float pointEndX;
    /**
     * constructor
     *
     * @param context Context
     */
    public SlideStepBar(Context context) {
        this(context, null);
    }

    /**
     * constructor
     *
     * @param context Context
     * @param attrs   AttributeSet
     */
    public SlideStepBar(Context context, AttrSet attrs) {
        this(context, attrs, "");
    }

    /**
     * constructor
     *
     * @param context   Context
     * @param attrSet   AttrSet
     * @param styleName styleName
     */
    public SlideStepBar(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
        this.context = context;
        initDpParams(context);
        initEvent();
    }

    private void initDpParams(Context context) {
        centerPointSelectedRadius = AttrHelper.vp2px(CENTER_POINT_SELECTED_RADIUS, context) / HALF_DIVIDER;
        specialPointRadius = AttrHelper.vp2px(SPECIAL_POINT_RADIUS, context) / HALF_DIVIDER;
        circleRadius = AttrHelper.vp2px(CIRCLE_RADIUS, context) / HALF_DIVIDER;
        circleSelectedRadius = AttrHelper.vp2px(CIRCLE_SELECTED_RADIUS, context) / HALF_DIVIDER;
        circleStrokeWidth = AttrHelper.vp2px(CIRCLE_STROKE_WIDTH, context);
        leftOrRightPadding = AttrHelper.vp2px(LEFT_OR_RIGHT_PADDING, context);
        subtitleTextSize = AttrHelper.vp2px(SUBTITLE1_TEXT_SIZE, context);
        viewWidthAll = AttrHelper.vp2px(VIEW_WIDTH_ALL, context);
        viewHeightAll = AttrHelper.vp2px(VIEW_HEIGHT_ALL, context);
        pointYHeight = AttrHelper.vp2px(POINT_Y_HEIGHT, context);
        textYHeight = AttrHelper.vp2px(TEXT_Y_HEIGHT, context);
        defaultPointGap = AttrHelper.vp2px(DEFAULT_POINT_GAP, context);

        width = viewWidthAll;
        height = viewHeightAll;
        pointY = height - pointYHeight;
        textY = pointY - textYHeight;
        pointGap = defaultPointGap;
        pointEndX = width - leftOrRightPadding - specialPointRadius;
        pointStartX = leftOrRightPadding + specialPointRadius;
    }

    private void initEvent() {
        this.setTouchEventListener((component, event) -> {
            if (event == null) {
                return true;
            }
            float positionX = event.getPointerPosition(event.getIndex()).getX();

            switch (event.getAction()) {
                case TouchEvent.PRIMARY_POINT_UP: {
                    changePressedState(false);
                    if (onSlideBarChangeListener != null) {
                        onSlideBarChangeListener.onStopTrackingTouch(SlideStepBar.this);
                    }
                    break;
                }
                case TouchEvent.PRIMARY_POINT_DOWN: {
                    updatePosition(positionX);
                    break;
                }
                case TouchEvent.POINT_MOVE: {
                    if (onSlideBarChangeListener != null) {
                        onSlideBarChangeListener.onStartTrackingTouch(SlideStepBar.this);
                    }
                    changePressedState(true);
                    updatePosition(positionX);
                    break;
                }
                default: {
                    break;
                }
            }
            callOnClick();
            return true;
        });

        this.setLongClickedListener(new LongClickedListener() {
            @Override
            public void onLongClicked(Component component) {
                changePressedState(true);
            }
        });

        this.addDrawTask(new ViewDrawTask());
    }

    /**
     * set mOnSlideBarChangeListener
     *
     * @param listener OnSlideBarChangeListener
     */
    public void setOnSlideBarChangeListener(OnSlideBarChangeListener listener) {
        this.onSlideBarChangeListener = listener;
    }

    /**
     * change selected state
     *
     * @param isPressed selected or not
     */
    protected void changePressedState(boolean isPressed) {
        if (isSelected != isPressed) {
            isSelected = isPressed;
            if (onSlideBarChangeListener != null) {
                onSlideBarChangeListener.longPressedStateChanged(isPressed);
            }
            invalidate();
        }
    }

    /**
     * draw points
     *
     * @param canvas canvas
     */
    public void drawPoint(Canvas canvas) {
        if (canvas == null) {
            return;
        }
        pointPosition.clear();
        pointPosition.add(pointStartX);

        initPaint();

        /* End Point */
        canvas.drawColor(Color.BLACK.getValue(), Canvas.PorterDuffMode.SRC);
        canvas.drawCircle(pointEndX, pointY, specialPointRadius, paint);
        canvas.drawCircle(pointStartX, pointY, specialPointRadius, paint);

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
        float pGap = (pointEndX - pointStartX) / (pointNum - 1);
        /* Distance between two decorated points = gap / (num + 1) */
        float decorativePointGap = (decorativePointNum != 0) ? pGap / (decorativePointNum + 1) : 0;

        float pointX = pointStartX;
        for (int i = 1; i < pointNum; i++) {
            for (int j = 1; j <= decorativePointNum; j++) {
                pointX = pointX + decorativePointGap;
                paint.setAlpha(ALPHA_DECORATIVE);
                canvas.drawCircle(pointX, pointY, circleStrokeWidth / HALF_DIVIDER, paint);
            }

            if (i < pointNum - 1) {
                paint.setAlpha(ALPHA_NORMAL);
                if (decorativePointNum > 0) {
                    pointX = pointX + decorativePointGap;
                } else {
                    pointX = pointX + pGap;
                }
                canvas.drawCircle(pointX, pointY, circleStrokeWidth / HALF_DIVIDER, paint);
                pointPosition.add(pointX);
            }
        }
        pointPosition.add(pointEndX);
    }

    /**
     * Draw selected Points and Circle
     *
     * @param canvas canvas
     */
    public void drawCircle(Canvas canvas) {
        if (canvas == null) {
            return;
        }
        float circleX = getCenterPosition();
        initPaint();
        float pointRadius = isSelected ? centerPointSelectedRadius : specialPointRadius;
        canvas.drawCircle(circleX, pointY, pointRadius, paint);
        paint.setStyle(Paint.Style.STROKE_STYLE);
        paint.setStrokeWidth(circleStrokeWidth);
        if (isSelected) {
            canvas.drawCircle(circleX, pointY, circleSelectedRadius, paint);
        } else {
            canvas.drawCircle(circleX, pointY, circleRadius, paint);
        }
    }

    /**
     * Draw SlideBar Text
     *
     * @param canvas canvas
     */
    protected void drawText(Canvas canvas) {
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
        float centerX = getCenterPosition();
        canvas.drawText(paint, centerText, centerX, textY);
    }

    /**
     * Initialize Paint
     */
    protected void initPaint() {
        paint.reset();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_STYLE);
        paint.setColor(color);
        paint.setAlpha(ALPHA_NORMAL);
    }

    /**
     * Obtains Selected Point X coordinate
     *
     * @return center point position
     */
    public float getCenterPosition() {
        float centerPosition = DEFAULT_INVALID_VALUE;
        float fx = 1.0f * level / wholeLevel * (pointEndX - pointStartX) + pointStartX;
        if (fx <= pointPosition.get(0)) {
            return pointPosition.get(0);
        }
        int pointNum = pointPosition.size();
        if (fx >= pointPosition.get(pointNum - 1)) {
            return pointPosition.get(pointNum - 1);
        }
        float lastPosition = pointPosition.get(0);
        for (float position : pointPosition) {
            if ((fx >= lastPosition) && (fx <= position)) {
                centerPosition = ((fx - lastPosition) > (position - fx)) ? position : lastPosition;
                break;
            }
            lastPosition = position;
        }
        return centerPosition;
    }

    /**
     * Update Level Value
     *
     * @param fx Touch Point X coordinate
     */
    public void updatePosition(float fx) {
        int pointSize = pointPosition.size();
        if (pointSize <= 0) {
            return;
        }
        float centerPosition = DEFAULT_INVALID_VALUE;
        float lastPosition = pointPosition.get(0);
        if (fx < lastPosition) {
            centerPosition = lastPosition;
        } else {
            for (float position : pointPosition) {
                if ((fx >= lastPosition) && (fx <= position)) {
                    centerPosition = ((fx - lastPosition) > (position - fx)) ? position : lastPosition;
                    break;
                }
                lastPosition = position;
            }
            if (centerPosition < 0) {
                centerPosition = pointPosition.get(pointSize - 1);
            }
        }
        int slideLevel = Math.round((centerPosition - pointStartX) / (pointEndX - pointStartX) * wholeLevel);
        updateLevel(slideLevel);
    }

    /**
     * Update Level
     *
     * @param level level
     */
    protected void updateLevel(int level) {
        int lastLevel = this.level;
        this.level = Math.max(level, 0);
        this.level = Math.min(this.level, wholeLevel);
        if (lastLevel != this.level && onSlideBarChangeListener != null) {
            onSlideBarChangeListener.onProgressChanged(this);
            invalidate();
        }
    }

    /**
     * set SlideBar mode
     *
     * @param mode Mode
     */
    public void setMode(Mode mode) {
        this.mode = mode;
        switch (this.mode) {
            case NORMAL_MODE: {
                wholeLevel = MAX_LEVEL;
                level = MAX_LEVEL / (int) HALF_DIVIDER;
                break;
            }
            case ADJUST_MODE: {
                wholeLevel = MAX_LEVEL * (int) HALF_DIVIDER;
                level = MAX_LEVEL;
                break;
            }
            case SHARPEN_MODE: {
                wholeLevel = MAX_LEVEL;
                level = 0;
                break;
            }
            default: {
                // do nothing
                break;
            }
        }
        invalidate();
    }

    /**
     * get SlideBar current mode
     *
     * @return Mode
     */
    public Mode getCurrentMode() {
        return mode;
    }

    /**
     * get max value
     * 1. NORMAL MODE 10
     * 2. ADJUST MODE 20
     * 3. SHARPEN MODE 10
     *
     * @return max value
     */
    public int getMax() {
        return wholeLevel;
    }

    /**
     * Set Progress Value
     *
     * @param progress Progress Value
     */
    public void setProgressValue(int progress) {
        updateLevel(progress);
    }

    /**
     * get progress
     * 1. NORMAL MODE 0--10
     * 2. ADJUST MODE 0--20
     * 3. SHARPEN MODE 0--10
     *
     * @return level
     */
    public int getProgress() {
        return level;
    }

    /**
     * Mode Enum
     */
    public enum Mode {
        /**
         * adjust
         */
        ADJUST_MODE,
        /**
         * sharpen
         */
        SHARPEN_MODE,
        /**
         * normal
         */
        NORMAL_MODE
    }

    /**
     * define interface for handle adjust slideBar
     */
    public interface OnSlideBarChangeListener {
        /**
         * on progress changed
         *
         * @param slideStepBar SlideBar
         */
        void onProgressChanged(SlideStepBar slideStepBar);

        /**
         * on start tracking touch
         *
         * @param slideStepBar SlideBar
         */
        void onStartTrackingTouch(SlideStepBar slideStepBar);

        /**
         * on stop tracking touch
         *
         * @param slideStepBar SlideBar
         */
        void onStopTrackingTouch(SlideStepBar slideStepBar);

        /**
         * long pressed state changed
         *
         * @param isLongPressed boolean
         */
        void longPressedStateChanged(boolean isLongPressed);
    }

    private class ViewDrawTask implements DrawTask {
        @Override
        public void onDraw(Component component, Canvas canvas) {
            drawPoint(canvas);
            drawCircle(canvas);
            drawText(canvas);
        }
    }

}
