package com.sns.login;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBhelper {

	static Connection connection = null;

	public static Connection getConnection() throws SQLException {
		
		if(connection == null || connection.isClosed() == true)
		{
			try 
			{
				Class.forName("com.mysql.jdbc.Driver");
			
				connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/IDS",
					"root", "mysql");
			
			} 
			catch (ClassNotFoundException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch(SQLException e1)
			{
				e1.printStackTrace();
			}
		}
		
		return connection;
	}
	
	public static void closeConnection()
	{
		try {
			
			if(connection != null)
				connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		connection = null;
	}
	
	
	
}
