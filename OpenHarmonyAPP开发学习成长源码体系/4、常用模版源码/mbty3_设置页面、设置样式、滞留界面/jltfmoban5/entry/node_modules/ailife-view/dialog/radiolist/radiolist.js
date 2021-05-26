import dialogManager from '../dialogManager.js';
import observed from '../../observed/observed.js';

const RADIO_ITEM_HEIGHT = 48;

export default {
    props: {
        radioListData: {
            default: {}
        }
    },
    parseDialogInfo(dialogInfo, dialog, url) {
        dialog.items = [];
        for (let i = 0; i < dialogInfo.range.length; i++) {
            let item = {
                name: dialogInfo.range[i],
                value: dialogInfo.value[i]
            }
            dialog.items.push(item);
        }
        dialog.height = dialogInfo.range.length * RADIO_ITEM_HEIGHT + 'px';
        observed.addObserver(dialog.path, (key, value) => {
            if ('range' in value && 'value' in value) {
                dialog.items = [];
                for (let i = 0; i < value.range.length; i++) {
                    let item = {
                        name: value.range[i],
                        value: value.value[i]
                    }
                    dialog.items.push(item);
                }
            }
        });
        return dialog;
    },
    onInit() {
        dialogManager.setDialogKeyValue(null);
    },
    onChange(checkedRadioInfo) {
        if (checkedRadioInfo.checked == true) {
            let dialogKeyValue = dialogManager.cloneDialogKeyValue(this.radioListData);
            dialogKeyValue[this.radioListData.path] = checkedRadioInfo.target.attr.value;
            dialogKeyValue.dialogList.splice(0, 1);
            dialogManager.setDialogKeyValue(dialogKeyValue);
        }
    }
};