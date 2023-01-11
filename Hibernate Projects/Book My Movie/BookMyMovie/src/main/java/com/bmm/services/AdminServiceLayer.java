package com.bmm.services;

import java.util.Scanner;

import com.bmm.Dao.AdminDao;
import com.bmm.DaoImpl.AdminDaoImpl;
import com.bmm.entity.Admin;
import com.bmm.entity.Discount;
import com.bmm.entity.Movie;
import com.bmm.servicesDao.AdminServiceDao;

public class AdminServiceLayer implements AdminServiceDao
{
	Scanner sc=new Scanner(System.in);
	AdminDao dao=new AdminDaoImpl();
	
	public void createAdmin() {
		Admin admin=new Admin();
		dao.createAdmin(admin);
	}
	public void adminLogin() {
		Admin admin=new Admin();
		Movie movie=new Movie();
		Discount discount=new Discount();
		System.out.println("Enter Admin Id :");
		String id=sc.next();
		System.out.println("Enter Admin Password : ");
		String pwd=sc.next();
		dao.adminLogin(id, pwd, admin ,movie, discount);
	}
}
