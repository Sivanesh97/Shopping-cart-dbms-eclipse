package Pojo;
//$Id$

// POJO
public class History {
	private int billId;
	private int pId;
	private int quantity;

	private History(int billId, int pId, int quantity) {
		this.billId = billId;
		this.pId = pId;
		this.quantity = quantity;

	}

	public void setBillId(int billId) {
		this.billId = billId;
	}

	public void setpId(int pId) {
		this.pId = pId;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	// Getters
	public int getBillId() {
		return this.billId;
	}

	public int getPId() {
		return this.pId;
	}

	public int getQuantity() {
		return this.quantity;
	}

	// ToString

	@Override
	public String toString() {
		return "History = [ " + ",billId = " + billId + ",pId = " + pId + ",quantity = " + quantity;
	}

	// Since using it in different package just made it public
	public static class HistoryBuilder {
		private int billId;
		private int pId;
		private int quantity;

		public HistoryBuilder() {
		}

		// Setters
		public HistoryBuilder setBillId(int billId) {
			this.billId = billId;
			return this;
		}

		public HistoryBuilder setPId(int pId) {
			this.pId = pId;
			return this;
		}

		public HistoryBuilder setQuantity(int quantity) {
			this.quantity = quantity;
			return this;
		}

		// builder
		public History build() {
			return new History(this.billId, this.pId, this.quantity);
		}

	}

}
