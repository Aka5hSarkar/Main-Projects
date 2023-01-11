package com.bmm.DaoImpl;

import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bmm.Dao.User_Da0;
import com.bmm.entity.BookingList;
import com.bmm.entity.Customer;
import com.bmm.entity.Discount;
import com.bmm.entity.Movie;
import com.bmm.util.BookMyMovieUtil;

public class User_DaoImpl implements User_Da0 {
	static Scanner sc = new Scanner(System.in);

	@Override
	public void createCustomer(Customer customer) {
		Session s = BookMyMovieUtil.getSessionFactory().openSession();
		Transaction t = s.beginTransaction();
		String mail, fname, lname, phn, pwd;
		while (true) {
			while (true) {
				System.out.println("Enter Email : ");
				mail = sc.next();
				if (!Pattern.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", mail)) {
					System.out.println("Invalid Email...\nTry Again : ");
					continue;
				} else
					break;
			}
			if (isEmailPresent(s, mail)) {
				System.out.println(
						"An account with this email already exists..." + "\nPlease Try with a different email...");
				continue;
			} else
				break;
		}
		while (true) {
			System.out.println("Enter First Name : ");
			fname = sc.next();
			if (!Pattern.matches("[a-zA-Z]{1,20}", fname)) {
				System.out.println("Invalid Name...\nTry Again : ");
				continue;
			} else
				break;
		}
		while (true) {
			System.out.println("Enter Last Name : ");
			lname = sc.next();
			if (!Pattern.matches("[a-zA-Z]{1,20}", lname)) {
				System.out.println("Invalid Name...\nTry Again : ");
				continue;
			} else
				break;
		}
		while (true) {
			System.out.println("Enter Phone No. : ");
			phn = sc.next();
			if (!Pattern.matches("[1-9]{1}[0-9]{9}", phn)) {
				System.out.println("Invalid Name...\nTry Again : ");
				continue;
			} else
				break;
		}
		while (true) {
			System.out.println(
					" create password " + "\n(Note : you must follow these rules while creating the password : "
							+ "\nIt should be six characters." + "\nIt should contain at least one digit."
							+ "\nIt should contain at least one upper case alphabet."
							+ "\nIt should contain at least one lower case alphabet."
							+ "\nIt should contain at least one special character."
							+ "\nIt shouldn't contain any white space.)");
			pwd = sc.next();

			while (!Pattern.matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{6}$", pwd)) {
				System.out.println("Your password doesn't match the validations." + "\n Try again : ");
				pwd = sc.next();
			}
			System.out.println("Re-enter the password to confirm : ");
			String confirmPassword = sc.next();
			while (!pwd.equals(confirmPassword)) {
				System.out.println("Passwords doesn't match." + "\nTry again : ");
				confirmPassword = sc.next();
			}
			if (isPasswordPresent(s, pwd)) {
				System.out.println("Password Already Exists....\nPlease choose a different password...");
				continue;
			} else
				break;
		}
		customer.setCustomerEmail(mail);
		customer.setCustomerFName(fname);
		customer.setCustomerLName(lname);
		customer.setCustomerPhone(phn);
		customer.setCustomerPassword(pwd);
		customer.setSubscription(false);
		s.save(customer);
		t.commit();
		System.out.println("Profile created successfully...");
		s.close();
	}

	private static boolean isPasswordPresent(Session s, String pwd) {
		List userPassList = s.createQuery("select c.customerPassword from Customer c").list();
		if (userPassList.contains(pwd))
			return true;
		else
			return false;
	}

	private static boolean isEmailPresent(Session s, String mail) {
		List userEmailList = s.createQuery("select c.customerEmail from Customer c").list();
		if (userEmailList.contains(mail)) {
			return true;
		} else
			return false;
	}

