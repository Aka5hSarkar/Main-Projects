package com.bmm;

import java.util.Scanner;
import java.util.regex.Pattern;

import com.bmm.services.AdminServiceLayer;
import com.bmm.services.User_ServiceLayer;
import com.bmm.servicesDao.AdminServiceDao;
import com.bmm.servicesDao.UserServiceDao;

public class BMM_App {

	@SuppressWarnings("finally")
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		UserServiceDao userService = new User_ServiceLayer();
		AdminServiceDao adminService = new AdminServiceLayer();
		System.out.println("Welcoe to Book My Movie");
		while (true) {
			System.out.println("[1->Create Account]\n[2->Log In]\n[0->Exit]");
			String ch = sc.next();
			if (ch.equals("0")) {
				break;
			} else if (ch.equals("1")) {
				while (true) {
					System.out.println("[1->Admin Account(For Authorized Personals Only)]" + "\n[2->User Acount]");
					String ch2 = sc.next();
					if (ch2.equals("1")) {
						adminService.createAdmin();
						break;
					} else if (ch2.equals("2")) {
						userService.createCustomer();
						break;
					} else {
						System.out.println("Invalid choice...try again");
						continue;
					}
				}
			} else if (ch.equals("2")) {
				while (true) {
					System.out.println("[1->Admin Log In(For Authorized Personals Only)]" + "\n[2->User Log In]");
					String ch2 = sc.next();
					if (ch2.equals("1")) {
						adminService.adminLogin();
						break;
					} else if (ch2.equals("2")) {
						userService.customerLogin();
						break;
					} else {
						System.out.println("Invalid choice...try again");
						continue;
					}
				}
			} else {
				System.out.println("Invalid choice...Try again");
				continue;
			}
			String ch3;
			while(true) {
				System.out.println("[1->Main Menu],[0->Exit]");
				ch3=sc.next();
				if(!Pattern.matches("[0-1]",ch3)) {
					System.out.println("Invalid choice...try again");
					continue;
				}else {
					break;
				}
			}
			if(ch3.equals("1")) {
				continue;
			}else
				break;
		}
	}
}
