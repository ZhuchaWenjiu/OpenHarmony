import itemView from '../item/item.js';
import utils from '../../utils/utils.js';
import observed from '../../observed/observed.js';

const INTERVAL = 200;
const ICON_LEFT = 24;
const ICON_TOP = 16;
const ICON_RIGHT = 48;
const ICON_BOTTOM = 40;
const INCREASE_DECREASE_HEIGHT = 64;
const MARGIN = 8;

class IncreaseDecreaseObj {
    constructor() {
        this.name = '';
        this.sid = '';
        this.characteristic = '';
        this.defaultValue = 0;
        this.operationValue = 0;
        this.iconDecrease = '';
        this.iconIncrease = '';
        this.ableIconDecrease = '';
        this.ableIconIncrease = '';
        this.disableIconDecrease = '';
        this.disableIconIncrease = '';
        this.decreaseDownStartTime = 0;
        this.decreaseDown = false;
        this.decreaseDownLong = false;
        this.increaseDownStartTime = 0;
        this.increaseDown = false;
        this.increaseDownLong = false;
        this.max = 0;
        this.min = 0;
        this.ratio = 0;
        this.step = 0;
        this.item = {};
        this.disableStack = [];
        this.disable = false;
        this.alpha = 1;
        this.backgroundColor = '#FFFFFF';
    }
}

