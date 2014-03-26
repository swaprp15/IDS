package com.sns.login;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Product")
public class Product extends HttpServlet 
{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		//super.doGet(req, resp);
		
		int id = Integer.parseInt(req.getParameter("product_id"));
		
		try 
		{
			Connection conn = DBhelper.getConnection();
			
			PrintWriter writer = resp.getWriter();
			
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from Products where product_id = " + id);
			
			writer.println("<h1>Product details</h1>");
			
			writer.println("<table border=\"1\">");
			writer.println("<th>");
			writer.println("ID");
			writer.println("</th>");
			writer.println("<th>");
			writer.println("NAME");
			writer.println("</th>");
			writer.println("<th>");
			writer.println("DETAILS");
			writer.println("</th>");
			
			while(rs.next())
			{
				writer.println("<tr>");
				writer.println("<td>");
				writer.println(rs.getInt("product_id"));
				writer.println("</td>");
				writer.println("<td>");
				writer.println(rs.getString("name"));
				writer.println("</td>");
				writer.println("<td>");
				writer.println(rs.getString("details"));
				writer.println("</td>");
				writer.println("</tr>");
			}
			
			writer.println("</table border=\"1\">");
			
			stmt.close();
			conn.close();
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	}

}
