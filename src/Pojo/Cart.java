package Pojo;
//$Id$

// POJO
public class Cart {
	private int userId;
	private int productId;
	private int quantity;

	private Cart(int userId, int productId, int quantity) {
		this.userId = userId;
		this.productId = productId;
		this.quantity = quantity;

	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	// Getters
	public int getUserId() {
		return this.userId;
	}

	public int getProductId() {
		return this.productId;
	}

	public int getQuantity() {
		return this.quantity;
	}

	// ToString

	@Override
	public String toString() {
		return "Cart = [ " + ",userId = " + userId + ",productId = " + productId + ",quantity = " + quantity;
	}

	// Since using it in different package just made it public
	public static class CartBuilder {
		private int userId;
		private int productId;
		private int quantity;

		public CartBuilder() {
		}

		// Setters
		public CartBuilder setUserId(int userId) {
			this.userId = userId;
			return this;
		}

		public CartBuilder setProductId(int productId) {
			this.productId = productId;
			return this;
		}

		public CartBuilder setQuantity(int quantity) {
			this.quantity = quantity;
			return this;
		}

		// builder
		public Cart build() {
			return new Cart(this.userId, this.productId, this.quantity);
		}

	}

}
