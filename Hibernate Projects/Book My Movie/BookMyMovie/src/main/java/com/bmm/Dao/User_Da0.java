package com.bmm.Dao;

import com.bmm.entity.BookingList;
import com.bmm.entity.Customer;
import com.bmm.entity.Discount;
import com.bmm.entity.Movie;

public interface User_Da0 {
	void createCustomer(Customer customer);
	void customerLogin(Customer customer, String mail, String pwd,Discount discount,Movie movie,BookingList bookinglist);
}
