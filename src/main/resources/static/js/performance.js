function Diagram(id, title, seriesNames, yAxisMax) {
    var that = this;

    this.seriesNames = seriesNames;
    this.seriesLength = seriesNames.length;
    this.seriesCopy = new Array(this.seriesLength);
    this.yAxisMax = yAxisMax;

    new Highcharts.chart(id, {
        chart: {
            type: 'line',
            events: {
                load: function () {
                    for (var i = 0; i < that.seriesLength; i++) {
                        that.seriesCopy[i] = this.series[i];
                    }
                }
            }
        },
        colors: ['#0066FF', '#00CCFF'],
        title: {
            text: title
        },
        xAxis: {
            type: 'datetime',
            minRange: 60 * 1000
        },
        yAxis: {
            title: {
                text: false
            },
            max: that.yAxisMax
        },
        legend: {
            enabled: false
        },
        plotOptions: {
            series: {
                threshold: 0,
                marker: {
                    enabled: false
                }
            }
        },
        series: (function () {
            var series = [];
            for (var i = 0; i < that.seriesLength; i++) {
                series.push({
                    name: that.seriesNames[i]
                });
            }
            return series;
        }())
    });

    this.addPoints = function (points) {
        var shift = this.seriesCopy[0].data.length > 60;
        for (var i = 0; i < points.length; i++) {
            this.seriesCopy[i].addPoint(points[i], true, shift, false);
        }
    };
}

var diagram1 = new Diagram('chart1', 'every 10 seconds', ['every 10 seconds', 'timestamp'], 200);
var diagram2 = new Diagram('chart2', 'every minute', ['every minute', 'timestamp'], 200);
var diagram3 = new Diagram('chart3', 'every hour', ['every hour', 'timestamp'], 200);

var eventSource1 = new EventSource('http://localhost:8080/average?window_size=10');
var eventSource2 = new EventSource('http://localhost:8080/average?window_size=60');
var eventSource3 = new EventSource('http://localhost:8080/average?window_size=3600');

eventSource1.onmessage = function (message) {

    var performance = JSON.parse(message.data);
    var time = performance.timestamp;

    diagram1.addPoints([
        [time, performance.moving_average_response_time]
    ]);

};

eventSource2.onmessage = function (message) {

    var performance = JSON.parse(message.data);
    console.log(performance);
    var time = performance.timestamp;

    diagram2.addPoints([
        [time, performance.moving_average_response_time]
    ]);

};

eventSource3.onmessage = function (message) {

    var performance = JSON.parse(message.data);
    console.log(performance);
    var time = performance.timestamp;

    diagram3.addPoints([
        [time, performance.moving_average_response_time]
    ]);

};