package com.example.jltfmoban8.view;

import ohos.agp.components.AttrHelper;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.render.Paint;
import ohos.agp.utils.Color;
import ohos.agp.utils.Rect;
import ohos.app.Context;

/**
 * Shadow Text View
 */
public class ShadowTextView extends Component {
    private static final int DEFAULT_TEXT_SIZE = 50;
    private static final int HALF_DIVIDER = 2;
    private Paint shadowPaint;
    private Paint textPaint;
    private int viewAllHeight;
    private int viewAllWidth;
    private String textContent;
    private int textSize;
    private Color textColor = Color.WHITE;
    private Color shadowColor = Color.GRAY;
    private float shadowX = 2.5f;
    private float shadowY = 2.5f;
    private float shadowAlpha = 0.3f;

    public ShadowTextView(Context context) {
        this(context, null);
    }

    public ShadowTextView(Context context, AttrSet attrSet) {
        this(context, attrSet, "");
    }

    public ShadowTextView(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
        initParams(context);
        initTextPaint();
        initShadowPaint();
        initEvent();
    }

    private void initParams(Context context) {
        textSize = AttrHelper.fp2px(DEFAULT_TEXT_SIZE, context);
    }

    private void initTextPaint() {
        textPaint = new Paint();
        textPaint.setAntiAlias(false);
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setStyle(Paint.Style.FILL_STYLE);
    }

    private void initShadowPaint() {
        shadowPaint = new Paint();
        shadowPaint.setAntiAlias(false);
        shadowPaint.setColor(shadowColor);
        shadowPaint.setAlpha(shadowAlpha);
        shadowPaint.setTextSize(textSize);
        shadowPaint.setStrokeWidth(Math.max(shadowX, shadowY));
        shadowPaint.setStyle(Paint.Style.FILLANDSTROKE_STYLE);
    }

    private void initEvent() {
        this.setLayoutRefreshedListener(component -> {
            viewAllWidth = component.getWidth();
            viewAllHeight = component.getHeight();
        });
        this.addDrawTask((component, canvas) -> {
            Rect textBounds = textPaint.getTextBounds(textContent);
            Paint.FontMetrics fontMetrics = shadowPaint.getFontMetrics();
            float centerX = ((float) (viewAllWidth - textBounds.getWidth())) / HALF_DIVIDER;
            float centerY = ((float) (viewAllHeight - textBounds.getHeight())) / HALF_DIVIDER;
            float baseY = centerY - fontMetrics.bottom / HALF_DIVIDER - fontMetrics.top / HALF_DIVIDER;
            canvas.drawText(shadowPaint, textContent, centerX + shadowX, baseY + shadowY);
            canvas.drawText(textPaint, textContent, centerX, baseY);
        });
    }

    /**
     * set text
     *
     * @param text text content
     */
    public void setText(String text) {
        textContent = text;
        invalidate();
    }

    /**
     * set text color
     *
     * @param textColor text color
     */
    public void setTextColor(Color textColor) {
        this.textColor = textColor;
        initTextPaint();
        invalidate();
    }

    /**
     * set text size
     *
     * @param fpTextSize textSize(fp)
     */
    public void setTextSize(int fpTextSize) {
        textSize = AttrHelper.fp2px(fpTextSize, getContext());
        initTextPaint();
        initShadowPaint();
        invalidate();
    }

    /**
     * set shadow text params
     *
     * @param pxShadowX   shadow offset x(px)
     * @param pxShadowY   shadow offset y(px)
     * @param shadowColor shadow color
     * @param shadowAlpha shadow alpha
     */
    public void setShadowText(float pxShadowX, float pxShadowY, Color shadowColor, float shadowAlpha) {
        this.shadowX = pxShadowX;
        this.shadowY = pxShadowY;
        this.shadowColor = shadowColor;
        this.shadowAlpha = shadowAlpha;
        initShadowPaint();
        invalidate();
    }
}