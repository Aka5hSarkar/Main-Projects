package com.cms;

import java.util.Scanner;

public class Menu_Driven_CMS {

	@SuppressWarnings("finally")
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int choice;
		System.out.println("Welcome to ABC College");
		while (true) {
			System.out.println("[1->Register],[2->Login],[0->Exit]");
			choice = sc.nextInt();
			while (choice < 0 && choice > 2) {
				System.out.println("Invalid choice...\nTry again : ");
				choice = sc.nextInt();
			}
			if (choice == 0) {
				System.out.println("Thank you for visiting");
				break;
			} else {
				try {
					switch (choice) {
					case 1:
						CMS_Loader.register();
						break;
					case 2:
						CMS_Loader.login();
					}
				} catch (Exception e) {
					System.out.println(e.getMessage());
				} finally {
					System.out.println("[1->Main Menu],[0->Exit]");
					choice = sc.nextInt();
					while (choice < 0 && choice > 1) {
						System.out.println("Invalid choice...\nTry again : ");
						choice = sc.nextInt();
					}
					if (choice == 1)
						continue;
					else
						System.out.println("Thank you for visiting");
						break;
				}
			}
		}
	}
}
