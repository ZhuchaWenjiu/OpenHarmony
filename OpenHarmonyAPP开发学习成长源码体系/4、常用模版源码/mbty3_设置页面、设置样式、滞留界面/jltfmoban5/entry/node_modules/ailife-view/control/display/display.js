import itemView from '../item/item.js';
import utils from '../../utils/utils.js';
import observed from '../../observed/observed.js';

const GRAVITY_DEFAULT = 'default';
const BACKGROUND_COLOR = '#FFFFFF';
const DISPLAY_HEIGHT = 96;
const MARGIN = 8;

class DisplayObj {
    constructor(name, defaultGravity) {
        this.name = name;
        this.defaultGravity = defaultGravity;
        this.leftItem = null;
        this.showLeftItem = false;
        this.centerItem = null;
        this.showCenterItem = false;
        this.rightItem = null;
        this.showRightItem = false;
        this.itemList = [];
        this.disableStack = [];
        this.disable = false;
        this.alpha = 1;
        this.backgroundColor = BACKGROUND_COLOR;
    }
}

export default {
    props: {
        displayData: {
            default: ''
        }
    },
    convertJson(idx, templateUIInfo, url) {
        let displayData = this.initDisplayData(templateUIInfo[idx], url);
        return displayData;
    },
    initDisplayData(displayUIInfo, url) {
        let displayData = new DisplayObj(displayUIInfo.name, displayUIInfo.gravity == GRAVITY_DEFAULT);
        let uiList = displayUIInfo.uiList;
        if (displayUIInfo.gravity == GRAVITY_DEFAULT) {
            if (uiList.length == 1) {
                this.setItemLeft(displayData, uiList, url, displayUIInfo);
            } else if (uiList.length == 2) {
                this.setItemLeft(displayData, uiList, url, displayUIInfo);
                this.setItemRight(displayData, uiList, url, displayUIInfo);
            } else {
                this.setItemLeft(displayData, uiList, url, displayUIInfo);
                this.setItemCenter(displayData, uiList, url, displayUIInfo);
                this.setItemRight(displayData, uiList, url, displayUIInfo);
            }
        } else {
            for (let i = 0; i < uiList.length; i++) {
                displayData.itemList[i] = itemView.convertJson(uiList[i], url,
                    displayUIInfo.gravity, displayUIInfo.heightType, 'center');
                displayData.itemList[i].type = 'DISPLAY';
            }
        }
        observed.addObserver(displayData.name, (key, value) => {
            utils.setAlphaAndDisable(displayData, value);
        });
        return displayData;
    },
    setItemLeft(displayData, uiList, url, displayUIInfo) {
        displayData.showLeft = true;
        displayData.itemLeft = itemView.convertJson(uiList[0], url,
            displayUIInfo.gravity, displayUIInfo.heightType, 'left');
        displayData.itemLeft.type = 'DISPLAY';
    },
    setItemCenter(displayData, uiList, url, displayUIInfo) {
        displayData.showCenter = true;
        displayData.itemCenter = itemView.convertJson(uiList[1], url,
            displayUIInfo.gravity, displayUIInfo.heightType, 'center');
        displayData.itemCenter.type = 'DISPLAY';
    },
    setItemRight(displayData, uiList, url, displayUIInfo) {
        displayData.showRight = true;
        displayData.itemRight = itemView.convertJson(uiList[uiList.length - 1], url,
            displayUIInfo.gravity, displayUIInfo.heightType, 'right');
        displayData.itemRight.type = 'DISPLAY';
    },
    getDisplayHeight() {
        return DISPLAY_HEIGHT + MARGIN;
    }
};