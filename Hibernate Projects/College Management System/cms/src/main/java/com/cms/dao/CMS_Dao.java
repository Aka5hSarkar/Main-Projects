package com.cms.dao;

public interface CMS_Dao {
	public void addStudent(int id,String fname,String lname,double fees,long ph);
	public void readStudent();
	public void updateStudentFees(int id,double fees);
	public void deleteStudent(int id);
}
