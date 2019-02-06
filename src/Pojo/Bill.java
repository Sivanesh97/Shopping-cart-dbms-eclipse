package Pojo;
//$Id$

// POJO
public class Bill {
	private int billId;
	private int userId;
	private String timestamp;
	private int total;
	private int addressId;

	private Bill(int billId, int userId, String timestamp, int total, int addressId) {
		this.billId = billId;
		this.userId = userId;
		this.timestamp = timestamp;
		this.total = total;
		this.addressId = addressId;

	}

	// Getters
	public int getBillId() {
		return this.billId;
	}

	public int getUserId() {
		return this.userId;
	}

	public String getTimestamp() {
		return this.timestamp;
	}

	public int getTotal() {
		return this.total;
	}

	public int getAddressId() {
		return this.addressId;
	}

	// ToString

	public void setBillId(int billId) {
		this.billId = billId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}

	@Override
	public String toString() {
		return "Bill = [ " + ",billId = " + billId + ",userId = " + userId + ",timestamp = " + timestamp + ",total = "
				+ total + ",addressId = " + addressId;
	}

	// Since using it in different package just made it public
	public static class BillBuilder {
		private int billId;
		private int userId;
		private String timestamp;
		private int total;
		private int addressId;

		public BillBuilder() {
		}

		// Setters
		public BillBuilder setBillId(int billId) {
			this.billId = billId;
			return this;
		}

		public BillBuilder setUserId(int userId) {
			this.userId = userId;
			return this;
		}

		public BillBuilder setTimestamp(String timestamp) {
			this.timestamp = timestamp;
			return this;
		}

		public BillBuilder setTotal(int total) {
			this.total = total;
			return this;
		}

		public BillBuilder setAddressId(int addressId) {
			this.addressId = addressId;
			return this;
		}

		// builder
		public Bill build() {
			return new Bill(this.billId, this.userId, this.timestamp, this.total, this.addressId);
		}

	}

}
