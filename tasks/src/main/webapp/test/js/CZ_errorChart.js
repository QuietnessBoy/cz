$(document).ready(function(){
	var getParams = function(){
		var timeFrame = $("#timeFrame>.active").text();
		return "timeFrame="+timeFrame;
	}
	var time;
	var t ;
	function show(){
		var i = 0;
		clearInterval(time);
		clearTimeout(t);
		time = setInterval(function(){
			i+=10;
			$("#time").text(i);
		},10);
		//ajax请求动态展示走势图
		$.ajax({
			method:"get",
			url:"../util/CZ_logChart.json",
			data:getParams(),
			dataType:"json",
			async:true,
			error:function(XMLHttpRequest, textStatus, errorThrown){
				console.log(XMLHttpRequest.status);
				console.log(XMLHttpRequest.readyState);
				console.log(textStatus);
				console.log(errorThrown);
				alert("请求失败！");
			},
			success:function(data){
				chart(data["chart"][0],document.getElementById("errorRequest"));
				chart(data["chart"][1],document.getElementById("errorResponse"));
			}
		});
	}
	show();
	//根据显示时段展示
	$("#timeFrame>button").click(function(){
		$(this).siblings().removeClass("active");
		$(this).addClass("active");
		show();
	})
})