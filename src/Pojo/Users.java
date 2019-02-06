package Pojo;

//$Id$

// POJO
public class Users {
	private int id;
	private String username;
	private String password;
	private String phone;

	private Users(int id, String username, String password, String phone) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.phone = phone;

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

	public void setPhone(String phone) {
		this.phone = phone;
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

	public String getPhone() {
		return this.phone;
	}

	// ToString

	@Override
	public String toString() {
		return "Users = [ " + ",id = " + id + ",username = " + username + ",password = " + password + ",phone = "
				+ phone;
	}

	public static class UsersBuilder {
		private int id;
		private String username;
		private String password;
		private String phone;

		public UsersBuilder() {
		}

		// Setters
		public UsersBuilder setId(int id) {
			this.id = id;
			return this;
		}

		public UsersBuilder setUsername(String username) {
			this.username = username;
			return this;
		}

		public UsersBuilder setPassword(String password) {
			this.password = password;
			return this;
		}

		public UsersBuilder setPhone(String phone) {
			this.phone = phone;
			return this;
		}

		// builder
		public Users build() {
			return new Users(this.id, this.username, this.password, this.phone);
		}

	}

}
