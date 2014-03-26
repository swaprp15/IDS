package com.sns.login;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;






import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sns.login.Blockip;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;



/**
 * Servlet implementation class Login
 */
@WebServlet("/Registration")
public class Registration extends HttpServlet 
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Registration() 
	{
		super();
		// TODO Auto-generated constructor stub
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		// TODO Auto-generated method stub
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		PrintWriter pw=response.getWriter();
		Connection conn = null;
		Statement stmt = null;
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		
		//get current date time with Date()
		Date date = new Date();
	 
		String currtime=dateFormat.format(date);
		
		//get current date time with Calendar()
		Calendar cal = Calendar.getInstance();
		String splitValue[] = currtime.split(" "); 
		
		boolean returnvalue = Blockip.block(request.getRemoteAddr(),splitValue[0]);
		
		if(returnvalue == true )
		{
			pw.print("Sorry... Too many requests from IP");
			return ;
		}
		
		try 
		{
			// STEP 3: Open a connection
			conn = DBhelper.getConnection();

			// STEP 4: Execute a query
			stmt = conn.createStatement();
			
			String sql;
			
			// First check for duplicate
			sql = "SELECT count(username) from STUDENT where username = '" + username + "'" ;
			
			ResultSet result = stmt.executeQuery(sql);
			
			while(result.next())
			{
				if(result.getInt(1) > 0)
				{
					pw.println("Username already exists. Please use another username.");
					stmt.close();
					conn.close();
					return;
				}
			}
		
		 
			sql="INSERT INTO STUDENT (username,password,blockvalue,timeStamp,cookie,browser,ipaddress) VALUES ('" + username + "','"+ password +"','" + 0 + "','" + null + "','null','null','null')";
			stmt.executeUpdate(sql);


			pw.println("Registration successful.");
			
			stmt.close();
			conn.close();
		}
		catch (SQLException se)
		{
			// Handle errors for JDBC
			se.printStackTrace();
		}
		catch (Exception e) 
		{
			// Handle errors for Class.forName
			e.printStackTrace();
		}
		finally 
		{
			// finally block used to close resources
			try 
			{
				if (stmt != null)
					stmt.close();
			}
			catch (SQLException se2) 
			{
			}
			// nothing we can do
			try 
			{
				if (conn != null)
					conn.close();
			}
			catch (SQLException se) 
			{	
				se.printStackTrace();
			}// end finally try
		}// end try
		
		System.out.println("Goodbye!");

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}