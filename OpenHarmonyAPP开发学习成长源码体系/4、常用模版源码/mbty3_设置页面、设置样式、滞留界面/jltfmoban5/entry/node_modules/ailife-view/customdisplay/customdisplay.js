class ItemObj {
    constructor(path, icon, iconNight, text, value) {
        this.path = path;
        this.icon = icon;
        this.iconNight = iconNight;
        this.text = text;
        this.value = value;
    }
}

class CustomDisplayItemObj {
    constructor(title) {
        this.title = title;
        this.itemList = [];
    }
}

export default {
    props: {
        customDisplayItemList: {
            default: []
        }
    },
    convertJson(deviceInfo) {
        let customDisplayItemList = [];
        for (let i = 0; i < deviceInfo.customDisplayUIInfo.length; i++) {
            let customDisplayItem = new CustomDisplayItemObj(deviceInfo.customDisplayUIInfo[i].title);
            let path = deviceInfo.customDisplayUIInfo[i].sid + '/' + deviceInfo.customDisplayUIInfo[i].characteristic;
            for (let j = 0; j < deviceInfo.customDisplayUIInfo[i].itemList.length; j++) {
                let item = new ItemObj(path, deviceInfo.iconUrl + deviceInfo.customDisplayUIInfo[i].itemList[j].icon,
                    deviceInfo.iconUrl + deviceInfo.customDisplayUIInfo[i].itemList[j].iconNight,
                    deviceInfo.customDisplayUIInfo[i].itemList[j].text,
                    deviceInfo.customDisplayUIInfo[i].itemList[j].value);
                customDisplayItem.itemList.push(item);
            }
            customDisplayItemList.push(customDisplayItem);
        }
        return customDisplayItemList;
    },
    iconClick(item) {
        let customDisplayData = {
            path: item.path,
            value: item.value,
            title: item.text
        };
        this.$emit('iconClick', customDisplayData);
    }
};