package com.bmm.services;

import java.util.Scanner;

import com.bmm.Dao.User_Da0;
import com.bmm.DaoImpl.User_DaoImpl;
import com.bmm.entity.BookingList;
import com.bmm.entity.Customer;
import com.bmm.entity.Discount;
import com.bmm.entity.Movie;
import com.bmm.servicesDao.UserServiceDao;

public class User_ServiceLayer implements UserServiceDao{
	Scanner sc=new Scanner(System.in);
	User_Da0 bmmDao=new User_DaoImpl();
	Customer customer=new Customer();
	Movie movie=new Movie();
	Discount discount=new Discount();
	BookingList bookinglist=new BookingList();
	public void createCustomer() {
		bmmDao.createCustomer(customer);
	}
	public void customerLogin() {
		System.out.println("Enter Email :");
		String mail=sc.next();
		System.out.println("Enter Password : ");
		String pwd=sc.next();
		bmmDao.customerLogin(customer,mail,pwd, discount, movie, bookinglist);
	}
}
