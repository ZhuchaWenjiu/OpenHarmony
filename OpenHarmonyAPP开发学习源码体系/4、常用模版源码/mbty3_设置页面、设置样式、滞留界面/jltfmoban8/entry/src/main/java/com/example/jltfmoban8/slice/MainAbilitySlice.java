package com.example.jltfmoban8.slice;

import com.example.jltfmoban8.ResourceTable;
import com.example.jltfmoban8.core.animation.FilterNameShowAnimation;
import com.example.jltfmoban8.core.exceptions.CropPickerException;
import com.example.jltfmoban8.core.exceptions.EditActionException;
import com.example.jltfmoban8.core.handler.EditActionFactory;
import com.example.jltfmoban8.core.handler.ImageEditorImp;
import com.example.jltfmoban8.core.handler.ImageEditorProxy;
import com.example.jltfmoban8.core.strategy.IEditAction;
import com.example.jltfmoban8.core.strategy.action.BatchEditAction;
import com.example.jltfmoban8.core.strategy.action.StateAction;
import com.example.jltfmoban8.core.strategy.bean.AdjustStrategyParams;
import com.example.jltfmoban8.core.strategy.bean.AdjustType;
import com.example.jltfmoban8.core.strategy.imp.BrightnessStrategy;
import com.example.jltfmoban8.core.strategy.imp.ContrastStrategy;
import com.example.jltfmoban8.core.strategy.imp.CropEditStrategy;
import com.example.jltfmoban8.core.strategy.imp.SaturationStrategy;

import com.example.jltfmoban8.view.BackGroundLayer;
import com.example.jltfmoban8.view.CropPicker;
import com.example.jltfmoban8.view.ImageEditView;
import com.example.jltfmoban8.view.SlideStepBar;
import com.example.jltfmoban8.view.SlideStepBarLand;
import com.example.jltfmoban8.view.SlideStepBar.OnSlideBarChangeListener;
import com.example.jltfmoban8.view.ShadowTextView;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.AttrSet;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.TabList;
import ohos.agp.components.Text;
import ohos.agp.components.AttrHelper;
import ohos.agp.components.Component;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.element.Element;
import ohos.agp.components.element.ElementScatter;

import ohos.agp.utils.Color;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.utils.TextAlignment;
import ohos.agp.window.dialog.CommonDialog;
import ohos.agp.window.dialog.ToastDialog;
import ohos.agp.window.service.WindowManager;
import ohos.app.Context;
import ohos.bundle.AbilityInfo;
import ohos.global.configuration.Configuration;
import ohos.global.resource.NotExistException;
import ohos.global.resource.WrongTypeException;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.image.PixelMap;
import ohos.media.image.common.Rect;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observer;
import java.util.ArrayList;
import java.util.Observable;

import static com.example.jltfmoban8.core.strategy.IEditAction.ActionType.ACT_REDO;
import static com.example.jltfmoban8.core.strategy.IEditAction.ActionType.ACT_UNDO;

/**
 * Main Ability Slice
 */
public class MainAbilitySlice extends AbilitySlice implements Observer {
    private static final int EDIT_ICON_SIZE = 24;
    private static final int TAB_TEXT_SIZE = 10;
    private static final int TAB_LAND_MARGIN = 20;
    private static final int ADJUST_SLIDE_BAR_HEIGHT = 64;
    private static final int ADJUST_SLIDE_BAR_WIDTH = 296;
    private static final int ADJUST_SLIDE_MODE_NORMAL = 0;
    private static final int ADJUST_SLIDE_MODE_IMMERSION = 1;
    private static final int CONSTANT_TWO = 2;
    private static final int TAB_DEFAULT_SELECTED = 0;
    private static final float SIXTEEN_NINE = 16f / 9F;
    private static final float NINE_SIXTEEN = 9F / 16F;
    private static final float SQUARE = 1F / 1F;
    private static final int TAB_LIST_ICON_SIZE = 24;
    private static final HiLogLabel EDIT_EXCEPTIONS = new HiLogLabel(1, 1, "MainAbilitySlice");

    private ImageEditView imageEditView;

    private CropPicker cropPickerPortrait;

    private CropPicker cropPickerLandscape;

    private BackGroundLayer backgroundLayerPortrait;

    private BackGroundLayer backgroundLayerLandscape;

    private ImageEditorImp imageEditorImp;

    private PixelMap preview;

    private PixelMap tempOriginalImage;

    private boolean alreadyAdjust = false;

    private FilterNameShowAnimation toastAnimation;

    private ShadowTextView adjustToast;

