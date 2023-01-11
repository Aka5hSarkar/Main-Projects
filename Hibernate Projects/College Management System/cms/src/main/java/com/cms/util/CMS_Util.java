package com.cms.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class CMS_Util {
	private static SessionFactory ses;
	static {
		try {
			ses =new Configuration().configure().buildSessionFactory();
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	public static SessionFactory getSessionFactory() {
		return ses;
	}
}
