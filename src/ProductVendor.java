import Pojo.History;
import Pojo.Products;
import Pojo.Vendor;

//$Id$

public class ProductVendor {
	Products products;
	Vendor vendor;
	History history;
	int total;
	
	public ProductVendor(Products products, Vendor vendor) {
		super();
		this.products = products;
		this.vendor = vendor;
	}

	public ProductVendor(Products products, Vendor vendor, History history, int total) {
		super();
		this.products = products;
		this.vendor = vendor;
		this.history = history;
		this.total = total;
	}
}
