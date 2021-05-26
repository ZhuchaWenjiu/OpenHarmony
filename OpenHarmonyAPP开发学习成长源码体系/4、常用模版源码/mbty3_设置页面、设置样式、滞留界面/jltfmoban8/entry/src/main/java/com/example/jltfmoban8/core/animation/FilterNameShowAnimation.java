package com.example.jltfmoban8.core.animation;

import com.example.jltfmoban8.view.ShadowTextView;
import ohos.agp.animation.AnimatorProperty;
import ohos.agp.components.Component;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;

/**
 * Filter Number Animation
 */
public class FilterNameShowAnimation {
    private static final int MSG_SHOW_ANIMATION = 101;

    private static final int MSG_HIDE_ANIMATION = 102;

    private static final int COMPONENT_STAY_TIME = 1000;

    private AnimatorProperty fadeInAnimation;

    private AnimatorProperty fadeOutAnimation;

    private ShadowTextView toastText;

    private EventHandler handler = new EventHandler(EventRunner.getMainEventRunner()) {
        @Override
        protected void processEvent(InnerEvent event) {
            super.processEvent(event);
            switch (event.eventId) {
                case MSG_SHOW_ANIMATION: {
                    showToastAnimation();
                    hideToastWithNoQueue();
                    break;
                }
                case MSG_HIDE_ANIMATION: {
                    hideToastAnimation();
                    break;
                }
                default:
                    break;
            }
        }
    };

    /**
     * constructed function
     */
    public FilterNameShowAnimation() {
    }

    private void showToastAnimation() {
        if (toastText.getVisibility() != Component.VISIBLE) {
            fadeInAnimation = toastText.createAnimatorProperty();
            fadeInAnimation.alphaFrom(0).alpha(1)
                    .scaleXFrom(0.5f).scaleX(1f).scaleXBy(0.5f)
                    .scaleYFrom(0.5f).scaleY(1f).scaleYBy(0.5f)
                    .setDuration(250);
            fadeInAnimation.start();
            toastText.setVisibility(Component.VISIBLE);
        }
    }

    private void hideToastAnimation() {
        if (toastText.getVisibility() == Component.VISIBLE) {
            fadeOutAnimation = toastText.createAnimatorProperty();
            fadeOutAnimation.alphaFrom(1).alpha(0)
                    .scaleXFrom(1f).scaleX(0.9f).scaleXBy(0.5f)
                    .scaleYFrom(1f).scaleY(0.9f).scaleYBy(0.5f)
                    .setDuration(150);
            fadeOutAnimation.start();
            toastText.setVisibility(Component.HIDE);
        }
    }

    private void showToastWithNoQueue() {
        handler.removeEvent(MSG_SHOW_ANIMATION);
        handler.sendEvent(MSG_SHOW_ANIMATION);
    }

    private void hideToastWithNoQueue() {
        handler.removeEvent(MSG_HIDE_ANIMATION);
        handler.sendEvent(MSG_HIDE_ANIMATION, COMPONENT_STAY_TIME);
    }

    /**
     * set text content and show animation
     *
     * @param name String to show
     */
    public void setFilterNameAndShow(String name) {
        toastText.setText(name);
        toastText.setVisibility(Component.HIDE);
        showToastWithNoQueue();
    }

    /**
     * Bind View
     *
     * @param toastText toastText
     */
    public void bindView(ShadowTextView toastText) {
        this.toastText = toastText;
    }

}