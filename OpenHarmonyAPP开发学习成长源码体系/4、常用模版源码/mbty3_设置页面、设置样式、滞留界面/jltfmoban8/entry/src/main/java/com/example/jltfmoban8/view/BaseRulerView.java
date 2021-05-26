package com.example.jltfmoban8.view;

import ohos.agp.components.AttrHelper;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.utils.Color;
import ohos.agp.utils.TextAlignment;
import ohos.app.Context;

/**
 * Base RulerView
 */
public abstract class BaseRulerView extends Component {
    private static final int RULER_END = 70;
    private static final int RULER_START = -70;
    private static final int USER_SCROLL_END = 45;
    private static final int USER_SCROLL_START = -45;
    private static final int LINE_MARGIN = 4;
    private static final int SHORT_LINE_LENGTH = 4;
    private static final int TEN_LINE_LENGTH = 8;
    private static final int SHORT_LINE_TO_BOTTOM_LENGTH = 21;
    private static final int TEN_LINE_TO_NUMBER_LENGTH = 10;
    private static final int MIDDLE_LINE_LENGTH = 18;
    private static final int MIDDLE_LINE_TO_END = 15;
    private static final int RULER_VIEW_HEIGHT = 60;
    private static final int NUMBER_TEXT_SIZE = 12;
    private static final int STROKE_WIDTH = 1;
    private static final float VALID_ALPHA = 0.9f;
    private static final float INVALID_ALPHA = 0.38f;
    /**
     * RULER VIEW HEIGHT
     */
    protected static int rulerViewHeight;
    /**
     * MIDDLE LINE TO END
     */
    protected static int middleLineToEnd;
    /**
     * MIDDLE LINE LENGTH
     */
    protected static int middleLineLength;
    /**
     * TEN LINE TO NUMBER LENGTH
     */
    protected static int tenLineToNumberLength;
    /**
     * SHORT LINE TO BOTTOM LENGTH
     */
    protected static int shortLineToBottomLength;
    /**
     * TEN LINE LENGTH
     */
    protected static int tenLineLength;
    /**
     * LINE MARGIN
     */
    protected static int lineMargin;
    /**
     * SHORT LINE LENGTH
     */
    protected static int shortLineLength;
    /**
     * font size
     */
    protected static int numberTextSize;
    /**
     * Stroke Width
     */
    protected static int strokeWidth;
    /**
     * degree change callback
     */
    protected OnRotateChangeListener rotateChangeListener;

    /**
     * max display value of view
     */
    protected int displayRulerEnd;

    /**
     * min display value of view
     */
    protected int displayRulerStart;

    /**
     * max user can scroll value of view
     */
    protected int userScrollEnd;

    /**
     * min user can scroll value of view
     */
    protected int userScrollStart;

    /**
     * already scrolled scale
     */
    protected float countLine;

    /**
     * ruler view total range
     */
    protected float rulerViewRange;

    /**
     * ruler view half range count
     */
    protected float halfRangeCount;

    /**
     * measure width of view
     */
    protected int totalWidth;

    /**
     * measure height of view
     */
    protected int totalHeight;

    /**
     * Scroller Position
     */
    protected int scrollLastX;

    /**
     * context
     */
    protected Context context;

    /**
     * font size
     */
    protected int numberFontSize;

    private Paint paint;

    /**
     * constructor
     *
     * @param context Context
     */
    public BaseRulerView(Context context) {
        this(context, null);
    }

    /**
     * constructor
     *
     * @param context Context
     * @param attrs   AttributeSet
     */
    public BaseRulerView(Context context, AttrSet attrs) {
        this(context, attrs, "");
    }

    /**
     * constructor
     *
     * @param context   Context
     * @param attrSet   AttrSet
     * @param styleName StyleName
     */
    public BaseRulerView(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
        this.context = context;
        initDpParams(context);
        initBaseParams();
        initEvent();
    }

    /**
     * VP Params to Pixel
     *
     * @param context context
     */
    private void initDpParams(Context context) {
        lineMargin = AttrHelper.vp2px(LINE_MARGIN, context);
        shortLineLength = AttrHelper.vp2px(SHORT_LINE_LENGTH, context);
        tenLineLength = AttrHelper.vp2px(TEN_LINE_LENGTH, context);
        shortLineToBottomLength = AttrHelper.vp2px(SHORT_LINE_TO_BOTTOM_LENGTH, context);
        tenLineToNumberLength = AttrHelper.vp2px(TEN_LINE_TO_NUMBER_LENGTH, context);
        rulerViewHeight = AttrHelper.vp2px(RULER_VIEW_HEIGHT, context);
        numberTextSize = AttrHelper.vp2px(NUMBER_TEXT_SIZE, context);
        strokeWidth = AttrHelper.vp2px(STROKE_WIDTH, context);
        middleLineLength = AttrHelper.vp2px(MIDDLE_LINE_LENGTH, context);
        middleLineToEnd = AttrHelper.vp2px(MIDDLE_LINE_TO_END, context);
    }

    private void initBaseParams() {
        displayRulerStart = RULER_START;
        displayRulerEnd = RULER_END;
        userScrollEnd = USER_SCROLL_END;
        userScrollStart = USER_SCROLL_START;
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE_STYLE);
        paint.setTextAlign(TextAlignment.CENTER);
        paint.setAlpha(VALID_ALPHA);
        numberFontSize = numberTextSize;
        initLayoutParam();
    }

    /**
     * Init Event
     */
    protected void initEvent() {
        this.addDrawTask((component, canvas) -> {
            updateMiddleLineDegree(canvas, paint);
            handleDrawRuler(canvas);
        });
    }

    /**
     * draw ruler
     *
     * @param canvas Canvas
     */
    private void handleDrawRuler(Canvas canvas) {
        int delta = getDelta();
        drawRuler(canvas, paint, delta);
    }

    /**
     * get Delta distance
     *
     * @return delta distance
     */
    private int getDelta() {
        return (int) ((displayRulerEnd - halfRangeCount + countLine) * lineMargin);
    }

    /**
     * view param init
     */
    protected abstract void initLayoutParam();

    /**
     * draw scale
     *
     * @param canvas canvas
     * @param paint  paint
     * @param delta  delta distance
     */
    protected abstract void drawRuler(Canvas canvas, Paint paint, int delta);

    /**
     * draw pointer
     *
     * @param canvas canvas
     * @param paint  paint
     */
    protected abstract void updateMiddleLineDegree(Canvas canvas, Paint paint);

    /**
     * scroll to target scale
     *
     * @param val value
     */
    public abstract void scrollToLine(float val);

    /**
     * set paint color
     *
     * @param paint  Paint
     * @param degree degreeValue
     */
    protected void setPaintColor(Paint paint, int degree) {
        if (degree < USER_SCROLL_START || degree > USER_SCROLL_END) {
            paint.setAlpha(INVALID_ALPHA);
        } else {
            paint.setAlpha(VALID_ALPHA);
        }
    }

    /**
     * set target scale
     *
     * @param degree target degree
     */
    public void setCurrentDegree(float degree) {
        if (degree >= userScrollStart && degree <= userScrollEnd) {
            scrollToLine(degree);
        }
    }

    /**
     * set listener
     *
     * @param listener OnRotateChangeListener
     */
    public void setOnRotateChangeListener(OnRotateChangeListener listener) {
        this.rotateChangeListener = listener;
    }

    /**
     * define interface for handle rotate photo
     */
    public interface OnRotateChangeListener {
        /**
         * angle changed callback
         *
         * @param degrees target degrees
         */
        void onAngleChanged(float degrees);
    }
}