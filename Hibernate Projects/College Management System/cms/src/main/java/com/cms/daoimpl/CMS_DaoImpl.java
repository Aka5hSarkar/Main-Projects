package com.cms.daoimpl;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.cms.dao.CMS_Dao;
import com.cms.entity.CMS_Entity;
import com.cms.util.CMS_Util;

public class CMS_DaoImpl implements CMS_Dao {
	public void addStudent(int id, String fName, String lName, double fees, long ph) {
		try {
			Session s = CMS_Util.getSessionFactory().openSession();
			Transaction t = s.beginTransaction();
			CMS_Entity cms = new CMS_Entity();
			cms.setId(id);
			cms.setFName(fName);
			cms.setLName(lName);
			cms.setFees(fees);
			cms.setPh(ph);
			s.save(cms);
			t.commit();
			System.out.println("Inserted Successfully");
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	public void readStudent() {
		try {
			Session s = CMS_Util.getSessionFactory().openSession();
			List l = s.createQuery("from CMS_Entity ").list();
			for (Iterator itr = l.iterator(); itr.hasNext();) {
				CMS_Entity cms = (CMS_Entity) itr.next();
				System.out.println("ID : "+cms.getId() + " Name : " + cms.getFName() + " " + cms.getLName() + " Fees : " + cms.getFees()
					+"Phone : "	+ cms.getPh());
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void updateStudentFees(int id, double fees) {
		try {
			Session s = CMS_Util.getSessionFactory().openSession();
			Transaction t = s.beginTransaction();
			CMS_Entity cms = s.get(CMS_Entity.class, id);
			cms.setFees(fees);
			s.update(cms);
			t.commit();
			System.out.println("Updated Successfully");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void deleteStudent(int id) {
		try {
			Session ses = CMS_Util.getSessionFactory().openSession();
			Transaction t = ses.beginTransaction();
			CMS_Entity e = ses.get(CMS_Entity.class, id);
			ses.delete(e);
			t.commit();
			System.out.println("Deleted Succesfully");
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
