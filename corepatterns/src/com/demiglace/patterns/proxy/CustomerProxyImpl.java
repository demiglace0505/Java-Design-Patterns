package com.demiglace.patterns.proxy;

import java.util.ArrayList;
import java.util.List;

public class CustomerProxyImpl implements Customer {
	CustomerImpl customer = new CustomerImpl();
	
	@Override
	public int getId() {
		return customer.getId();
	}

	@Override
	public List<Order> getOrders() {
		ArrayList<Order> orders = new ArrayList<Order>();
		Order order1 = new Order();
		order1.setId(1);
		order1.setProductName("Welkin moon");
		order1.setQuantity(100);
		orders.add(order1);
		
		Order order2 = new Order();
		order2.setId(2);
		order2.setProductName("Primogems");
		order2.setQuantity(160);
		orders.add(order2);
		return orders;
	}
}
