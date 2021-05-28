package com.example.jltfmoban7.component;

import com.example.jltfmoban7.ResourceTable;
import com.example.jltfmoban7.model.GridItemInfo;
import com.example.jltfmoban7.provider.GridAdapter;
import com.example.jltfmoban7.utils.AppUtils;

import ohos.aafwk.ability.AbilitySlice;
import ohos.agp.components.AttrHelper;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentTransition;
import ohos.agp.components.Image;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.ScrollView;
import ohos.agp.utils.MimeData;
import ohos.agp.utils.Rect;
import ohos.agp.utils.TextTool;
import ohos.multimodalinput.event.MmiPoint;
import ohos.multimodalinput.event.TouchEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * The DragLayout
 */
public class DragLayout {
    // Item count
    private static final int UP_ITEM_COUNT = 16;
    private static final int DOWN_ITEM_COUNT = 8;
    private static final String UP_GRID_TAG = "upGrid";
    private static final String DOWN_GRID_TAG = "downGrid";
    private static final int INVALID_POSITION = -1;

    private AbilitySlice slice;
    private boolean isViewOnDrag;
    private boolean isViewOnExchange;
    private boolean isScroll;
    private GridView parentView;
    private ScrollView scrollView;

    // Item when dragged
    private int scrollViewTop;
    private int scrollViewLeft;
    private Component selectedView;
    private final Component.LongClickedListener longClickListener =
            component -> {
                Component shadowComponent = getShadow();
                shadowComponent.setWidth(component.getWidth());
                shadowComponent.setHeight(component.getHeight());
                shadowComponent.setAlpha(0.8f);
                Component.DragFeedbackProvider dragFeedbackProvider =
                        new Component.DragFeedbackProvider(shadowComponent);
                component.startDragAndDrop(new MimeData(), dragFeedbackProvider);
                component.setVisibility(Component.INVISIBLE);
                selectedView = component;
                isViewOnDrag = true;
                if (UP_GRID_TAG.equals(selectedView.getTag())) {
                    if (slice.findComponentById(ResourceTable.Id_grid_view_up) instanceof GridView) {
                        parentView = (GridView) slice.findComponentById(ResourceTable.Id_grid_view_up);
                    }
                } else {
                    if (slice.findComponentById(ResourceTable.Id_grid_view_down) instanceof GridView) {
                        parentView = (GridView) slice.findComponentById(ResourceTable.Id_grid_view_down);
                    }
                }
                bindComponentTransition();
            };
    private int currentDragX;
    private int currentDragY;

    public DragLayout(AbilitySlice slice) {
        this.slice = slice;
    }

    /**
     * method for init view
     */
    public void initGridView() {
        if (slice.findComponentById(ResourceTable.Id_grid_layout) instanceof ScrollView) {
            scrollView = (ScrollView) slice.findComponentById(ResourceTable.Id_grid_layout);
        }
        initUpListItem();
        initDownListItem();
        initBottomItem();
        initEventListener();
        initAppBar();
    }

    private void initUpListItem() {
        String itemText = AppUtils.getStringResource(slice, ResourceTable.String_grid_item_text);
        List<GridItemInfo> upperItemList = new ArrayList<>();

        for (int i = 0; i < UP_ITEM_COUNT; i++) {
            upperItemList.add(new GridItemInfo(itemText + i, ResourceTable.Media_icon, UP_GRID_TAG));
        }
        GridView gridView = (GridView) slice.findComponentById(ResourceTable.Id_grid_view_up);
        GridAdapter adapter = new GridAdapter(slice.getContext(), upperItemList);
        gridView.setAdapter(adapter, longClickListener);
        gridView.setTag(UP_GRID_TAG);
    }

