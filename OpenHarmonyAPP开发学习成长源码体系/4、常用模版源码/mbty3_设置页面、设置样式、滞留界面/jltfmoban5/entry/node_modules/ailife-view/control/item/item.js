import observed from '../../observed/observed.js';
import utils from '../../utils/utils.js';
import enumView from '../enum/enum.js';

const UI_TYPE_ENUM = 'ENUM';
const GRAVITY_CENTER = 'center';
const GRAVITY_CENTER_DIVIDER = 'centerDivider';
const SUB_TYPE_TEXT = 'text';
const SUB_TYPE_ICON = 'icon';
const SUB_TYPE_ICON_TEXT_VERTICAL = 'iconTextVertical';
const SUB_TYPE_ICON_TEXT_HORIZONTAL = 'iconTextHorizontal';
const LOCATION_LEFT = 'left';
const LOCATION_CENTER = 'center';
const FLEX_LOCATION_START = 'flex-start';
const FLEX_LOCATION_CENTER = 'center';
const FLEX_LOCATION_END = 'flex-end';
const HEIGHT_TYPE_LOW = 'low';
const DEFAULT_GRAVITY_LEFT_TEXT_SIZE = '18px';
const CENTER_GRAVITY_LEFT_TEXT_SIZE = '24px';
const HEIGHT_TYPE_LOW_TEXT_SIZE = '16px';
const TEXT_DEFAULT_COLOR = '#000000';
const DEFAULT_VALUE = '--';

class LocationTextObj {
    constructor() {
        this.text = '';
        this.paramText = '';
        this.path = '';
        this.textDesc = '';
        this.size = '';
        this.color = TEXT_DEFAULT_COLOR;
        this.colorDesc = '';
        this.show = false;
        this.ratio = 1;
    }
}

class LocationIconObj {
    constructor() {
        this.icon = '';
        this.path = '';
        this.valueList = [];
    }
}

class TextItemObj {
    constructor() {
        this.upLeft = new LocationTextObj();
        this.upRight = new LocationTextObj();
        this.down = new LocationTextObj();
    }
}

class IconItemObj {
    constructor() {
        this.upLeft = new LocationIconObj();
        this.upRight = new LocationTextObj();
        this.down = new LocationTextObj();
    }
}

class ItemObj {
    constructor(url, itemLocation) {
        this.url = url;
        this.textItem = new TextItemObj();
        this.showTextItem = false;
        this.iconItem = new IconItemObj();
        this.showIconItem = false;
        this.itemLocation = itemLocation;
    }
}

