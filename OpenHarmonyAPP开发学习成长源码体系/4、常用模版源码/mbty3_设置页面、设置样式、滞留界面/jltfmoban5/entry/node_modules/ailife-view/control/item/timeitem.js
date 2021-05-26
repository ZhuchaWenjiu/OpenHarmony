import observed from '../../observed/observed.js';

const ONE_SECOND = 1000;
const SIXTY = 60;
const SHOW_DIGITS = 2;

class ItemObj {
    constructor() {
        this.time = '';
        this.desc = '';
        this.show = false;
    }
}

export default {
    props: {
        itemdata: {
            default: {}
        }
    },
    data: {
        second: -1
    },
    convertJson(itemInfo) {
        let itemData = new ItemObj();
        itemData.desc = itemInfo.textDisplay.down.value;
        let path = this.getPath(itemInfo.textDisplay.upLeft.value);
        observed.addObserver(path, (key, value) => {
            this.data.second = value;
            if (value < 0) {
                itemData.show = false;
            } else {
                itemData.show = true;
            }
            itemData.time = this.convertTime(value);
            let interval = setInterval(() => {
                value = value - 1;
                if (value < 0 || this.data.second < 0) {
                    clearInterval(interval);
                } else {
                    itemData.time = this.convertTime(value);
                }
            }, ONE_SECOND);
        });
        return itemData;
    },
    getPath(value) {
        return value.substring(value.indexOf('${') + 2, value.indexOf('}'));
    },
    convertTime(value) {
        // convert seconds to hh:mm:ss
        let hours = parseInt(value / SIXTY / SIXTY);
        let minutes = parseInt((value - hours * SIXTY * SIXTY) / SIXTY);
        let seconds = parseInt(value - hours * SIXTY * SIXTY - minutes * SIXTY);
        return ('00' + hours).slice(-SHOW_DIGITS) + ':' +
        ('00' + minutes).slice(-SHOW_DIGITS) + ':' + ('00' + seconds).slice(-SHOW_DIGITS);
    },
};
