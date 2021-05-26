const ALPHA_DISABLE = 0.38;
const ALPHA_NORMAL = 1;

export default {
    data: {
        bundleName: '',
        abilityName: '',
        abilityType: ''
    },
    getValueTarget(data, valueList) {
        if (Array.isArray(valueList)) {
            let value = this.getValue(data, valueList);
            return value.target;
        } else {
            return data;
        }
    },
    getValueDefaultTarget(data, valueList) {
        if (Array.isArray(valueList)) {
            let value = this.getValue(data, valueList);
            return value.defaultTarget;
        } else {
            return data;
        }
    },
    getValue(data, valueList) {
        if (Array.isArray(valueList)) {
            data = parseInt(data);
            for (let i = 0; i < valueList.length; i++) {
                let valueScope = this.getValueScope(valueList[i].scope);
                if (valueScope.start == valueScope.end && valueScope.start == '-' ||
                valueScope.start == valueScope.end && valueScope.start !== '-' &&
                parseInt(valueScope.start) == data ||
                valueScope.start == '-' && data <= parseInt(valueScope.end) ||
                valueScope.end == '-' && data > parseInt(valueScope.start) ||
                valueScope.start !== '-' && valueScope.end !== '-' && parseInt(valueScope.start) < data &&
                data <= parseInt(valueScope.end)) {
                    return valueList[i];
                }
            }
        } else {
            return {};
        }
    },
    getValueScope(scope) {
        let valueScope = {
            start: '-',
            end: '-'
        };
        if (scope == '-') {
            return valueScope;
        } else if (scope.indexOf('-') !== -1) {
            let splits = scope.split('-');
            if (splits[0].length == 0) {
                valueScope.end = splits[1];
            } else if (splits[1].length == 0) {
                valueScope.start = splits[0];
            } else {
                valueScope.start = splits[0];
                valueScope.end = splits[1];
            }
        } else if (!isNaN(Number(scope))) {
            valueScope.start = scope;
            valueScope.end = scope;
        }
        return valueScope;
    },
    setActionParam(bundleName, abilityName, abilityType) {
        this.bundleName = bundleName;
        this.abilityName = abilityName;
        this.abilityType = abilityType;
    },
    makeAction(code, data) {
        let action = {};
        action.bundleName = this.bundleName;
        action.abilityName = this.abilityName;
        action.messageCode = code;
        action.abilityType = this.abilityType;
        action.data = data;
        return action;
    },
    setAlphaAndDisable(data, isDisable) {
        if (isDisable) {
            data.disableStack.push(isDisable);
            data.alpha = ALPHA_DISABLE;
            data.disable = true;
        } else {
            data.disableStack.pop();
            if (data.disableStack.length == 0) {
                data.alpha = ALPHA_NORMAL;
                data.disable = false;
            }
        }
    },
    getCurrentIndex(idx, templateUIInfo, uiType) {
        let index = -1;
        for (let i = 0; i <= idx; i++) {
            if (templateUIInfo[i].uiType == uiType) {
                index++;
            }
        }
        return index;
    }
};