	@Override
	public void customerLogin(Customer customer, String mail, String pwd, Discount discount, Movie movie,
			BookingList bookinglist) {
		Session s = BookMyMovieUtil.getSessionFactory().openSession();
		Transaction t = s.beginTransaction();
		if (!isEmailPresent(s, mail)) {
			System.out.println("No account with this email...");
		} else if (!s.get(Customer.class, mail).getCustomerPassword().equals(pwd)) {
			System.out.println("Wrong Password...");
		} else {
			System.out.println("Welcome " + s.get(Customer.class, mail).getCustomerFName());
			while (true) {
				System.out.println("[1->Book Movies],[2->My Profile],[3->Get A Monthly Subscription],[0->Log Out]");
				String ch = sc.next();
				if (ch.equals("0")) {
					break;
				} else if (ch.equals("2")) {
					userProfile(mail, customer, bookinglist);
				} else if (ch.equals("3")) {
					userSub(mail, customer);
				} else if (ch.equals("1")) {
					bookMovie(customer, movie, discount, bookinglist, mail);
				} else {
					System.out.println("Invalid choice...Try again");
					continue;
				}
				String choice3;
				while (true) {
					System.out.println("[1->Main Menu]\n[0->Log Out]");
					choice3 = sc.next();
					if (!Pattern.matches("[0-1]", choice3)) {
						System.out.println("Invalid choice..try again");
						continue;
					} else
						break;
				}
				if (choice3.equals("1")) {
					continue;
				} else {
					break;
				}
			}
		}
	}

	// user profile method
	public static void userProfile(String mail, Customer customer, BookingList bookingList) {
		Session s = BookMyMovieUtil.getSessionFactory().openSession();
		customer = s.get(Customer.class, mail);
		System.out.println(customer);
		while (true) {
			System.out.println("[1->Edit Profile],[2->My Tickets]");
			String ch = sc.next();
			if (ch.equals("1")) {
				while (true) {
					System.out.println(
							"[1->Change Name]\n[2->Change Email]" + "\n[3->Change Phone]\n[4->Change Password]");
					String ch2 = sc.next();
					if (ch2.equals("1")) {
						changeUserName(customer);
						break;
					} else if (ch2.equals("2")) {
						changeUserEmail(customer);
						break;
					} else if (ch2.equals("3")) {
						changeUserPhone(customer);
						break;
					} else if (ch2.equals("4")) {
						changeUserPassword(customer);
						break;
					} else {
						System.out.println("Invalid choice...Try again");
						continue;
					}
				}
				break;
			} else if (ch.equals("2")) {
				myTicket(mail, bookingList, s);
				break;
			} else {
				System.out.println("Invalid choice...Try again");
				continue;
			}
		}
	}

	// method to change customer name
	public static void changeUserName(Customer customer) {
		Session s = BookMyMovieUtil.getSessionFactory().openSession();
		Transaction t = s.beginTransaction();
		System.out.println("Your Current Name : " + customer.getCustomerFName() + " " + customer.getCustomerLName());
		String newFName, newLName;
		while (true) {
			System.out.println("Enter New First Name : ");
			newFName = sc.next();
			if (Pattern.matches("[a-zA-Z]{1,20}", newFName)) {
				break;
			} else {
				System.out.println("Invalid First Name..Try again");
				continue;
			}
		}
		while (true) {
			System.out.println("Enter New last Name : ");
			newLName = sc.next();
			if (Pattern.matches("[a-zA-Z]{1,20}", newLName)) {
				break;
			} else {
				System.out.println("Invalid Last Name..Try again");
				continue;
			}
		}
		customer.setCustomerFName(newFName);
		customer.setCustomerLName(newLName);
		s.update(customer);
		t.commit();
		s.close();
		System.out.println("Name successfully changed to " + newFName + " " + newLName);
	}

	// method to change user email
	public static void changeUserEmail(Customer customer) {
		Session s = BookMyMovieUtil.getSessionFactory().openSession();
		Transaction t = s.beginTransaction();
		System.out.println("Current Email : " + customer.getCustomerEmail());
		String newMail, pwdInput;
		while (true) {
			System.out.println("Enter New Email : ");
			newMail = sc.next();
			if (!Pattern.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", newMail)) {
				System.out.println("Invalid Email..Try again");
				continue;
			} else {
				if (isEmailPresent(s, newMail)) {
					System.out.println("Email Already Exists...choose a different email");
					continue;
				} else {
					break;
				}
			}
		}
		while (true) {
			System.out.println("Enter PassWord : ");
			pwdInput = sc.next();
			if (customer.getCustomerPassword().equals(pwdInput)) {
				break;
			} else {
				System.out.println("Wrong Pasword...Try again");
				continue;
			}
		}
		customer.setCustomerEmail(newMail);
		s.update(customer);
		t.commit();
		s.close();
		System.out.println("Email changed successfully to " + newMail);
	}

