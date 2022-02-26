package com.demiglace.patterns.decorator;

public class CheesePizzaDecorator extends PizzaDecorator {
	public CheesePizzaDecorator(Pizza pizza) {
		super(pizza);
	}

	public void bake() {
		super.bake();
		System.out.println("Adding Cheese Toppings");
	}
}
