<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: ex-wangnaipeng001
  Date: 2018/7/23
  Time: 14:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
</head>
<body>
<table class="">
    <tr>
        <th>#</th>
        <th>任务名称</th>
        <th>时间表达式</th>
    </tr>
    <c:forEach items="${taskList}" var="taskConfig">
        <tr>
            <td><input type="text" name="taskId" value="${taskConfig.id}" hidden></td>
            <td>${taskConfig.taskName}</td>
            <td>${taskConfig.cron}</td>
            <td><button  onclick="taskStart('${taskConfig.id}');" >启动</button> </td>
            <td><button  onclick="" >删除</button> </td>
        </tr>
    </c:forEach>
</table>
</body>
<script type="text/javascript">
    function taskStart(id) {
        // var id =$("input[name='taskId']").val();
        var data = {id:id};
        $.ajax({
            url: '../../task/start',
            type: 'POST',
            data:data,
            error:function (data) {
                alert("false")
            },
            success: function (data) {

            }
        });
    }
</script>
</html>
