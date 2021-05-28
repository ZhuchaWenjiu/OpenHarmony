// @ts-nocheck
const injectRef = Object.getPrototypeOf(global) || global;

injectRef.regeneratorRuntime = require('@babel/runtime/regenerator');

/**
 * data request duration (ms)
 */
const TASK_DURATION = 2000;

/**
 * refresh result duration (ms)
 */
const REFRESH_RESULT_DURATION = 500;

export default {
    data: {
        isRefreshing: null,
        refreshResultIsVisibility: '',
        refreshResultColor: '',
        refreshResult: '',
        hideResult: ''
    },
    onInit() {
        this.refreshResultIsVisibility = 'hidden';
    },

/**
    * which simulates a long running  data request task, developer customization
    */
    requestData(delay) {
        return new Promise((resolve) => {
            setTimeout(() => {
                this.isRefreshing = false;
                // Simulate refresh results
                const messageNum = Math.round(Math.random());
                resolve(messageNum);
            }, delay);
        });
    },
    async showResult() {
        const refreshSucceed = this.$t('strings.refresh_bar.refreshSucceed');
        const refreshFailed = this.$t('strings.refresh_bar.refreshFailed');
        const refreshSucceedColor = this.$t('color.refresh_bar.refreshSucceed');
        const refreshFailedColor = this.$t('color.refresh_bar.refreshFailed');
        var that = this;

        try {
            const messageNum = await this.requestData(TASK_DURATION);

            if (messageNum != 0) {
                // Set the number of current refresh data
                this.refreshResult = refreshSucceed.replace('{count}', messageNum);
                this.refreshResultColor = refreshSucceedColor;
            } else {
                this.refreshResult = refreshFailed;
                this.refreshResultColor = refreshFailedColor;
            }

            this.refreshResultIsVisibility = 'visible';
            that.hideResult = '';
            setTimeout(function () {
                that.hideResult = 'hide-result';
            }, REFRESH_RESULT_DURATION);

        } catch (e) {
            console.error('requestData caught exception' + e);
        }
    },
    refresh: function (refreshingValue) {
        this.showResult();
        this.isRefreshing = refreshingValue.refreshing;
    }
};