    private void initDownListItem() {
        String itemText = AppUtils.getStringResource(slice.getContext(), ResourceTable.String_grid_item_text);
        List<GridItemInfo> lowerItemList = new ArrayList<>();
        for (int i = 0; i < DOWN_ITEM_COUNT; i++) {
            lowerItemList.add(new GridItemInfo(itemText + i, ResourceTable.Media_icon, DOWN_GRID_TAG));
        }

        if (slice.findComponentById(ResourceTable.Id_grid_view_down) instanceof GridView) {
            GridView gridView = (GridView) slice.findComponentById(ResourceTable.Id_grid_view_down);
            GridAdapter adapter = new GridAdapter(slice.getContext(), lowerItemList);
            gridView.setAdapter(adapter, longClickListener);
            gridView.setTag(DOWN_GRID_TAG);
        }
    }

    /**
     * Calculating button width based on screen width.
     * The actual width is the screen width minus the margin of the buttons.
     */
    private void initBottomItem() {
        int screenWidth = AppUtils.getScreenInfo(slice.getContext()).getPointXToInt();
        int buttonWidth = (screenWidth - AttrHelper.vp2px(80, slice.getContext())) / 2;
        Component leftButton = slice.findComponentById(ResourceTable.Id_bottom_left_button);
        leftButton.setWidth(buttonWidth);
        Component rightButton = slice.findComponentById(ResourceTable.Id_bottom_right_button);
        rightButton.setWidth(buttonWidth);
    }

    private void initEventListener() {
        if (slice.findComponentById(ResourceTable.Id_left_arrow) instanceof Image) {
            Image backIcon = (Image) slice.findComponentById(ResourceTable.Id_left_arrow);
            backIcon.setClickedListener(component -> slice.terminateAbility());
        }

        scrollView.setTouchEventListener(
                (component, touchEvent) -> {
                    MmiPoint downScreenPoint = touchEvent.getPointerScreenPosition(touchEvent.getIndex());
                    switch (touchEvent.getAction()) {
                        case TouchEvent.PRIMARY_POINT_DOWN:
                            currentDragX = (int) downScreenPoint.getX();
                            currentDragY = (int) downScreenPoint.getY();
                            MmiPoint downPoint = touchEvent.getPointerPosition(touchEvent.getIndex());
                            scrollViewTop = (int) downScreenPoint.getY() - (int) downPoint.getY();
                            scrollViewLeft = (int) downScreenPoint.getX() - (int) downPoint.getX();
                            return true;
                        case TouchEvent.PRIMARY_POINT_UP:
                        case TouchEvent.CANCEL:
                            if (isViewOnDrag) {
                                selectedView.setScale(1.0f, 1.0f);
                                selectedView.setAlpha(1.0f);
                                selectedView.setVisibility(Component.VISIBLE);
                                isViewOnDrag = false;
                                isScroll = false;
                                return true;
                            }
                            break;
                        case TouchEvent.POINT_MOVE:
                            if (!isViewOnDrag) {
                                break;
                            }
                            int pointX = (int) downScreenPoint.getX();
                            int pointY = (int) downScreenPoint.getY();
                            this.exchangeItem(pointX, pointY);
                            if (UP_GRID_TAG.equals(selectedView.getTag())) {
                                this.swapItems(pointX, pointY);
                            }
                            this.handleScroll(pointY);
                            return true;
                    }
                    return false;
                });
    }

    private void initAppBar() {
        if (TextTool.isLayoutRightToLeft(Locale.getDefault())) {
            Image appBackImg = (Image) slice.findComponentById(ResourceTable.Id_left_arrow);
            appBackImg.setRotation(180);
        }
    }

    private void exchangeItem(int pointX, int pointY) {
        if (!isStartupExchage(pointY)) {
            return;
        }

        GridView gridView;
        parentView.removeComponent(selectedView);
        int addPosition;
        if (UP_GRID_TAG.equals(selectedView.getTag())) {
            gridView = (GridView) slice.findComponentById(ResourceTable.Id_grid_view_down);
            selectedView.setTag(DOWN_GRID_TAG);
            addPosition = 0;
        } else {
            gridView = (GridView) slice.findComponentById(ResourceTable.Id_grid_view_up);
            selectedView.setTag(UP_GRID_TAG);
            addPosition = gridView.getChildCount();
        }
        gridView.addComponent(selectedView, addPosition);
        selectedView = gridView.getComponentAt(addPosition);
        parentView = gridView;
        currentDragX = pointX;
        currentDragY = pointY;
    }

