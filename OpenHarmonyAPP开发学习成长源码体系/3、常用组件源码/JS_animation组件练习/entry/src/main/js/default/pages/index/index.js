//xxx.js
export default {
    data: {
        images: [
                {
                    src: "image/tu1.png",
                },
                {
                    src: "image/tu2.png",
                },
                {
                    src: "image/tu3.png",
                },
                {
                    src: "image/tu4.png",
                },
                {
                    src: "image/tu5.png",
                },
                {
                    src: "image/tu6.png",
                },
                {
                    src: "image/tu7.png",
                },
                {
                    src: "image/tu8.png",
                },
                {
                    src: "image/tu9.png",
                },
                {
                    src: "image/tu10.png",
                },
                {
                    src: "image/tu11.png",
                },
                {
                    src: "image/tu12.png",
                },
                {
                    src: "image/tu13.png",
                },
                {
                    src: "image/tu14.png",
                },
                {
                    src: "image/tu15.png",
                },
                {
                    src: "image/tu16.png",
                },
                {
                    src: "image/tu17.png",
                },
                {
                    src: "image/tu18.png",
                },
                {
                    src: "image/tu19.png",
                },
                {
                    src: "image/tu20.png",
                },
                {
                    src: "image/tu21.png",
                },
                {
                    src: "image/tu22.png",
                },
                {
                    src: "image/tu23.png",
                },
                {
                    src: "image/tu24.png",
                },
                {
                    src: "image/tu25.png",
                }
        ],
    },
    handleStart() {
        this.$refs.animator.start();
    },
    handlePause() {
        this.$refs.animator.pause();
    },
    handleResume() {
        this.$refs.animator.resume();
    },
    handleStop() {
        this.$refs.animator.stop();
    },
};