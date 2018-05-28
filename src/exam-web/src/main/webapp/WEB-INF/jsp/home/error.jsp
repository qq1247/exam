<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="my" uri="myTag/core"%>
<!DOCTYPE html>
<html>
	<head>
		<title>在线考试系统</title>
		<%@include file="/script/home/common.jspf"%>
	</head>
	<body class="homepage" style="background-color: #f2f2f2">
		<%@include file="/script/home/head.jspf"%>
		<section class="service-item" style="padding-top: 100px;">
			<div class="container">
				<div class="center wow fadeInDown">
					<h2>${message }</h2>
				</div>
			</div>
		</section>
		<%@include file="/script/home/footer.jspf"%>
	</body>
</html>