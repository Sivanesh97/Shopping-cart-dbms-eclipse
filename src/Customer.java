
//$Id$

import java.sql.*;
import java.util.*;

import Pojo.Address;
import Pojo.Cart;
import Pojo.Products;

public class Customer {
	String username;
	int id;
	int billId;
	Scanner d = new Scanner(System.in);

	protected void main(String username) {
		this.username = username;
		id = Sql.getId("users", username);

		try {
			mainMenu();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void mainMenu() throws SQLException {
		int choice;
		char ch = 'y';
		while (ch == 'y') {
			System.out.println("Enter a choice");
			System.out.println("1. Add to Cart");
			System.out.println("2. Update Cart");
			System.out.println("3. Delete from Cart");
			System.out.println("4. List Products");
			System.out.println("5. Buy");
			System.out.println("6. Save Cart");
			System.out.println("7. View Cart");
			System.out.println("8. Clear cart");
			System.out.println("9. View Bills");
			System.out.println("10. Wish list");
			System.out.println("11. Logout");
			choice = d.nextInt();
			switch (choice) {
			case 1:
				insert();
				break;
			case 2:
				update();
				break;
			case 3:
				delete();
				break;
			case 4:
				productPreview();
				break;
			case 5:
				buy();
				break;
			case 6:
				saveCart();
				break;
			case 7:
				viewCart();
				break;
			case 8:
				clearCart();
				break;
			case 9:
				billList();
				break;
			case 10:
				wishList();
				break;
			case 11:
				logout();
				break;
			default:
				System.out.println("Enter between 1 to 8");
				mainMenu();
			}
		}
	}

	private void insert() throws SQLException {
		productPreview();
		System.out.println("--- INSERTION ---");
		System.out.println("Enter ID");
		int productId = d.nextInt();

		Sql.beginTransaction();

		// Checking whether already a product is present or not.
		if (Sql.isProductPresent("products", productId)) {
			System.out.printf("User ID = %d\n", id);
			System.out.println("Enter quantity");
			int quantity = d.nextInt();

			if (Sql.isAvailable(productId, quantity)) {

				if (Sql.insertions("cart", "(userId, productId, quantity)",
						String.format("%d, %d, %d", id, productId, quantity))) {

					// Sql.smt.execute(String.format("INSERT INTO cart "
					// + "(userId, productId, quantity)"
					// + "VALUES (%d, %d, %d)", id, productId, quantity
					// ));
					Sql.commitTransaction();
					System.out.println("Inserted successfully");
					mainMenu();
					return;
				} else {
					Sql.smt.execute("ROLLBACK");
					System.out.println("Already element present.");
					System.out.println("Do you want to update? (y/others)");
					char ch = d.next().charAt(0);
					if (ch == 'y')
						update();
					else
						mainMenu();
				}
			} else {
				Sql.rollbackTransaction();

				System.out.println("Check the stock available and enter again.");
				insert();
			}

		} else {
			Sql.rollbackTransaction();
			System.out.println("Not a valid ID enter another.");
			insert();
		}

	}

	private void update() throws SQLException {
		viewCart();
		System.out.println("--- UPDATION ---");
		System.out.println("Enter the product ID");
		int productId = d.nextInt();

		if (Sql.isHisProduct(productId, id)) {
			System.out.println("Enter the quantity");
			int quantity = d.nextInt();
			if (Sql.isAvailable(productId, quantity)) {
				Sql.updations("cart", "quantity = " + quantity, "productId = " + productId);
				System.out.println("Updated Successfully");
				mainMenu();
			} else {
				System.out.println("Check the quantity available and enter carefully.");
				update();
			}
		} else {
			System.out.println("No product found");
			update();
		}
	}

	private void delete() throws SQLException {
		// productPreview();
		System.out.println("--- DELETION ---");
		System.out.println("Enter id");
		int productId = d.nextInt();

		Sql.deletions("cart", String.format("userId = %d AND productId = %d", id, productId));

		// String checker = String.format("DELETE FROM cart WHERE userId = %d
		// AND productId = %d", id, productId);
		// Sql.smt.execute(checker);
		System.out.println("Deleted the product from the cart successfully");
		mainMenu();
	}

	private void productPreview() throws SQLException {
		System.out.println("--- Products ---");
		System.out.printf("%-10s %-20s %-20s %-20s %-10s %-20s %-20s %-20s \n", "id", "name", "category", "price",
				"quantity", "Vendor name", "Company", "Rating");

		ArrayList<ProductVendor> productVendors = Sql.productPreview();

		for (int i = 0; i < productVendors.size(); i++) {
			System.out.printf("%-10d %-20s %-20s %-20f %-10d %-20s %-20s %-20s \n",
					productVendors.get(i).products.getId(), productVendors.get(i).products.getName(),
					productVendors.get(i).products.getCategory(), productVendors.get(i).products.getPrice(),
					productVendors.get(i).products.getQuantity(), productVendors.get(i).vendor.getUsername(),
					productVendors.get(i).vendor.getCompany(), productVendors.get(i).products.getRating());
		}
	}

	private void buy() {
		try {
			int total = Sql.getTotal(id);
			if (total == 0) {
				System.out.println("No products bought.");
				mainMenu();
			} else {
				System.out.println("Confirm Buying? (y/ others)");
				if (d.next().charAt(0) != 'y') {
					System.out.println("Nothing changed...");
					mainMenu();
					return;
				}

				// Address
				int addressId;
				System.out.println("=== Address ===");
				System.out.println("1. Use permanent address");
				System.out.println("2. Use other addresses");
				System.out.println("3. Use new Address");
				switch (d.nextInt()) {
				case 1:
					addressId = assignPermanentAddress();
					break;
				case 2:
					addressId = chooseAddress();
					break;
				case 3:
					addressId = Auth.updateAddress(username);
					break;
				default:
					addressId = assignPermanentAddress();
				}

				// printAddress();

				// INSERTION in bill table
				Sql.insertions("bill", "(userId, timestamp, total, addressId)",
						String.format("%d, CURRENT_TIMESTAMP, %d, %d", id, Sql.getTotal(id), addressId));

				// getBillId
				int billId = Sql.getCurrentBillId();

				// INSERTION in history table
				ArrayList<Cart> carts = Sql.getCartProducts(id);

				for (int i = 0; i < carts.size(); i++) {

					// Statement smt1 = Sql.con.createStatement();
					Sql.insertions("history", "(billId, pId, quantity)", String.format("%d, %d, %d", billId,
							carts.get(i).getProductId(), carts.get(i).getQuantity()));

					Sql.updations("products",
							String.format("quantity = quantity - %d, saledCount = saledCount + %d",
									carts.get(i).getQuantity(), carts.get(i).getQuantity()),
							"id = " + carts.get(i).getProductId());

				}

				// DELETE all (From particular user) From Cart table
				Sql.deletions("cart", "userId = " + id);

				// Update details in Products (QUANTITY)
				System.out.println("Buyed successfully");
				System.out.println("Thanks for Coming... <3");
				viewBill(billId);
			}
		} catch (SQLException er) {
			System.out.println("Exception at BUY");
			System.out.println(er);
		}
	}

	private int assignPermanentAddress() throws SQLException {
		int addressId = Sql.assignPermanentAddress(id);
		if (addressId != -1) {
			return addressId;
		} else {
			System.out.println("No permanent address found.");
			return chooseAddress();
		}
	}

	private int chooseAddress() throws SQLException {
		System.out.println("--------- Address lists ------------");
		printAddress();
		System.out.println("Enter addressId");
		int addressId = d.nextInt();
		
		int address = Sql.getAddressId(addressId, id);
		
		if (address != -1) {
			return Sql.rs.getInt(1);
		} else {
			System.out.println("Wrong addressId chosen. Choose again");
			return chooseAddress();
		}
	}

	private void printAddress() throws SQLException {
		System.out.println("======== Addresses ========");
		
		System.out.println("* Indicates your permanent address");
		System.out.printf("%-10s %-20s %-20s %-20s %-20s %-20s %-20s %-20s\n", "addressId", "number", "street",
				"landmark", "city", "pincode", "state", "country");
		
		ArrayList<Address> addresses = Sql.getUserAddresses(id);
		
		for(int i = 0; i < addresses.size(); i++) {
			if (addresses.get(i).getPermanent() == 1)
				System.out.print("*");
			else
				System.out.println(" ");
			System.out.printf("%-10d %-20s %-20s %-20s %-20s %-20s %-20s %-20s\n", 
					addresses.get(i).getAddressId(),
					addresses.get(i).getNumber(),
					addresses.get(i).getStreet(),
					addresses.get(i).getLandmark(),
					addresses.get(i).getCity(),
					addresses.get(i).getPincode(),
					addresses.get(i).getState(),
					addresses.get(i).getCountry()
			);
		}

	}

	protected void viewBill(int billId) throws SQLException {
		try {

			System.out.println("******** Bill **********");
			ArrayList<ProductVendor> productVendors = Sql.getProducts(billId);

			System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s \n", "No", "Name", "Company", "quantity",
					"Unit price", "Total Price");
			for(int i = 0; i < productVendors.size(); i++) {
				System.out.printf("%-20d %-20s %-20s %-20s %-20s %-20s \n", i + 1, 
						productVendors.get(i).products.getName(),
						productVendors.get(i).vendor.getCompany(),
						productVendors.get(i).history.getQuantity(),
						productVendors.get(i).products.getPrice(),
						productVendors.get(i).total);
			}
			
			if (Sql.rs.next())
				System.out.println("================== Total = " + Sql.getBillTotal(billId) + " ======================");

		} catch (Exception er) {
			System.out.println(er);
		}
	}

	private void saveCart() throws SQLException {
		System.out.println("Saved successfully");
		System.out.println("Do you want to log out? (y/others)");
		if (d.next().charAt(0) == 'y') {
			Auth.mainMenu();
		} else {
			mainMenu();
		}
	}

	private void viewCart() throws SQLException {
		System.out.println("--- VIEW CART ---");


		System.out.println(String.format("%-20s %-20s %-20s %-20s %-20s %-20s %-20s\n", "id", "name", "category",
				"company", "unit price", "quantity", "total"));

		
		try {
			ResultSet resultSet = Sql.viewCart(id);
			while (resultSet.next()) {
				System.out.printf(String.format("%-20d %-20s %-20s %-20s %-20s %-20s %-20s\n", resultSet.getInt("id"),
						resultSet.getString("name"), resultSet.getString("category"), resultSet.getString("company"),
						resultSet.getString("unit price"), resultSet.getString("quantity"), resultSet.getString("total")));
			}
		} catch (SQLException e) {

		}

		int total = Sql.getTotal(id);

		System.out.println("====================== Total = " + total + " ===========================\n");

	}



	private void clearCart() throws SQLException {
		System.out.println("--- Clear Cart ---");
		System.out.println("Are you sure wish to clear the cart? (y/others) ");
		if (d.next().charAt(0) == 'y') {
			Sql.deletions("cart", "userId = " + id);
			System.out.println("Deleted your cart Successfully");
			mainMenu();
		} else {
			mainMenu();
		}
	}

	private void billList() throws SQLException {
		int i = 1;
		System.out.println("--- Bill History ---");
		
		System.out.printf("%-20s %-20s %-30s %-20s\n", "No", "Bill Id", "Timestamp", "Address");
		try {
			ResultSet resultSet = Sql.allUserBills(id);
			while (resultSet.next()) {
				System.out.printf("%-20d %-20d %-30s %-20s\n", i++, resultSet.getInt("billId"), resultSet.getString("timestamp"),
						resultSet.getString("number") + " " + resultSet.getString("street") + " " + resultSet.getString("landmark"));
			}
		} catch(SQLException er) {
			System.err.println(er);
		}
		
		

		if (i == 1) {
			System.out.println("XXXXXXXXXXXXXXXX No bill present. XXXXXXXXXXXXXXXXXXXX");
			mainMenu();
			return;
		}

		System.out.println("Enter Bill ID");
		int bill = d.nextInt();

		if (!Sql.isBillIdExist(id, bill)) {
			System.out.println("Wrong bill ID used. Enter a valid one");
			billList();
			return;
		}

		viewBill(bill);

	}

	private void wishList() throws SQLException {
		System.out.println("--- Wish List Menu ---");
		System.out.println("1. View wish List");
		System.out.println("2. Add one to wish list");
		System.out.println("3. Clear wish list");
		System.out.println("4. Remove one from wish list");
		System.out.println("5. Main menu");
		switch (d.nextInt()) {
		case 1:
			viewWishList();
			wishList();
			break;
		case 2:
			addToWishList();
			break;
		case 3:
			clearWishList();
			break;
		case 4:
			removeFromWishList();
			break;
		default:
			mainMenu();
		}
	}

	private void viewWishList() throws SQLException {

		System.out.println("--- Wish List <3 ---");

		ArrayList<Products> products = Sql.viewWishList(id);

		System.out.printf("%-20s %-20s %-20s \n", "id", "name", "price");

		for(int i = 0; i < products.size(); i++) {
			System.out.print(String.format("%-20d %-20s %-20f \n", 
					products.get(i).getId(),
					products.get(i).getName(),
					products.get(i).getPrice()		
			));
		}
	}

	private void addToWishList() throws SQLException {
		productPreview();
		System.out.println("Enter id to be wishlisted");
		int pId = d.nextInt();
		
		if(Sql.insertions("wishlist", "(uid, pid)", String.format("%d, %d", id, pId))) {
			System.out.println("Added wish List successfully");
			viewWishList();
			wishList();
		} else {
			System.out.println("Already this product is present.");
			addToWishList();
		}
	}

	private void clearWishList() {
		Sql.deletions("wishlist", "uid = " + id);
		System.out.println("Cleared wish List");
	}

	private void removeFromWishList() throws SQLException {
		viewWishList();
		System.out.println("Enter a product to remove from wishlist");
		int choice = d.nextInt();
		
		if(Sql.removeFromWishList(id, choice)) {
			System.out.println("Removed successfully");			
		} else {
			System.out.println("Choose product correctly`");
			wishList();
		}

	}

	private void logout() throws SQLException {
		System.out.println("Logged out " + username + " successfully.");
		Auth.mainMenu();
	}
}