export default {
    props: {
        itemdata: {
            default: {}
        }
    },
    convertJson(itemInfo, url, gravity, heightType, itemLocation) {
        itemLocation = this.convertItemLocation(itemLocation);
        let itemData = new ItemObj(url, itemLocation);
        if (itemInfo.subType == SUB_TYPE_TEXT) {
            itemData.showTextItem = true;
            this.setLocationText(itemData.textItem.upLeft, itemInfo.textDisplay.upLeft);
            this.addTextObserver(itemData.textItem.upLeft, itemInfo.textDisplay.upLeft);
            this.setUpLeftTextSize(itemData.textItem, heightType, gravity, itemLocation);
            if ('value' in itemInfo.textDisplay.upRight) {
                this.setLocationText(itemData.textItem.upRight, itemInfo.textDisplay.upRight);
                this.addTextObserver(itemData.textItem.upRight, itemInfo.textDisplay.upRight);
            }
            if ('value' in itemInfo.textDisplay.down &&
            (itemData.textItem.upLeft.text != '' || itemData.textItem.upRight.text != '')) {
                this.setLocationText(itemData.textItem.down, itemInfo.textDisplay.down);
                this.addTextObserver(itemData.textItem.down, itemInfo.textDisplay.down);
            }
            return itemData;
        } else if (itemInfo.subType == SUB_TYPE_ICON) {
            this.setLocationIcon(itemData.iconItem.upLeft, itemInfo.icon);
            this.addIconObserver(itemData.iconItem.upLeft, itemInfo.icon, url);
        } else if (itemInfo.subType == SUB_TYPE_ICON_TEXT_VERTICAL) {
            itemData.iconItem.down.show = true;
            this.setLocationIcon(itemData.iconItem.upLeft, itemInfo.icon);
            this.addIconObserver(itemData.iconItem.upLeft, itemInfo.icon, url);
            this.setLocationIconText(itemData.iconItem.down, itemInfo);
            this.addTextObserver(itemData.iconItem.down, itemInfo.iconText);
        } else if (itemInfo.subType == SUB_TYPE_ICON_TEXT_HORIZONTAL) {
            itemData.iconItem.upRight.show = true;
            this.setLocationIcon(itemData.iconItem.upLeft, itemInfo.icon);
            this.addIconObserver(itemData.iconItem.upLeft, itemInfo.icon, url);
            this.setLocationIconText(itemData.iconItem.upRight, itemInfo);
            this.addTextObserver(itemData.iconItem.upRight, itemInfo.iconText);
        }
        itemData.showIconItem = true;
        return itemData;
    },
    addTextObserver(locationText, jsonText) {
        if (jsonText.type == 'dynamicText') {
            locationText.show = false;
            let path = this.getPath(jsonText.value);
            observed.addObserver(path, (key, value) => {
                if (typeof (value) == 'string' && value == '') {
                    locationText.show = false;
                } else {
                    locationText.show = true;
                }
                if (/^\d+([\.]\d+)?$/.test(value)) {
                    this.setLocationTextAndColor(locationText, key, Math.round(value * locationText.ratio));
                } else {
                    this.setLocationTextAndColor(locationText, key, value);
                }
            });
        }
    },
    addIconObserver(locationIcon, jsonIcon, url) {
        observed.addObserver(jsonIcon.path, (path, value) => {
            this.setDefaultLocationIcon(locationIcon, path, value, url);
        });
    },
    convertItemLocation(itemLocation) {
        if (itemLocation == LOCATION_LEFT) {
            return FLEX_LOCATION_START;
        } else if (itemLocation == LOCATION_CENTER) {
            return FLEX_LOCATION_CENTER;
        } else {
            return FLEX_LOCATION_END;
        }
    },
    setLocationTextAndColor(locationText, path, data) {
        if (locationText.show && locationText.path == path) {
            let textValue = utils.getValueTarget(data, locationText.textDesc.value);
            locationText.text =
            locationText.paramText.replace('${', '').replace('}', '').replace(locationText.path, textValue);
            let colorValue = '#' + utils.getValueTarget(data, locationText.colorDesc.value);
            if (this.checkIsColor(colorValue)) {
                locationText.color = colorValue;
            }
        }
    },
    setDefaultLocationIcon(locationIcon, path, data, url) {
        let iconValue = utils.getValueDefaultTarget(data, locationIcon.valueList);
        locationIcon.icon = url + iconValue;
    },
    checkIsColor(color) {
        var RegExp = /^#[0-9A-F]{6}$/i;
        if (RegExp.test(color)) {
            return true;
        }
        return false;
    },
    setLocationText(locationText, jsonText) {
        locationText.show = true;
        if ('color' in jsonText) {
            locationText.colorDesc = jsonText.color;
        }
        if ('description' in jsonText) {
            locationText.textDesc = jsonText.description;
            locationText.path = jsonText.description.path;
        } else {
            locationText.path = this.getPath(jsonText.value);
        }
        if ('ratio' in jsonText) {
            locationText.ratio = parseFloat(jsonText.ratio);
        }
        if (jsonText.type == 'normalText') {
            locationText.text = jsonText.value;
        } else {
            locationText.text = DEFAULT_VALUE;
        }
        locationText.paramText = jsonText.value;
    },
    getPath(value) {
        if (value.indexOf('${') != -1 && value.indexOf('}') != -1) {
            return value.substring(value.indexOf('${') + 2, value.indexOf('}'));
        } else {
            return '';
        }
    },
    setLocationIcon(locationIcon, jsonIcon) {
        locationIcon.path = jsonIcon.path;
        locationIcon.valueList = jsonIcon.value;
    },
    setLocationIconText(locationIconText, itemInfo) {
        locationIconText.text = itemInfo.iconText.value;
        locationIconText.path = itemInfo.icon.path;
        locationIconText.paramText = itemInfo.iconText.value;
    },
    setUpLeftTextSize(textItem, heightType, gravity, itemLocation) {
        if (heightType == HEIGHT_TYPE_LOW) {
            if (gravity == GRAVITY_CENTER || gravity == GRAVITY_CENTER_DIVIDER) {
                textItem.upLeft.size = CENTER_GRAVITY_LEFT_TEXT_SIZE;
            } else if (textItem.down.show && itemLocation == LOCATION_CENTER) {
                textItem.upLeft.size = CENTER_GRAVITY_LEFT_TEXT_SIZE;
            } else {
                textItem.upLeft.size = HEIGHT_TYPE_LOW_TEXT_SIZE;
            }
        } else if (gravity == GRAVITY_CENTER || gravity == GRAVITY_CENTER_DIVIDER) {
            textItem.upLeft.size = CENTER_GRAVITY_LEFT_TEXT_SIZE;
        } else if (itemLocation == LOCATION_CENTER) {
            textItem.upLeft.size = CENTER_GRAVITY_LEFT_TEXT_SIZE;
        } else {
            textItem.upLeft.size = DEFAULT_GRAVITY_LEFT_TEXT_SIZE;
        }
    },
    async itemClick() {
        // pass the click event to the enum control
        if (this.itemdata.type == UI_TYPE_ENUM) {
            await enumView.enumItemClick(this.itemdata.sid, this.itemdata.characteristic, this.itemdata.value);
        }
    }
};
