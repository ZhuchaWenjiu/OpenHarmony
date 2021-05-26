export default {
    data: {
        scrollAmount: 20,
        loop: true,
        marqueeDir: 'left',
        marqueeCustomData: '富强、民主、文明、和谐, 自由、平等、公正、法治, 爱国、敬业、诚信、友善、',
        shudu: 30,
        chuchang : 'right',
        shudu_show:50,
    },
    onMarqueeBounce: function() {
        console.log("onMarqueeBounce");
    },
    onMarqueeStart: function() {
        console.log("onMarqueeStart");
    },
    onMarqueeFinish: function() {
        console.log("onMarqueeFinish");
    },
    onStartClick (evt) {
        this.$element('customMarquee').start();
    },
    onStopClick (evt) {
        this.$element('customMarquee').stop();
    },
    zhuo (evt) {
        this.chuchang = 'right'
    },
    you (evt) {
        this.chuchang = 'left'
    }
}