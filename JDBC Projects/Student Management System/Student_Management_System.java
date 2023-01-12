package com.sms.student_main;
import java.sql.SQLException;
import java.util.Scanner;

import com.sms.delete.Delete_Details;
import com.sms.fetch.Fetch_Details;
import com.sms.insert.Insert_Details;
import com.sms.update.Update_Details;
public class Student_Management_System{

	public static void main(String[] args) throws SQLException{
		Scanner sc=new Scanner(System.in);
		Insert_Details i=new Insert_Details();
		Update_Details u=new Update_Details();
		Delete_Details d=new Delete_Details();
		Fetch_Details f=new Fetch_Details();
		System.out.println("Welcome to the Student Management System .");
		while(true) {
			System.out.println("[1.insert],[2.update],[3.delete],[4.display]");
			int choice=sc.nextInt();
			if(choice==1) {
				i.saveData();
			}else if(choice==2) {
				u.updateData();
			}else if(choice==3) {
				d.deleteData();
			}else if(choice==4) {
				f.fetchData();
			}
		}
	}

}
