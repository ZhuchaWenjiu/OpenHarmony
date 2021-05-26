// controlView is a collection of UI controls corresponding to the template json.
import controlView from '../../../../../../node_modules/ailife-view/control/control.js';
// utils is a utility js that contains some common methods to handle data.
import utils from '../../../../../../node_modules/ailife-view/utils/utils.js';
// observed is used to handle data interactions between the JS and Java and used for event notification.
import observed from '../../../../../../node_modules/ailife-view/observed/observed.js';
// dialogManager is used to process and store dialog parameters.
import dialogManager from '../../../../../../node_modules/ailife-view/dialog/dialogManager.js';
// customDisplayView contains clickable controls
import customDisplayView from '../../../../../../node_modules/ailife-view/customdisplay/customdisplay.js';
import prompt from '@system.prompt';
import app from '@system.app';

const ABILITY_TYPE_INTERNAL = 1;
const ACTION_MESSAGE_CODE_GET_TEMPLATE = 1002;
const ACTION_MESSAGE_CODE_INIT_DEVICE_DATA = 1005;
const ACTION_MESSAGE_CODE_SUBMIG_DIALOG_DATA = 1006;
const ACTION_MESSAGE_CODE_JUMP_TO_HILINK = 1008;
const SHRINK_CONTROL_COUNT = 3;
const BACKGROUND_COLOR = '#FFF7F7F7';
const DURATION_TIME = 10000;
const PROMPT_DURATION_TIME = 3000;

export default {
    data: {
        controlData: {},
        customDisplayItemList: [],
        imageSrc: '',
        logoSrc: '',
        deviceName: [],
        isSpread: true,
        showSpread: true,
        spreadText: '',
        spreadIcon: '',
        showMessage: '',
        message: '',
        showErrorMessage: false,
        errorMessage: '',
        dialog: {},
        dialogShow: false,
        backgroundColor: BACKGROUND_COLOR
    },
    onInit() {
        utils.setActionParam('com.example.jltfmoban5',
            'com.example.jltfmoban5.DataHandlerAbility', ABILITY_TYPE_INTERNAL)
    },
    async onShow() {
        observed.addObserver('showMessage', (key, value) => {
            this.showMessage = value.show;
            if (value.show == true) {
                this.message = this.$t('strings.loading');
                setTimeout(function () {
                    observed.notifyObservers('showMessage', {
                        'show': false
                    });
                }, DURATION_TIME);
            }
        });
        observed.addObserver('error', (key, value) => {
            this.errorMessage = value;
            this.showErrorMessage = true;
            let that = this;
            setTimeout(function () {
                that.showErrorMessage = false;
            }, PROMPT_DURATION_TIME);
        });
        observed.notifyObservers('showMessage', {
            'show': true
        });
        console.info('index.js requestTemplate');
        await this.requestTemplate();
        observed.subscribeAbility();
    },
    async requestTemplate() {
        // This function requests the JSON configuration used for display from Java
        let action = utils.makeAction(ACTION_MESSAGE_CODE_GET_TEMPLATE, {});
        let result = await FeatureAbility.callAbility(action);
        let resultJson = JSON.parse(result);
        if (resultJson.code == 0) {
            let template = JSON.parse(resultJson.data);
            this.parseJson(template.template);
        }
    },
    parseJson(deviceInfo) {
        this.deviceName = deviceInfo.devName;
        this.imageSrc = deviceInfo.iconUrl + deviceInfo.deviceIcon;
        this.logoSrc = deviceInfo.iconUrl + deviceInfo.logoIcon;
        this.controlData = controlView.convertJson(deviceInfo, this.isSpread);
        if (this.controlData.fullLines.length <= SHRINK_CONTROL_COUNT) {
            this.showSpread = false;
        }
        if ('customDisplayUIInfo' in deviceInfo) {
            this.customDisplayItemList = customDisplayView.convertJson(deviceInfo);
        }
        for (let idx in deviceInfo.dialogUIInfo) {
            let dialogInfo = deviceInfo.dialogUIInfo[idx];
            let dialog = dialogManager.parseDialogInfo(dialogInfo, deviceInfo.iconUrl);
            dialogManager.addDialog(dialog);
            observed.addObserver(dialog.id, (key, value) => {
                this.dialog = dialogManager.getDialog(key, value);
                if (this.dialogShow == false) {
                    this.$element('dialog').show();
                    this.dialogShow = true;
                }
            });
            observed.addObserver(dialog.path, (key, value) => {
                if ('dialogInfo' in value) {
                    let updateDialog = dialogManager.parseDialogInfo(value.dialogInfo, deviceInfo.iconUrl);
                    dialogManager.updateDialog(updateDialog);
                }
            });
        }
        observed.addObserver('language', () => {
            this.requestTemplate();
            let action = utils.makeAction(ACTION_MESSAGE_CODE_INIT_DEVICE_DATA, {});
            FeatureAbility.callAbility(action);
        });
        this.setSpreadTextIcon();
    },
    async spreadClick() {
        // This function is used to expand and contract controls
        this.isSpread = !this.isSpread;
        this.setSpreadTextIcon();
        controlView.updateControlLines(this.controlData, this.isSpread);
    },
    setSpreadTextIcon() {
        if (this.isSpread) {
            this.spreadText = this.$t('strings.more');
            this.spreadIcon = '/common/ic_arrow_down.png';
        } else {
            this.spreadText = this.$t('strings.pack_up');
            this.spreadIcon = '/common/ic_arrow_up.png';
        }
    },
    customDisplayClick(customDisplayData) {
        prompt.showToast({
            message: customDisplayData.detail.title,
            duration: PROMPT_DURATION_TIME
        });
    },
    dialogSubmit(dialogData) {
        if (dialogData.detail.dialogList.length == 0) {
            observed.setKeyValue(dialogData.detail.path, dialogData.detail);
            this.dialogShow = false;
            this.$element('dialog').close();
        } else {
            observed.notifyObservers(dialogData.detail.dialogList[0], dialogData.detail);
        }
    },
    async dialogSubmitServer(dialogData) {
        let key = dialogData.detail.path;
        let value = dialogData.detail;
        let data = {
            key: value
        }
        let action = utils.makeAction(ACTION_MESSAGE_CODE_SUBMIG_DIALOG_DATA, data);
        await FeatureAbility.callAbility(action);
        if (dialogData.detail.dialogList.length == 0) {
            this.dialogShow = false;
            this.$element('dialog').close();
        } else {
            observed.notifyObservers(dialogData.detail.dialogList[0], dialogData.detail);
        }
    },
    dialogCancel() {
        dialogManager.resetDialogData();
        this.dialogShow = false;
        this.$element('dialog').close();
    },
    backClick() {
        app.terminate();
    },
    moreClick() {
        this.$element('hiLinkDialog').show();
    },
//Transfer the URL to be redirected to Java.
    urlClick() {
        let data = {
            url: this.$t('strings.hiLinkUrl')
        }
        let action = utils.makeAction(ACTION_MESSAGE_CODE_JUMP_TO_HILINK, data);
        FeatureAbility.callAbility(action);
    },
    cancelClick() {
        this.$element('hiLinkDialog').close();
    }
};
