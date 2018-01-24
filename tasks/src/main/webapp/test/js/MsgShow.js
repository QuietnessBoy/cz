$(function () {
    // 自定义标量
    var createTime;
    var msgNum;
    // 为各个队列名称
    var CreateFileCzQueue = "com.zhjl.tech.attest.CreateFileCz";
    var CreateFileCzByAddrQueue = "com.zhjl.tech.store.CreateFileCzByAddr";
    var CreateHashCzQueue = "com.zhjl.tech.attest.CreateHashCz";
    var CreateCzXqQueue = "com.zhjl.tech.attest.CreateCzXq";
    var datas1 = [];    // CreateFileCzQueue
    var datas2 = [];    // CreateFileCzByAddrQueue
    var datas3 = [];    // CreateHashCzQueue
    var datas4 = [];    // CreateCzXqQueue
    var num = [];
    var t;  // 获取"-"下标位置
    var ts; // 截取createTime字段
    var x_max;  //x轴最大值
    var max1;    //单个队列最大值
    var max2;    //单个队列最大值
    var max3;    //单个队列最大值
    var max4;    //单个队列最大值
    var interval;   // y轴间距

    // 页面初始化
    query();

    // 循环5秒调用query()
    window.setInterval(query, 5000);

    function query() {
        $.ajax({
            type: "get",
            //方法所在页面和方法名
            url: "/tasks/queryQueueMsg",
            contentType: "application/json;charset=UTF-8",
            dataType: "json",
            success: function (data) {
                $.each(data["obj"][CreateFileCzQueue], function (key, value) {
                    t = value["createTime"].indexOf("-");
                    ts = value["createTime"].substring(t + 1);
                    datas1[key] = [ts, value["num"]];
                    max1 = parseInt(value["num"]);
                    console.log("@@@@@@@@" + max1)
                    for (var i = 0; i < data["obj"][CreateFileCzQueue].length; i++) {
                        if (parseInt(value["num"][i]) > max1) {
                            max1 = parseInt(value["num"][i]);
                        }
                    }
                });
                $.each(data["obj"][CreateFileCzByAddrQueue], function (key, value) {
                    t = value["createTime"].indexOf("-");
                    ts = value["createTime"].substring(t + 1);
                    datas2[key] = [ts, value["num"]];
                    max2 = parseInt(value["num"]);
                    for (var i = 0; i < data["obj"][CreateFileCzByAddrQueue].length; i++) {
                        if (parseInt(value["num"][i]) > max2) {
                            max2 = parseInt(value["num"][i]);
                        }
                    }

                });
                $.each(data["obj"][CreateHashCzQueue], function (key, value) {
                    t = value["createTime"].indexOf("-");
                    ts = value["createTime"].substring(t + 1);
                    datas3[key] = [ts, value["num"]];
                    max3 = parseInt(value["num"]);
                    for (var i = 0; i < data["obj"][CreateHashCzQueue].length; i++) {
                        if (parseInt(value["num"][i]) > max3) {
                            max3 = parseInt(value["num"][i]);
                        }
                    }
                });
                $.each(data["obj"][CreateCzXqQueue], function (key, value) {
                    t = value["createTime"].indexOf("-");
                    ts = value["createTime"].substring(t + 1);
                    datas4[key] = [ts, value["num"]];
                    max4 = parseInt(value["num"]);
                    for (var i = 0; i < data["obj"][CreateCzXqQueue].length; i++) {
                        if (parseInt(value["num"][i]) > max4) {
                            max4 = parseInt(value["num"][i]);
                        }
                    }
                });
                num = [max1, max2, max3, max4];
                x_max = contrast(num);
                interval = x_max / 10;
                console.log("####" + x_max);
                console.log("@@@@@" + interval);

                chart(datas1, datas2, datas3, datas4, x_max, interval);
            }
        });
    }

    // 现状图属性设置
    function chart(datas1, datas2, datas3, datas4, x_max, interval) {
        $('#jqChart').jqChart({
            title: {
                text: '消息走势图'
            },
            axes: [
                {
                    location: 'left',//y轴位置，取值：left,right
                    minimum: 0,//y轴刻度最小值
                    maximum: x_max,//y轴刻度最大值
                    interval: interval//刻度间距
                }
            ],
            series: [
                //数据1开始
                {
                    type: 'line',//图表类型，取值：column 柱形图，line 线形图
                    title: 'CreateFileCz',//标题
                    data: datas1
                },
                //数据1结束
                //数据2
                {
                    type: 'line',
                    title: 'CreateFileCzByAddr',
                    data: datas2
                },
                //数据2结束
                //数据3
                {
                    type: 'line',
                    title: 'CreateHashCz',
                    data: datas3
                },
                //数据3结束
                //数据4
                {
                    type: 'line',
                    title: 'CreateCzXq',
                    data: datas4
                },
                //数据4结束
            ]
        });
    }

    // js函数，比较多个值得大小，设置x/y轴属性
    function contrast(num) {
        x_max = num[0];
        for (var i = 0; i < num.length; i++) {
            if (num[i] > x_max) {
                x_max = num[i];
            }
        }
        x_max = x_max + 200;
        return x_max;
    }
});