	// method to change user phone no
	public static void changeUserPhone(Customer customer) {
		Session s = BookMyMovieUtil.getSessionFactory().openSession();
		Transaction t = s.beginTransaction();
		System.out.println("Current Phone No. : " + customer.getCustomerPhone());
		String newphn;
		while (true) {
			System.out.println("Enter New Phone No. :");
			newphn = sc.next();
			if (!Pattern.matches("[1-9]{1}[0-9]{9}", newphn)) {
				System.out.println("Invalid Phone No...Try again");
				continue;
			} else {
				break;
			}
		}
		customer.setCustomerPhone(newphn);
		s.update(customer);
		t.commit();
		s.close();
		System.out.println("Phone No. successfully changed to " + newphn);
	}

	// method to change user password
	public static void changeUserPassword(Customer customer) {
		Session s = BookMyMovieUtil.getSessionFactory().openSession();
		Transaction t = s.beginTransaction();
		String curPwd, newPwd, confirmNewPwd;
		while (true) {
			System.out.println("Enter Current Password :");
			curPwd = sc.next();
			if (customer.getCustomerPassword().equals(curPwd)) {
				break;
			} else {
				System.out.println("Wrong Password...Try again");
				continue;
			}
		}
		while (true) {
			System.out.println(
					"Create new password " + "\n(Note : you must follow these rules while creating the password : "
							+ "\nIt should be six characters." + "\nIt should contain at least one digit."
							+ "\nIt should contain at least one upper case alphabet."
							+ "\nIt should contain at least one lower case alphabet."
							+ "\nIt should contain at least one special character."
							+ "\nIt shouldn't contain any white space.)");
			newPwd = sc.next();
			if (!Pattern.matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{6}$", newPwd)) {
				System.out.println("Your password doesn't meet the validations...Try again");
				continue;
			} else if (isPasswordPresent(s, newPwd)) {
				System.out.println("Password already exists...Choose a different password");
				continue;
			} else {
				break;
			}
		}
		while (true) {
			System.out.println("Confirm New Password : ");
			confirmNewPwd = sc.next();
			if (confirmNewPwd.equals(newPwd)) {
				break;
			} else {
				System.out.println("Passwords doesn't match...Try again");
				continue;
			}
		}
		if (isPasswordPresent(s, confirmNewPwd)) {
			System.out.println("Password already exists...");
		} else {
			customer.setCustomerPassword(confirmNewPwd);
			s.update(customer);
			t.commit();
			s.close();
			System.out.println("Password changed successfully...");
		}
	}

	// subscription method
	public static void userSub(String mail, Customer customer) {
		Session s = BookMyMovieUtil.getSessionFactory().openSession();
		Transaction t = s.beginTransaction();
		customer = s.get(Customer.class, mail);
		if (customer.isSubscription()) {
			System.out.println("You are currently subscribed...");
			System.out.println("[1->Cancel Subscription],[Any Other Key->Exit]");
			String ch = sc.next();
			if (ch.equals("1")) {
				customer.setSubscription(false);
				System.out.println("Subscription Cancelled..You can subsribe again anytime..");
			}
		} else {
			System.out.println("Get a Monthly subscription of 200 Rs/month and enjoy 50% discount" +
		"on all the movie tickets..\nYou can Cancel anytime... ");
			System.out.println("[1->Subscribe],[Any Other Key->Exit]");
			String ch = sc.next();
			if (ch.equals("1")) {
				while (true) {
					System.out.println("Payment Getaway...\nPlease Enter Amount :");
					String pay = sc.next();
					if (Pattern.matches("[0-9]+", pay)) {
						if (pay.equals("200")){
							customer.setSubscription(true);
							System.out.println("Succefully subscribed");
						} else {
							System.out.println("Wrong amount...");
						}
						break;
					} else {
						System.out.println("Invalid...Try again");
						continue;
					}
				}
			}
		}
		s.update(customer);
		t.commit();
		s.close();
	}
	//method to book movies
	public static void bookMovie(Customer customer, Movie movie, Discount discount, BookingList bookinglist,
			String mail) {
		Session s = BookMyMovieUtil.getSessionFactory().openSession();
		while (true) {
			System.out.println("Book your first movie ticket and get 30% discount");
			System.out.println("[1->Recommended Movies],[2->Search]");
			String ch = sc.next();
			if (ch.equals("1")) {
				recommendedMovie(movie, s, mail, bookinglist, customer, discount);
				break;
			} else if (ch.equals("2")) {
				searchMovie(movie, s, customer, bookinglist, discount, mail);
				break;
			} else {
				System.out.println("Invalid choice...Try again");
				continue;
			}
		}s.close();
	}

