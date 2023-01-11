package com.cms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Year;
import java.util.*;
import java.util.regex.Pattern;
import com.bms.Helper;
//custom exception class for duplicate password 
class DuplicatePasswordException extends Exception {
	DuplicatePasswordException(String str) {
		super(str);
	}
}
//custom exception class for Registration id not present
class RegistrationNotFoundException extends Exception {
	RegistrationNotFoundException(String str) {
		super(str);
	}
}
//custom exception class for incorrect password
class IncorrectPasswordException extends Exception {
	public IncorrectPasswordException(String str) {
		super(str);
	}
}
//custom exception for invalid marks
class InvalidMarksException extends Exception{
	InvalidMarksException(String str){
		super(str);
	}
}
public class CMS_Loader {
	//instance variables and scanner object
	static Scanner sc = new Scanner(System.in);
	static long reg_id,ph_no;
	static String password,fname,lname,address,course;
	static double tenthMarks,twelvethMarks;
	static int yop;
	//this method is for registration and creating new account
	public static void register() throws Exception {
		Connection conn = Helper.con();
		Statement stmt = conn.createStatement();
		reg_id = new Random().longs(10000000, 99999999).findFirst().getAsLong();//generating 8 digit random number Random class
		ResultSet rs1 = stmt.executeQuery("select reg_id from register where reg_id=" + reg_id);
		while (rs1.next()) {
			reg_id = new Random().longs(10000000, 99999999).findFirst().getAsLong();
		}
		System.out.println("Please create a password for your profile"
				+ "\n(Note : you must follow these rules while creating the password : "
				+ "\nIt should be six characters." + "\nIt should contain at least one digit."
				+ "\nIt should contain at least one upper case alphabet."
				+ "\nIt should contain at least one lower case alphabet."
				+ "\nIt should contain at least one special character." + "\nIt shouldn't contain any white space.)");
		password = sc.next();
		while (!Pattern.matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{6}$", password)) {
			System.out.println("Your password doesn't match the validations." + "\n Try again : ");
			password = sc.next();
		}
		System.out.println("Re-enter the password to confirm : ");
		String confirmPassword = sc.next();
		while (!password.equals(confirmPassword)) {
			System.out.println("Passwords doesn't match." + "\nTry again : ");
			confirmPassword = sc.next();
		}
		rs1 = stmt.executeQuery("select password from register where password='" + password + "'");
		if (rs1.next()) {
			throw new DuplicatePasswordException("Duplicate Password not allowed.");
		} else {
			PreparedStatement pstmt = conn.prepareStatement("insert into register values(?,?)");
			pstmt.setLong(1, reg_id);
			pstmt.setString(2, confirmPassword);
			pstmt.executeUpdate();
			System.out.println("Account created successfully..." + "\nThis is your Registration Id : " + reg_id
					+ "\nKeep it safe.It will be needed while logging into your account.");
		}
	}

