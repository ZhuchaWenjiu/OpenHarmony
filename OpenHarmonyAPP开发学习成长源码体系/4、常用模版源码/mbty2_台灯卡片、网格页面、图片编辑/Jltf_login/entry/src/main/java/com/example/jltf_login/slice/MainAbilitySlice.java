package com.example.jltf_login.slice;

import com.example.jltf_login.ResourceTable;
import com.example.jltf_login.utils.ElementUtil;
import com.example.jltf_login.utils.Toast;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.AttrHelper;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.ScrollView;
import ohos.agp.components.Text;
import ohos.agp.components.TextField;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.render.Paint;
import ohos.agp.utils.Color;
import ohos.agp.window.dialog.CommonDialog;
import ohos.app.dispatcher.task.TaskPriority;
import ohos.bundle.AbilityInfo;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The MainAbilitySlice, the first page of app
 */
public class MainAbilitySlice extends AbilitySlice {
    private static final String VALID_MAIL = "123456789@huawei.com";
    private static final int LOGIN_SUCCESS = 1000;
    private static final int LOGIN_FAIL = 1001;

    private ScrollView loginScroll;
    private Text validMail;
    private Text validPassword;
    private TextField mailText;
    private TextField passwordText;
    private Button loginButton;
    private Text helpText;
    private Text privateText;
    private CommonDialog commonDialog;
    // Current number of the animation for the circle progress
    private int roateNum = 0;
    // Instance a EventHandler get the result of login
    private final EventHandler myEventHandler =
            new EventHandler(EventRunner.getMainEventRunner()) {
                @Override
                protected void processEvent(InnerEvent event) {
                    super.processEvent(event);
                    showProgress(false);
                    switch (event.eventId) {
                        case LOGIN_SUCCESS:
                            showLoginDialog(true);
                            break;
                        case LOGIN_FAIL:
                            showLoginDialog(false);
                            break;
                        default:
                            break;
                    }
                }
            };

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_ability_main);
        this.getWindow().setStatusBarColor(ElementUtil.getColor(this, ResourceTable.Color_colorSubBackground));
        this.getWindow().setNavigationBarColor(ElementUtil.getColor(this, ResourceTable.Color_colorSubBackground));

        initView();
        initListener();
    }

    @Override
    protected void onOrientationChanged(AbilityInfo.DisplayOrientation displayOrientation) {
        super.onOrientationChanged(displayOrientation);
        if (displayOrientation == AbilityInfo.DisplayOrientation.LANDSCAPE) {
            loginScroll.setMarginTop(AttrHelper.vp2px(12, this));
        } else {
            loginScroll.setMarginTop(AttrHelper.vp2px(108, this));
        }
    }

    /**
     * The initView, get component from xml
     */
    private void initView() {
        loginScroll = (ScrollView) findComponentById(ResourceTable.Id_loginScroll);
        validMail = (Text) findComponentById(ResourceTable.Id_validMail);
        validPassword = (Text) findComponentById(ResourceTable.Id_validPassword);
        mailText = (TextField) findComponentById(ResourceTable.Id_mailText);
        passwordText = (TextField) findComponentById(ResourceTable.Id_passwordText);
        loginButton = (Button) findComponentById(ResourceTable.Id_loginButton);
        loginButton.setEnabled(false);
        helpText = (Text) findComponentById(ResourceTable.Id_helpText);
        privateText = (Text) findComponentById(ResourceTable.Id_privateText);
    }

    /**
     * The initListener, set listener of component
     */
    private void initListener() {
        mailText.addTextObserver(
                (text, var, i1, i2) -> {
                    validMail.setVisibility(Component.HIDE);
                    validPassword.setVisibility(Component.HIDE);
                });
        passwordText.addTextObserver(this::onTextUpdated);
        loginButton.setClickedListener(component -> login(mailText.getText(), passwordText.getText()));
        helpText.setClickedListener(
                component ->
                        Toast.makeToast(
                                MainAbilitySlice.this,
                                getString(ResourceTable.String_clickedHelp),
                                Toast.TOAST_SHORT)
                                .show());
        privateText.setClickedListener(
                component ->
                        Toast.makeToast(
                                MainAbilitySlice.this,
                                getString(ResourceTable.String_clickedPrivate),
                                Toast.TOAST_SHORT)
                                .show());
    }

    /**
     * The onTextUpdated, change the loginButton background when password is empty or not
     * Hide the valid component when text change
     *
     * @param text text,the text of TextComponent
     * @param var  var
     * @param i1   i1
     * @param i2   i2
     */
    private void onTextUpdated(String text, int var, int i1, int i2) {
        if (text != null && !text.isEmpty()) {
            loginButton.setEnabled(true);
            loginButton.setBackground(new ShapeElement(this, ResourceTable.Graphic_background_login_can));
        } else {
            loginButton.setEnabled(false);
            loginButton.setBackground(new ShapeElement(this, ResourceTable.Graphic_background_login));
        }
        validMail.setVisibility(Component.HIDE);
        validPassword.setVisibility(Component.HIDE);
    }

    /**
     * The mailValid, valid the mail format local
     *
     * @param mail mail,the text of mail
     * @return whether valid of the mail
     */
    private boolean mailValid(String mail) {
        return mail.matches("^[a-z0-9A-Z]+[- |a-z0-9A-Z._]+@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-z]{2,}$");
    }

    /**
     * The mailValid, valid the password local
     *
     * @param password password,the text of password
     * @return whether valid of the password
     */
    private boolean passwordValid(String password) {
        return password.length() >= 6;
    }

    /**
     * The login, the login action when clicked loginButton
     * First, valid mail and password local
     * Second, show the login dialog
     * Third, a thread for valid the login info simulate local
     * Fourth, sendEvent whether login success or fail
     *
     * @param mail     mail,the text of mail
     * @param password password,the text of password
     */
    private void login(final String mail, final String password) {
        validMail.setVisibility(Component.HIDE);
        validPassword.setVisibility(Component.HIDE);

        if (!mailValid(mail)) {
            validMail.setVisibility(Component.VISIBLE);
        } else if (!passwordValid(password)) {
            validPassword.setVisibility(Component.VISIBLE);
        } else {
            showProgress(true);
            // A thread for valid the login info
            getGlobalTaskDispatcher(TaskPriority.DEFAULT)
                    .asyncDispatch(
                            () -> {
                                try {
                                    Thread.sleep(3000);
                                } catch (InterruptedException e) {
                                    Logger.getLogger(ElementUtil.class.getName()).log(Level.SEVERE, e.getMessage());
                                }

                                // SendEvent whether login success or fail
                                if (mail.equals(VALID_MAIL)) {
                                    myEventHandler.sendEvent(LOGIN_SUCCESS);
                                } else {
                                    myEventHandler.sendEvent(LOGIN_FAIL);
                                }
                            });
        }
    }

    /**
     * The showProgress, when loginButton clicked, the dialog show progress
     *
     * @param show show, show or hide the dialog
     */
    private void showProgress(final boolean show) {
        // Instance the dialog when dialog is null
        if (commonDialog == null) {
            commonDialog = new CommonDialog(this);

            // Get circleProgress animation
            Component circleProgress = drawCircleProgress(AttrHelper.vp2px(6, this), AttrHelper.vp2px(3, this));
            commonDialog
                    .setContentCustomComponent(circleProgress)
                    .setTransparent(true)
                    .setSize(
                            DirectionalLayout.LayoutConfig.MATCH_CONTENT, DirectionalLayout.LayoutConfig.MATCH_CONTENT);
        }

        // Show or hide the dialog
        if (show) {
            commonDialog.show();
        } else {
            commonDialog.destroy();
            commonDialog = null;
        }
    }

    /**
     * The drawCircleProgress, draw circle progress function
     *
     * @param maxRadius maxRadius,the radius of animation for the max circle
     * @param minRadius minRadius,the radius of animation for the min circle
     * @return The component of the animation
     */
    private Component drawCircleProgress(int maxRadius, int minRadius) {
        final int circleNum = 12;

        // Init the paint
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL_STYLE);
        paint.setColor(Color.WHITE);

        // Init the component
        Component circleProgress = new Component(this);
        circleProgress.setComponentSize(AttrHelper.vp2px(100, this), AttrHelper.vp2px(100, this));
        circleProgress.setBackground(new ShapeElement(this, ResourceTable.Graphic_login_dialog_bg));

        // Draw the animation
        circleProgress.addDrawTask(
                (component, canvas) -> {
                    // Reset when a round
                    if (roateNum == circleNum) {
                        roateNum = 0;
                    }

                    // Rotate the canvas
                    canvas.rotate(30 * roateNum, (float) (component.getWidth() / 2), (float) (component.getHeight() / 2));
                    roateNum++;
                    int radius = (Math.min(component.getWidth(), component.getHeight())) / 2 - maxRadius;
                    float radiusIncrement = (float) (maxRadius - minRadius) / circleNum;
                    double angle = 2 * Math.PI / circleNum;

                    // Draw the small circle
                    for (int i = 0; i < circleNum; i++) {
                        float x = (float) (component.getWidth() / 2 + Math.cos(i * angle) * radius);
                        float y = (float) (component.getHeight() / 2 - Math.sin(i * angle) * radius);
                        paint.setAlpha((1 - (float) i / circleNum));
                        canvas.drawCircle(x, y, maxRadius - radiusIncrement * i, paint);
                    }

                    // Refresh the component delay
                    getUITaskDispatcher()
                            .delayDispatch(
                                    circleProgress::invalidate,
                                    150);
                });
        return circleProgress;
    }

    /**
     * The showLoginDialog, show the result of login whether success or fail, can clicked outside to cancel the dialog
     *
     * @param success success, success or fail
     */
    private void showLoginDialog(boolean success) {
        // Init dialog
        CommonDialog loginDialog = new CommonDialog(this);
        // Get component from xml
        Component layoutComponent =
                LayoutScatter.getInstance(this).parse(ResourceTable.Layout_login_dialog, null, false);
        Text dialogText = (Text) layoutComponent.findComponentById(ResourceTable.Id_dialog_text);
        Text dialogSubText = (Text) layoutComponent.findComponentById(ResourceTable.Id_dialog_sub_text);

        if (success) {
            dialogText.setText(ResourceTable.String_success);
            dialogSubText.setText(ResourceTable.String_loginSuccess);
        } else {
            dialogText.setText(ResourceTable.String_fail);
            dialogSubText.setText(ResourceTable.String_loginFail);
        }

        loginDialog
                .setContentCustomComponent(layoutComponent)
                .setTransparent(true)
                .setSize(AttrHelper.vp2px(300, this), DirectionalLayout.LayoutConfig.MATCH_CONTENT)
                .setAutoClosable(true);

        loginDialog.show();
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
