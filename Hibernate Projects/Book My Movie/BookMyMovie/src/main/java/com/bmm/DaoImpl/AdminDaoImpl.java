package com.bmm.DaoImpl;

import java.util.List;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bmm.Dao.AdminDao;
import com.bmm.entity.Admin;
import com.bmm.entity.Discount;
import com.bmm.entity.Movie;
import com.bmm.util.BookMyMovieUtil;

public class AdminDaoImpl implements AdminDao {
	static Scanner sc = new Scanner(System.in);

	// implementing method which creates a new admin account
	@Override
	public void createAdmin(Admin admin) {
		System.out.println("Enter The Security Key : ");
		String key = sc.next();
		if (key.equals("*Siuu777#")) {
			Session s = BookMyMovieUtil.getSessionFactory().openSession();
			Transaction t = s.beginTransaction();
			System.out.println("Enter First Name : ");
			String fname = sc.next();
			while (!Pattern.matches("[a-zA-z]{1,20}", fname)) {
				System.out.println("Invalid First Name...\nTry Again :");
				fname = sc.next();
			}
			System.out.println("Enter Last name : ");
			String lname = sc.next();
			while (!Pattern.matches("[a-zA-Z]{1,20}", lname)) {
				System.out.println("Invalid Last Name...\nTry Again :");
				lname = sc.next();
			}
			System.out.println("Enter Address : ");
			String address = sc.next();
			while (!Pattern.matches("[a-zA-Z0-9]{1,50}", address)) {
				System.out.println("Invalid Address...\nTry Again : ");
				address = sc.next();
			}
			System.out.println("Enter Phone No. : ");
			String ph = sc.next();
			while (!Pattern.matches("[1-9]{1}[0-9]{9}", ph)) {
				System.out.println("Invalid Phone No...\nTry Again : ");
				ph = sc.next();
			}
			String password;
			while (true) {
				System.out.println(
						" create password " + "\n(Note : you must follow these rules while creating the password : "
								+ "\nIt should be six characters." + "\nIt should contain at least one digit."
								+ "\nIt should contain at least one upper case alphabet."
								+ "\nIt should contain at least one lower case alphabet."
								+ "\nIt should contain at least one special character."
								+ "\nIt shouldn't contain any white space.)");
				password = sc.next();

				while (!Pattern.matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{6}$", password)) {
					System.out.println("Your password doesn't match the validations." + "\n Try again : ");
					password = sc.next();
				}
				System.out.println("Re-enter the password to confirm : ");
				String confirmPassword = sc.next();
				while (!password.equals(confirmPassword)) {
					System.out.println("Passwords doesn't match." + "\nTry again : ");
					confirmPassword = sc.next();
				}
				if (validateDuplicatePass(s, password)) {
					System.out.println("Password Already Exists....\nPlease choose a different password...");
					continue;
				} else
					break;
			}
			String id;
			while (true) {
				id = String.valueOf(new Random().longs(10000, 99999).findFirst().getAsLong());
				if (isPresentId(s, id)) {
					continue;
				} else
					break;
			}
			admin.setAdminId(id);
			admin.setAdminFName(fname);
			admin.setAdminLName(lname);
			admin.setAdminAddress(address);
			admin.setAdminPhn(ph);
			admin.setAdminPwd(password);
			s.save(admin);
			t.commit();
			System.out.println("Profile Created Successfully...");
			System.out.println("Your Admin Id is : " + s.get(Admin.class, id).getAdminId());
			System.out.println("Keep it safe..It will be needed for login...");
		} else {
			System.out.println("Wrong Key...");
		}
	}

	// method to check whether the given password already exists in the database
	private static boolean validateDuplicatePass(Session s, String pwd) {
		List passwordList = s.createQuery("select a.adminPwd from Admin a").list();
		if (passwordList.contains(pwd)) {
			return true;
		} else
			return false;
	}

	// method to check whether the given id already exists in the database
	private static boolean isPresentId(Session s, String id) {
		List idList = s.createQuery("select a.adminId from Admin a").list();
		if (idList.contains(id)) {
			return true;
		} else
			return false;
	}

