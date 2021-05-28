import itemView from '../item/item.js';
import timeItemView from '../item/timeitem.js';
import utils from '../../utils/utils.js';
import observed from '../../observed/observed.js';

const BACKGROUND_COLOR = '#FFFFFF';
const MULTIFUNCTION_HEIGHT = 88;
const MARGIN = 8;

class MultifunctionCommandObj {
    constructor() {
        this.sid = '';
        this.characteristic = '';
        this.value = 0;
        this.valueString = '';
        this.icon = '';
        this.ableIcon = '';
        this.disableIcon = '';
        this.disableNameList = [];
    }
}

class MultifunctionObj {
    constructor() {
        this.name = '';
        this.commandOne = new MultifunctionCommandObj();
        this.commandTwo = new MultifunctionCommandObj();
        this.commandSize = 0;
        this.multifunctionInfo = null;
        this.itemOne = null;
        this.itemTwo = null;
        this.disableStack = [];
        this.disable = false;
        this.alpha = 1;
        this.backgroundColor = BACKGROUND_COLOR;
    }
}

export default {
    props: {
        multifunctionData: {
            default: ''
        }
    },
    convertJson(idx, templateUIInfo, url) {
        let multifunctionData = this.initMultifunctionData(templateUIInfo[idx], url);
        return multifunctionData;
    },
    initMultifunctionData(multifunctionUIInfo, url) {
        let multifunctionData = new MultifunctionObj();
        multifunctionData.name = multifunctionUIInfo.name;
        multifunctionData.itemOne = itemView.convertJson(multifunctionUIInfo.displayItemOne, url,
            multifunctionUIInfo.gravity, multifunctionUIInfo.heightType, 'left');
        multifunctionData.itemTwo = timeItemView.convertJson(multifunctionUIInfo.displayItemTwo);
        multifunctionData.multifunctionInfo = multifunctionUIInfo.multifunctionInfo;
        observed.addObserver(multifunctionData.name, (key, value) => {
            multifunctionData.disable = value;
            utils.setAlphaAndDisable(multifunctionData, value);
            this.setIconDisable(multifunctionData.commandOne, value);
            if (multifunctionData.commandSize == 2) {
                this.setIconDisable(multifunctionData.commandTwo, value);
            }
        });
        observed.addObserver(multifunctionData.multifunctionInfo.path, (key, value) => {
            let commands = utils.getValue(value, multifunctionData.multifunctionInfo.value);
            this.setOtherDisable(multifunctionData.commandOne, false);
            this.setOtherDisable(multifunctionData.commandTwo, false);
            multifunctionData.commandSize = 0;
            if ('commandOne' in commands) {
                this.setCommandObj(multifunctionData.commandOne, commands.commandOne[0], url);
                multifunctionData.commandSize = multifunctionData.commandSize + 1;
                this.setOtherDisable(multifunctionData.commandOne, true);
            }
            if ('commandTwo' in commands) {
                this.setCommandObj(multifunctionData.commandTwo, commands.commandTwo[0], url);
                multifunctionData.commandSize = multifunctionData.commandSize + 1;
                this.setOtherDisable(multifunctionData.commandTwo, true);
            }
        });
        return multifunctionData;
    },
    setCommandObj(commandObj, commandJson, url) {
        commandObj.sid = commandJson.sid;
        commandObj.characteristic = commandJson.characteristic;
        commandObj.ableIcon = url + commandJson.icon;
        commandObj.disableIcon = url + commandJson.disableIcon;
        commandObj.icon = url + commandJson.icon;
        commandObj.value = commandJson.value;
        if ('valueString' in commandJson) {
            commandObj.valueString = commandJson.valueString;
        }
        if ('disable' in commandJson) {
            commandObj.disableNameList = commandJson.disable.name;
        }
    },
    setIconDisable(command, isDisable) {
        if (isDisable) {
            command.icon = command.disableIcon;
        } else {
            command.icon = command.ableIcon;
        }
    },
    setOtherDisable(command, isDisable) {
        for (let i = 0; i < command.disableNameList.length; i++) {
            observed.notifyObservers(command.disableNameList[i], isDisable);
        }
    },
    commandClick(command) {
        observed.setKeyValue(command.sid + '/' + command.characteristic, command.value);
    },
    getMultifunctionHeight() {
        return MULTIFUNCTION_HEIGHT + MARGIN;
    }
};