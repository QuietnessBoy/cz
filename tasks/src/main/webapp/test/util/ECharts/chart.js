function chart(data,box){
	var legend = [];
	var series = new Array();
	for(var x in data.params){
		legend = legend.concat(data.params[x].name);
		series = series.concat([{
			name: data.params[x].name[0],
            type: 'line',
            smooth: true,
            itemStyle: {
                normal: {
                    areaStyle: {
                        type: 'default'
                    }
                }
            },
            data: data.params[x].value
		}]);
	}
	var mychar = echarts.init(box);
   	var option = {
   		//图表的标题
        title: {
            text: data.title
        },
        animation: false,
        //图表展示的类别，这里的data和series中得name对应
        legend: {
            data: legend
        },
        //这里的是采用什么方式触发数据，这里显示的竖轴，跟随鼠标移动到节点上会显示内容
        tooltip: {
            trigger: 'axis'
        },
        //代表x轴，这里type类型代表字符
        xAxis: [{
            type: 'category',
            boundaryGap: false,//这里表示是否补齐空白
            data: data.xAxis
        }],
        //代表y轴，这里type类型代表num类型
        yAxis: [{
            type: 'value',
        }],
        //图表类型，type表示按照什么类型图表显示，line代表折线，下面的内容与
        //legend一一对应
        series: series
    };
    //配置图表设置并读取
    mychar.setOption(option);
}