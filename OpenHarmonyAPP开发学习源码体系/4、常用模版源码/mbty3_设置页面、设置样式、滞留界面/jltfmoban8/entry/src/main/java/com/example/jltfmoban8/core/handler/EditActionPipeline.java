package com.example.jltfmoban8.core.handler;

import com.example.jltfmoban8.core.strategy.IEditAction;
import com.example.jltfmoban8.core.strategy.IImageEditor;
import com.example.jltfmoban8.core.exceptions.EditActionException;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;
import ohos.media.image.PixelMap;

import static com.example.jltfmoban8.core.strategy.IEditAction.ActionType.ACT_EDIT;

/**
 * Image Edit Pipeline
 * ImageEditorProxy use this pipeline to arrange edit actions
 */
public final class EditActionPipeline extends EventHandler {
    private IImageEditor imageEditor;
    private EditPipelineListener editPipelineListener;
    /**
     * Pipeline Constructor
     *
     * @param eventRunner Pipeline eventRunner
     * @param imageEditor ImageEditor instance
     */
    protected EditActionPipeline(EventRunner eventRunner, IImageEditor imageEditor) {
        super(eventRunner);
        this.imageEditor = imageEditor;
    }

    /**
     * Bind Action Listener
     *
     * @param listener Pipeline action listener
     */
    protected void setOnEditActionListener(EditPipelineListener listener) {
        editPipelineListener = listener;
    }

    @Override
    public void processEvent(InnerEvent event) {
        if (!(event.object instanceof IEditAction)) {
            return;
        }
        IEditAction act = (IEditAction) event.object;
        try {
            if (act.getActionType() == ACT_EDIT) {
                imageEditor.applyAction(act);
                notifyEditResult(act, null);
            } else {
                act.setParams(imageEditor);
                PixelMap output = act.execute(null);
                notifyStateUpdate(act, output);
            }
        } catch (EditActionException e) {
            notifyEditResult(act, e);
        }
    }

    private void notifyEditResult(IEditAction action, EditActionException error) {
        if (editPipelineListener == null) {
            return;
        }

        final boolean isUndoable = imageEditor.isUndoable();
        final boolean isRedoable = imageEditor.isRedoable();

        if (error != null) {
            editPipelineListener.onEditActionDone(action, error);
        }
        editPipelineListener.onStateUpdate(imageEditor.export(), isUndoable, isRedoable, action);
    }

    private void notifyStateUpdate(IEditAction action, PixelMap preview) {
        if (editPipelineListener == null) {
            return;
        }
        editPipelineListener.onStateUpdate(preview, imageEditor.isUndoable(), imageEditor.isRedoable(), action);
    }

    interface EditPipelineListener {
        /**
         * Image edit status update
         *
         * @param preview    Image edit preview
         * @param isUndoable Image edit step undoable
         * @param isRedoable Image edit step redoable
         * @param act        Image edit action
         */
        void onStateUpdate(PixelMap preview, boolean isUndoable, boolean isRedoable, IEditAction act);

        /**
         * Image edit completed.
         *
         * @param act Image edit action
         * @param e   Image edit action exception
         */
        void onEditActionDone(IEditAction act, EditActionException e);
    }
}