	// method for admin login
	public void adminLogin(String id, String pwd, Admin admin, Movie movie,Discount discount) {
		Session s = BookMyMovieUtil.getSessionFactory().openSession();
		Transaction t = s.beginTransaction();
		if (!isPresentId(s, id)) {
			System.out.println("No account exist with this id...");
		} else if (!s.get(Admin.class, id).getAdminPwd().equals(pwd)) {
			System.out.println("Wrong Password...");
		} else {
			while (true) {
				String choice;
				while (true) {
					System.out.println("[1->My Profile]\n[2->Movies]\n[0->Log Out]");
					choice = sc.next();
					if (!Pattern.matches("[0-2]", choice)) {
						System.out.println("Invalid Choice... ");
						continue;
					} else
						break;
				}
				if (choice.equals("0")) {
					break;
				} else if (choice.equals("1")) {
					System.out.println("Welcome " + fetchAdminDetails(s, id).getAdminFName() + " "
							+ fetchAdminDetails(s, id).getAdminLName());
					System.out.println("Your Details : \n" + fetchAdminDetails(s, id));
					String choice2;
					while (true) {
						System.out.println("[1->Edit Profile]\n[2->Change Password]\n[3->Delete Account]");
						choice2 = sc.next();
						if (!Pattern.matches("[1-3]", choice2)) {
							System.out.println("invalid Choice...Try Again");
							continue;
						} else
							break;
					}
					if (choice2.equals("1")) {
						updateAdmin(s, id, t, admin);
					} else if (choice2.equals("2")) {
						changeAdminPass(id, pwd, admin);
					} else if (choice2.equals("3")) {
						deleteAdmin(admin, id, pwd);break;
					}
				} else if (choice.equals("2")) {
					movieOps(movie,discount);
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
				} else if (choice3.equals("0")) {
					break;
				}
			}
		}
	}

	// method to fetch admin details
	public static Admin fetchAdminDetails(Session s, String id) {
		return s.get(Admin.class, id);
	}

	// method to update details
	public static void updateAdmin(Session s, String id, Transaction t, Admin admin) {
		while (true) {
			System.out.println("[1->Change Name]\n[2->Change Phone No.]");
			String choice = sc.next();
			if (choice.equals("1")) {
				changeAdminName(id, admin);
				break;
			} else if (choice.equals("2")) {
				changeAdminPhone(id, admin);
				break;
			} else {
				System.out.println("Invalid choice...Try again");
				continue;
			}
		}
	}

	// method to change admin name
	public static void changeAdminName(String id, Admin admin) {
		Session s = BookMyMovieUtil.getSessionFactory().openSession();
		Transaction t = s.beginTransaction();
		String fname, lname;
		while (true) {
			System.out.println("Enter New First Name : ");
			fname = sc.next();
			if (!Pattern.matches("[a-zA-Z]{1,20}", fname)) {
				System.out.println("Invalid Name...Try again");
				continue;
			} else
				break;
		}
		while (true) {
			System.out.println("Enter New Last Name : ");
			lname = sc.next();
			if (!Pattern.matches("[a-zA-Z]{1,20}", lname)) {
				System.out.println("Invalid Name...Try again");
				continue;
			} else
				break;
		}
		admin = s.get(Admin.class, id);
		admin.setAdminFName(fname);
		admin.setAdminLName(lname);
		s.update(admin);
		t.commit();
		s.close();
		System.out.println("Name changed successfully to " + admin.getAdminFName() + " " + admin.getAdminLName());

	}

	// method to change admin phone no.
	public static void changeAdminPhone(String id, Admin admin) {
		Session s = BookMyMovieUtil.getSessionFactory().openSession();
		Transaction t = s.beginTransaction();
		String newPhn;
		while (true) {
			System.out.println("Enter New Phone No. : ");
			newPhn = sc.next();
			if (!Pattern.matches("[1-9]{1}[0-9]{9}", newPhn)) {
				System.out.println("Invalid Phone No...Try again");
				continue;
			} else
				break;
		}
		admin = s.get(Admin.class, id);
		admin.setAdminPhn(newPhn);
		s.update(admin);
		t.commit();
		s.close();
		System.out.println("Phone No. changed successfully to " + admin.getAdminPhn());
	}

