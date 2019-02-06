package Pojo;
//$Id$

// POJO
public class Address {
	private int id;
	private String number;
	private String street;
	private String landmark;
	private String city;
	private int pincode;
	private String state;
	private String country;
	private byte permanent;
	private int addressId;

	private Address(int id, String number, String street, String landmark, String city, int pincode, String state,
			String country, byte permanent, int addressId) {
		this.id = id;
		this.number = number;
		this.street = street;
		this.landmark = landmark;
		this.city = city;
		this.pincode = pincode;
		this.state = state;
		this.country = country;
		this.permanent = permanent;
		this.addressId = addressId;

	}

	// Getters
	public int getId() {
		return this.id;
	}

	public String getNumber() {
		return this.number;
	}

	public String getStreet() {
		return this.street;
	}

	public String getLandmark() {
		return this.landmark;
	}

	public String getCity() {
		return this.city;
	}

	public int getPincode() {
		return this.pincode;
	}

	public String getState() {
		return this.state;
	}

	public String getCountry() {
		return this.country;
	}

	public byte getPermanent() {
		return this.permanent;
	}

	public int getAddressId() {
		return this.addressId;
	}

	// ToString

	public void setId(int id) {
		this.id = id;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setPincode(int pincode) {
		this.pincode = pincode;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setPermanent(byte permanent) {
		this.permanent = permanent;
	}

	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}

	@Override
	public String toString() {
		return "Address = [ " + ",id = " + id + ",number = " + number + ",street = " + street + ",landmark = "
				+ landmark + ",city = " + city + ",pincode = " + pincode + ",state = " + state + ",country = " + country
				+ ",permanent = " + permanent + ",addressId = " + addressId;
	}

	// Since using it in different package just made it public
	public static class AddressBuilder {
		private int id;
		private String number;
		private String street;
		private String landmark;
		private String city;
		private int pincode;
		private String state;
		private String country;
		private byte permanent;
		private int addressId;

		public AddressBuilder() {
		}

		// Setters
		public AddressBuilder setId(int id) {
			this.id = id;
			return this;
		}

		public AddressBuilder setNumber(String number) {
			this.number = number;
			return this;
		}

		public AddressBuilder setStreet(String street) {
			this.street = street;
			return this;
		}

		public AddressBuilder setLandmark(String landmark) {
			this.landmark = landmark;
			return this;
		}

		public AddressBuilder setCity(String city) {
			this.city = city;
			return this;
		}

		public AddressBuilder setPincode(int pincode) {
			this.pincode = pincode;
			return this;
		}

		public AddressBuilder setState(String state) {
			this.state = state;
			return this;
		}

		public AddressBuilder setCountry(String country) {
			this.country = country;
			return this;
		}

		public AddressBuilder setPermanent(byte permanent) {
			this.permanent = permanent;
			return this;
		}

		public AddressBuilder setAddressId(int addressId) {
			this.addressId = addressId;
			return this;
		}

		// builder
		public Address build() {
			return new Address(this.id, this.number, this.street, this.landmark, this.city, this.pincode, this.state,
					this.country, this.permanent, this.addressId);
		}

	}

}
