package com.cms;

import java.util.Scanner;
import com.cms.daoimpl.CMS_DaoImpl;

public class CMS_App 
{
    public static void main( String[] args )
    {
    	Scanner sc=new Scanner(System.in);
    	int id;
    	String fname,lname;
    	double fees;
    	long phone;
    	CMS_DaoImpl cmsImpl=new CMS_DaoImpl();
       while(true) {
    	   System.out.println("[1->Add New Student]\n[2->See Details]\n[3->Update Fees]\n[4->Delete]"
    	   		+ "\n[0->Exit]");
    	   int choice=sc.nextInt();
    	   switch(choice) {
    	   case 1:
    		   System.out.println("Enter Student id :");
    		   id=sc.nextInt();
    		   System.out.println("Enter First Name : ");
    		   fname=sc.next();
    		   System.out.println("Enter Last Name :");
    		   lname=sc.next();
    		   System.out.println("Enter Fees :");
    		   fees=sc.nextDouble();
    		   System.out.println("Enter Phone No. :");
    		   phone=sc.nextLong();
    		   cmsImpl.addStudent(id, fname, lname, fees, phone);break;
    	   case 2:cmsImpl.readStudent();break;
    	   case 3:
    		   System.out.println("Enter Student Id :");
    		   id=sc.nextInt();
    		   System.out.println("Enter New Fees : ");
    		   fees=sc.nextDouble();
    		   cmsImpl.updateStudentFees(id, fees);break;
    	   case 4:
    		   System.out.println("Enter Student Id :");
    		   id=sc.nextInt();
    		   cmsImpl.deleteStudent(id);break;
    	   case 0:System.exit(0);break;
    	   default:System.out.println("Invalid Choice...");
    	   }
       }
    }
}
