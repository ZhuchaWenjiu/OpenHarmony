import enumlist from './enumlist/enumlist.js';
import info from './info/info.js';
import picker from './picker/picker.js';
import radiolist from './radiolist/radiolist.js';

const BACKGROUND_COLOR = '#ffffff';

export default {
    data: {
        dialogList: [],
        dialogKeyValue: null
    },
    setDialogKeyValue(dialogKeyValue) {
        this.dialogKeyValue = dialogKeyValue;
    },
    getDialogKeyValue() {
        return this.dialogKeyValue;
    },
    parseDialogInfo(dialogInfo, url) {
        // parses the parameters of the dialog based on its type
        let dialog = {};
        dialog.id = dialogInfo.id;
        dialog.name = dialogInfo.name;
        dialog.type = dialogInfo.dialogType;
        dialog.path = dialogInfo.path;
        dialog.backgroundColor = BACKGROUND_COLOR;
        if (dialogInfo.controlItems.length == 1) {
            dialog.centerControlName = dialogInfo.controlItems[0].name;
            dialog.centerControlType = dialogInfo.controlItems[0].type;
        } else if (dialogInfo.controlItems.length == 2) {
            dialog.leftControlName = dialogInfo.controlItems[0].name;
            dialog.leftControlType = dialogInfo.controlItems[0].type;
            dialog.rightControlName = dialogInfo.controlItems[1].name;
            dialog.rightControlType = dialogInfo.controlItems[1].type;
        }
        if (dialogInfo.dialogType == 'RADIO') {
            dialog = radiolist.parseDialogInfo(dialogInfo, dialog, url);
        } else if (dialogInfo.dialogType == 'ENUM') {
            dialog = enumlist.parseDialogInfo(dialogInfo, dialog, url);
        } else if (dialogInfo.dialogType == 'PICKER') {
            dialog = picker.parseDialogInfo(dialogInfo, dialog, url);
        } else if (dialogInfo.dialogType == 'INFO') {
            dialog = info.parseDialogInfo(dialogInfo, dialog, url);
        }
        return dialog;
    },
    addDialog(dialog) {
        this.data.dialogList.push(dialog);
    },
    updateDialog(dialog) {
        for (let i = 0; i < this.data.dialogList.length; i++) {
            if (this.data.dialogList[i].path == dialog.path) {
                this.data.dialogList[i] = dialog;
                break;
            }
        }
    },
    getDialog(key, value) {
        let dialog = {};
        for (let i = 0; i < this.data.dialogList.length; i++) {
            if (this.data.dialogList[i].id == key) {
                dialog = this.data.dialogList[i];
                dialog.dialogKeyValue = value;
            }
        }
        return dialog;
    },
    cloneDialogKeyValue(data) {
        let dialogKeyValue = {};
        for (let key in data.dialogKeyValue) {
            dialogKeyValue[key] = data.dialogKeyValue[key];
        }
        dialogKeyValue.dialogList = [];
        for (let i = 0; i < data.dialogKeyValue.dialogList.length; i++) {
            dialogKeyValue.dialogList.push(data.dialogKeyValue.dialogList[i]);
        }
        return dialogKeyValue;
    },
    resetDialogData() {
        // If the dialog type is ENUM, reset the displayed image to the default unclicked state
        let dialogList = this.data.dialogList;
        for (let i = 0; i < dialogList.length; i++) {
            if (dialogList[i].type == 'ENUM') {
                for (let j = 0; j < dialogList[i].items.length; j++) {
                    let rowItems = dialogList[i].items[j];
                    for (let k = 0; k < rowItems.length; k++) {
                        rowItems[k].icon = rowItems[k].defaultTarget;
                    }
                }
            }
        }
    }
};