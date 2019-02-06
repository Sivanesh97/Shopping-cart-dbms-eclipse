package Pojo;
//$Id$

// POJO
public class Vendor {
	private int id;
	private String username;
	private String password;
	private String company;
	private byte rating;

	private Vendor(int id, String username, String password, String company, byte rating) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.company = company;
		this.rating = rating;

	}

	public void setId(int id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public void setRating(byte rating) {
		this.rating = rating;
	}

	// Getters
	public int getId() {
		return this.id;
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}

	public String getCompany() {
		return this.company;
	}

	public byte getRating() {
		return this.rating;
	}

	// ToString

	@Override
	public String toString() {
		return "Vendor = [ " + ",id = " + id + ",username = " + username + ",password = " + password + ",company = "
				+ company + ",rating = " + rating;
	}

	// Since using it in different package just made it public
	public static class VendorBuilder {
		private int id;
		private String username;
		private String password;
		private String company;
		private byte rating;

		public VendorBuilder() {
		}

		// Setters
		public VendorBuilder setId(int id) {
			this.id = id;
			return this;
		}

		public VendorBuilder setUsername(String username) {
			this.username = username;
			return this;
		}

		public VendorBuilder setPassword(String password) {
			this.password = password;
			return this;
		}

		public VendorBuilder setCompany(String company) {
			this.company = company;
			return this;
		}

		public VendorBuilder setRating(byte rating) {
			this.rating = rating;
			return this;
		}

		// builder
		public Vendor build() {
			return new Vendor(this.id, this.username, this.password, this.company, this.rating);
		}

	}

}