	@SuppressWarnings("unchecked")
	public static void recommendedMovie(Movie movie, Session s, String mail, BookingList bookingList, Customer customer,
			Discount discount) {
		Transaction t = s.beginTransaction();
		List<Movie> ml = s.createQuery("select distinct m.movieName from Movie m").list();
		for (int i = 0; i <= ml.size() - 1; i++) {
			System.out.println("[" + (i + 1) + "->" + ml.get(i) + "]");
		}
		System.out.println("select from above Options :");
		String ch;
		while (true) {
			ch = sc.next();
			if (!Pattern.matches("[1-" + ml.size() + "]", ch)) {
				System.out.println("Invalid choice...Try again");
				continue;
			} else {
				break;
			}
		}
		Object selectMovie = ml.get(Integer.valueOf(ch) - 1);
		ml = s.createQuery("select distinct m.multiplex from Movie m where m.movieName='" + selectMovie + "'").list();
		for (int i = 0; i <= ml.size() - 1; i++) {
			System.out.println("[" + (i + 1) + "->" + ml.get(i) + "]");
		}
		System.out.println("select from above Options :");
		while (true) {
			ch = sc.next();
			if (!Pattern.matches("[1-" + ml.size() + "]", ch)) {
				System.out.println("Invalid choice...Try again");
				continue;
			} else {
				break;
			}
		}
		Object selectMultiplex = ml.get(Integer.valueOf(ch) - 1);
		ml = s.createQuery("select distinct m.time from Movie m where m.movieName='" + selectMovie + "' and "
				+ "m.multiplex='" + selectMultiplex + "'").list();
		for (int i = 0; i <= ml.size() - 1; i++) {
			System.out.println("[" + (i + 1) + "->" + ml.get(i) + "]");
		}
		System.out.println("select from above Options :");
		while (true) {
			ch = sc.next();
			if (!Pattern.matches("[1-" + ml.size() + "]", ch)) {
				System.out.println("Invalid choice...Try again");
				continue;
			} else {
				break;
			}
		}
		Object selectTime = ml.get(Integer.valueOf(ch) - 1);

		ml = s.createQuery("select m.screen from Movie m where m.movieName='" + selectMovie + "' and " + "m.multiplex='"
				+ selectMultiplex + "' and m.time='" + selectTime + "'").list();
		for (int i = 0; i <= ml.size() - 1; i++) {
			System.out.println("[" + (i + 1) + "->" + ml.get(i) + "]");
		}
		System.out.println("select from above Options :");
		while (true) {
			ch = sc.next();
			if (!Pattern.matches("[1-" + ml.size() + "]", ch)) {
				System.out.println("Invalid choice...Try again");
				continue;
			} else {
				break;
			}
		}
		Object selectScreen = ml.get(Integer.valueOf(ch) - 1);
		ml = s.createQuery("from Movie m where m.movieName='" + selectMovie + "' and m.multiplex='" + selectMultiplex
				+ "' and " + "m.time='" + selectTime + "' and m.screen='" + selectScreen + "'").list();
		movie = ml.get(0);
		customer = s.get(Customer.class, mail);
		System.out.println("Seats are Filling fast.. " + movie.getSeatCount() + "seats are available");
		System.out.println("Ticket Price : "+movie.getPrice());
		System.out.println("How many tickets do you want ?");
		String inputTcount = sc.next();
		while (!Pattern.matches("[0-9]{1,2}", inputTcount)) {
			System.out.println("Invalid input...Try again");
			inputTcount = sc.next();
		}
		int tcount = Integer.valueOf(inputTcount);
		if (tcount > movie.getSeatCount()) {
			System.out.println(tcount + " seats not available");
		} else {
			List<BookingList> bl = s.createQuery("from BookingList b where b.customerEmail='" + mail + "'").list();
			int userPay = calculateTcktPrice(bl, customer, movie, tcount, s);
			System.out.println("Pay total charge : " + userPay);
			String charge = sc.next();
			while (!charge.equals(String.valueOf(userPay))) {
				System.out.println("Wrong Payment..Try again");
				charge = sc.next();
			}
			bookingList.setCustomerEmail(mail);
			bookingList.setMovieName(movie.getMovieName());
			bookingList.setMultiplex(movie.getMultiplex());
			bookingList.setTime(movie.getTime());
			bookingList.setScreen(movie.getScreen());
			bookingList.setTicketCount(tcount);
			bookingList.setPrice(userPay);
			movie.setSeatCount(movie.getSeatCount() - tcount);
			s.save(bookingList);
			s.update(movie);
			t.commit();
			System.out.println("Ticket Booking successful...");
			System.out.println("your Ticket : ");
			System.out.println(
					"Movie : " + selectMovie + ",Multplex : " + selectMultiplex + ",Time : " + selectTime + ",Screen : "
							+ selectScreen + "," + "No. of Tickets : " + tcount + ",Total : " + userPay + " Rs.");
		}
	}

