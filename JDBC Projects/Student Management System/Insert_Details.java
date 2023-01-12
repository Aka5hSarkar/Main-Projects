package com.sms.insert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;
import com.sms.Helper;

public class Insert_Details {
	Scanner sc=new Scanner(System.in);
	int id,age,fees;
	long phone;
	String name,address;
	//saving student details in database
	public void saveData() throws SQLException{
		System.out.println("Enter student id : ");
		id=sc.nextInt();
		System.out.println("Enter student name : ");
		name=sc.next();
		System.out.println("Enter student age : ");
		age=sc.nextInt();
		System.out.println("Enter student fees : ");
		fees=sc.nextInt();
		System.out.println("Enter student address : ");
		address=sc.next();
		System.out.println("Enter student phone no : ");
		phone=sc.nextLong();
		Connection conn=Helper.con();
		PreparedStatement stmt=conn.prepareStatement("insert into student_management_system values(?,?,?,?,?,?)");
		stmt.setInt(1, id);
		stmt.setString(2, name);
		stmt.setInt(4,age);
		stmt.setString(3, address);
		stmt.setLong(5, phone);
		stmt.setInt(6,fees);
		stmt.executeUpdate();
		System.out.println("Data inserted successfully");
	}
}
