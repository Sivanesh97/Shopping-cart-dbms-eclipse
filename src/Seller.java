import java.util.*;

import Pojo.Products;

public class Seller {
	Scanner d = new Scanner(System.in);
	Checker c = Checker.getInstance();
	String username;
	int id;

	protected void main(String username) {
		this.username = username;
		this.id = Sql.getId("vendor", username);
		mainMenu();
	}

	private void mainMenu() {
		System.out.println("--- Vendor Menu ---");
		System.out.println("1. Insert\n2. Update\n3. Delete\n4. Print Products\n5. LogOut");
		int choice = d.nextInt();
		d.nextLine();

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
			printProducts();
			break;
		case 5:
			logOut();
			break;
		default:
			System.out.println("Enter a valid one... :(");
			mainMenu();
		}
		mainMenu();
	}

	private void insert() {
		System.out.println();
		System.out.println("Add a Product");
		System.out.println("Enter Product name");
		String name = d.nextLine();
		if (c.isEmpty(name)) {
			insert();
			return;
		}

		System.out.println("-- Present existing categories --");
		StringBuilder categories = Sql.getCategories();

		System.out.print(categories);

		System.out.println();
		System.out.println("Enter category");
		String category = d.nextLine();
		System.out.println("Enter price");
		String price = d.nextLine();
		if (c.isEmpty(price)) {
			price = "0";
			System.out.println("So assigned to 0");
		}
		System.out.println("Enter quantity");
		String quantity = d.nextLine();
		if (quantity.equals("")) {
			quantity = "0";
			System.out.println("Quantity assigned to 0");
		}

		if (Sql.insertions("products", "(name, vId, category, price, quantity)",
				String.format("'%s', %d, '%s', '%s', '%s'", name, id, category, price, quantity))) {
			System.out.println("Inserted successfully");
		} else {
			System.err.println("There's Error in insertion");
		}

		System.out.println();
		mainMenu();

	}

	private void update() {
		printProducts();
		System.out.println(
				"Hint: If you want to update Product name or Category. Delete this product and create new Product. ;)");
		System.out.println("UPDATION");
		System.out.println("--------------------------------------");
		System.out.println("Enter id of the product (0 <- Back)");
		int productId = d.nextInt();
		if (productId == 0) {
			mainMenu();
			return;
		}

		if (!Sql.isVendorProduct(id, productId)) {
			System.out.println("Wrong id used.");
			update();
			return;
		}

		System.out.println("What to update?");
		System.out.println("1. Price");
		System.out.println("2. Quantity");
		System.out.println("3. Exit");
		int choice = d.nextInt();
		switch (choice) {
		case 1:
			updatePriceQuantity(productId, true);
			break;
		case 2:
			updatePriceQuantity(productId, false);
			break;
		case 3:
			mainMenu();
		default:
			System.out.println("Enter a valid one");
			update();
		}
		mainMenu();
	}

	private void updatePriceQuantity(int id, boolean isPrice) {
		System.out.println("Enter the " + (isPrice ? "Price" : "Quantity") + " to be updated");
		int price = d.nextInt();

		// Checking whether that Product belongs to Vendor

		if(Sql.updations("products", (isPrice ? "price" : "quantity") + " = " + price, "id = " + id)) {
			System.out.println("Updated successfully.");
			mainMenu();
		} else {
			System.out.println("There's some error in Updation. Update correctly or contact Admin.");
			updatePriceQuantity(id, isPrice);
		}
	}

	private void delete() {
		printProducts();
		System.out.println("DELETION");
		System.out.println("Enter a choice (0 <- Back)");
		int deleteId = d.nextInt();
		if (deleteId == 0) {
			mainMenu();
			return;
		}
		if (Sql.isVendorProduct(id, deleteId)) {
			Sql.deletions("products", "id = " + deleteId);

			System.out.println("Deletion made successfully");
			mainMenu();
		} else {
			System.out.println("Wrong Product id is given.");
			delete();
		}
	}

	private void printProducts()  {
		System.out.println();
		System.out.printf("Products of Vendor: %s %20s%d\n", username, "id = ", id);

		System.out.printf("%-10s %-20s %-20s %-20s %-20s %-20s %-20s\n", "id", "name", "category", "price", "quantity",
				"rating", "sales Count");

		ArrayList<Products> products = Sql.getProductVendor(id);

		for (int i = 0; i < 10; i++)
			System.out.print("-----------");
		System.out.println();

		for (int i = 0; i < products.size(); i++) {
			System.out.printf("%-10d %-20s %-20s %-20f %-20d %-20f %-20d\n", products.get(i).getId(),
					products.get(i).getName(), products.get(i).getCategory(), products.get(i).getPrice(),
					products.get(i).getQuantity(), products.get(i).getRating(), products.get(i).getSaledCount());
		}
		System.out.println();
	}

	private void logOut() {
		System.out.println("Logged out successfully " + username);
		Auth.mainMenu();
	}
}