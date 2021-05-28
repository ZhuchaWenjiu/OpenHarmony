package com.example.jltfmoban8.view;

import com.example.jltfmoban8.core.utils.ImageEditUtils;
import ohos.agp.components.AttrHelper;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.render.PixelMapHolder;
import ohos.agp.utils.Rect;
import ohos.agp.utils.RectFloat;
import ohos.app.Context;
import ohos.media.image.PixelMap;

/**
 * BackGroundLayer
 */
public class BackGroundLayer extends Component {
    private static final int DISPLAY_PADDING_LEFT = 30;
    private static final int DISPLAY_PADDING_TOP = 24;
    private static final int DISPLAY_PADDING_RIGHT = 30;
    private static final int DISPLAY_PADDING_BOTTOM = 24;
    private final Rect contentRect = new Rect();
    private final RectFloat currentViewport = new RectFloat();
    private PixelMap previewImg;
    private OnUpdateListener updateListener;
    private Rect boundRect;

    /**
     * BackGround Layer Constructor
     *
     * @param context context
     * @param attrSet attrSet
     */
    public BackGroundLayer(Context context, AttrSet attrSet) {
        super(context, attrSet);
        LayoutRefreshedListener refreshedListener = new RefreshedListener();
        this.setLayoutRefreshedListener(refreshedListener);
        DrawTask drawTask = new AnimationDrawTask();
        this.addDrawTask(drawTask);
    }

    private void updateViewport() {
        if (previewImg == null || boundRect == null) {
            return;
        }
        float scaleFactor = ImageEditUtils.fitSrcImageToBounds(previewImg, boundRect, contentRect);
        currentViewport.modify(contentRect);
        updateViewport(currentViewport, scaleFactor);
    }

    private void updateViewport(RectFloat viewport, float scaleFactor) {
        if (updateListener != null) {
            updateListener.onUpdate(contentRect, viewport, scaleFactor);
        }
    }

    /**
     * Obtains Preview PixelMap
     *
     * @return Preview PixelMap
     */
    public PixelMap getPreviewImage() {
        return previewImg;
    }

    /**
     * Set Preview PixelMap
     *
     * @param pixelMap Preview PixelMap
     */
    public void setPreviewImage(PixelMap pixelMap) {
        if (pixelMap != null) {
            previewImg = pixelMap;
            currentViewport.clear();
            updateViewport();
            invalidate();
        }
    }

    /**
     * Obtains Content Rect
     *
     * @return Content Rect
     */
    public Rect getContentRect() {
        return contentRect;
    }

    /**
     * Set OnUpdateListener
     *
     * @param listener OnUpdateListener
     */
    public void setOnUpdateListener(OnUpdateListener listener) {
        updateListener = listener;
    }

    /**
     * OnUpdateListener
     */
    public interface OnUpdateListener {
        /**
         * This method will be called when BackgroundLayer update
         *
         * @param baselineRect Content Rect
         * @param viewport     Viewport
         * @param scaleFactor  Viewport ScaleFactor
         */
        void onUpdate(Rect baselineRect, RectFloat viewport, float scaleFactor);
    }

    private class RefreshedListener implements LayoutRefreshedListener {
        @Override
        public void onRefreshed(Component component) {
            int left = component.getLeft();
            int top = component.getTop();
            int right = component.getRight();
            int bottom = component.getBottom();
            Context ctx = getContext();
            boundRect = new Rect(left + AttrHelper.vp2px(DISPLAY_PADDING_LEFT, ctx),
                    top + AttrHelper.vp2px(DISPLAY_PADDING_TOP, ctx),
                    right - AttrHelper.vp2px(DISPLAY_PADDING_RIGHT, ctx),
                    bottom - AttrHelper.vp2px(DISPLAY_PADDING_BOTTOM, ctx));
            updateViewport();
        }
    }

    private class AnimationDrawTask implements DrawTask {
        @Override
        public void onDraw(Component component, Canvas canvas) {
            if (previewImg == null) {
                return;
            }
            Paint paint = new Paint();
            canvas.save();
            PixelMapHolder mPrefix = new PixelMapHolder(previewImg);
            canvas.drawPixelMapHolderRect(mPrefix, currentViewport, paint);
            canvas.restore();
        }
    }
}
