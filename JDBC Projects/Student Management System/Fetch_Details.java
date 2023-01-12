package com.sms.fetch;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.sms.Helper;

public class Fetch_Details {
	public void fetchData() throws SQLException{
		Connection conn=Helper.con();
		Statement stmt=conn.createStatement();
		//execute query fetch data from database
		ResultSet rs=stmt.executeQuery("select*from student_management_system");
		//iteration
		while(rs.next()) {
			System.out.println(rs.getInt(1)+","+rs.getString(2)+","+rs.getString(3)+","+
		rs.getInt(4)+","+rs.getLong(5)+","+rs.getInt(6));
		}
	}
}
