<%--
  Created by IntelliJ IDEA.
  User: ex-wangnaipeng001
  Date: 2018/7/20
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
    <form  id="taskAdd"  method="post" >
        <label>任务名称：</label>
        <input type="text" name="taskName"/><br/>
        <label>种子链接：</label>
        <input type="text" name="seedUrl"/><br/>
        <label>链接元素：</label>
        <input type="text" name="urlCssSelector"/><br/>
        <label>链接正则：</label>
        <input type="text" name="urlFilter"/><br/>
        <label>列表元素：</label>
        <input type="text" name="contentListCssSelector"/><br/>
        <label>提取字段：</label>
        <input type="text" name="fieldsCssSelector"/><br/>
        <label>链接模版：</label>
        <input type="text" name="urlTemplate"/><br/>
        <label>分页参数：</label>
        <input type="text" name="pageParams"/><br/>
        <label>分页页数：</label>
        <input type="text" name="pageSize"/><br/>
        <label>时间表达式：</label>
        <input type="text" name="cron"/><br/>
        <button type="submit" onclick="addTask()">提交</button>
    </form>
</body>
<script type="text/javascript">
    function addTask() {

        $.ajax({
            url: '../../task/addTask',
            type: 'POST',
            dataType: "json",
            data: $("#taskAdd").serialize(),
            error:function (data) {
                console.log("error:"+data)
            },
            success: function (data) {
                console.log("success:"+data)
            }
        });
    }
</script>
</html>
