import itemView from '../item/item.js';
import utils from '../../utils/utils.js';
import observed from '../../observed/observed.js';

const GRAVITY_DEFAULT = 'default';
const BACKGROUND_COLOR = '#FFFFFF';
const HIGH_ENUM_HEIGHT = 192;
const LOW_ENUM_HEIGHT = 96;
const MARGIN = 8;

class EnumItemObj {
    constructor(item, value, sid, characteristic, disableNameList) {
        this.url = item.url;
        this.textItem = item.textItem;
        this.showTextItem = item.showTextItem;
        this.iconItem = item.iconItem;
        this.showIconItem = item.showIconItem;
        this.itemLocation = item.itemLocation;
        this.value = value;
        this.sid = sid;
        this.characteristic = characteristic;
        this.disableNameList = disableNameList;
        this.type = 'ENUM';
    }
}

class EnumObj {
    constructor(name, defaultGravity, sid, characteristic, url) {
        this.name = name;
        this.defaultGravity = defaultGravity;
        this.sid = sid;
        this.characteristic = characteristic;
        this.url = url;
        this.enumItemList = [];
        this.checkedValue = -1;
        this.disableStack = [];
        this.disable = false;
        this.alpha = 1;
        this.backgroundColor = BACKGROUND_COLOR;
    }
}

export default {
    props: {
        enumData: {
            default: {}
        }
    },
    data: {
        enumDataList: []
    },
    resetEnumDataList() {
        this.data.enumDataList = [];
    },
    convertJson(idx, templateUIInfo, url) {
        let enumData = this.initEnumData(templateUIInfo[idx], url);
        this.data.enumDataList.push(enumData);
        return enumData;
    },
    initEnumData(enumUIInfo, url) {
        let enumData = new EnumObj(enumUIInfo.name, enumUIInfo.gravity == GRAVITY_DEFAULT, enumUIInfo.command[0].sid,
            enumUIInfo.command[0].characteristic, url);
        let enumInfo = enumUIInfo.command[0].enumInfo;
        for (let i = 0; i < enumInfo.length; i++) {
            let enumItem = this.initEnumItem(enumInfo[i], url, enumUIInfo, enumData.sid, enumData.characteristic);
            enumData.enumItemList.push(enumItem);
        }
        observed.addObserver(enumData.name, (key, value) => {
            utils.setAlphaAndDisable(enumData, value);
        });
        observed.addObserver(enumData.sid + '/' + enumData.characteristic, (key, value) => {
            let checkedValue = enumData.checkedValue;
            enumData.checkedValue = value;
            this.updateEnumIcon(enumData.enumItemList, value, enumData);
            this.setOtherDisable(enumData.enumItemList, false, checkedValue);
            this.setOtherDisable(enumData.enumItemList, true, value);
        });
        return enumData;
    },
    initEnumItem(enumInfo, url, enumUIInfo, sid, characteristic) {
        let item = itemView.convertJson(enumInfo.displayItem, url, enumUIInfo.gravity, enumUIInfo.heightType, 'center');
        let nameList = 'disable' in enumInfo ? enumInfo.disable.name : [];
        let enumItem = new EnumItemObj(item, enumInfo.value, sid, characteristic, nameList);
        return enumItem;
    },
    updateEnumIcon(enumItemList, value, enumData) {
        for (let i = 0; i < enumItemList.length; i++) {
            if (enumData.checkedValue == enumItemList[i].value) {
                let iconValue = utils.getValue(value, enumItemList[i].iconItem.upLeft.valueList).target;
                enumItemList[i].iconItem.upLeft.icon = enumData.url + iconValue;
            } else {
                let iconValue = utils.getValue(value, enumItemList[i].iconItem.upLeft.valueList).defaultTarget;
                enumItemList[i].iconItem.upLeft.icon = enumData.url + iconValue;
            }
        }
    },
    setOtherDisable(enumItemList, isDisable, value) {
        for (let i = 0; i < enumItemList.length; i++) {
            let item = enumItemList[i];
            if (item.value == value) {
                for (let j = 0; j < item.disableNameList.length; j++) {
                    observed.notifyObservers(item.disableNameList[j], isDisable);
                }
            }
        }
    },
    async enumItemClick(sid, characteristic, value) {
        for (let i = 0; i < this.data.enumDataList.length; i++) {
            let enumData = this.data.enumDataList[i];
            if (enumData.sid == sid && enumData.characteristic == characteristic) {
                if (enumData.disable == true) {
                    return;
                }
                if (enumData.checkedValue == value) {
                    return;
                }
                observed.setKeyValue(sid + '/' + characteristic, value);
            }
        }
    },
    getEnumHeight(enumData) {
        let enumHeight = 0;
        if (enumData.enumItemList.length <= 4) {
            enumHeight = enumHeight + LOW_ENUM_HEIGHT;
        } else {
            enumHeight = enumHeight + HIGH_ENUM_HEIGHT;
        }
        enumHeight = enumHeight + MARGIN;
        return enumHeight;
    }
};