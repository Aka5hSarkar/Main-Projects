package com.cms;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.TreeSet;
import com.bms.Helper;

class Student implements Comparable<Student>{
	long reg_id,ph_no;
	String fname,lname,address,course;
	double tenthMarks,twelvethMarks;
	int yop;
	Student (long reg_id, long ph_no, String fname, String lname, String address, String course,
			double tenthMarks, double twelvethMarks, int yop) {
		super();
		this.reg_id = reg_id;
		this.ph_no = ph_no;
		this.fname = fname;
		this.lname = lname;
		this.address = address;
		this.course = course;
		this.tenthMarks = tenthMarks;
		this.twelvethMarks = twelvethMarks;
		this.yop = yop;	
}
	@Override
	public String toString() {
		return "Student [reg_id=" + reg_id + ", ph_no=" + ph_no + ", fname=" + fname + ", lname=" + lname + ", address="
				+ address + ", course=" + course + ", tenthMarks=" + tenthMarks + ", twelvethMarks=" + twelvethMarks
				+ ", yop=" + yop + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}
	@Override
	public int compareTo(Student o) {
		return this.fname.compareTo(o.fname);
	}
}
public class ArrayToTreeSet{
	public static void main(String[] args) throws SQLException {
		ArrayList<Student> arr=new ArrayList<>();
		Connection conn=Helper.con();
		Statement stmt=conn.createStatement();
		ResultSet rs=stmt.executeQuery("select*from admission");
		while(rs.next()) {
			arr.add(new Student(rs.getLong(1),rs.getLong(5),rs.getString(2),rs.getString(3),
					rs.getString(4),rs.getString(6),rs.getDouble(7),rs.getDouble(8),rs.getInt(9)));
		}
		System.out.println(arr);
		TreeSet<Student> tset=new TreeSet<Student>(arr);
		for(Student s:tset) {
			System.out.println(s);
		}
	}
}
