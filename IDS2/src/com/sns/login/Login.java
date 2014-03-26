package com.sns.login;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	static int count=0;
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	
	public boolean updateInDb(HttpServletRequest request,HttpServletResponse response,String userName) throws ServletException, IOException 
	{
		try{
				Connection conn = null;
				Statement stmt = null;
				System.out.println("In Update");
				//Class.forName("com.mysql.jdbc.Driver");
		
				// STEP 3: Open a connection
				System.out.println("Connecting to database...");
				//conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/IDS",
				//		"root", "mysql");
		
				conn = DBhelper.getConnection();
				
				// STEP 4: Execute a query
				System.out.println("Creating statement...1");
				stmt = conn.createStatement();
				Cookie cookies[]=request.getCookies();
				//session.setAttribute("cookies", cookies);
				String sql;
				sql = "SELECT * FROM Users where username='"+userName+"'";
				System.out.println("Creating statement...2");
				ResultSet rs = stmt.executeQuery(sql);
				System.out.println("Creating statement...3");
				int flag=0;
				// STEP 5: Extract data from result set
			
				boolean tobeUpdated;
				String cookiesInDb="";
				String browserInDb="";
				String ipAddressInDb="";
					// Retrieve by column name
				rs.next();
				cookiesInDb = rs.getString("cookie");
				browserInDb = rs.getString("browser");
				ipAddressInDb = rs.getString("ipaddress");
				
				
			
				String CookieValuetoBeStoredInDb="";
				
				if(cookies!=null)
				{
					for(Cookie c:cookies)
					{	
						CookieValuetoBeStoredInDb = c.getValue();
						if(c.getName().equals("user"))
							System.out.println("cookieval:"+c.getValue());
						else
							System.out.println("cookiename:"+c.getValue()+" cookiedomain:"+c.getDomain());
					}
				}
				else
				{
					request.getSession().invalidate();
				}
				
				
				String ipAddressToBeStoredInDb=request.getRemoteAddr();
				System.out.println("ipaddress="+ipAddressToBeStoredInDb);
				String userAgent = request.getHeader("user-agent");
				System.out.println("browser name="+userAgent);
				String browserNameToBeStoredInDb = "unknown";
				String browserVer = "unknown";
				//String userAgent = request.getHeader("user-agent");
				if (userAgent.contains("Chrome")) { //checking if Chrome
					System.out.println("----chro------");
					String substring = userAgent.substring(
							userAgent.indexOf("Chrome")).split(" ")[0];
					browserNameToBeStoredInDb = substring.split("/")[0];
					browserVer = substring.split("/")[1];
				} else if (userAgent.contains("Firefox")) { //Checking if Firefox
					System.out.println("----fire------");
					String substring = userAgent.substring(
							userAgent.indexOf("Firefox")).split(" ")[0];
					browserNameToBeStoredInDb = substring.split("/")[0];
					browserVer = substring.split("/")[1];
				} else if (userAgent.contains("MSIE")) { //Checking if Internet Explorer
					String substring = userAgent.substring(
							userAgent.indexOf("MSIE")).split(";")[0];
					browserNameToBeStoredInDb = substring.split(" ")[0];
					browserVer = substring.split(" ")[1];
				}
				
				System.out.println("----1------"+cookiesInDb);
				
				// First login
				if(cookiesInDb.trim().compareToIgnoreCase("null")==0)
				{
					System.out.println("Value="+CookieValuetoBeStoredInDb);
					String valueToBeUpdated="UPDATE Users SET cookie='"+CookieValuetoBeStoredInDb+"', ipaddress='"+ipAddressToBeStoredInDb+"' , browser='"+browserNameToBeStoredInDb+"' WHERE username = '"+userName+"'";
					stmt.executeUpdate(valueToBeUpdated);
					return true;
				}
				else
				{
					String tokenForCookie[] = cookiesInDb.split("-");
					String tokenForBrowser[] = browserInDb.split("-");
					String tokenipAddress[] = ipAddressInDb.split("-");
					
					boolean cookiesMatch = false;
					int i;
					for(i=0;i<tokenForCookie.length;i++)
					{
						if(tokenForCookie[i].compareToIgnoreCase(CookieValuetoBeStoredInDb)==0)
						{
							cookiesMatch = true;
							 break;
						}
					}
					
					if(cookiesMatch == true)
					{
						// If from same browser and same IP address then allow.
						if(tokenForBrowser[i].compareToIgnoreCase(browserNameToBeStoredInDb)==0 && tokenipAddress[i].compareToIgnoreCase(ipAddressToBeStoredInDb)==0)
						{
							//System.out.println("true1");
							return true;
						}
						
						// Stored browser and address do NOT match
						return false;
					}
					else
					{
						// New cookie from user
						
						CookieValuetoBeStoredInDb = cookiesInDb+"-"+CookieValuetoBeStoredInDb ;
						ipAddressToBeStoredInDb = ipAddressInDb +"-" + ipAddressToBeStoredInDb;
						browserNameToBeStoredInDb = browserInDb + "-" + browserNameToBeStoredInDb;
						String valueToBeUpdated="UPDATE Users SET cookie='"+CookieValuetoBeStoredInDb+"', ipaddress='"+ipAddressToBeStoredInDb+"' , browser='"+browserNameToBeStoredInDb+"' WHERE username = '"+userName+"'";
						stmt.executeUpdate(valueToBeUpdated);
						System.out.println("true2");
						return true;
					}
				}
				//return false;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		System.out.println("false1");
		return false;

	}
	public Login() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		// TODO Auto-generated method stub
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		String userValue="";
		
		System.out.println("User name : " + username);
		
		PrintWriter pw=response.getWriter();
		
		Connection conn = null;
		Statement stmt = null;
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		   
		//get current date time with Date()
		Date date = new Date();
		System.out.println(dateFormat.format(date));
 
		String currtime=dateFormat.format(date);
		//get current date time with Calendar()
		Calendar cal = Calendar.getInstance();
		System.out.println(dateFormat.format(cal.getTime()));
		String splitValue[] = currtime.split(" "); 
		   
		boolean returnvalue=Blockip.block(request.getRemoteAddr(),splitValue[0]);
		
		if(returnvalue == true)
		{
			pw.print("sorry... Your account has been blocked.");
			return ;
		}
		//request.getAttribute("cookies");
		
	/*	Cookie cookies[]=request.getCookies();
		//session.setAttribute("cookies", cookies);
		if(cookies!=null){
			for(Cookie c:cookies){			
				if(c.getName().equals("user"))
					System.out.println("cookieval:"+c.getValue());
				else
					System.out.println("cookiename:"+c.getValue()+" cookiedomain:"+c.getDomain());
			}

		}else{
			request.getSession().invalidate();
		}
		*/
	
		try {
			// STEP 2: Register JDBC driver
			//Class.forName("com.mysql.jdbc.Driver");

			// STEP 3: Open a connection
			System.out.println("Connecting to database...");
			//conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/IDS",
			//		"root", "mysql");

			conn = DBhelper.getConnection();			
					
			// STEP 4: Execute a query
			//System.out.println("Creating statement...1");
			stmt = conn.createStatement();
			//System.out.println("Creating statement...2");
			String sql;
			
			//************************* SQL Injection not possible ******************
			
//			//System.out.println("Creating statement...");
//			sql = "SELECT * FROM Users";
//			//System.out.println("Creating statement...");
//			ResultSet rs = stmt.executeQuery(sql);
//			//System.out.println("Creating statement...");
//			int flag=0;
//			// STEP 5: Extract data from result set
//			String username="";
//			String password="";
//			String blockValue="";
//			
//			boolean tobeUnlocked;
//			while (rs.next()) 
//			{
//				// Retrieve by column name
//				username = rs.getString("username");
//				password = rs.getString("password");
//				blockValue = rs.getString("blockvalue");
//				if(username.compareToIgnoreCase(uname)==0 && password.compareToIgnoreCase(pwd)==0)
//				{
//					// User name and password match
//					flag=1;
//					break;
//				}
//				else if(username.compareToIgnoreCase(uname)==0)
//				{
//					// User name matches but pwd doesn't
//					userValue =  username;
//					flag=2;
//					break;
//				}
//				
//			//	System.out.println("username:"+username);
//			//	System.out.println("password:"+password);
//			//	pw.println(username);
//			}
//
//			if(flag == 1) // Username and password match
//			{
//				// But if user is blocked
//				if(blockValue.compareToIgnoreCase("1")==0)
//				{
//					String time = rs.getString("timeStamp");
//					String token[] = time.split(" ");
//					String dateValue[] = token[0].split("/");
//					String timeValue[] = token[1].split(":"); 
//					
//					int dateinInt = Integer.parseInt(dateValue[2]);
//					int hourinInt = Integer.parseInt(timeValue[0]);
//					int minInint = Integer.parseInt(timeValue[1]);
//					int secInInt =  Integer.parseInt(timeValue[2]);
//					
//					String currtoken[] = currtime.split(" ");
//					String currdateValue[] = currtoken[0].split("/");
//					String currtimeValue[] = currtoken[1].split(":"); 
//					int currdateinInt = Integer.parseInt(currdateValue[2]);
//					int currhourinInt = Integer.parseInt(currtimeValue[0]);
//					int currminInint = Integer.parseInt(currtimeValue[1]);
//					int currsecInInt =  Integer.parseInt(currtimeValue[2]);
//					
//					
//					tobeUnlocked = false;
//					if(dateinInt > currdateinInt+1)
//					{
//						tobeUnlocked = true;
//						
//					}
//					else if(dateinInt == currdateinInt + 1 )
//					{
//						if(currhourinInt > hourinInt+1)
//						{
//							tobeUnlocked = true;
//						}
//						else if(currhourinInt == hourinInt +1)
//						{
//							if(currminInint > minInint +1)
//							{
//								tobeUnlocked = true;
//							}
//							else if(currminInint == minInint + 1)
//							{
//								if(currsecInInt >= secInInt)
//								{
//									tobeUnlocked = true;
//								}
//								
//							}
//						}
//					}
//					if(tobeUnlocked == true)
//					{
//						blockValue = "0";
//						String valueToBeUpdated="UPDATE Users SET blockvalue=0, timeStamp = '"+null+"' WHERE username = '"+username+"'";
//						stmt.executeUpdate(valueToBeUpdated);
//						
//						boolean returnValue=updateInDb(request,response,username);
//						//pw.println("Congrates Login ");
//						if(returnValue ==  true)
//						{
//							RequestDispatcher RequetsDispatcherObj =request.getRequestDispatcher("/Success.jsp");
//							RequetsDispatcherObj.forward(request, response);
//						}
//						else
//						{
//							pw.println("Sorry... Cookie and address did not match");
//						}
//					}
//					else
//					{
//						pw.println("Sorry... Your account has been blocked for 1 day.");
//					}
//				}
//				else // User is not blocked hence login successful..
//				{
//					
//					
//					boolean returnValue=updateInDb(request, response, username);
//					
//		//			String valueToBeUpdated="UPDATE Users SET blockvalue=0, timeStamp = '"+null+"' WHERE username = '"+username+"'";
//		//			stmt.executeUpdate(valueToBeUpdated);
//					if(returnValue ==  true)
//					{
//						RequestDispatcher RequetsDispatcherObj =request.getRequestDispatcher("/Success.jsp");
//						RequetsDispatcherObj.forward(request, response);
//					}
//					else
//					{
//						pw.println("Sorry... Cookie and address did not match");
//					}
//				//	RequestDispatcher RequetsDispatcherObj =request.getRequestDispatcher("/Success.jsp");
//				//	RequetsDispatcherObj.forward(request, response);
//					
//				}
//				
//			}
//			else if(flag == 2) // Password did not match
//			{	//pw.println("Sorry...");
//				count = count +1;
//				
//				if(count == 3) // Block user
//				{
//					System.out.print("-----");
//					System.out.println(dateFormat.format(date));
//					String valueToBeUpdated="UPDATE Users SET blockvalue=1, timeStamp = '"+dateFormat.format(date)+"' WHERE username = '"+userValue+"'";
//					stmt.executeUpdate(valueToBeUpdated);
//				}
//				
//				request.setAttribute("count", count);
//				pw.println("Password did not match");
//				RequestDispatcher RequetsDispatcherObj =request.getRequestDispatcher("/Login.jsp");
//				RequetsDispatcherObj.forward(request, response);
//				
//				
//			}
//			else
//			{
//				pw.println("Sorry... Username did not match");
//			}
			
			//************************* SQL Injection not possible ******************
			
			
			//************************* SQL Injection possible ********************
			
			int flag = 0;
			
			sql = "select * from Users where username = '" + username + "' and password = '" + password + "'";
			
			ResultSet result = stmt.executeQuery(sql);
			
			if(result.next())
			{
				String valueToBeUpdated="UPDATE Users SET blockvalue=0, timeStamp = '"+null+"' WHERE username = '"+username+"'";
				stmt.executeUpdate(valueToBeUpdated);
						
				boolean returnValue=updateInDb(request,response,username);
				//pw.println("Congrates Login ");
				if(returnValue ==  true)
				{
					RequestDispatcher RequetsDispatcherObj =request.getRequestDispatcher("/Success.jsp");
					RequetsDispatcherObj.forward(request, response);
				}
				else
				{
					pw.println("Sorry... Cookie and address did not match");
				}
			}
			else
			{
				pw.println("Sorry... Invalid credentials.");
			}
			
			//************************* SQL Injection possible ********************
			
			// STEP 6: Clean-up environment
			result.close();
			stmt.close();
			//conn.close();
			DBhelper.closeConnection();
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