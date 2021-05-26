import utils from '../utils/utils.js';

const ACTION_MESSAGE_CODE_SUBSCRIBE = 1001;
const ACTION_MESSAGE_CODE_DATACHANGED = 1004;
const ACTION_MESSAGE_CODE_INIT_DEVICE_DATA = 1005;
const WAIT_TIME = 1000;

export default {
    data: {
        observers: {},
        timer: null
    },
    async setKeyValue(key, value) {
        let data = {};
        data[key] = value;
        let action = utils.makeAction(ACTION_MESSAGE_CODE_DATACHANGED, data);
        let that = this;
        that.data.timer = setTimeout(function () {
            that.notifyObservers('showMessage', {
                'show': true
            });
        }, WAIT_TIME);
        await FeatureAbility.callAbility(action);
    },
    async subscribeAbility() {
        let action = utils.makeAction(ACTION_MESSAGE_CODE_SUBSCRIBE, {});
        let that = this;
        await FeatureAbility.subscribeAbilityEvent(action, async function(callbackData) {
            let callbackJson;
            if ((typeof (JSON.parse(callbackData)) == "object") && ('data' in JSON.parse(callbackData))) {
                callbackJson = JSON.parse(callbackData)['data'];
            } else {
                callbackJson = JSON.parse(callbackData);
            }
            for (const key in callbackJson) {
                that.notifyObservers(key, callbackJson[key]);
            }
            if (that.data.timer != null) {
                clearTimeout(that.data.timer);
            }
            that.notifyObservers('showMessage', {
                'show': false
            });
        });
        action = utils.makeAction(ACTION_MESSAGE_CODE_INIT_DEVICE_DATA, {});
        await FeatureAbility.callAbility(action);
    },
    addObserver(key, callback) {
        if (Object.prototype.hasOwnProperty.call(this.data.observers, key)) {
            this.data.observers[key].push({
                callback: callback
            });
        } else {
            this.data.observers[key] = [{
                                            callback: callback
                                        }];
        }
    },
    notifyObservers(key, value) {
        if (Object.prototype.hasOwnProperty.call(this.data.observers, key)) {
            for (let i = 0; i < this.data.observers[key].length; i++) {
                this.data.observers[key][i].callback(key, value);
            }
        }
    }
};
