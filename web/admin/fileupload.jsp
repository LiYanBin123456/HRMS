<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">

    <title>My JSP 'fileupload.jsp' starting page</title>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <!--
    <link rel="stylesheet" type="text/css" href="styles.css">
    -->

</head>

<body>
<!-- enctype 默认是 application/x-www-form-urlencoded -->
<form action="/hrms/contract?op:'insertContract'" enctype="multipart/form-data" method="post" >

    用户名：<input type="text" name="name"> <br/>
    客户id：<input type="text" name="cid">
    签约时间:<input type="text" name="sign">
    生效时间:<input type="text" name="start">
    失效时间：<input type="text" name="end">
    合同类型：<input type="text" name="type">
    状态:<input type="text" name="status">
    合同文件：<input type="file" name="accessory"><br/>
    上传文件： <input type="file" name="file2"><br/>
    <input type="submit" value="提交"/>

</form>



</body>
</html>