	// method to calculate ticket price
	public static int calculateTcktPrice(List<BookingList> bl, Customer customer, Movie movie, int tcount, Session s) {
		int userPay = 0;
		if (bl.isEmpty() && customer.isSubscription() == true) {
			userPay = (movie.getPrice() * tcount * 35) / 100;
		} else if (bl.isEmpty() && customer.isSubscription() == false) {
			userPay = (movie.getPrice() * tcount * 70) / 100;
		} else if (customer.isSubscription() == true && !bl.isEmpty()) {
			userPay = (movie.getPrice() * tcount * 50) / 100;
		} else if (customer.isSubscription() == false && !bl.isEmpty()) {
			System.out.println("Do you have any discount code ? [y->Yes],[Any other key->No]");
			String ch = sc.next();
			String discode = null;
			if (ch.equalsIgnoreCase("y")) {
				System.out.println("Enter code : ");
				discode = sc.next();
				@SuppressWarnings("unchecked")
				List<Discount> dl = s
						.createQuery("select d.discountCode from Discount d where d.discountCode='" + discode + "'")
						.list();
				if (!dl.contains(discode)) {
					System.out.println("Code not found...");
					userPay = movie.getPrice() * tcount;
				} else {
					userPay = (movie.getPrice() * tcount * (100 - s.get(Discount.class, discode).getDiscountPercent()))
							/ 100;
				}
			}
		}
		return userPay;
	}

	// search movie
	private static void searchMovie(Movie movie, Session s, Customer customer, BookingList bookingList, Discount disc,
			String mail) {
		System.out.println("[1->search by movie title]" + "\n[2->search by category/genre]\n[3->search by multiplex]");
		String ch = sc.next();
		while (!Pattern.matches("[1-3]", ch)) {
			System.out.println("Invalid choice...try again");
			ch = sc.next();
		}
		if (ch.equals("1")) {
			System.out.println("Enter movie name :");
			String inputChoie = sc.next();
			String movieChoice=inputChoie.toUpperCase();
			searchByMovieTitle(movie, s, customer, bookingList, disc, mail, movieChoice);
		} else if (ch.equals("2")) {
			searchByGenre(movie, s, customer, bookingList, disc, mail);
		}else {
			System.out.println("Enter Multiplex name : ");
			String inputMultiplex=sc.next();
			String selectMultiplex=inputMultiplex.toUpperCase();
			searchByMultiplex(movie, s, customer, bookingList, disc, mail, selectMultiplex);	
		}
	}

