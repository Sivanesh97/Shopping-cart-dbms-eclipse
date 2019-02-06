package Pojo;
//$Id$

// POJO
public class Wishlist {
	private int uId;
	private int pId;

	private Wishlist(int uId, int pId) {
		this.uId = uId;
		this.pId = pId;

	}

	public void setuId(int uId) {
		this.uId = uId;
	}

	public void setpId(int pId) {
		this.pId = pId;
	}

	// Getters
	public int getUId() {
		return this.uId;
	}

	public int getPId() {
		return this.pId;
	}

	// ToString

	@Override
	public String toString() {
		return "Wishlist = [ " + ",uId = " + uId + ",pId = " + pId;
	}

	// Since using it in different package just made it public
	public static class WishlistBuilder {
		private int uId;
		private int pId;

		public WishlistBuilder() {
		}

		// Setters
		public WishlistBuilder setUId(int uId) {
			this.uId = uId;
			return this;
		}

		public WishlistBuilder setPId(int pId) {
			this.pId = pId;
			return this;
		}

		// builder
		public Wishlist build() {
			return new Wishlist(this.uId, this.pId);
		}

	}

}