export default {
    props: {
        increasedecreaseData: {
            default: ''
        }
    },
    convertJson(idx, templateUIInfo, url) {
        let increaseDecreaseData = this.initIncreaseDecreaseData(templateUIInfo[idx], url);
        return increaseDecreaseData;
    },
    initIncreaseDecreaseData(increasedecreaseUIInfo, url) {
        let increaseDecreaseData = new IncreaseDecreaseObj();
        increaseDecreaseData.name = increasedecreaseUIInfo.name;
        increaseDecreaseData.sid = increasedecreaseUIInfo.command[0].sid;
        increaseDecreaseData.characteristic = increasedecreaseUIInfo.command[0].characteristic;
        increaseDecreaseData.defaultValue = increasedecreaseUIInfo.command[0].defaultValue;
        increaseDecreaseData.operationValue = increasedecreaseUIInfo.command[0].defaultValue;
        increaseDecreaseData.ableIconDecrease = increasedecreaseUIInfo.command[0].iconDecrease;
        increaseDecreaseData.ableIconIncrease = increasedecreaseUIInfo.command[0].iconIncrease;
        increaseDecreaseData.disableIconDecrease = increasedecreaseUIInfo.command[0].disableIconDecrease;
        increaseDecreaseData.disableIconIncrease = increasedecreaseUIInfo.command[0].disableIconIncrease;
        increaseDecreaseData.max = increasedecreaseUIInfo.command[0].max;
        increaseDecreaseData.min = increasedecreaseUIInfo.command[0].min;
        increaseDecreaseData.ratio = increasedecreaseUIInfo.command[0].ratio;
        increaseDecreaseData.step = increasedecreaseUIInfo.command[0].step;
        increaseDecreaseData.item = itemView.convertJson(increasedecreaseUIInfo.displayItem, url,
            increasedecreaseUIInfo.gravity, increasedecreaseUIInfo.heightType, 'center');
        observed.addObserver(increaseDecreaseData.name, (key, value) => {
            utils.setAlphaAndDisable(increaseDecreaseData, value);
        });
        observed.addObserver(increaseDecreaseData.sid + '/' + increaseDecreaseData.characteristic, (key, value) => {
            increaseDecreaseData.operationValue = value;
            if (value <= increaseDecreaseData.min) {
                increaseDecreaseData.iconDecrease = url + increaseDecreaseData.disableIconDecrease;
            } else {
                increaseDecreaseData.iconDecrease = url + increaseDecreaseData.ableIconDecrease;
            }
            if (value >= increaseDecreaseData.max) {
                increaseDecreaseData.iconIncrease = url + increaseDecreaseData.disableIconIncrease;
            } else {
                increaseDecreaseData.iconIncrease = url + increaseDecreaseData.ableIconIncrease;
            }
        });
        return increaseDecreaseData;
    },
    onTouchStart(isIncrease) {
        let isClickEnable = this.isClickEnable(false);
        if (this.increasedecreaseData.disable || !isClickEnable) {
            return;
        }
        if (isIncrease) {
            this.increasedecreaseData.increaseDown = true;
            this.increasedecreaseData.increaseDownStartTime = new Date().getTime();
        } else {
            this.increasedecreaseData.decreaseDown = true;
            this.increasedecreaseData.decreaseDownStartTime = new Date().getTime();
        }
    },
    onTouchMove(isIncrease, event) {
        if (this.increasedecreaseData.disable) {
            return;
        }
        let localX = event.touches[0].localX;
        let localY = event.touches[0].localY;
        if (localX < ICON_LEFT || localY < ICON_TOP || localX > ICON_RIGHT || localY > ICON_BOTTOM) {
            this.onTouchEnd();
        } else {
            let currentTime = new Date().getTime();
            if (isIncrease) {
                if (currentTime - this.increasedecreaseData.increaseDownStartTime > INTERVAL) {
                    this.increasedecreaseData.increaseDownStartTime =
                    this.increasedecreaseData.increaseDownStartTime + INTERVAL;
                    this.increasedecreaseData.increaseDownLong = true;
                    this.increaseDecreaseValue(true);
                }
            } else if (currentTime - this.increasedecreaseData.decreaseDownStartTime > INTERVAL) {
                this.increasedecreaseData.decreaseDownStartTime =
                this.increasedecreaseData.decreaseDownStartTime + INTERVAL;
                this.increasedecreaseData.decreaseDownLong = true;
                this.increaseDecreaseValue(false);
            }
        }
    },
    onTouchEnd(isIncrease) {
        if (isIncrease) {
            if (this.increasedecreaseData.disable || !this.increasedecreaseData.increaseDown) {
                return;
            }
            this.increasedecreaseData.increaseDown = false;
            if (!this.increasedecreaseData.increaseDownLong) {
                this.increaseDecreaseValue(true);
            }
            this.increasedecreaseData.increaseDownLong = false;
        } else {
            if (this.increasedecreaseData.disable || !this.increasedecreaseData.decreaseDown) {
                return;
            }
            this.increasedecreaseData.decreaseDown = false;
            if (!this.increasedecreaseData.decreaseDownLong) {
                this.increaseDecreaseValue(false);
            }
            this.increasedecreaseData.decreaseDownLong = false;
        }
    },
    isClickEnable(isIncrease) {
        let isValueEnable = this.isValueEnable(isIncrease);
        if (!isValueEnable) {
            return false;
        }
        return true;
    },
    increaseDecreaseValue(isIncrease) {
        let operationValue = this.getOperationValue(isIncrease);
        if (operationValue == this.increasedecreaseData.operationValue) {
            return;
        }
        observed.setKeyValue(this.increasedecreaseData.sid + '/' +
        this.increasedecreaseData.characteristic, operationValue);
    },
    isValueEnable(isIncrease) {
        if (isIncrease) {
            if (this.increasedecreaseData.operationValue >= this.increasedecreaseData.max) {
                return false;
            }
        } else if (this.increasedecreaseData.operationValue <= this.increasedecreaseData.min) {
            return false;
        }
        return true;
    },
    getOperationValue(isIncrease) {
        let value = this.increasedecreaseData.operationValue;
        let operationValue = value;
        if (isIncrease) {
            if (value > this.increasedecreaseData.max) {
                return value;
            }
            if (value + this.increasedecreaseData.step > this.increasedecreaseData.max) {
                operationValue = this.increasedecreaseData.max;
            } else {
                operationValue = value + this.increasedecreaseData.step;
            }
        } else {
            if (value <= this.increasedecreaseData.min) {
                return value;
            }
            if (value - this.increasedecreaseData.step < this.increasedecreaseData.min) {
                operationValue = this.increasedecreaseData.min;
            } else {
                operationValue = value - this.increasedecreaseData.step;
            }
        }
        return operationValue;
    },
    getIncreasedecreaseHeight() {
        return INCREASE_DECREASE_HEIGHT + MARGIN;
    }
};