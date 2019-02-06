
//$Id$
import java.sql.*;
import java.util.ArrayList;

import Pojo.Address;
import Pojo.Cart;
import Pojo.History;
import Pojo.Products;
import Pojo.Vendor;

/*
 * INSERTIONS, UPDATIONS, DELETIONS alone shares a common method.
 * SELECTIONS alone requires different methods to call for, for Different POJOS
 */

// Singleton Class - It reduces many object creations.
public class Sql {
	static Connection con;
	static Statement smt;
	static ResultSet rs;
	static Sql sql = null;

	static {
		try {
			getInstance();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("Error occured in ");
			;
		}
	}
	
	// SQL Utils
	protected static Sql getInstance() throws SQLException {
		if (sql == null) {
			Sql sql = new Sql();
			sql.dbConnect();
		}
		return sql;
	}

	protected void dbConnect() throws SQLException {
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/shopping?useSSL=false", "root", "");
		smt = con.createStatement();
	}

	// Common
	protected static boolean insertions(String table, String columns, String data) {
		String query = "INSERT INTO " + table + " " + columns + " VALUES (" + data + ")";
		System.out.println(query);
		try {
			smt.execute(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("Error occured in ");
			;
			return false;
		}
		return true;
	}

	protected static boolean updations(String table, String datas, String conditions) {
		String query = "UPDATE " + table + " SET " + datas + " WHERE " + conditions;
		System.out.println(query);
		try {
			smt.execute(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("Error Occured in Updations");
			return false;
		}
		return true;
	}

	protected static boolean deletions(String table, String conditions) {

		String checker = "DELETE FROM " + table + " WHERE " + conditions;
		try {
			smt.execute(checker);
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	protected static void beginTransaction() {
		try {
			smt.execute("BEGIN");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("Error occured in ");
			;
		}
	}

	protected static void commitTransaction() {
		try {
			smt.execute("COMMIT");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("Error occured in ");
			;
		}
	}

	protected static void rollbackTransaction() {
		try {
			smt.execute("ROLLBACK");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("Error occured in ");
			;
		}
	}

	// Users
	protected static String getPassword(String username, String table) {
		try {
			rs = smt.executeQuery("SELECT username, password FROM " + table + " WHERE username = '" + username + "'");
			if (rs.next())
				return rs.getString("password");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("Error occured in ");
			;
		}
		return null;
	}

	protected static boolean isUserAlreadyPresent(String username) {
		try {
			rs = smt.executeQuery("SELECT username FROM users WHERE username = '" + username + "'");
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("Error occured in ");
			;
		}
		return false;
	}

	protected static int getId(String table, String username) {
		try {
			rs = smt.executeQuery("SELECT id FROM " + table + " WHERE username = '" + username + "'");
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("Error occured in ");
			;
		}
		return -1;
	}

	protected static int getAddress() {
		try {
			rs = smt.executeQuery("SELECT MAX(addressId) FROM address");
			if (rs.next()) {
				return rs.getInt(1);
			} else {
				return -1;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("Error occured in ");
			;
			return -1;
		}
	}

	// Customers
	protected static boolean isProductPresent(String table, int id) {
		try {
			rs = smt.executeQuery("SELECT id FROM " + table + " WHERE id = " + id);
			if (rs.next())
				return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("Error occured in ");
			;
		}
		return false;
	}

	protected static boolean isAvailable(int productId, int quantity) throws SQLException {
		rs = smt.executeQuery(String.format("SELECT quantity FROM products WHERE id = %d", productId));
		if (rs.next()) {
			if (rs.getInt(1) >= quantity) {
				return true;
			}
		}
		return false;
	}

	protected static boolean isHisProduct(int productId, int id) throws SQLException {
		rs = smt.executeQuery(
				String.format("SELECT productId FROM cart WHERE userId = %d AND productId = %d", id, productId));
		if (rs.next())
			return true;
		return false;
	}

	protected static ArrayList<ProductVendor> productPreview() {
		ArrayList<ProductVendor> productVendors = new ArrayList<>();

		try {
			rs = smt.executeQuery("SELECT products.id," + "products.name," + "products.category,"
					+ "products.price," + "products.quantity," + "vendor.username," + "vendor.company,"
					+ "vendor.rating " + "FROM vendor " + "INNER JOIN " + "products ON products.vid = vendor.Id");

			while (rs.next()) {
				Products.ProductsBuilder products = new Products.ProductsBuilder();
				Vendor.VendorBuilder vendor = new Vendor.VendorBuilder();
				products.setId(rs.getInt("id"));
				products.setName(rs.getString("name"));
				products.setCategory(rs.getString("category"));
				products.setPrice(rs.getInt("price"));
				products.setQuantity(rs.getInt("quantity"));
				vendor.setUsername(rs.getString("username"));
				vendor.setCompany(rs.getString("company"));
				products.setRating(rs.getInt("rating"));

				ProductVendor productVendor = new ProductVendor(products.build(), vendor.build());
				productVendors.add(productVendor);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return productVendors;

	}

	protected static int getCurrentBillId() {
		try {
			rs = smt.executeQuery("SELECT MAX(billId) FROM bill");
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	protected static ArrayList<Cart> getCartProducts(int id) {
		ArrayList<Cart> carts = new ArrayList<>();
		try {
			rs = smt.executeQuery("SELECT productId, quantity FROM cart WHERE userId = " + id);
			while (rs.next()) {
				carts.add(new Cart.CartBuilder().setProductId(rs.getInt("productId")).setQuantity(rs.getInt("quantity"))
						.build());
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return carts;

	}

	protected static int assignPermanentAddress(int id) {
		try {
			rs = smt.executeQuery("SELECT addressId FROM address WHERE id = " + id + " AND permanent = 1");
			if (rs.next())
				return rs.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	protected static int getAddressId(int addressId, int userId) {
		try {
			rs = smt.executeQuery(String
					.format("SELECT addressId, id FROM address WHERE addressId = %d AND id = %d", addressId, userId));
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	protected static ArrayList<Address> getUserAddresses(int userId) {
		ArrayList<Address> addresses = new ArrayList<>();
		try {
			rs = smt.executeQuery("SELECT * FROM address WHERE id = " + userId);
			while (rs.next()) {
				Address address = new Address.AddressBuilder().setAddressId(rs.getInt("addressId"))
						.setNumber(rs.getString("number")).setStreet(rs.getString("street"))
						.setLandmark(rs.getString("landmark")).setCity(rs.getString("city"))
						.setPincode(rs.getInt("pincode")).setState(rs.getString("state"))
						.setCountry(rs.getString("country")).setPermanent(rs.getByte("permanent")).build();
				addresses.add(address);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return addresses;
	}

	protected static ArrayList<ProductVendor> getProducts(int billId) {

		ArrayList<ProductVendor> productVendors = new ArrayList<>();

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("SELECT ");
		stringBuilder.append("products.name, ");
		stringBuilder.append("vendor.company, ");
		stringBuilder.append("history.quantity, ");
		stringBuilder.append("products.price AS `Unit price`, ");
		stringBuilder.append("history.quantity * products.price AS `Total Price` ");
		stringBuilder.append("FROM history ");
		stringBuilder.append("INNER JOIN products ON products.id = history.pId ");
		stringBuilder.append("INNER JOIN vendor ON vendor.id = products.vId ");
		stringBuilder.append("WHERE history.billId = ");
		stringBuilder.append(billId);

		try {
			rs = smt.executeQuery(String.format(stringBuilder.toString()));
			System.err.println(true);
			while (rs.next()) {
				Products products = new Products.ProductsBuilder().setName(rs.getString("name"))
						.setPrice(rs.getInt("unit price")).build();

				History history = new History.HistoryBuilder().setQuantity(rs.getInt("quantity"))

						.build();

				Vendor vendor = new Vendor.VendorBuilder().setCompany(rs.getString("company")).build();

				ProductVendor productVendor = new ProductVendor(products, vendor, history, rs.getInt("total price"));
				productVendors.add(productVendor);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return productVendors;

	}
	
	protected static int getBillTotal(int billId) {
		try {
			rs = smt.executeQuery("SELECT total FROM bill WHERE billId = " + billId);
			if(rs.next()) 
				return rs.getInt(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	protected static ResultSet viewCart(int userId) {
		
//		ArrayList<ProductVendor> productVendors = new ArrayList<>();
		
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("SELECT ");
		stringBuilder.append("products.id,");
		stringBuilder.append("products.name,");
		stringBuilder.append("products.category,");
		stringBuilder.append("vendor.company,");
		stringBuilder.append("products.price AS `Unit Price`,");
		stringBuilder.append("cart.quantity,");
		stringBuilder.append("cart.quantity * products.price AS total ");
		stringBuilder.append("FROM cart ");
		stringBuilder.append("INNER JOIN products ON cart.productId = products.id ");
		stringBuilder.append("INNER JOIN vendor ON products.vId = vendor.id ");
		stringBuilder.append("WHERE userId = ");
		stringBuilder.append(userId);
		try {
			rs = smt.executeQuery(stringBuilder.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
	
	protected static int getTotal(int userId) {
		try {
			rs = smt.executeQuery("SELECT SUM(cart.quantity * products.price) FROM cart"
					+ " INNER JOIN products ON products.id = cart.productId " + "WHERE userId = " + userId);
			if (rs.next())
				return rs.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public static ResultSet allUserBills(int id) {
		try {
			rs = smt.executeQuery("SELECT billId, timestamp, address.street, address.number, address.landmark"
					+ " FROM bill INNER JOIN address ON address.addressId = bill.addressId WHERE userId = " + id);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}

	public static boolean isBillIdExist(int userId, int bill) {
		try {
			rs = smt.executeQuery(String.format("SELECT billId FROM bill WHERE userId = %d AND billId = %d", userId, bill));
			if(rs.next()) return true;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	protected static ArrayList<Products> viewWishList(int userId) {
		ArrayList<Products> productsArray = new ArrayList<>();
		try {
			rs = smt.executeQuery("SELECT products.id, products.name, products.price FROM products "
					+ "INNER JOIN wishlist ON wishlist.pid = products.id WHERE wishlist.uid = " + userId);
			while(rs.next()) {
				Products products = new Products.ProductsBuilder()
						.setName(rs.getString("name"))
						.setId(rs.getInt("id"))
						.setPrice(rs.getDouble("price"))
						.build();
				productsArray.add(products);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return productsArray;
	}

	public static boolean removeFromWishList(int id, int choice) {
		try {
			rs = smt.executeQuery("SELECT pid from wishList WHERE uid = " + id + " AND pid = " + choice);
			if (rs.next()) {
				smt.execute("DELETE FROM wishList WHERE uid = " + id + " AND pid = " + choice);
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
		
	}

	public static StringBuilder getCategories() {
		StringBuilder stringBuilder = new StringBuilder();
		
		try {
			rs = smt.executeQuery("SELECT DISTINCT category FROM products");
			while(rs.next()) {
				stringBuilder.append(rs.getString(1) + ", ");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stringBuilder;
	}
	
    protected static boolean isVendorProduct(int vId, int productId) {
        
        try {
			rs = smt.executeQuery(String.format("SELECT id FROM products WHERE vId = %d", vId));
			while (rs.next()) {
				if (rs.getInt(1) == productId) {
					return true;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return false;
    }

	
    protected static ArrayList<Products> getProductVendor(int id) {
		ArrayList<Products> productsArray = new ArrayList<>();
		
		 try {
			rs = smt.executeQuery(String.format("SELECT * FROM products WHERE vId = %s", id));
			
			while(rs.next()) {
				
				Products products = new Products.ProductsBuilder()
						.setId(rs.getInt("id"))
						.setName(rs.getString("name"))
						.setCategory(rs.getString("category"))
						.setPrice(rs.getDouble("price"))
						.setQuantity(rs.getInt("quantity"))
						.setSaledCount(rs.getInt("saledCount"))
						.build();
				
				productsArray.add(products);
			}
			
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			 System.err.println("Error occurs here");
			e.printStackTrace();
		}
		return productsArray;
	}

	// SELLER
	
	
	
	
	
	
	
	
	
	
	
}