	public static void login() throws Exception {
		System.out.println("Enter your Registration Id : ");
		reg_id = sc.nextLong();
		while (!Pattern.matches("[1-9]{1}[0-9]{7}", String.valueOf(reg_id))) {
			System.out.println("Invalid Registration Id...\nTry again : ");
			reg_id = sc.nextLong();
		}
		Connection conn = Helper.con();
		Statement stmt = conn.createStatement();
		ResultSet rs1 = stmt.executeQuery("select*from register where reg_id=" + reg_id);
		if (!rs1.next()) {
			throw new RegistrationNotFoundException("No match found for Registration no : " + reg_id
					+ "\nEnter correct Registration no. or create a new account. ");
		} else {
			System.out.println("Enter password : ");
			password = sc.next();
			if (!(rs1.getString(2).equals(password))) {
				throw new IncorrectPasswordException("Incorrect Password");
			} else {
				System.out.println("You have successfully logged in...");
				while(true) {
					System.out.println("[1->Admission to new Course],[2-->My Course],"
							+ "[3->Display all details],[4->Change Details],[5->Change Password],"
							+ "[0->Log Out]");
					int choice = sc.nextInt();
					while (choice<0 || choice>5) {
						System.out.println("Invalid choice...\nTry again : ");
						choice = sc.nextInt();
					}if(choice==0) {
						break;
					}
					switch(choice) {
					case 1:admission(reg_id);break;
					case 2:System.out.println("You are currently enrolled in : "+myCourse(reg_id)+" course.");break;
					case 3:displayData(reg_id);break;
					case 4:update(reg_id);break;
					case 5:changePassword(reg_id);
					}
					System.out.println("[1->Main Menu],[0->Log Out]");
					choice=sc.nextInt();
					while(choice<0 && choice>1) {
						System.out.println("Invalid choice...\nTry again : ");
						choice=sc.nextInt();
					}
					if(choice==1) continue;
					else break;
				}
			}
		}
	}
	public static void admission(long reg_id) throws Exception {
		System.out.println("Enter Details \n(Note : Enter details according to the Documents you have."
				+ "\nAll Documents will be verified during the physical verification at college ) : ");
		System.out.println("Enter First Name (as per Adhaar Card): ");
		fname=sc.next();
		System.out.println("Enter Last Name (as per Adhaar Card): ");
		lname=sc.next();
		System.out.println("Enter Permanent Address : "); 
		address=sc.next();
		System.out.println("Enter Your 10 Digit Mobile No. : ");
		ph_no=sc.nextLong();
		System.out.println("Enter 10th Percentage : ");
		tenthMarks=sc.nextDouble();
		System.out.println("Enter 12th Percentage : ");
		twelvethMarks=sc.nextDouble();
		if(!validateMarks(tenthMarks, twelvethMarks)) {
			throw new InvalidMarksException("Students with less than 70% marks in 10th and 12th are not allowed to enroll in any courses");
		}else {
			System.out.println("Enter Year of Passing 12th : ");
			yop=sc.nextInt();
			if(yop<Year.now().getValue()-1) {
				System.out.println("Year of Passing must be Previous Year...");
			}else {
				System.out.println("Choose from the below courses you want to enroll : ");
				System.out.println("[1->BCA]\n[2->B.Tech in CS]\n[3->B.Tech in Mechanical]\n[4->B.Tech in Civil]"
						+ "\n[5->B.Tech in Electronics]\n[6->B.Tech in Electrical]\n[7->B.Tech in Biomedical]"
						+ "\n[8->B.Pharm]");
				int choice=sc.nextInt();
				while(choice<1 || choice>8) {
					System.out.println("Invalid choice...\nTry again : ");
					choice=sc.nextInt();
				}
				switch(choice) {
				case 1 : course="BCA";break;
				case 2 : course="B.Tech in CS";break;
				case 3 : course="B.Tech in Mechanical";break;
				case 4 : course="B.Tech in Civil";break;
				case 5 : course="B.Tech in Electronics";break;
				case 6 : course="B.Tech in Electrical";break;
				case 7 : course="B.Tech in Biomedical";break;
				case 8 : course="B.Pharm";break;
				}
				Connection conn = Helper.con();
				PreparedStatement pstmt=conn.prepareStatement("insert into admission values(?,?,?,?,?,?,?,?,?)");
				pstmt.setLong(1,reg_id);
				pstmt.setString(2,fname);
				pstmt.setString(3,lname);
				pstmt.setString(4,address);
				pstmt.setLong(5,ph_no);
				pstmt.setString(6,course);
				pstmt.setDouble(7,tenthMarks);
				pstmt.setDouble(8,twelvethMarks);
				pstmt.setInt(9,yop);
				pstmt.executeUpdate();
				System.out.println("Admission Successful...");
				displayData(reg_id);
			}
		}
	}
	public static boolean validateMarks(double marks10th,double marks12th) {
		if(marks10th>=70 && marks12th>=70) {
			return true;
		}else return false;
	}
	public static void displayData(long reg_id) throws Exception {
		Connection conn=Helper.con();
		Statement stmt=conn.createStatement();
		ResultSet rs=stmt.executeQuery("select*from admission where reg_id="+reg_id);
		while(rs.next()) {
			System.out.println("Registration No. : "+rs.getLong(1)+
					"\nName : "+rs.getString(2)+" "+rs.getString(3)
					+"\nAddress : "+rs.getString(4)
					+"\nPhone No. : "+rs.getLong(5)
					+"\nCourse : "+rs.getString(6)
					+"\n10th Marks : "+rs.getDouble(7)+"%"
					+"\n12th Marks : "+rs.getDouble(8)+"%"
					+"\nYear Of Passing 12th : "+rs.getInt(9));
		}
	}
	public static void update(long reg_id) throws Exception {
		System.out.println("You can only change the following details :"
				+ "\n[1->Change Name],[2->Chnage Address],[3->Change Phone No.]");
		int choice=sc.nextInt();
		while(choice<1 && choice>3) {
			System.out.println("Invalid choice...\nTry again : ");
			choice=sc.nextInt();
		}
		switch(choice) {
		case 1:changeName(reg_id);break;
		case 2:changeAddress(reg_id);break;
		case 3:changePhone(reg_id);
		}
		
	}
	public static void changeName(long reg_id) throws Exception {
		Connection conn=Helper.con();
		Statement stmt=conn.createStatement();
		ResultSet rs=stmt.executeQuery("select fname,lname from admission where reg_id="+reg_id);
		while(rs.next()) {
			System.out.println("Current Name : "+rs.getString(1)+" "+rs.getString(2));
		}rs.close();
		System.out.println("Enter new First Name (As per Adhaar Card) :");
		fname=sc.next();
		System.out.println("Enter new Last Name (As per Adhaar Card) :");
		lname=sc.next();
		stmt.executeUpdate("update admission set fname='"+fname+"',lname='"+lname+"' where reg_id="+reg_id);
		System.out.println("Name changed successfully...");
		rs=stmt.executeQuery("select fname,lname from admission where reg_id="+reg_id);
		while(rs.next()) {
			System.out.println("New Name : "+rs.getString(1)+" "+rs.getString(2));
		}
	}
	public static void changeAddress(long reg_id) throws Exception {
		Connection conn=Helper.con();
		Statement stmt=conn.createStatement();
		ResultSet rs=stmt.executeQuery("select address from admission where reg_id="+reg_id);
		while(rs.next()) {
			System.out.println("Current Address : "+rs.getString(1));
		}rs.close();
		System.out.println("Enter New Address : ");
		address=sc.next();
		stmt.executeUpdate("update admission set address='"+address+"' where reg_id="+reg_id);
		System.out.println("Address changed successfully...");
		rs=stmt.executeQuery("select address from admission where reg_id="+reg_id);
		while(rs.next()) {
			System.out.println("New Address : "+rs.getString(1));
		}
	}
	public static void changePhone(long reg_id) throws Exception {
		Connection conn=Helper.con();
		Statement stmt=conn.createStatement();
		ResultSet rs=stmt.executeQuery("select ph_no from admission where reg_id="+reg_id);
		while(rs.next()) {
			System.out.println("Current Phone No. : "+rs.getLong(1));
		}rs.close();
		System.out.println("Enter New Phone No. : ");
		ph_no=sc.nextLong();
		stmt.executeUpdate("update admission set ph_no="+ph_no+"where reg_id="+reg_id);
		System.out.println("Phone No. changed successfully...");
		rs=stmt.executeQuery("select ph_no from admission where reg_id="+reg_id);
		while(rs.next()) {
			System.out.println("New Phone No. : "+rs.getLong(1));
		}
	}
	public static void changePassword(long reg_id) throws Exception {
		Connection conn=Helper.con();
		Statement stmt=conn.createStatement();
		System.out.println("Enter your current password : ");
		password=sc.next();
		ResultSet rs=stmt.executeQuery("select password from register where reg_id="+reg_id);
		while(rs.next()) {
			while(!password.equals(rs.getString(1))) {
				System.out.println("Incorrect Password...\nTry again :");
				password=sc.next();
			}
		}
		System.out.println("Enter New Password : "
				+ "\n(Note : you must follow these rules while creating the password : "
				+ "\nIt should be six characters.\" + \"\\nIt should contain at least one digit."
				+ "\nIt should contain at least one upper case alphabet."
				+ "\nIt should contain at least one lower case alphabet."
				+ "\nIt should contain at least one special character." + "\nIt shouldn't contain any white space.)");
		password=sc.next();
		while(!Pattern.matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{6}$",password)) {
			System.out.println("Invalid Password...\nTry again : ");
			password=sc.next();
		}
		System.out.println("Re-Enter Password to Confirm : ");
		String confirmPassword=sc.next();
		while(!password.equals(confirmPassword)) {
			System.out.println("Passwords don't match...\nTry again : ");
			confirmPassword=sc.next();
		}
		stmt.executeUpdate("update register set password='"+confirmPassword+"' where reg_id="+reg_id);
		System.out.println("Password changed successfully...");
	}
	public static String myCourse(long reg_id) throws Exception {
		Connection conn=Helper.con();
		Statement stmt=conn.createStatement();
		ResultSet rs=stmt.executeQuery("select course from admission where reg_id="+reg_id);
		while(rs.next()) {
			course=rs.getString(1);
		}return course;
	}
}
