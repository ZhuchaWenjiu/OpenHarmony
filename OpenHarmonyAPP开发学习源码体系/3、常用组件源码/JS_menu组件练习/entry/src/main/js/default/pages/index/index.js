// @ts-nocheck
export default {
    onMenuSelected(e) {
        prompt.showToast({
            message: e.value
        })
    },
    onMenu_Two(e) {
        prompt.showToast({
            message: e.value
        })
    },
    onTextClick() {
        this.$element("apiMenu").show({x:360,y:0});
    },
    onTextClick_Two() {
        this.$element("apiMenu_Two").show({x:200,y:550});
    }
}