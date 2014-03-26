package com.sns.login;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ChangePassword")
public class ChangePassword extends HttpServlet 
{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException 
	{
		String username = req.getParameter("username");
		String oldPassword = req.getParameter("oldPassword");
		String newPassword = req.getParameter("newPassword");
		
		PrintWriter writer = resp.getWriter();
		
		writer.println("<html>");
		writer.println("<head>");
		writer.println("</head>");
		writer.println("<body>");
		
		try 
		{
			Connection conn = DBhelper.getConnection();
			
			
			
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select password from Users where username = '" + username + "'");
			
			if(rs.next())
			{
				if(rs.getString("password").equals(oldPassword))
				{
					// update new password
					
					stmt.executeUpdate("update Users set password = '" + newPassword + "' where username = '" + username + "'" );
					writer.println("Passoword changed successfully.");
				}
				else
				{
					writer.println("old password did not match");
				}
			}
			else
			{
				writer.println("Username doesn't exist.");
			}
			
			writer.println("</br>Click <a href=\"/IDS2/Login.jsp\"> here </a> to go to Login page.");
			
			stmt.close();
			conn.close();
		}
		catch(Exception e)
		{
			writer.println("Failed to change password.");
		}
		writer.println("</body>");
		writer.println("</html>");
	}

}
