<html>
<head>
	<title>aaa</title>
</head>
<body>

	
	列表:<table border="1">
		<tr><th>序号</th><th>id</th><th>name</th></tr>
		<#list studentList as item>
		
		<#if item_index % 2 == 0>
			<tr bgcolor="red">
			<td>${item_index}</td>
			<td>${item.id}</td>
			<td>${item.name}</td>
			</tr>
		<#else>
			<tr bgcolor="blue">
			<td>${item_index}</td>
			<td>${item.id}</td>
			<td>${item.name}</td>
			</tr>
		</#if>
			
		</#list>
	</table>
	
	当前日期：${date?string('yyyy/MM/dd HH:mm:ss')}<br/>
	
	<#include "hello.ftl">
</body>
</html>