$(function () {

    console.log("进入js");
    var attest = $('#store');
    var attestContinue = $('#attestContinue');
    var attestDetail = $('#attestDetail');
    var sub = $('#sub');
    //attachEvent
    attestContinue.on('click',function () {
        console.log("选入续期模式");
        attest.attr("checked",false);
        attestDetail.hide();
    });
    attest.on('click',function () {
        console.log("选入存证模式");
        attestContinue.attr("checked",false);
        attestDetail.show();
    });
    sub.on("click",function () {
        if(confirm("是否提交订单?")){}

    })



});