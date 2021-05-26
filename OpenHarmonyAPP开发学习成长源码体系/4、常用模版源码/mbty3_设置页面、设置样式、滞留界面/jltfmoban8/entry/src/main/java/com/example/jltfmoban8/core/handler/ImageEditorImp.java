package com.example.jltfmoban8.core.handler;

import com.example.jltfmoban8.MyApplication;
import com.example.jltfmoban8.core.strategy.IEditAction;
import com.example.jltfmoban8.core.strategy.IEditStrategy;
import com.example.jltfmoban8.core.strategy.IImageEditor;
import com.example.jltfmoban8.core.exceptions.EditActionException;
import com.example.jltfmoban8.core.utils.ImageLoader;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.image.PixelMap;

import java.util.LinkedList;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * ImageEditor implementation
 */
public class ImageEditorImp implements IImageEditor {
    private static final HiLogLabel EDIT_EXCEPTIONS = new HiLogLabel(1, 1, "IMAGE EDIT IMP");
    private String originImgPath;
    private PixelMap originPixelMap;
    private EditStrategyManager editStrategyManager = new EditStrategyManager();

    private LinkedList<IEditAction> actionStack = new LinkedList<>();
    private int lastAction = 0;

    private volatile EditActionFactory editActionFactory;

    private PixelMap previewBmp;
    private Map<IEditAction, PixelMap> bmpCache = new WeakHashMap<>();

    /**
     * ImageEditor Constructor
     *
     * @param imgPath Image file path
     */
    public ImageEditorImp(String imgPath) {
        try {
            setImageSource(imgPath);
            getEditActionFactory();
        } catch (EditActionException ignored) {
            HiLog.error(EDIT_EXCEPTIONS, "SET IMAGE SOURCE EXCEPTION", "");
        }
    }

    @Override
    public void setImageSource(String imgPath) throws EditActionException {
        if (previewBmp != null) {
            throw new EditActionException("ImageSource already set");
        }
        originImgPath = imgPath;
        reset();
    }

    @Override
    public void addEditStrategy(IEditStrategy strategy) throws EditActionException {
        editStrategyManager.addStrategy(strategy);
    }

    private EditActionFactory getEditActionFactory() {
        if (editActionFactory == null) {
            synchronized (ImageEditorImp.class) {
                editActionFactory = new EditActionFactory(editStrategyManager);
            }
        }
        return editActionFactory;
    }

    @Override
    public void applyAction(IEditAction act) throws EditActionException {
        if (isRedoable()) {
            cleanRedoActions();
        }
        processAction(act);
    }

    private void processAction(IEditAction editAction)
            throws EditActionException {
        PixelMap srcBmp = previewBmp;
        PixelMap outBmp = editAction.execute(srcBmp);
        pushToStack(editAction);
        bmpCache.put(editAction, outBmp);
        previewBmp = outBmp;
    }

    private void cleanRedoActions() {
        final int size = actionStack.size();
        for (int i = lastAction + 1; i <= size; i++) {
            IEditAction act = actionStack.removeLast();
            PixelMap bmp = bmpCache.get(act);
        }
    }

    private void pushToStack(IEditAction act) {
        actionStack.add(act);
        lastAction++;
    }

    /**
     * Check if editor can process redo action
     *
     * @return True if redoable, otherwise False
     */
    public boolean isRedoable() {
        return lastAction < actionStack.size();
    }

    /**
     * Check if editor can process undo action
     *
     * @return True if undoable, otherwise False
     */
    public boolean isUndoable() {
        return lastAction > 0;
    }

    @Override
    public void redo() {
        if (!isRedoable()) {
            return;
        }

        IEditAction act = actionStack.get(lastAction++);
        previewBmp = bmpCache.get(act);
    }

    @Override
    public void undo() {
        if (!isUndoable()) {
            return;
        }
        if (--lastAction > 0) {
            IEditAction action = actionStack.get(lastAction - 1);
            previewBmp = bmpCache.get(action);
        } else {
            previewBmp = getOriginPixelMap();
        }
    }

    private PixelMap getOriginPixelMap() {
        if (originPixelMap == null) {
            originPixelMap = ImageLoader.getPixelMap(MyApplication.getAbilityContext(), originImgPath);
        }
        return originPixelMap;
    }

    @Override
    public PixelMap export() {
        return previewBmp;
    }

    @Override
    public void drop() {
        reset();
    }

    /**
     * reset previewBmp
     *
     * @param pixelMap new previewBmp
     */
    public void reSetPreviewBmp(PixelMap pixelMap) {
        this.previewBmp = pixelMap;
    }

    private void reset() {
        previewBmp = getOriginPixelMap();

        lastAction = 0;
        actionStack.clear();

        bmpCache.clear();
    }
}