	// search by movie title
	private static void searchByMovieTitle(Movie movie, Session s, Customer customer, BookingList bookingList,
			Discount disc, String mail, String movieChoice) {
		Transaction t = s.beginTransaction();
		List<Movie> ml = s.createQuery("select distinct m.movieName from Movie m").list();
		if (!ml.contains(movieChoice)) {
			System.out.println("No Results...");
		} else {
			ml = s.createQuery("select distinct m.multiplex from Movie m where m.movieName='" + movieChoice + "'")
					.list();
			for (int i = 0; i <= ml.size() - 1; i++) {
				System.out.println("[" + (i + 1) + "->" + ml.get(i) + "]");
			}
			System.out.println("Select from above : ");
			String ch = sc.next();
			while (!Pattern.matches("[1-" + ml.size() + "]", ch)) {
				System.out.println("invalid choice...try again");
				ch = sc.next();
			}
			Object selectMultiplex = ml.get(Integer.valueOf(ch) - 1);
			ml = s.createQuery("select distinct m.time from Movie m where m.movieName='" + movieChoice + "' and "
					+ "m.multiplex='" + selectMultiplex + "'").list();
			for (int i = 0; i <= ml.size() - 1; i++) {
				System.out.println("[" + (i + 1) + "->" + ml.get(i) + "]");
			}
			System.out.println("select from above Options :");
			ch = sc.next();
			while (!Pattern.matches("[1-" + ml.size() + "]", ch)) {
				System.out.println("invalid choice...try again");
				ch = sc.next();
			}
			Object selectTime = ml.get(Integer.valueOf(ch) - 1);
			ml = s.createQuery("select m.screen from Movie m where m.movieName='" + movieChoice + "' and "
					+ "m.multiplex='" + selectMultiplex + "' and m.time='" + selectTime + "'").list();
			for (int i = 0; i <= ml.size() - 1; i++) {
				System.out.println("[" + (i + 1) + "->" + ml.get(i) + "]");
			}
			System.out.println("select from above Options :");
			ch = sc.next();
			while (!Pattern.matches("[1-" + ml.size() + "]", ch)) {
				System.out.println("invalid choice...try again");
				ch = sc.next();
			}
			Object selectScreen = ml.get(Integer.valueOf(ch) - 1);
			ml = s.createQuery("from Movie m where m.movieName='" + movieChoice + "' and m.multiplex='"
					+ selectMultiplex + "' and " + "m.time='" + selectTime + "' and m.screen='" + selectScreen + "'")
					.list();
			movie = ml.get(0);
			customer = s.get(Customer.class, mail);
			System.out.println("Seats are Filling fast.. " + movie.getSeatCount() + "seats available");
			System.out.println("How many tickets do you want ?");
			String inputTcount = sc.next();
			while (!Pattern.matches("[0-9]{1,2}", inputTcount)) {
				System.out.println("Invalid input...Try again");
				inputTcount = sc.next();
			}
			int tcount = Integer.valueOf(inputTcount);
			if (tcount > movie.getSeatCount()) {
				System.out.println(tcount + " seats not available");
			} else {
				List<BookingList> bl = s.createQuery("from BookingList b where b.customerEmail='" + mail + "'").list();
				int userPay = calculateTcktPrice(bl, customer, movie, tcount, s);
				System.out.println("Pay total charge : " + userPay);
				String charge = sc.next();
				while (!charge.equals(String.valueOf(userPay))) {
					System.out.println("Wrong Payment..Try again");
					charge = sc.next();
				}
				bookingList.setCustomerEmail(mail);
				bookingList.setMovieName(movie.getMovieName());
				bookingList.setMultiplex(movie.getMultiplex());
				bookingList.setTime(movie.getTime());
				bookingList.setScreen(movie.getScreen());
				bookingList.setTicketCount(tcount);
				bookingList.setPrice(userPay);
				movie.setSeatCount(movie.getSeatCount() - tcount);
				s.save(bookingList);
				s.update(movie);
				t.commit();
				System.out.println("Ticket Booking successful...");
				System.out.println("your Ticket : ");
				System.out.println("Movie : " + movieChoice + ",Multplex : " + selectMultiplex + ",Time : " + selectTime
						+ ",Screen : " + selectScreen + "," + "No. of Tickets : " + tcount + ",Total : " + userPay
						+ " Rs.");
			}
		}
	}

