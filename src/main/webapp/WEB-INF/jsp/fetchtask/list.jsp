
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <c:forEach items="${taskList}" var="taskDO">
        <tr>
            <td><input type="text" name="taskId" value="${taskDO.id}" hidden></td>
            <td>${taskDO.taskName}</td>
            <td><button  onclick="taskStart('${taskDO.id}');" >启动</button> </td>
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
            url: '../../fetchTask/start',
            type: 'POST',
            data:data,
            error:function (data) {

            },
            success: function (data) {

            }
        });
    }
</script>
</html>
