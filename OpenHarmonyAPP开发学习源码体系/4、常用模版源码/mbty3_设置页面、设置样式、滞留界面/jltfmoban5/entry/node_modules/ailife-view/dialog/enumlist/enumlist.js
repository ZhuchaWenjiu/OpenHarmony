import dialogManager from '../dialogManager.js';

const ENUMERATOR_PRE_ROW = 4;
const ENUM_ROW_HEIGHT = 106;

export default {
    props: {
        enumListData: {
            default: {}
        }
    },
    parseDialogInfo(dialogInfo, dialog, url) {
        let items = [];
        let height = 0;
        for (let i = 0; i < dialogInfo.command.length / ENUMERATOR_PRE_ROW; i++) {
            let rowItems = [];
            for (let j = ENUMERATOR_PRE_ROW * i;
                 j < Math.min(dialogInfo.command.length, ENUMERATOR_PRE_ROW * i + ENUMERATOR_PRE_ROW); j++) {
                let item = {};
                item.target = url + dialogInfo.command[j].icon.value.target;
                item.defaultTarget = url + dialogInfo.command[j].icon.value.defaultTarget;
                item.nightTarget = url + dialogInfo.command[j].icon.valueNight.target;
                item.defaultNightTarget = url + dialogInfo.command[j].icon.valueNight.defaultTarget;
                item.icon = item.defaultTarget;
                item.name = dialogInfo.command[j].name;
                item.value = dialogInfo.command[j].value;
                item.dialogList = dialogInfo.command[j].dialogList;
                rowItems.push(item);
            }
            items.push(rowItems);
            height = height + ENUM_ROW_HEIGHT;
        }
        dialog.height = height + 'px';
        dialog.items = items;
        return dialog;
    },
    onChange(item) {
        for (let i = 0; i < this.enumListData.items.length; i++) {
            let rowItems = this.enumListData.items[i];
            for (let j = 0; j < rowItems.length; j++) {
                rowItems[j].icon = rowItems[j].defaultTarget;
            }
        }
        item.icon = item.target;
        let dialogKeyValue = dialogManager.cloneDialogKeyValue(this.enumListData);
        dialogKeyValue[this.enumListData.path] = item.value;
        dialogKeyValue.dialogList.splice(0, 1);
        for (let i = item.dialogList.length - 1; i >= 0; i--) {
            dialogKeyValue.dialogList.unshift(item.dialogList[i]);
        }
        dialogManager.setDialogKeyValue(dialogKeyValue);
    }
};