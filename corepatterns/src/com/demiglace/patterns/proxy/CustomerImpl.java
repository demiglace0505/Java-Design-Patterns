package com.demiglace.patterns.proxy;

import java.util.List;

public class CustomerImpl implements Customer {
	private int id;
	private List<Order> orders;
	
	CustomerImpl() {
		this.id = 123;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}
}