    private void swapItems(int pointX, int pointY) {
        if (isViewOnExchange) {
            isViewOnExchange = false;
            return;
        }
        int currentPosition = parentView.getChildIndex(selectedView);
        int endPosition = this.pointToPosition(pointX, pointY);

        if (endPosition == INVALID_POSITION || endPosition == currentPosition) {
            return;
        }
        parentView.removeComponent(selectedView);
        parentView.addComponent(selectedView, endPosition);
        currentDragX = pointX;
        currentDragY = pointY;
        selectedView = parentView.getComponentAt(endPosition);
    }

    private int pointToPosition(int pointX, int pointY) {
        int currentX = pointX - scrollViewLeft;
        int currentY = pointY + scrollView.getScrollValue(Component.VERTICAL) - scrollViewTop;
        int childCount = parentView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            Rect child = parentView.getComponentAt(i).getComponentPosition();
            if (child.isInclude(currentX, currentY)
                    && ((pointX >= currentDragX && currentX >= child.getCenterX())
                    || (pointX < currentDragX && currentX < child.getCenterX()))
                    && ((pointY >= currentDragY && currentY >= child.getCenterY())
                    || (pointY < currentDragY && currentY < child.getCenterY()))) {
                return i;
            }
        }

        return INVALID_POSITION;
    }

    private void handleScroll(int pointY) {
        if (pointY < currentDragY && pointY - scrollViewTop < selectedView.getWidth()) {
            scrollView.fluentScrollByY(-selectedView.getWidth());
            isScroll = true;
        }
        if (pointY > currentDragY
                && pointY - scrollViewTop
                >= scrollView.getHeight()
                + scrollView.getScrollValue(Component.VERTICAL)
                - selectedView.getWidth()) {
            scrollView.fluentScrollByY(selectedView.getWidth());
            isScroll = true;
        }
    }

    private void bindComponentTransition() {
        if (parentView != null && parentView.getComponentTransition() == null) {
            ComponentTransition transition = new ComponentTransition();
            transition.removeTransitionType(ComponentTransition.SELF_GONE);
            transition.removeTransitionType(ComponentTransition.OTHERS_GONE);
            transition.removeTransitionType(ComponentTransition.CHANGING);
            parentView.setComponentTransition(transition);
        }
    }

    private Component getShadow() {
        Component itemLayout =
                LayoutScatter.getInstance(slice.getContext()).parse(ResourceTable.Layout_grid_item, null, false);
        if (itemLayout.findComponentById(ResourceTable.Id_grid_item_image) instanceof Image) {
            Image imageItem = (Image) itemLayout.findComponentById(ResourceTable.Id_grid_item_image);
            imageItem.setPixelMap(ResourceTable.Media_icon);
            imageItem.setScale(1.2f, 1.2f);
        }
        return itemLayout;
    }

    private boolean isStartupExchage(int pointY) {
        int scrollY = isScroll ? scrollView.getScrollValue(Component.VERTICAL) : 0;
        int offsetY = pointY - currentDragY;
        offsetY = offsetY < 0 ? offsetY - scrollY : offsetY + scrollY;

        Rect currentRect = selectedView.getComponentPosition();
        int curOffsetY = currentRect.getCenterY() + offsetY;

        if (UP_GRID_TAG.equals(selectedView.getTag())) {
            isViewOnExchange = curOffsetY > parentView.getComponentPosition().bottom;
        } else {
            Component downView = scrollView.findComponentById(ResourceTable.Id_table_layout_down);
            Component descText = downView.findComponentById(ResourceTable.Id_table_layout_down_desc);
            isViewOnExchange = curOffsetY + descText.getComponentPosition().bottom + descText.getMarginBottom() <= 0;
        }
        return isViewOnExchange;
    }
}
