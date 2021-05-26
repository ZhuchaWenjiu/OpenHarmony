import itemView from '../item/item.js';
import utils from '../../utils/utils.js';
import observed from '../../observed/observed.js';

const BACKGROUND_COLOR = '#FFFFFF';
const PROGRESS_HEIGHT = 64;
const MARGIN = 8;

class ProgressObj {
    constructor() {
        this.name = '';
        this.sid = '';
        this.characteristic = '';
        this.max = 0;
        this.min = 0;
        this.ratio = 0;
        this.operationValue = 0;
        this.leftItem = {};
        this.disableStack = [];
        this.disable = false;
        this.alpha = 1;
        this.backgroundColor = BACKGROUND_COLOR;
    }
}

export default {
    props: {
        progressData: {
            default: ''
        }
    },
    convertJson(idx, templateUIInfo, url) {
        let progressData = this.initProgressData(templateUIInfo[idx], url);
        return progressData;
    },
    initProgressData(progressUIInfo, url) {
        let progressData = new ProgressObj();
        progressData.name = progressUIInfo.name;
        progressData.sid = progressUIInfo.command[0].sid;
        progressData.characteristic = progressUIInfo.command[0].characteristic;
        progressData.max = progressUIInfo.command[0].max;
        progressData.min = progressUIInfo.command[0].min;
        progressData.ratio = progressUIInfo.command[0].ratio;
        progressData.operationValue = progressUIInfo.command[0].defaultValue;
        progressData.leftItem = itemView.convertJson(progressUIInfo.displayItemLeft, url,
            progressUIInfo.gravity, progressUIInfo.heightType, 'left');
        observed.addObserver(progressData.name, (key, value) => {
            utils.setAlphaAndDisable(progressData, value);
        });
        observed.addObserver(progressData.sid + '/' + progressData.characteristic, (key, value) => {
            progressData.operationValue = value;
        });
        return progressData;
    },
    valueChange(progressData) {
        this.progressData.operationValue = progressData.progress;
        if (progressData.isEnd == 'true') {
            observed.setKeyValue(this.progressData.sid + '/' +
            this.progressData.characteristic, this.progressData.operationValue);
        }
    },
    getProgressHeight() {
        return PROGRESS_HEIGHT + MARGIN;
    }
};