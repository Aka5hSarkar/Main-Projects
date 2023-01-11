package com.cms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Helper {
	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static Connection con()throws SQLException{
		return DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "root");
	}
}
