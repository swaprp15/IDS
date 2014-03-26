package com.sns.login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Blockip {
	
	// Istead of maintaing such count maintain a table with IP address with count...
	// and read those IP addresses every time we get a request with last access date..
	// reset to 0 if we get a request on another day.
	//static int count=0;
	//static String date="";
	static int day,month,year;
	static int day1,month1,year1;
	
	public static boolean block(String ipaddress, String date1)
	{
		//count++;
		
		Connection conn = null;
		try {
			conn = (Connection) DBhelper.getConnection();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try
		{
			int count = 0;
			String date = null;
			
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from IPADDR_STATS");
			
			boolean foundIP = false;
			
			while(rs.next())
			{
				if(rs.getString(1).equals(ipaddress))
				{
					foundIP = true;
					count = rs.getInt(2);
					date = rs.getString(3);
					break;
				}
			}
			
			
			if(!foundIP)
			{
				// add new address
				date = date1;
				String token[] = date1.split("/");
				year = Integer.parseInt(token[0]);
				month = Integer.parseInt(token[1]);
				day = Integer.parseInt(token[2]);
				count = 1;
				
				// Add this IP address..
				
				PreparedStatement pstmt = conn.prepareStatement("insert into IPADDR_STATS values (?,?,?)");
				
				pstmt.setString(1, ipaddress);
				pstmt.setInt(2, count);
				pstmt.setString(3, date);
				
				//String insertIPStmt = "insert into IPADDR_STATS values ()";
				
				pstmt.executeUpdate();
				
				System.out.println("day="+day+"count="+count);
				
				return false;
			}
			else
			{  	// there is already an entry available in DB for this IP.
				
				// Date in DB
				String token[] = date.split("/");
				year = Integer.parseInt(token[0]);
				month = Integer.parseInt(token[1]);
				day = Integer.parseInt(token[2]);
				
				// Current date
				token = date1.split("/");
				year1 = Integer.parseInt(token[0]);
				month1 = Integer.parseInt(token[1]);
				day1 = Integer.parseInt(token[2]);
				
				if(day ==  day1 && month == month1 && year == year1)
				{
					count++;
					System.out.println("day1="+day+"count1="+count);
				}
				else
				{
					count=1;
					year= year1;
					month = month1;
					day = day1;
					//return false;
				}
				
				PreparedStatement pst = conn.prepareStatement("UPDATE IPADDR_STATS set count = ? , lastAccessTime = ? where ipAddress = ?");
				
				pst.setInt(1, count);
				pst.setString(2, date1);
				pst.setString(3, ipaddress);
				
				pst.executeUpdate();
				
				if(count >= 10)
				{
					return true;
				}
				else
					return false;
				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return true;
	}
}