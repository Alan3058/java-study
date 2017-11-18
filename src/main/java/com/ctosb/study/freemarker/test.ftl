select * from user where username = "${username}"
<#list users as user>
username = ${user.userName}
password = ${user.password}
</#list>