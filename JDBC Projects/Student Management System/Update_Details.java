package com.sms.update;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import com.sms.Helper;
import java.util.Scanner;

public class Update_Details {
	public void updateData()throws SQLException{
		Scanner sc=new Scanner(System.in);
		Connection conn=Helper.con();
		Statement stmt=conn.createStatement();
		int id,new_id,new_age,new_fees;
		long new_phone;
		String new_name,new_address;
		System.out.println("Enter student id where you want to update data : ");
		id=sc.nextInt();
		System.out.println("Update :: ---[1.id],[2.name],[3.address],[4.age],[5.phone],[6.fees]---");
		int choice=sc.nextInt();
		if(choice==1) {
			System.out.println("Enter new id : ");
			new_id=sc.nextInt();
			stmt.executeUpdate("update student_management_system set id= "+new_id+"  where id= "+id);
		}else if(choice==2) {
			System.out.println("Enter new student name : ");
			new_name=sc.next();
			stmt.executeUpdate("update student_management_system set name=' "+new_name+" ' where id="+id);
		}else if(choice==3) {
			System.out.println("Enter new student address : ");
			new_address=sc.next();
			stmt.executeUpdate("update student_management_system set address=' "+new_address+" ' where id="+id);
		}else if(choice==4) {
			System.out.println("Enter new student age : ");
			new_age=sc.nextInt();
			stmt.executeUpdate("update student_management_system set address= "+new_age+"  where id="+id);
		}else if(choice==5) {
			System.out.println("Enter new student phone : ");
			new_phone=sc.nextLong();
			stmt.executeUpdate("update student_management_system set address= "+new_phone+"  where id="+id);
		}else if(choice==6) {
			System.out.println("Enter new student fees : ");
			new_fees=sc.nextInt();
			stmt.executeUpdate("update student_management_system set address= "+new_fees+"  where id="+id);
		}
		System.out.println("Data updated successfully");
	}
}
