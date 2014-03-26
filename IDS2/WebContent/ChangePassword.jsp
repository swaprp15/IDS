<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Change Password</title>
</head>
<body>
	<form action="ChangePassword">
		<h1>Change password</h1>
		<table>
			<tr>
				<td>Username : </td>
				<td><input name="username" type="text" size="15"/></td>
			</tr>
			<tr>
				<td>Old password : </td>
				<td><input name="oldPassword" type="password" size="15"/></td>
			</tr>
			<tr>
				<td>New password : </td>
				<td><input name="newPassword" type="password" size="15"/></td>
			</tr>
			<tr>
				<td clospan="2"><input name="change" type="submit" size="15"/></td>
			</tr>
		</table>
	</form>
</body>
</html>