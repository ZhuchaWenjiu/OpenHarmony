package com.example.jltfmoban8.view;

import com.example.jltfmoban8.core.exceptions.CropPickerException;
import com.example.jltfmoban8.core.handler.ImageEditorProxy;

import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.utils.RectFloat;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.image.PixelMap;
import ohos.media.image.common.Rect;

import java.util.ArrayList;
import java.util.List;

/**
 * Image Edit View
 */
public class ImageEditView extends ComponentContainer {
    private static final HiLogLabel CROP_PICKER_EXCEPTIONS = new HiLogLabel(1, 1, "Element Loader");
    private final List<UpdateInterceptor> interceptors = new ArrayList<>();
    private EditorAdapter adapter;
    private BackGroundLayer backgroundLayer;
    private ImagePicker imagePicker;

    public ImageEditView(Context context) {
        super(context);
        initView(context, null);
    }

    public ImageEditView(Context context, AttrSet attrSet) {
        super(context, attrSet);
        initView(context, attrSet);
    }

    public ImageEditView(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
        initView(context, attrSet);
    }

    private void initView(Context ctx, AttrSet attrs) {
        if (adapter == null) {
            return;
        }
        backgroundLayer = adapter.getBackgroundView(ctx, attrs);
        imagePicker = adapter.getImagePicker(ctx, attrs);
        backgroundLayer.setOnUpdateListener(getBackgroundListener(imagePicker));
        this.addComponent(backgroundLayer);
        this.addComponent(imagePicker);
        this.setLayoutRefreshedListener(new RefreshedListener());
    }

    /**
     * Obtains Edit Rect
     *
     * @return Edit Rect
     */
    public Rect getEditRect() {
        RectFloat cropRatioRect = imagePicker.getPickRatioRect();
        PixelMap srcBmp = backgroundLayer.getPreviewImage();
        float width = srcBmp.getImageInfo().size.width;
        float height = srcBmp.getImageInfo().size.height;
        return new Rect(Math.round(cropRatioRect.left * width),
                Math.round(cropRatioRect.top * height),
                Math.round(cropRatioRect.right * width),
                Math.round(cropRatioRect.bottom * height));
    }

    public void setAdapter(EditorAdapter adapter) {
        this.adapter = adapter;
        initView(getContext(), null);
        setEditProxy(this.adapter.getImageEditorProxy());
        this.adapter.getImageEditorProxy().export();
    }

    private void setEditProxy(ImageEditorProxy editorProxy) {
        editorProxy.addObserver((proxy, data) -> {
            for (UpdateInterceptor interceptor : interceptors) {
                if (!interceptor.preUpdate(data)) {
                    return;
                }
            }
            handleStateUpdate(data);
            for (UpdateInterceptor interceptor : interceptors) {
                interceptor.postUpdate(data);
            }
        });
    }

    private void handleStateUpdate(Object data) {
        if (data instanceof ImageEditorProxy.EditorState) {
            final ImageEditorProxy.EditorState editorState = (ImageEditorProxy.EditorState) data;
            handleEditorStateUpdate(editorState);
        }
    }

    private void handleEditorStateUpdate(final ImageEditorProxy.EditorState editorState) {
        PixelMap srcBmp = editorState.previewPixelMap;
        PixelMap current = backgroundLayer.getPreviewImage();
        if (current != null && current.isSameImage(srcBmp)) {
            return;
        }
        backgroundLayer.setPreviewImage(srcBmp);
    }

    private BackGroundLayer.OnUpdateListener getBackgroundListener(ImagePicker imagePicker) {
        return (baselineRect, viewport, scaleRatio) -> {
            try {
                imagePicker.update(baselineRect);
            } catch (CropPickerException e) {
                HiLog.error(CROP_PICKER_EXCEPTIONS, "CROP PICK UPDATE ERROR", "");
            }
        };
    }

    public BackGroundLayer getBackgroundLayer() {
        return backgroundLayer;
    }

    public ImagePicker getImagePicker() {
        return imagePicker;
    }

    /**
     * Update Interceptor
     */
    public interface UpdateInterceptor {
        default boolean preUpdate(Object data) {
            // Do Something after update
            return false;
        }

        default void postUpdate(Object data) {
            // Do Something before update
        }
    }

    /**
     * Editor Adapter
     */
    public abstract static class EditorAdapter {
        /**
         * Obtains Image Editor Proxy
         *
         * @return Image Editor Proxy
         */
        protected abstract ImageEditorProxy getImageEditorProxy();

        /**
         * Obtains Background Layer
         *
         * @param ctx   context
         * @param attrs AttributeSet
         * @return Background Layer Instance
         */
        protected BackGroundLayer getBackgroundView(Context ctx, AttrSet attrs) {
            return new BackGroundLayer(ctx, attrs);
        }

        /**
         * Obtains ImagePicker
         *
         * @param ctx   context
         * @param attrs AttributeSet
         * @return ImagePicker
         */
        protected ImagePicker getImagePicker(Context ctx, AttrSet attrs) {
            return new CropPicker(ctx, attrs);
        }
    }

    private class RefreshedListener implements LayoutRefreshedListener {
        @Override
        public void onRefreshed(Component component) {
            int left = component.getLeft();
            int right = component.getRight();
            int top = component.getTop();
            int bottom = component.getBottom();
            final int width = right - left;
            final int height = bottom - top;

            // Overlap all children views in z-order
            final int count = getChildCount();
            for (int i = 0; i < count; i++) {
                final Component child = getComponentAt(i);
                if (child.getVisibility() != HIDE) {
                    child.setComponentPosition(0, 0, width, height);
                }
            }
        }
    }
}