    private DirectionalLayout slideBarContainer;

    private int inImmersionMode = ADJUST_SLIDE_MODE_NORMAL;

    private String testImgFilePath = "Screenshot_20210122_135810_com.huawei.photos.jpg";

    private ImageEditorProxy imageEditorProxy;

    private int displayOrientation = Configuration.DIRECTION_VERTICAL;

    private SliceState sliceState;

    private SlideStepBar adjustSlideBar;

    private TabList toolTabList;

    private ToolTabClickedListener toolTabClickedListener;

    private TabList cropTabList;

    private CropTabClickedListener cropTabClickedListener;

    private TabList adjustTabList;

    private AdjustTabClickedListener adjustTabClickedListener;

    private TabList cropSubTabList;

    private CropSizeClickedListener cropSizeClickedListener;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        getWindow().addFlags(WindowManager.LayoutConfig.MARK_ALLOW_LAYOUT_OVERSCAN);
        getWindow().addFlags(WindowManager.LayoutConfig.MARK_FULL_SCREEN);
        getWindow().setNavigationBarColor(Color.BLACK.getValue());
        displayOrientation = getOrientation();
        setUIContent(displayOrientation == Configuration.DIRECTION_VERTICAL ?
                ResourceTable.Layout_ability_main : ResourceTable.Layout_ability_main_landscape);
        requestPermission();
        initSliceState();
        initImageEditorView();
        initToolTabList();
        initAppBar();
    }

    @Override
    protected void onOrientationChanged(AbilityInfo.DisplayOrientation displayOrientation) {
        if (displayOrientation == AbilityInfo.DisplayOrientation.PORTRAIT) {
            this.displayOrientation = Configuration.DIRECTION_VERTICAL;
            setUIContent(ResourceTable.Layout_ability_main);
        } else if (displayOrientation == AbilityInfo.DisplayOrientation.LANDSCAPE) {
            this.displayOrientation = Configuration.DIRECTION_HORIZONTAL;
            setUIContent(ResourceTable.Layout_ability_main_landscape);
        }
        if (imageEditView != null) {
            imageEditView.removeAllComponents();
        }
        alreadyAdjust = true;
        initImageEditorView();
        initToolTabList();
        initAppBar();
    }

    private void initSliceState() {
        sliceState = new SliceState();
        initAdjustMap();
    }

    private void initAdjustMap() {
        Map<String, AdjustState> adjustStateMap = new HashMap<>();
        for (AdjustType type : AdjustType.values()) {
            AdjustState adjustState = new AdjustState();
            adjustState.setType(type);
            adjustState.setProgress(type.getMode() == SlideStepBar.Mode.ADJUST_MODE ?
                    SlideStepBar.MAX_LEVEL : 0);
            adjustStateMap.put(type.getName(), adjustState);
        }
        sliceState.setAdjustStateMap(adjustStateMap);
    }

    private void initImageEditorView() {
        imageEditView = (ImageEditView) findComponentById(ResourceTable.Id_edit_fraction);
        imageEditView.setAdapter(new ImageEditView.EditorAdapter() {
            @Override
            public ImageEditorProxy getImageEditorProxy() {
                if (imageEditorProxy == null) {
                    try {
                        imageEditorProxy = loadImageEditor(testImgFilePath, imageEditView);
                    } catch (EditActionException e) {
                        HiLog.error(EDIT_EXCEPTIONS, "Image Proxy Load Failed: ", "");
                        terminate();
                    }
                }
                return imageEditorProxy;
            }

            @Override
            protected BackGroundLayer getBackgroundView(Context ctx, AttrSet attrs) {
                if (backgroundLayerPortrait == null && displayOrientation == Configuration.DIRECTION_VERTICAL) {
                    backgroundLayerPortrait = new BackGroundLayer(ctx, attrs);
                }
                if (backgroundLayerLandscape == null && displayOrientation == Configuration.DIRECTION_HORIZONTAL) {
                    backgroundLayerLandscape = new BackGroundLayer(ctx, attrs);
                }
                return getBackgroundLayer();
            }

            @Override
            protected CropPicker getImagePicker(Context ctx, AttrSet attrs) {
                if (cropPickerPortrait == null && displayOrientation == Configuration.DIRECTION_VERTICAL) {
                    cropPickerPortrait = new CropPicker(ctx, attrs);
                }
                if (cropPickerLandscape == null && displayOrientation == Configuration.DIRECTION_HORIZONTAL) {
                    cropPickerLandscape = new CropPicker(ctx, attrs);
                }
                return getCropPicker();
            }
        });
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    @Override
    public Object getLastStoredDataWhenConfigChanged() {
        return super.getLastStoredDataWhenConfigChanged();
    }

    private void requestPermission() {
        List<String> applyPermissions = new ArrayList<>();
        applyPermissions.add("ohos.permission.READ_MEDIA");
        requestPermissionsFromUser(applyPermissions.toArray(new String[0]), 0);
    }

    private ImageEditorProxy loadImageEditor(final String imgFilePath, ImageEditView imageEditView)
            throws EditActionException {
        ImageEditorProxy imageEditProxy = createImageEditProxy(imgFilePath, imageEditView);
        imageEditProxy.export();
        return imageEditProxy;
    }

    private ImageEditorProxy createImageEditProxy(String imgFilePath, ImageEditView editorView)
            throws EditActionException {
        imageEditorImp = new ImageEditorImp(imgFilePath);

        // Add ImageEdit strategy here
        imageEditorImp.addEditStrategy(new CropEditStrategy());
        imageEditorImp.addEditStrategy(new BrightnessStrategy());
        imageEditorImp.addEditStrategy(new SaturationStrategy());
        imageEditorImp.addEditStrategy(new ContrastStrategy());

        // Bind image editor to proxy
        final ImageEditorProxy editorProxy = new ImageEditorProxy(imageEditorImp);
        // Observe proxy
        editorProxy.addObserver(this);
        return editorProxy;
    }

    @Override
    public void update(Observable observable, Object data) {
        if (data instanceof EditActionException) {
            ((EditActionException) data).printStackTrace();
        } else if (data instanceof ImageEditorProxy.EditorState) {
            final ImageEditorProxy.EditorState editorState = (ImageEditorProxy.EditorState) data;
            preview = editorState.previewPixelMap;
            // Reset the edit state after redo and undo
            if (editorState.action.getActionType() == ACT_UNDO || editorState.action.getActionType() == ACT_REDO) {
                alreadyAdjust = false;
            }
            if (!alreadyAdjust) {
                // adjust and crop option after reset tempOriginalImage
                tempOriginalImage = preview;
            }
        }
    }

    private void initToolTabList() {
        if (toolTabList != null) {
            toolTabList.removeTabSelectedListener(toolTabClickedListener);
        }
        toolTabList = (TabList) findComponentById(ResourceTable.Id_toolBar);
        toolTabList.removeAllComponents();
        toolTabList.addTab(createTab("Crop", ResourceTable.Graphic_ic_crop, ResourceTable.String_crop));
        toolTabList.addTab(createTab("Adjust", ResourceTable.Graphic_ic_adjust, ResourceTable.String_adjust));
        toolTabList.setTabTextSize(AttrHelper.vp2px(TAB_TEXT_SIZE, getContext()));
        toolTabList.setTabTextColors(Color.WHITE.getValue(), Color.getIntColor("#007DFF"));
        if (toolTabClickedListener == null) {
            toolTabClickedListener = new ToolTabClickedListener();
        }
        toolTabList.addTabSelectedListener(toolTabClickedListener);
        toolTabList.setFixedMode(true);
        toolTabList.getTabAt(sliceState.getToolIndex()).select();

        resetTabListOrientation(toolTabList);
    }

    private void initCropTabList() {
        if (cropTabList != null) {
            cropTabList.removeTabSelectedListener(cropTabClickedListener);
        }
        cropTabList = (TabList) findComponentById(ResourceTable.Id_paraBar);
        cropTabList.removeAllComponents();
        cropTabList.addTab(createTab("Size", ResourceTable.Graphic_ic_size, ResourceTable.String_size));
        cropTabList.setTabTextSize(AttrHelper.vp2px(TAB_TEXT_SIZE, getContext()));
        cropTabList.setTabTextColors(Color.WHITE.getValue(), Color.getIntColor("#007DFF"));
        cropTabList.setFixedMode(true);
        if (cropTabClickedListener == null) {
            cropTabClickedListener = new CropTabClickedListener();
        }
        cropTabList.addTabSelectedListener(cropTabClickedListener);
        cropTabList.getTabAt(sliceState.getCropIndex()).select();
        resetTabListOrientation(cropTabList);
    }

    private void initAdjustTabList() {
        if (adjustTabList != null) {
            adjustTabList.removeTabSelectedListener(adjustTabClickedListener);
        }
        adjustTabList = (TabList) findComponentById(ResourceTable.Id_paraBar);
        adjustTabList.removeAllComponents();
        adjustTabList.addTab(createTab("Brightness", ResourceTable.Graphic_ic_brightness,
                ResourceTable.String_brightness));
        adjustTabList.addTab(createTab("Contrast", ResourceTable.Graphic_ic_contrast,
                ResourceTable.String_contrast));
        adjustTabList.addTab(createTab("Saturation", ResourceTable.Graphic_ic_saturation,
                ResourceTable.String_saturation));
        adjustTabList.setTabTextSize(AttrHelper.vp2px(TAB_TEXT_SIZE, getContext()));
        adjustTabList.setTabTextColors(Color.WHITE.getValue(), Color.getIntColor("#007DFF"));
        adjustTabList.setFixedMode(true);
        if (adjustTabClickedListener == null) {
            adjustTabClickedListener = new AdjustTabClickedListener();
        }
        adjustTabList.addTabSelectedListener(adjustTabClickedListener);
        adjustTabList.getTabAt(sliceState.getAdjustIndex()).select();
        resetTabListOrientation(adjustTabList);
    }

    private void initAdjustSlideBar() {
        slideBarContainer = (DirectionalLayout) findComponentById(ResourceTable.Id_slideBar_container);
        slideBarContainer.removeAllComponents();
        initAdjustToast();
        if (displayOrientation == Configuration.DIRECTION_VERTICAL) {
            adjustSlideBar = new SlideStepBar(getContext());
            adjustSlideBar.setHeight(AttrHelper.vp2px(ADJUST_SLIDE_BAR_HEIGHT, getContext()));
            adjustSlideBar.setWidth(AttrHelper.vp2px(ADJUST_SLIDE_BAR_WIDTH, getContext()));
        } else {
            adjustSlideBar = new SlideStepBarLand(getContext());
            adjustSlideBar.setHeight(AttrHelper.vp2px(ADJUST_SLIDE_BAR_WIDTH, getContext()));
            adjustSlideBar.setWidth(AttrHelper.vp2px(ADJUST_SLIDE_BAR_HEIGHT, getContext()));
        }
        slideBarContainer.addComponent(adjustSlideBar);
        adjustSlideBar.setOnSlideBarChangeListener(new SlideBarChangeListener());
    }

    private void setCurrentAdjustSlideBar() {
        AdjustState adjustState = getCurrentAdjustState();
        AdjustType type = adjustState.getType();
        adjustSlideBar.setMode(type.getMode());
        adjustSlideBar.setProgressValue(adjustState.getProgress());
    }

    private AdjustState getCurrentAdjustState() {
        // get current adjust type
        Map<String, AdjustState> adjustStateMap = sliceState.getAdjustStateMap();
        return adjustStateMap.get(sliceState.getAdjustName());
    }

    private void initAdjustToast() {
        adjustToast = (ShadowTextView) findComponentById(ResourceTable.Id_adjust_toast);
        if (toastAnimation == null) {
            toastAnimation = new FilterNameShowAnimation();
        }
        toastAnimation.bindView(adjustToast);
    }

    private void initCropSizeList() {
        slideBarContainer = (DirectionalLayout) findComponentById(ResourceTable.Id_slideBar_container);
        slideBarContainer.removeAllComponents();
        if (cropSubTabList != null) {
            cropSubTabList.removeTabSelectedListener(cropSizeClickedListener);
        }
        cropSubTabList = new TabList(getContext());
        cropSubTabList.setWidth(DirectionalLayout.LayoutConfig.MATCH_PARENT);
        cropSubTabList.setTabTextSize(0);
        cropSubTabList.addTab((createTab("Free", ResourceTable.Graphic_ic_crop_size_free, 0)));
        cropSubTabList.addTab((createTab("1:1", ResourceTable.Graphic_ic_crop_size1, 0)));
        cropSubTabList.addTab((createTab("16:9", ResourceTable.Graphic_ic_crop_size2, 0)));
        cropSubTabList.addTab((createTab("9:16", ResourceTable.Graphic_ic_crop_size3, 0)));
        cropSubTabList.setFixedMode(true);
        slideBarContainer.addComponent(cropSubTabList);
        if (cropSizeClickedListener == null) {
            cropSizeClickedListener = new CropSizeClickedListener();
        }
        cropSubTabList.addTabSelectedListener(cropSizeClickedListener);
        cropSubTabList.getTabAt(sliceState.getCropSubIndex()).select();

        resetTabListOrientation(cropSubTabList);
    }

    private void initAppBar() {
        DirectionalLayout undo = (DirectionalLayout) findComponentById(ResourceTable.Id_appBar_undoButton_touchTarget);
        DirectionalLayout redo = (DirectionalLayout) findComponentById(ResourceTable.Id_appBar_redoButton_touchTarget);
        DirectionalLayout back = (DirectionalLayout) findComponentById(ResourceTable.Id_appBar_backButton_touchTarget);
        DirectionalLayout save = (DirectionalLayout) findComponentById(ResourceTable.Id_appBar_saveButton_touchTarget);
        redo.setClickedListener(component -> {
            if (imageEditorProxy.isRedoable()) {
                imageEditorProxy.redo();
            }
        });
        undo.setClickedListener(component -> {
            if (imageEditorProxy.isUndoable()) {
                imageEditorProxy.undo();
            }
        });
        back.setClickedListener(component -> {
            showExitDialog();
        });
        save.setClickedListener(component -> {
            ToastDialog toast = new ToastDialog(getContext());
            toast.setText("Please implement your save function");
            toast.setDuration(1000);
            toast.setOffset(0, 200);
            toast.setSize(1000, 100);
            toast.show();
        });
    }

    private void resetTabListOrientation(TabList tabList) {
        if (tabList != null) {
            if (displayOrientation == Configuration.DIRECTION_VERTICAL) {
                tabList.setOrientation(Component.HORIZONTAL);
            } else {
                tabList.setOrientation(Component.VERTICAL);
                tabList.setTabMargin(AttrHelper.vp2px(TAB_LAND_MARGIN, getContext()));
            }
        }
    }

    private TabList.Tab createTab(String name, int resourceId, int textResourceId) {
        TabList.Tab tab = toolTabList.new Tab(getContext());
        if (textResourceId != 0) {
            tab.setText(getStringElements(textResourceId));
        }
        tab.setName(name);
        Element drawable = ElementScatter.getInstance(getContext()).parse(resourceId);
        drawable.setBounds(0, 0,
                AttrHelper.vp2px(EDIT_ICON_SIZE, getContext()), AttrHelper.vp2px(EDIT_ICON_SIZE, getContext()));
        tab.setAroundElements(null, drawable, null, null);
        tab.setPadding(0, 0, 0, 0);
        tab.setTextAlignment(TextAlignment.CENTER);
        return tab;
    }

    private IEditAction getAdjustAction() throws EditActionException {
        BatchEditAction batchEditAction = new BatchEditAction();
        int index = 0;
        if (imageEditorProxy.isUndoable() && alreadyAdjust) {
            batchEditAction.appendEditAction(getUndoAction());
        }
        for (AdjustType type : AdjustType.values()) {
            Map<String, AdjustState> adjustStateMap = sliceState.getAdjustStateMap();
            AdjustState adjustState = adjustStateMap.get(type.getName());
            // get adjust num
            int adjustNum = adjustState.getProgress();
            if ((type.getMode() == SlideStepBar.Mode.SHARPEN_MODE && adjustNum != 0) ||
                    (type.getMode() == SlideStepBar.Mode.ADJUST_MODE && adjustNum != SlideStepBar.MAX_LEVEL)) {
                String strategyName = type.getName();
                IEditAction editAction = EditActionFactory.createAction(strategyName);
                float progress = (adjustNum * 1.0f) /
                        (type.getMode() == SlideStepBar.Mode.NORMAL_MODE ?
                                SlideStepBar.MAX_LEVEL : SlideStepBar.MAX_LEVEL * CONSTANT_TWO);
                AdjustStrategyParams strategyParams = new AdjustStrategyParams(progress);
                if (index == 0) {
                    // set first PixelMap origin
                    strategyParams.setSrcPixelMap(tempOriginalImage);
                }
                editAction.setParams(strategyParams);
                batchEditAction.appendEditAction(editAction);
                index++;
            }
        }
        if (index > 0) {
            return batchEditAction;
        }
        return null;
    }

    private IEditAction getCropAction() throws EditActionException {
        IEditAction cropAction = EditActionFactory.createAction("CropImage");
        cropAction.setParams(new Object() {
            /**
             * Crop Rect
             */
            public Rect cropRect = imageEditView.getEditRect();
        });
        return cropAction;
    }

    private IEditAction getUndoAction() {
        StateAction stateAction = new StateAction(ACT_UNDO);
        stateAction.setParams(imageEditorImp);
        return stateAction;
    }

    private Element getIcon(int resourceId) {
        Element drawable = ElementScatter.getInstance(getContext()).parse(resourceId);
        drawable.setBounds(0, 0,
                AttrHelper.vp2px(TAB_LIST_ICON_SIZE, getContext()), AttrHelper.vp2px(TAB_LIST_ICON_SIZE, getContext()));
        return drawable;
    }

    private int getOrientation() {
        Configuration configuration = getResourceManager().getConfiguration();
        return configuration.direction;
    }

    private CropPicker getCropPicker() {
        return displayOrientation == Configuration.DIRECTION_VERTICAL ? cropPickerPortrait : cropPickerLandscape;
    }

    private BackGroundLayer getBackgroundLayer() {
        return displayOrientation == Configuration.DIRECTION_VERTICAL ?
                backgroundLayerPortrait : backgroundLayerLandscape;
    }

    private void showExitDialog() {
        CommonDialog exitDialog = new CommonDialog(getContext());
        DirectionalLayout contentComponent = (DirectionalLayout) LayoutScatter.getInstance(getContext())
                .parse(ResourceTable.Layout_dialog_exit, null, false);
        Text tvCancel = (Text) contentComponent.findComponentById(ResourceTable.Id_tv_cancel);
        Text tvDiscard = (Text) contentComponent.findComponentById(ResourceTable.Id_tv_discard);
        exitDialog.setContentCustomComponent(contentComponent);
        tvCancel.setClickedListener(component -> exitDialog.destroy());
        tvDiscard.setClickedListener(component -> terminateAbility());
        exitDialog.setSize(DirectionalLayout.LayoutConfig.MATCH_CONTENT, DirectionalLayout.LayoutConfig.MATCH_CONTENT);
        exitDialog.setAlignment(LayoutAlignment.HORIZONTAL_CENTER | LayoutAlignment.BOTTOM);
        exitDialog.setOffset(0, 180);
        exitDialog.setTransparent(true);
        exitDialog.show();
    }

    private String getStringElements(int resId) {
        try {
            return getContext().getResourceManager().getElement(resId).getString();
        } catch (IOException e) {
            HiLog.error(EDIT_EXCEPTIONS, "Get String Elements failed: IO exception", "");
        } catch (NotExistException e) {
            HiLog.error(EDIT_EXCEPTIONS, "Get String Elements failed: Not Exist exception", "");
        } catch (WrongTypeException e) {
            HiLog.error(EDIT_EXCEPTIONS, "Get String Elements failed: Wrong Type exception", "");
        }
        return null;
    }

    private class SlideBarChangeListener implements OnSlideBarChangeListener {
        @Override
        public void onProgressChanged(SlideStepBar slideStepBar) {
            int progress = slideStepBar.getProgress();
            AdjustState currentAdjustState = getCurrentAdjustState();
            AdjustType type = currentAdjustState.getType();
            currentAdjustState.setProgress(progress);
            int showNumber = (type.getMode() == SlideStepBar.Mode.ADJUST_MODE) ?
                    progress - SlideStepBar.MAX_LEVEL : progress;
            // Set a origin PixelMap before adjust step
            if (inImmersionMode == ADJUST_SLIDE_MODE_IMMERSION) {
                // showSlideBarNumber
                toastAnimation.setFilterNameAndShow(String.valueOf(showNumber));
            }
            alreadyAdjust = true;
            try {
                IEditAction action = getAdjustAction();
                if (action != null) {
                    imageEditorProxy.applyAction(action);
                } else {
                    getBackgroundLayer().setPreviewImage(tempOriginalImage);
                    imageEditorImp.reSetPreviewBmp(tempOriginalImage);
                }
            } catch (EditActionException e) {
                HiLog.error(EDIT_EXCEPTIONS, "Adjust Action failed ", "");
            }
        }

        @Override
        public void onStartTrackingTouch(SlideStepBar slideStepBar) {
            inImmersionMode = ADJUST_SLIDE_MODE_IMMERSION;
        }

        @Override
        public void onStopTrackingTouch(SlideStepBar slideStepBar) {
            inImmersionMode = ADJUST_SLIDE_MODE_NORMAL;
        }

        @Override
        public void longPressedStateChanged(boolean isLongPressed) {
            // Do something
        }
    }

    private class ToolTabClickedListener implements TabList.TabSelectedListener {
        @Override
        public void onSelected(TabList.Tab tab) {
            String mode = tab.getName();
            int position = tab.getPosition();
            sliceState.setToolIndex(position);
            switch (mode) {
                case "Crop":
                    Element cropIcon = getIcon(ResourceTable.Graphic_ic_crop_selected);
                    tab.setAroundElements(null, cropIcon, null, null);
                    imageEditView.getImagePicker().setVisibility(Component.VISIBLE);
                    getCropPicker().resetHighlightRectangle();
                    initAdjustMap();
                    sliceState.setAdjustIndex(TAB_DEFAULT_SELECTED);
                    sliceState.setAdjustName(AdjustType.BRIGHTNESS.getName());
                    initCropTabList();
                    break;
                case "Adjust":
                    Element adjustIcon = getIcon(ResourceTable.Graphic_ic_adjust_selected);
                    tab.setAroundElements(null, adjustIcon, null, null);
                    imageEditView.getImagePicker().setVisibility(Component.INVISIBLE);
                    sliceState.setCropIndex(TAB_DEFAULT_SELECTED);
                    initAdjustSlideBar();
                    initAdjustTabList();
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onUnselected(TabList.Tab tab) {
            String mode = tab.getName();
            switch (mode) {
                case "Crop":
                    Element cropIcon = getIcon(ResourceTable.Graphic_ic_crop);
                    tab.setAroundElements(null, cropIcon, null, null);
                    try {
                        imageEditorProxy.applyAction(getCropAction());
                        alreadyAdjust = false;
                    } catch (EditActionException e) {
                        HiLog.error(EDIT_EXCEPTIONS, "Crop Action Failed ", "");
                    }
                    break;
                case "Adjust":
                    Element adjustIcon = getIcon(ResourceTable.Graphic_ic_adjust);
                    tab.setAroundElements(null, adjustIcon, null, null);
                    IEditAction adjustAction;
                    alreadyAdjust = true;
                    try {
                        adjustAction = getAdjustAction();
                        if (adjustAction != null) {
                            tempOriginalImage = preview;
                        }
                    } catch (EditActionException e) {
                        HiLog.error(EDIT_EXCEPTIONS, "Adjust Action failed ", "");
                    }
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onReselected(TabList.Tab tab) {
            // Do something when a tab is reselected
        }
    }

    private class CropTabClickedListener implements TabList.TabSelectedListener {
        @Override
        public void onSelected(TabList.Tab tab) {
            // Use Switch cases here to add more crop options
            String mode = tab.getName();
            int position = tab.getPosition();
            switch (mode) {
                case "Size":
                    Element sizeIcon = getIcon(ResourceTable.Graphic_ic_size_selected);
                    tab.setAroundElements(null, sizeIcon, null, null);
                    sliceState.setCropIndex(position);
                    initCropSizeList();
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onUnselected(TabList.Tab tab) {
            // Use Switch cases here to add more crop options
            String mode = tab.getName();
            switch (mode) {
                case "Size":
                    Element sizeIcon = getIcon(ResourceTable.Graphic_ic_size);
                    tab.setAroundElements(null, sizeIcon, null, null);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onReselected(TabList.Tab tab) {
            // Do some thing
        }
    }

    private class CropSizeClickedListener implements TabList.TabSelectedListener {
        @Override
        public void onSelected(TabList.Tab tab) {
            String mode = tab.getName();
            int position = tab.getPosition();
            sliceState.setCropSubIndex(position);
            try {
                switch (mode) {
                    case "Free":
                        getCropPicker().resetHighlightRectangle();
                        Element sizeIconFree = getIcon(ResourceTable.Graphic_ic_crop_size_free_selected);
                        tab.setAroundElements(null, sizeIconFree, null, null);
                        break;
                    case "1:1":
                        Element sizeIcon1 = getIcon(ResourceTable.Graphic_ic_crop_size1_selected);
                        tab.setAroundElements(null, sizeIcon1, null, null);
                        getCropPicker().setAspectRatio(SQUARE, null);
                        break;
                    case "16:9":
                        Element sizeIcon2 = getIcon(ResourceTable.Graphic_ic_crop_size2_selected);
                        tab.setAroundElements(null, sizeIcon2, null, null);
                        getCropPicker().setAspectRatio(SIXTEEN_NINE, null);
                        break;
                    case "9:16":
                        Element sizeIcon3 = getIcon(ResourceTable.Graphic_ic_crop_size3_selected);
                        tab.setAroundElements(null, sizeIcon3, null, null);
                        getCropPicker().setAspectRatio(NINE_SIXTEEN, null);
                        break;
                    default:
                        break;
                }
            } catch (CropPickerException e) {
                HiLog.error(EDIT_EXCEPTIONS, "Set CropPicker Ratio Aspect Failed ", "");
            }
        }

        @Override
        public void onUnselected(TabList.Tab tab) {
            String mode = tab.getName();
            switch (mode) {
                case "Free":
                    Element sizeIconFree = getIcon(ResourceTable.Graphic_ic_crop_size_free);
                    tab.setAroundElements(null, sizeIconFree, null, null);
                    break;
                case "1:1":
                    Element sizeIcon1 = getIcon(ResourceTable.Graphic_ic_crop_size1);
                    tab.setAroundElements(null, sizeIcon1, null, null);
                    break;
                case "16:9":
                    Element sizeIcon2 = getIcon(ResourceTable.Graphic_ic_crop_size2);
                    tab.setAroundElements(null, sizeIcon2, null, null);
                    break;
                case "9:16":
                    Element sizeIcon3 = getIcon(ResourceTable.Graphic_ic_crop_size3);
                    tab.setAroundElements(null, sizeIcon3, null, null);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onReselected(TabList.Tab tab) {
        }
    }

    private class AdjustTabClickedListener implements TabList.TabSelectedListener {
        @Override
        public void onSelected(TabList.Tab tab) {
            String mode = tab.getName();
            int position = tab.getPosition();
            switch (mode) {
                case "Brightness":
                    Element brightnessIcon = getIcon(ResourceTable.Graphic_ic_brightness_selected);
                    tab.setAroundElements(null, brightnessIcon, null, null);
                    toastAnimation.setFilterNameAndShow(tab.getName());
                    sliceState.setAdjustIndex(position);
                    sliceState.setAdjustName(mode);
                    setCurrentAdjustSlideBar();
                    break;
                case "Contrast":
                    Element contrastIcon = getIcon(ResourceTable.Graphic_ic_contrast_selected);
                    tab.setAroundElements(null, contrastIcon, null, null);
                    toastAnimation.setFilterNameAndShow(tab.getName());
                    sliceState.setAdjustIndex(position);
                    sliceState.setAdjustName(mode);
                    setCurrentAdjustSlideBar();
                    break;
                case "Saturation":
                    Element saturationIcon = getIcon(ResourceTable.Graphic_ic_saturation_selected);
                    tab.setAroundElements(null, saturationIcon, null, null);
                    toastAnimation.setFilterNameAndShow(tab.getName());
                    sliceState.setAdjustIndex(position);
                    sliceState.setAdjustName(mode);
                    setCurrentAdjustSlideBar();
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onUnselected(TabList.Tab tab) {
            String mode = tab.getName();
            switch (mode) {
                case "Brightness":
                    Element brightnessIcon = getIcon(ResourceTable.Graphic_ic_brightness);
                    tab.setAroundElements(null, brightnessIcon, null, null);
                    break;
                case "Contrast":
                    Element contrastIcon = getIcon(ResourceTable.Graphic_ic_contrast);
                    tab.setAroundElements(null, contrastIcon, null, null);
                    break;
                case "Saturation":
                    Element saturationIcon = getIcon(ResourceTable.Graphic_ic_saturation);
                    tab.setAroundElements(null, saturationIcon, null, null);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onReselected(TabList.Tab tab) {
        }
    }

    private class SliceState {
        private int toolIndex;
        private int cropIndex;
        private int cropSubIndex;
        private int adjustIndex;
        private String adjustName;
        private Map<String, AdjustState> adjustStateMap;

        public String getAdjustName() {
            return adjustName;
        }

        public void setAdjustName(String adjustName) {
            this.adjustName = adjustName;
        }

        public int getToolIndex() {
            return toolIndex;
        }

        public void setToolIndex(int toolIndex) {
            this.toolIndex = toolIndex;
        }

        public int getCropIndex() {
            return cropIndex;
        }

        public void setCropIndex(int cropIndex) {
            this.cropIndex = cropIndex;
        }

        public int getCropSubIndex() {
            return cropSubIndex;
        }

        public void setCropSubIndex(int cropSubIndex) {
            this.cropSubIndex = cropSubIndex;
        }

        public int getAdjustIndex() {
            return adjustIndex;
        }

        public void setAdjustIndex(int adjustIndex) {
            this.adjustIndex = adjustIndex;
        }

        public Map<String, AdjustState> getAdjustStateMap() {
            return adjustStateMap;
        }

        public void setAdjustStateMap(Map<String, AdjustState> adjustStateMap) {
            this.adjustStateMap = adjustStateMap;
        }
    }

    private class AdjustState {
        private AdjustType type;
        private int progress;

        public AdjustType getType() {
            return type;
        }

        public void setType(AdjustType type) {
            this.type = type;
        }

        public int getProgress() {
            return progress;
        }

        public void setProgress(int progress) {
            this.progress = progress;
        }
    }
}