	// search by genre
	private static void searchByGenre(Movie movie, Session s, Customer customer, BookingList bookingList, Discount disc,
			String mail) {
		Transaction t=s.beginTransaction();
		List<Movie> ml = s.createQuery("select distinct m.genre from Movie m").list();
		if (ml.isEmpty()) {
			System.out.println("No Results Found...");
		} else {
			for(int i=0;i<ml.size();i++) {
				System.out.println("[" + (i + 1) + "->" + ml.get(i) + "]");
			}
			System.out.println("Select from above : ");
			String ch = sc.next();
			while (!Pattern.matches("[1-" + ml.size() + "]", ch)) {
				System.out.println("invalid choice...try again");
				ch = sc.next();
			}
			Object selectGenre = ml.get(Integer.valueOf(ch) - 1);
			ml = s.createQuery("select distinct m.movieName from Movie m where m.genre='" + selectGenre + "'").list();
			for (int i = 0; i <= ml.size() - 1; i++) {
				System.out.println("[" + (i + 1) + "->" + ml.get(i) + "]");
			}
			System.out.println("Select from above : ");
			ch = sc.next();
			while (!Pattern.matches("[1-" + ml.size() + "]", ch)) {
				System.out.println("invalid choice...try again");
				ch = sc.next();
			}
			Object selectMovie = ml.get(Integer.valueOf(ch) - 1);
			ml = s.createQuery("select distinct m.multiplex from Movie m where m.movieName='" + selectMovie + "'")
					.list();
			for (int i = 0; i <= ml.size() - 1; i++) {
				System.out.println("[" + (i + 1) + "->" + ml.get(i) + "]");
			}
			System.out.println("Select from above : ");
			ch = sc.next();
			while (!Pattern.matches("[1-" + ml.size() + "]", ch)) {
				System.out.println("invalid choice...try again");
				ch = sc.next();
			}
			Object selectMultiplex = ml.get(Integer.valueOf(ch) - 1);
			ml = s.createQuery("select distinct m.time from Movie m where m.movieName='" + selectMovie + "' and "
					+ "m.multiplex='" + selectMultiplex + "'").list();
			for (int i = 0; i <= ml.size() - 1; i++) {
				System.out.println("[" + (i + 1) + "->" + ml.get(i) + "]");
			}
			System.out.println("select from above Options :");
			ch = sc.next();
			while (!Pattern.matches("[1-" + ml.size() + "]", ch)) {
				System.out.println("invalid choice...try again");
				ch = sc.next();
			}
			Object selectTime = ml.get(Integer.valueOf(ch) - 1);
			ml = s.createQuery("select m.screen from Movie m where m.movieName='" + selectMovie + "' and "
					+ "m.multiplex='" + selectMultiplex + "' and m.time='" + selectTime + "'").list();
			for (int i = 0; i <= ml.size() - 1; i++) {
				System.out.println("[" + (i + 1) + "->" + ml.get(i) + "]");
			}
			System.out.println("select from above Options :");
			ch = sc.next();
			while (!Pattern.matches("[1-" + ml.size() + "]", ch)) {
				System.out.println("invalid choice...try again");
				ch = sc.next();
			}
			Object selectScreen = ml.get(Integer.valueOf(ch) - 1);
			ml = s.createQuery("from Movie m where m.movieName='" +selectMovie + "' and m.multiplex='"
					+ selectMultiplex + "' and " + "m.time='" + selectTime + "' and m.screen='" + selectScreen + "'")
					.list();
			movie = ml.get(0);
			customer = s.get(Customer.class, mail);
			System.out.println("Seats are Filling fast.. " + movie.getSeatCount() + "seats available");
			System.out.println("How many tickets do you want ?");
			String inputTcount = sc.next();
			while (!Pattern.matches("[0-9]{1,2}", inputTcount)) {
				System.out.println("Invalid input...Try again");
				inputTcount = sc.next();
			}
			int tcount = Integer.valueOf(inputTcount);
			if (tcount > movie.getSeatCount()) {
				System.out.println(tcount + " seats not available");
			} else {
				List<BookingList> bl = s.createQuery("from BookingList b where b.customerEmail='" + mail + "'").list();
				int userPay = calculateTcktPrice(bl, customer, movie, tcount, s);
				System.out.println("Pay total charge : " + userPay);
				String charge = sc.next();
				while (!charge.equals(String.valueOf(userPay))) {
					System.out.println("Wrong Payment..Try again");
					charge = sc.next();
				}
				bookingList.setCustomerEmail(mail);
				bookingList.setMovieName(movie.getMovieName());
				bookingList.setMultiplex(movie.getMultiplex());
				bookingList.setTime(movie.getTime());
				bookingList.setScreen(movie.getScreen());
				bookingList.setTicketCount(tcount);
				bookingList.setPrice(userPay);
				movie.setSeatCount(movie.getSeatCount() - tcount);
				s.save(bookingList);
				s.update(movie);
				t.commit();
				System.out.println("Ticket Booking successful...");
				System.out.println("your Ticket : ");
				System.out.println("Movie : " + selectMovie + ",Multplex : " + selectMultiplex + ",Time : " + selectTime
						+ ",Screen : " + selectScreen + "," + "No. of Tickets : " + tcount + ",Total : " + userPay
						+ " Rs.");
			}
		}
	}
	//search by multiplex method
	private static void searchByMultiplex(Movie movie, Session s, Customer customer, BookingList bookingList, Discount disc,
			String mail, String selectMultiplex) {
		Transaction t=s.beginTransaction();
		List<Movie> ml=s.createQuery("select distinct m.multiplex from Movie m").list();
		if(!ml.contains(selectMultiplex)) {
			System.out.println("No result found");
		}else {
			ml=s.createQuery("select distinct m.movieName from Movie m where m.multiplex='" + selectMultiplex + "'").list();
			for (int i = 0; i <= ml.size() - 1; i++) {
				System.out.println("[" + (i + 1) + "->" + ml.get(i) + "]");
			}
			System.out.println("Select from above : ");
			String ch = sc.next();
			while (!Pattern.matches("[1-" + ml.size() + "]", ch)) {
				System.out.println("invalid choice...try again");
				ch = sc.next();
			}
			Object selectMovie = ml.get(Integer.valueOf(ch) - 1);
			ml = s.createQuery("select distinct m.time from Movie m where m.movieName='" + selectMovie + "' and "
					+ "m.multiplex='" + selectMultiplex + "'").list();
			for (int i = 0; i <= ml.size() - 1; i++) {
				System.out.println("[" + (i + 1) + "->" + ml.get(i) + "]");
			}
			System.out.println("select from above Options :");
			ch = sc.next();
			while (!Pattern.matches("[1-" + ml.size() + "]", ch)) {
				System.out.println("invalid choice...try again");
				ch = sc.next();
			}
			Object selectTime = ml.get(Integer.valueOf(ch) - 1);
			ml = s.createQuery("select m.screen from Movie m where m.movieName='" + selectMovie + "' and "
					+ "m.multiplex='" + selectMultiplex + "' and m.time='" + selectTime + "'").list();
			for (int i = 0; i <= ml.size() - 1; i++) {
				System.out.println("[" + (i + 1) + "->" + ml.get(i) + "]");
			}
			System.out.println("select from above Options :");
			ch = sc.next();
			while (!Pattern.matches("[1-" + ml.size() + "]", ch)) {
				System.out.println("invalid choice...try again");
				ch = sc.next();
			}
			Object selectScreen = ml.get(Integer.valueOf(ch) - 1);
			ml = s.createQuery("from Movie m where m.movieName='" +selectMovie + "' and m.multiplex='"
					+ selectMultiplex + "' and " + "m.time='" + selectTime + "' and m.screen='" + selectScreen + "'")
					.list();
			movie = ml.get(0);
			customer = s.get(Customer.class, mail);
			System.out.println("Seats are Filling fast.. " + movie.getSeatCount() + "seats available");
			System.out.println("How many tickets do you want ?");
			String inputTcount = sc.next();
			while (!Pattern.matches("[0-9]{1,2}", inputTcount)) {
				System.out.println("Invalid input...Try again");
				inputTcount = sc.next();
			}
			int tcount = Integer.valueOf(inputTcount);
			if (tcount > movie.getSeatCount()) {
				System.out.println(tcount + " seats not available");
			} else {
				List<BookingList> bl = s.createQuery("from BookingList b where b.customerEmail='" + mail + "'").list();
				int userPay = calculateTcktPrice(bl, customer, movie, tcount, s);
				System.out.println("Pay total charge : " + userPay);
				String charge = sc.next();
				while (!charge.equals(String.valueOf(userPay))) {
					System.out.println("Wrong Payment..Try again");
					charge = sc.next();
				}
				bookingList.setCustomerEmail(mail);
				bookingList.setMovieName(movie.getMovieName());
				bookingList.setMultiplex(movie.getMultiplex());
				bookingList.setTime(movie.getTime());
				bookingList.setScreen(movie.getScreen());
				bookingList.setTicketCount(tcount);
				bookingList.setPrice(userPay);
				movie.setSeatCount(movie.getSeatCount() - tcount);
				s.save(bookingList);
				s.update(movie);
				t.commit();
				System.out.println("Ticket Booking successful...");
				System.out.println("your Ticket : ");
				System.out.println("Movie : " + selectMovie + ",Multplex : " + selectMultiplex + ",Time : " + selectTime
						+ ",Screen : " + selectScreen + "," + "No. of Tickets : " + tcount + ",Total : " + userPay
						+ " Rs.");
			}
		}
	}

	// my ticket
	private static void myTicket(String mail, BookingList bookingList, Session s) {
		List<BookingList> bl = s.createQuery("from BookingList b where b.customerEmail='" + mail + "'").list();
		if (bl.isEmpty()) {
			System.out.println("No tickets found...");
		} else {
			for (int i = 0; i < bl.size(); i++) {
				System.out.println(bl.get(i));
			}
		}
	}
}
