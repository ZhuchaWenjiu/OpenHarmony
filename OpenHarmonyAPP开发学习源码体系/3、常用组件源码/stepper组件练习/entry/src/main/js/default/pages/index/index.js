export default {
    data: {
        label_1:
        {
            prevLabel: 'BACK',
            nextLabel: 'NEXT',
            status: 'normal'
        },
        label_2:
        {
            prevLabel: 'BACK',
            nextLabel: 'NEXT',
            status: 'normal'
        },
        label_3:
        {
            prevLabel: 'BACK',
            nextLabel: 'NEXT',
            status: 'normal'
        },
    },
    setRightButton(e) {
        this.$element('mystepper').setNextButtonStatus({status: 'skip', label: 'SKIP'});
    },
    nextclick(e) {
        var index = {
            pendingIndex: e.pendingIndex
        }
        return index;
    },
    backclick(e) {
        var index = {
            pendingIndex: e.pendingIndex
        }
        return index;
    },
}