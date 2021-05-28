import prompt from '@system.prompt';
import dialogManager from '../dialogManager.js';
import picker from '../picker/picker.js';

const PROMPT_DURATION_TIME = 1000;

export default {
    props: {
        dialogBoxData: {
            default: {}
        }
    },
    centerClick() {
        if (this.dialogBoxData.centerControlType == 'submit') {
            this.submit('submit');
        } else if (this.dialogBoxData.centerControlType == 'submit-server') {
            this.submit('submit-server')
        } else {
            this.resetEnumData();
            this.$emit('cancelClick');
        }
    },
    leftClick() {
        if (this.dialogBoxData.leftControlType == 'submit') {
            this.submit('submit');
        } else if (this.dialogBoxData.leftControlType == 'submit-server') {
            this.submit('submit-server')
        } else {
            this.resetEnumData();
            this.$emit('cancelClick');
        }
    },
    rightClick() {
        if (this.dialogBoxData.rightControlType == 'submit') {
            this.submit('submit');
        } else if (this.dialogBoxData.rightControlType == 'submit-server') {
            this.submit('submit-server')
        } else {
            this.resetEnumData();
            this.$emit('cancelClick');
        }
    },
    submit(type) {
        if (dialogManager.getDialogKeyValue() == null && this.dialogBoxData.type == 'PICKER') {
            let pickerKeyValue = picker.getDefaultKeyValue(this.dialogBoxData);
            dialogManager.setDialogKeyValue(pickerKeyValue);
        }
        if (dialogManager.getDialogKeyValue() != null) {
            this.resetEnumData();
            if (type == 'submit') {
                this.$emit('submitClick', dialogManager.getDialogKeyValue());
            } else {
                this.$emit('submitServerClick', dialogManager.getDialogKeyValue());
            }
            dialogManager.setDialogKeyValue(null);
        } else {
            prompt.showToast({
                message: this.$t('strings.pleaseChoose'),
                duration: PROMPT_DURATION_TIME
            });
        }
    },
    resetEnumData() {
        // If the dialog type is ENUM, reset the displayed image to the default unclicked state
        if (this.dialogBoxData.type == 'ENUM') {
            for (let i = 0; i < this.dialogBoxData.items.length; i++) {
                let rowItems = this.dialogBoxData.items[i];
                for (let j = 0; j < rowItems.length; j++) {
                    rowItems[j].icon = rowItems[j].defaultTarget;
                }
            }
        }
    }
};