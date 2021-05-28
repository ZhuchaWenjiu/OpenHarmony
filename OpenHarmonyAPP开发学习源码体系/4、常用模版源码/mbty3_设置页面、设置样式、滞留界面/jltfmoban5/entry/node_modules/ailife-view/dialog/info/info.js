import dialogManager from '../dialogManager.js';

export default {
    props: {
        infoData: {
            default: {}
        }
    },
    onInit() {
        let dialogKeyValue = dialogManager.cloneDialogKeyValue(this.infoData);
        dialogKeyValue.dialogList.splice(0, 1);
        dialogManager.setDialogKeyValue(dialogKeyValue);
    },
    parseDialogInfo(dialogInfo, dialog, url) {
        dialog.icon = url + dialogInfo.icon;
        dialog.headline = dialogInfo.headline;
        dialog.desc = dialogInfo.desc;
        return dialog;
    }
};