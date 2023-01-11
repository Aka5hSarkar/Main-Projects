package com.bmm.Dao;

import com.bmm.entity.Admin;
import com.bmm.entity.Discount;
import com.bmm.entity.Movie;

public interface AdminDao {
	void createAdmin(Admin admin);
	void adminLogin(String id,String pwd,Admin admin,Movie movie,Discount discount);
}
