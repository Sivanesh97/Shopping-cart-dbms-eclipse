
//$Id$

import java.text.MessageFormat;
import java.util.Scanner;

import Pojo.Address;

public class Auth {
	static Scanner d = new Scanner(System.in);

	static Checker c = Checker.getInstance();

	public static void main(String args[]) {
		new Auth();
		mainMenu();
	}

	protected static void mainMenu() {
		System.out.println("===============");
		System.out.println("... Welcome ... ");
		System.out.println("1. Vendor");
		System.out.println("2. Customer");
		System.out.println("3. Exit");

		System.out.println("Enter a choice");
		int choice = d.nextInt();

		switch (choice) {
		case 1:
			signing(true);
			break;
		case 2:
			signing(false);
			break;
		case 3:
			System.exit(0);
			break;
		default:
			System.out.println("Enter a Valid one");
			mainMenu();
		}
	}

	// Both for Vendor and Customer
	private static void signing(boolean isVendor) {
		System.out.println("===============");
		System.out.println("Welcome " + (isVendor ? "Vendor" : "Customer"));

		System.out.println("1. Sign in");
		System.out.println("2. Sign up");
		System.out.println("3. <- Back ");

		System.out.println("Enter a choice");
		int choice = d.nextInt();
		d.nextLine();
		switch (choice) {
		case 1:
			signIn(isVendor);
			break;
		case 2:
			signUp(isVendor);
			break;
		case 3:
			mainMenu();
			break;
		default:
			System.out.println("Enter a valid one");
			signing(isVendor);
		}

	}

	// Both for Vendor and Customer
	private static void signIn(boolean isVendor) {
		String table = isVendor ? "vendor" : "users";
		String password;
		System.out.println("--- SIGN IN ---");
		System.out.println("Enter username (0. <- Back)");
		String username = d.nextLine();
		if (username.equals("0")) {
			System.out.println("Process Aborted... :(");
			mainMenu();
			return;
		} else if (c.isEmpty(username)) {
			signIn(isVendor);
			return;
		}

		password = Sql.getPassword(username, table);
		if (password == null) {
			System.out.println("Wrong Username");
			signIn(isVendor);
		} else {
			System.out.println("Enter password");
			if (password.equals(d.nextLine())) {
				System.out.println("Logged in Successfully.\nWelcome " + username);
				if (isVendor)
					new Seller().main(username);
				else
					new Customer().main(username);
			} else {
				System.out.println("Wrong Sign in Credentials");
				signIn(isVendor);
			}
		}

	}

	// Both for Vendor and Customer
	private static void signUp(boolean isVendor) {
		String table = isVendor ? "vendor" : "users";
		System.out.println("SIGN UP");
		System.out.println("Enter username");
		String username = d.nextLine();
		System.out.println("Username " + username);
		if (Sql.isUserAlreadyPresent(username)) {
			System.out.println("Username already present");
			signUp(isVendor);
			return;
		}

		System.out.println("Enter password");
		String password = d.nextLine();
		System.out.println("Confirm password");
		if (password.equals(d.nextLine())) {
			System.out.println("Confirmed successfully");
		} else {
			System.out.println("Wrong password Confirmation");
			signUp(isVendor);
			return;
		}

		// Insertion
		if (!Sql.insertions(table, "(username, password)", String.format("'%s', '%s'", username, password))) {
			System.out.println("Username already Present");
			signUp(isVendor);
		}

		System.out.println("Inserted Successfully");
		if (isVendor)
			updateVendor(username);
		else
			updateUser(username);

		signIn(isVendor);
	}

	// Update Phone number
	protected static void updateUser(String username) {
		System.out.println("Enter phone number");
		String phone = d.nextLine();
		String table = "users";
		String datas = "phone = '" + phone + "'";
		String conditions = "username = '" + username + "'";
		if(!Sql.updations(table, datas, conditions)) {
			System.out.println("Issue in updation");
			updateUser(username);
		}
		System.out.println("Updated successfully\n");
		updateAddress(username);
	}

	// Update Address
	protected static int updateAddress(String username) {
		Address.AddressBuilder addressBuilder = new Address.AddressBuilder();
		

		addressBuilder.setId(Sql.getId("users", username));
		
		System.out.println("--- Address ---");
		
		System.out.println("Enter plot number");
		addressBuilder.setNumber(d.nextLine());
		
		System.out.println("Enter street");
		addressBuilder.setStreet(d.nextLine());
		
		System.out.println("Landmark");
		addressBuilder.setLandmark(d.nextLine());
		
		System.out.println("City");
		addressBuilder.setCity(d.nextLine());
		
		System.out.println("Pincode");
		try {
			addressBuilder.setPincode(d.nextInt());
		} catch (Exception er) {
			System.out.println("Only numbers");
			addressBuilder.setPincode(d.nextInt());
		}
		d.nextLine();
		System.out.println("State");
		addressBuilder.setState(d.nextLine());
		System.out.println("Country");
		addressBuilder.setCountry(d.nextLine());
		System.out.println("Is This your permanent address? ");
		addressBuilder.setPermanent(d.nextByte());
		
		Address address = addressBuilder.build();
		
		String columns = "(id, number, street, landmark, city, pincode, state, country, permanent)";
		String data = MessageFormat.format("{0}, ''{1}'', ''{2}'', ''{3}'', ''{4}'', {5}, ''{6}'', ''{7}'', {8}", 
				address.getId(), address.getNumber(), address.getStreet(), address.getLandmark(), address.getCity(), address.getPincode(), address.getState(), address.getCountry(), address.getPermanent()
				);
		if(Sql.insertions("address", columns, data)) {
			System.out.println("Address Updated Successfully");
		}

		return Sql.getAddress();
	}

	// Further Details adding to Vendor
	protected static void updateVendor(String username) {
		System.out.println("Enter company name");
		String company = d.nextLine();
		
		if(Sql.updations("vendor", "company = '" + company + "'", "username = '" + username + "'")) {
			System.out.println("Updated Successfully\n");
			
//			Sql.smt.execute("UPDATE vendor SET company = '" + company + "' WHERE username = '" + username + "'");
		} else { 
			System.out.println("Cannot update due to ");
			updateVendor(username);
		}
	}
}