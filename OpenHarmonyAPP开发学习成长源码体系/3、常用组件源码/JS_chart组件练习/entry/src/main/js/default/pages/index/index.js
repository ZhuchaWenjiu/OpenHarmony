// xxx.js
export default {
    data: {
        lineData: [
                {
                    strokeColor: '#0081ff',
                    fillColor: '#cce5ff',
                    data: [763, 550, 551, 554, 731, 654, 525, 696, 595, 628, 791, 505, 613, 575, 475, 553, 491, 680, 657, 716],
                    gradient: true,
                }
        ],
        barData: [
                {
                    fillColor: '#f07826',
                    data: [763, 550, 551, 554, 731, 654, 525, 696, 595, 628],
                },
                {
                    fillColor: '#cce5ff',
                    data: [535, 776, 615, 444, 694, 785, 677, 609, 562, 410],
                },
                {
                    fillColor: '#ff88bb',
                    data: [673, 500, 574, 483, 702, 583, 437, 506, 693, 657],
                },
        ],
        barOps: {
            xAxis: {
                min: 0,
                max: 20,
                display: false,
                axisTick: 10,
            },
            yAxis: {
                min: 0,
                max: 1000,
                display: false,
            },
        },
        lineOps: {
            xAxis: {
                min: 0,
                max: 20,
                display: false,
            },
            yAxis: {
                min: 0,
                max: 1000,
                display: false,
            },
            series: {
                lineStyle: {
                    width: "5px",
                    smooth: true,
                },
                headPoint: {
                    shape: "circle",
                    size: 20,
                    strokeWidth: 5,
                    fillColor: '#ffffff',
                    strokeColor: '#007aff',
                    display: true,
                },
                loop: {
                    margin: 2,
                    gradient: true,
                }
            }
        },
    },
    addData() {
        this.$refs.linechart.append({
            serial: 0,
            data: [Math.floor(Math.random() * 400) + 400]
        })
    }
}