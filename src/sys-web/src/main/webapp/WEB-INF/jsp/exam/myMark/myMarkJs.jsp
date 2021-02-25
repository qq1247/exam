<%@ page language="java" pageEncoding="utf-8"%>
<script type="text/javascript">
	//定义变量
	var countdown = $("#countdown");//倒计时
	var curTimer;
	var endTimeStr ="${exam.markEndTime }";
	var endTime = new Date(
			endTimeStr.substring(0, 4),
			Number(endTimeStr.substring(5, 7) - 1),
			endTimeStr.substring(8, 10),
			endTimeStr.substring(11, 13),
			endTimeStr.substring(14, 16),
			endTimeStr.substring(17, 19)
			);
	
	//页面加载完毕，执行如下方法：
	$(function() {
		updateCountdown();
		setInterval(updateCountdown(), 5*60*1000);//5分钟和服务器同步一次，本地精度不够。
	});
	
	// 更新倒计时
	function updateCountdown() {
		$.ajax({
			url : "login/curTime",
			async : true,
			success : function(obj) {
				var serverTime = new Date(
					obj.data.substring(0, 4),
					Number(obj.data.substring(5, 7) - 1),
					obj.data.substring(8, 10),
					obj.data.substring(11, 13),
					obj.data.substring(14, 16),
					obj.data.substring(17, 19)
				);
				/* console.info(endTime.format("yyyy-MM-dd hh:mm:ss"));
				console.info(serverTime.format("yyyy-MM-dd hh:mm:ss")); */
				layui.util.countdown(endTime, serverTime, function(date, serverTime, timer) {
					if (curTimer) {
						clearTimeout(curTimer);
					}
					
					curTimer = timer;
					
					var timeLeft = "剩余时间：";
					if (date[0] > 0) {
						timeLeft += date[0] + "天";
					}
					timeLeft += date[1] + "时";
					timeLeft += date[2] + "分";
					timeLeft += date[3] + "秒";
					if (date[0] <= 0 && date[1] <= 0 && date[2] <= 4) {
						countdown.addClass("countdown-warn");
					} else {
						countdown.addClass("countdown-info");
					}
					countdown.html(timeLeft);
					
					if (date[0] <= 0 && date[1] <= 0 && date[2] <= 0 && date[3] <= 0) {
						doMark(true);
					}
				});
			}
		});
	}
	
	// 更新分数
	function scoreUpdate(curObj, myExamDetailId, maxScore) {
		var curInput = $(curObj);
		var curScore = curInput.val();
		var anCardOption = $("#examCard_" + myExamDetailId);
		anCardOption.removeClass("select");
		if (curScore == null || curScore == "") {
			layer.tips("必填项不能为空", curInput);
			curInput.val(null);
			setTimeout(function() {curInput.focus();});
			return;
		}
		
		var patt = /^(([1-9][0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/;
		if (!patt.test(curScore)) {
			layer.tips("必须为正数，最多保留两位小数", curInput);
			curInput.val(null);
			setTimeout(function() {curInput.focus();});
			return;
		}
		
		anCardOption.addClass("select");
		if (curScore < 0) {
			curScore = 0;
			curInput.val(0);
		}else if (curScore > maxScore) {
			curScore = maxScore;
			curInput.val(maxScore);
		}
		
		$.ajax({
			url : "myMark/doScoreUpdate",
			data : {
				myExamDetailId : myExamDetailId, 
				score : curScore
			},
			async : true, //异步提交
			success : function(obj) {
				if (!obj.succ) {
					layer.tips(obj.msg, curInput);
					curInput.val(null);
					setTimeout(function() {curInput.focus();});
				}
			}
		});
	}

	// 完成阅卷
	function doMark(auto) {
		$.ajax({
			url : "myMark/doMark",
			data : {myExamId : "${myExam.id }"},
			async : true, 
			success : function(obj) {
				if (!obj.succ) {
					layer.alert(obj.msg, {"title" : "提示消息"});
					return;
				}
				
				var message = auto ? "阅卷时间到！" : obj.msg;
				layer.alert(message, {"title" : "提示消息"});
				window.opener.myMarkDetailQuery();
				setTimeout("window.close();", 2000)
			}
		});
	}
</script>