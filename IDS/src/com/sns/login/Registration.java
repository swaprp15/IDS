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
		String uname = request.getParameter("username");
		String pwd = request.getParameter("password");
		
		System.out.println("User name : " + uname);
		PrintWriter pw=response.getWriter();
		Connection conn = null;
		Statement stmt = null;
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		
		//get current date time with Date()
		Date date = new Date();
		System.out.println("Current date : " + dateFormat.format(date));
	 
		String currtime=dateFormat.format(date);
		
		//get current date time with Calendar()
		Calendar cal = Calendar.getInstance();
		System.out.println(dateFormat.format(cal.getTime()));
		String splitValue[] = currtime.split(" "); 
		
		boolean returnvalue=Blockip.block(request.getRemoteAddr(),splitValue[0]);
		
		
		//boolean returnvalue=Blockip.block(request.getRemoteAddr());
		if(returnvalue == true )
		{
			pw.print("Sorry... Too many requests from IP");
			return ;
		}
		
		try {
			// STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/IDS",
					"root", "mysql");

			// STEP 4: Execute a query
			System.out.println("Creating statement...");
			stmt = conn.createStatement();
			String sql;
		
		 
			sql="INSERT INTO STUDENT (username,password,blockvalue,timeStamp,cookie,browser,ipaddress) VALUES ('"+uname+"','"+pwd+"','"+0+"','"+null+"','q','q','q')";
			stmt.executeUpdate(sql);

			
			stmt.close();
			conn.close();
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			}// nothing we can do
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
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