	// method to change passwordS
	public static void changeAdminPass(String id, String pwd, Admin admin) {
		Session s = BookMyMovieUtil.getSessionFactory().openSession();
		Transaction t = s.beginTransaction();
		String newPwd, confirmNewPwd, oldPwd;
		while (true) {
			System.out.println("Enter Old Password : ");
			oldPwd = sc.next();
			if (!oldPwd.equals(pwd)) {
				System.out.println("Wrong Password...Try again");
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
			newPwd = sc.next();
			if (!Pattern.matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{6}$", newPwd)) {
				System.out.println("Your password doesn't match the validations...Try again");
				continue;
			} else
				break;
		}
		while (true) {
			System.out.println("Confirm New Password : ");
			confirmNewPwd = sc.next();
			if (!confirmNewPwd.equals(newPwd)) {
				System.out.println("Passwords doesn't match...Try again");
				continue;
			} else
				break;
		}
		while (true) {
			System.out.println("Enter security key to change the password :");
			String key = sc.next();
			if (!key.equals("*Siuu777#")) {
				System.out.println("Wrong Key..Try again");
				continue;
			} else {
				if(validateDuplicatePass(s,confirmNewPwd)) {
					System.out.println("Password already exists..");
					break;
				}else {
					admin = s.get(Admin.class, id);
					admin.setAdminPwd(newPwd);
					s.update(admin);
					t.commit();
					s.close();
					System.out.println("Password changed succesfully");
					break;
				}
			}
		}
	}

	// method to delete admin account
	public static void deleteAdmin(Admin admin, String id, String pwd) {
		Session s = BookMyMovieUtil.getSessionFactory().openSession();
		Transaction t = s.beginTransaction();
		while (true) {
			System.out.println("Enter password :");
			String pass = sc.next();
			if (!pass.equals(s.get(Admin.class, id).getAdminPwd())) {
				System.out.println("Wrong password...Try again ");
				continue;
			} else {
				System.out.println("Enter Security Key to delete profile : ");
				String key = sc.next();
				if (!key.equals("*Siuu777#")) {
					System.out.println("Wrong key...Try again");
					continue;
				} else {
					admin = s.get(Admin.class, id);
					s.delete(admin);
					t.commit();
					s.close();
					System.out.println("Account Deleted...");
					break;
				}
			}
		}
	}

	// movie operations by admin
	public static void movieOps(Movie movie,Discount discount) {
		String choice;
		while (true) {
			System.out.println("[1->Add Movie]\n[2->See Movie Entries]\n[3->Update/Delete Movie]"
					+ "\n[4->Add/See Discount Code]");
			choice = sc.next();
			if (choice.equals("1")) {
				addMovie(movie);break;
			} else if (choice.equals("2")) {
				fetchMovie(movie);break;
			} else if (choice.equals("3")) {
				updateMovie(movie);break;
			}else if(choice.equals("4")) {
				discountCode(discount);break;
			}
			else {
				System.out.println("Invalid choice...Try again");
				continue;
			}
		}
		
	}
	
	//method discount codes operations
	private static void discountCode(Discount discount) {
		while(true) {
			System.out.println("[1->Add New Discount Code],[2->See Discount Codes],"
					+ "[3->Delete Discount Code]");
			String ch=sc.next();
			if(ch.equals("1")) {
				addDiscountCode(discount);break;
			}else if(ch.equals("2")) {
				fetchDiscountCode(discount);break;
			}else if(ch.equals("3")) {
				deleteDiscountCode(discount);break;
			}else {
				System.out.println("Invalid Choice...Try again");
				continue;
			}
		}	
	}
	private static void deleteDiscountCode(Discount discount) {
		Session s=BookMyMovieUtil.getSessionFactory().openSession();
		Transaction t=s.beginTransaction();
		int discPer;
		while(true) {
			System.out.println("Enter which percentage code you want to delete : ");
			discPer=sc.nextInt();
			if(!Pattern.matches("[0-9]+",String.valueOf(discPer))) {
				System.out.println("Invalid Input...Try again");
				continue;
			}else
				break;
		}
		List<Discount> discList=s.createQuery("from Discount d where d.discountPercent="+discPer).list();
		if(discList.isEmpty()) {
			System.out.println("No result found.");
		}else {
			discount=discList.get(0);
			s.delete(discount);
			t.commit();
			s.close();
			System.out.println("Discount Code Deleted Successfully...");
		}
	}

	public static void fetchDiscountCode(Discount discount) {
		Session s=BookMyMovieUtil.getSessionFactory().openSession();
		List discList=s.createQuery("From Discount").list();
		for (Iterator itr = discList.iterator(); itr.hasNext();) {
			discount= (Discount) itr.next();
			System.out.println(discount);
		}
	}

	//method to add discount codes
	public static void addDiscountCode(Discount disc) {
		Session s=BookMyMovieUtil.getSessionFactory().openSession();
		Transaction t=s.beginTransaction();
		String discPer;
		while(true) {
			System.out.println("Enter Discount Percentage you want to add :");
			discPer=sc.next();
			if(!Pattern.matches("[0-9]{1,2}", discPer)) {
				System.out.println("Invalid Percentage..Try again");
				continue;
			}else {
				break;
			}
		}
		List discPerList=s.createQuery("select d.discountPercent from Discount d").list();
		if(discPerList.contains(discPer)) {
			System.out.println("Discount code for "+discPer+"% is already present in the databse.");
		}else {
			String disCode;
			while(true) {
				disCode=String.valueOf(new Random().longs(10000, 99999).findFirst().getAsLong());
				List disCodeList=s.createQuery("select d.discountCode from Discount d").list();
				if(disCodeList.contains(disCode)) {
					continue;
				}else {
					break;
				}
			}
			disc.setDiscountCode(disCode);
			disc.setDiscountPercent(Integer.valueOf(discPer));
			s.save(disc);
			t.commit();
			s.close();
			System.out.println("Discount added successfully...");
		}
	}
	// method to add movie
	public static void addMovie(Movie movie) {
		Session s = BookMyMovieUtil.getSessionFactory().openSession();
		Transaction t = s.beginTransaction();
		String choice, multiplex = null, time = null, screen = null, genre = null, language = null, price;
		System.out.println("Enter Movie Name :");
		String inputName = sc.next();
		String mName = inputName.toUpperCase();
		while (true) {
			System.out.println("Choose Category : "
					+ "\n[1->Action],[2->Comedy],[3->Romance],[4->Sci-Fi],[5->Thriller],[6->Fantasy]");
			choice = sc.next();
			if (choice.equals("1")) {
				genre = "Action";
				break;
			} else if (choice.equals("2")) {
				genre = "Comedy";
				break;
			} else if (choice.equals("3")) {
				genre = "Romance";
				break;
			} else if (choice.equals("4")) {
				genre = "Sci-Fi";
				break;
			} else if (choice.equals("5")) {
				genre = "Thriller";
				break;
			} else if (choice.equals("6")) {
				genre = "Fantasy";
				break;
			} else {
				System.out.println("Invalid choice...Try again");
				continue;
			}
		}
		while (true) {
			System.out.println("Movie Language : [1->Bengali],[2->Hindi],[3->English]");
			choice = sc.next();
			if (choice.equals("1")) {
				language = "Bengali";
				break;
			} else if (choice.equals("2")) {
				language = "Hindi";
				break;
			} else if (choice.equals("3")) {
				language = "English";
				break;
			} else {
				System.out.println("Invalid choice...Try again");
				continue;
			}
		}
		while (true) {
			System.out.println("Choose Multiplex :\n[1->INOX],[2->PVR],[3->SVF]");
			choice = sc.next();
			if (!Pattern.matches("[1-3]", choice)) {
				System.out.println("Invalid Choice..Try again");
				continue;
			} else
				break;
		}
		if (choice.equals("1")) {
			multiplex = "INOX";
		} else if (choice.equals("2")) {
			multiplex = "PVR";
		} else if (choice.equals("3")) {
			multiplex = "SVF";
		}
		while (true) {
			System.out.println("Choose Show Timings :\n[1->12pm],[2->3pm][3->7pm]");
			choice = sc.next();
			if (!Pattern.matches("[1-3]", choice)) {
				System.out.println("invalid choice...Try again");
				continue;
			} else
				break;
		}
		if (choice.equals("1")) {
			time = "12pm";
		} else if (choice.equals("2")) {
			time = "3pm";
		} else if (choice.equals("3")) {
			time = "7pm";
		}
		while (true) {
			System.out.println("Choose Screen :\n[1->Screen 1][2->Screen 2]");
			choice = sc.next();
			if (!Pattern.matches("[1-2]", choice)) {
				System.out.println("Invalid choice...Try again");
				continue;
			} else
				break;
		}
		if (choice.equals("1")) {
			screen = "Screen 1";
		} else if (choice.equals("2")) {
			screen = "Screen 2";
		}

		while (true) {
			System.out.println("Enter Ticket Price : ");
			price = sc.next();
			if (!Pattern.matches("[0-9]{2,3}", price)) {
				System.out.println("Invalid Price..Try again");
				continue;
			} else
				break;
		}
		int ticketPrice = Integer.valueOf(price);
		List<Movie> ml= s.createQuery("from Movie m where m.multiplex='" + multiplex
				+ "' and m.time='" + time + "' and m.screen='" + screen + "'").list();
		System.out.println(ml.isEmpty());
		if (ml.isEmpty()) {
			setEntry(movie, mName, screen, time, multiplex, genre, language, ticketPrice);
			s.save(movie);
			System.out.println("Movie added succesfully...");
		} else {
			movie=ml.get(0);
			if(movie.getMovieName().equals(mName)) {
				System.out.println("The Movie already exists at this location...");
			}else {
				System.out.println("Currently "+movie.getMovieName()+" is going on at "+
						movie.getMultiplex()+" on "+movie.getScreen()+" at "+movie.getTime());
				System.out.println("It will be replaced with the new movie."
						+ "\nContinue ? [y->Yes][Any Other Key->No]");
				String ch=sc.next();
				if(ch.equalsIgnoreCase("y")) {
					setEntry(movie, mName, screen, time, multiplex, genre, language, ticketPrice);
					s.update(movie);
				}
			}
		}
		t.commit();
		s.close();
	}
	// method to replace movie entries
	public static void setEntry(Movie movie, String mName, String screen, String time, String multiplex,
			String genre, String language, int ticketPrice) {
		 movie.setMovieName(mName);movie.setGenre(genre);
		 movie.setLanguage(language);movie.setMultiplex(multiplex);
		 movie.setScreen(screen);movie.setTime(time);
		 movie.setPrice(ticketPrice);movie.setSeatCount(20);
	}
	// method to fetch movie entries
	public static void fetchMovie(Movie movie) {
		Session s = BookMyMovieUtil.getSessionFactory().openSession();
		Transaction t = s.beginTransaction();
		List movieEntryList = s.createQuery("from Movie").list();
		if(movieEntryList.isEmpty()) {
			System.out.println("No Movies in the database...");
		}else {
			for (Iterator itr = movieEntryList.iterator(); itr.hasNext();) {
				movie = (Movie) itr.next();
				System.out.println(movie);
			}
		}
	}

	// method to update fields or delete movie
	public static void updateMovie(Movie movie) {
		String multiplex = null, screen = null, time = null;
		System.out.println("Enter Details : ");
		while (true) {
			System.out.println("Choose Multiplex : [1->INOX],[2->PVR],[3->SVF]");
			String choice = sc.next();
			if (choice.equals("1")) {
				multiplex = "INOX";
				break;
			} else if (choice.equals("2")) {
				multiplex = "PVR";
				break;
			} else if (choice.equals("3")) {
				multiplex = "SVF";
				break;
			} else {
				System.out.println("Invalid choice...Try again");
				continue;
			}
		}
		while (true) {
			System.out.println("Choose Screen No. : [1->Screen 1],[2->Screen 2]");
			String choice = sc.next();
			if (choice.equals("1")) {
				screen = "Screen 1";
				break;
			} else if (choice.equals("2")) {
				screen = "Screen 2";
				break;
			} else {
				System.out.println("Invalid choice...Try again");
				continue;
			}
		}
		while (true) {
			System.out.println("Choose Show Timings :\n[1->12pm],[2->3pm][3->7pm]");
			String choice = sc.next();
			if (choice.equals("1")) {
				time = "12pm";
				break;
			} else if (choice.equals("2")) {
				time = "3pm";
				break;
			} else if (choice.equals("3")) {
				time = "7pm";
				break;
			} else {
				System.out.println("Invalid choice...Try again");
				continue;
			}
		}
		Session s=BookMyMovieUtil.getSessionFactory().openSession();
		Transaction t=s.beginTransaction();
		List<Movie> ml = s.createQuery("from Movie m where m.multiplex='" + multiplex
				+ "' and m.time='" + time + "' and m.screen='" + screen + "'").list();
		if(ml.isEmpty()) {
			System.out.println("No Movie Found here...");
		}else {
			System.out.println("Currently "+ml.get(0).getMovieName()+" is showing here.");
			while(true) {
				System.out.println("Choose : [1->Delete Movie],[2->Update Ticket Price],[3->Reset Seat Count]");
				String choice=sc.next();
				if(choice.equals("1")) {
					s.delete(ml.get(0));
					t.commit();
					System.out.println("Movie deleted successfully...");
					break;
				}else if(choice.equals("2")) {
					updateMoviePrice(ml);
					s.update(ml.get(0));
					t.commit();
					System.out.println("Movie price updated successfully...");
					break;
				}else if(choice.equals("3")) {
					ml.get(0).setSeatCount(20);
					s.update(ml.get(0));
					t.commit();
					System.out.println("Seat Count reset successful...");
					break;
				}else {
					System.out.println("Invalid choice...Try again");
					continue;
				}
			}
		}
		s.close();
	}
	//method to update movie price
	public static void updateMoviePrice(List<Movie> ml) {
		String newPrice;
		while(true) {
			System.out.println("Enter Ticket new price : ");
			newPrice=sc.next();
			if(!Pattern.matches("[0-9]{2,3}", newPrice)) {
				System.out.println("Invalid Price..Try again");
				continue;
			}else {
				ml.get(0).setPrice(Integer.valueOf(newPrice));
				break;
			}
		}
	}
	
}
