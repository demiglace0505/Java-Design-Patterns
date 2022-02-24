package com.demiglace.patterns.flyweight;

public class Rectangle extends Shape {
	private String label;

	public Rectangle() {
		label = "rectangle";
	}
	
	@Override
	public void draw(int length, int breadth, String fillStyle) {
		System.out.println("Drawing: " + label + length + breadth + fillStyle);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
