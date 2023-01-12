package com.sms.delete;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import com.sms.Helper;

public class Delete_Details {
	//deleting details
			public void deleteData() throws SQLException{
				Scanner sc=new Scanner(System.in);
				Connection conn=Helper.con();
				Statement stmt=conn.createStatement();
				System.out.println("Enter student id : ");
				int id=sc.nextInt();
				stmt.executeUpdate("delete from student_management_system where id="+id);
				System.out.println("Data deleted successfully");
			}
}
