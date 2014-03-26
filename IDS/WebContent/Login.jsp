<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page language="java" %>
<%
Cookie cookies[]=request.getCookies();
//session.setAttribute("cookies", cookies)
if(cookies!=null){
	for(Cookie c:cookies){			
		if(c.getName().equals("user"))
			out.println("cookieval:"+c.getValue());
		else
			out.println("cookiename:"+c.getValue()+" cookiedomain:"+c.getDomain());
	}

}else{
	session.invalidate();
}
%>



<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Please Login</title>
<script  src="jquery.js"></script>
<script src="jquery.motionCaptcha.0.2.js"></script>
<link rel="stylesheet" href="captcha.css"></link>



</head>



<body>
	<form action="Login">
		<table>
			<tr>
				<td>Username :</td>
				<td><input name="username" size=15 type="text" /></td>
			</tr>
			<tr>
				<td>Password :</td>
				<td><input name="password" size=15 type="text" /></td>
			</tr>
			<tr>
				<td>
					<div id="mc-form" class="mc-active">
						<div id="mc">
							<p>
								Please draw the shape in the box to submit the form: (<a
									onclick="window.location.reload()" href="#"
									title="Click for a new shape">new shape</a>)
							</p>
							<canvas id="mc-canvas" width="220" height="154"> no
							support </canvas>
							<input type="hidden" id="mc-action"
								value="http://josscrowcroft.com/projects/motioncaptcha-jquery-plugin/">
						</div>
					</div>

				</td>
			</tr>
			<tr>
				<td><input  type="submit" value="login" id="login" disabled="disabled"/></td>
				<td>Click <a href="Registration.jsp"/>here</a> to register</td>
			</tr>
			<%= request.getAttribute("count") %>
			<%= request.getRemoteAddr()%>
		</table>
		
		
	</form>
	<script>

jQuery(document).ready(function($) {
	
	// Do the biznizz:
	$('#mc-form').motionCaptcha({
		shapes: ['triangle', 'x', 'rectangle', 'circle', 'check', 'zigzag', 'arrow', 'delete', 'pigtail', 'star']
	});
});

</script>

	
</body>
</html>