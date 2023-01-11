package com.bmm.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class BookMyMovieUtil {
	private static SessionFactory ses;
	static {
		try {
			ses =new Configuration().configure().buildSessionFactory();
		}catch(Exception e) {
			e.printStackTrace();;
		}
	}
	public static SessionFactory getSessionFactory() {
		return ses;
	}
}
