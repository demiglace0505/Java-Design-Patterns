package com.demiglace.patterns.adapter;

public class WeatherAdapter {
	public int findTemperature(int zipcode) {
		String city = null;
		if (zipcode == 19406) {
			city = "King of Prussia";
		}
		
		WeatherFinder finder = new WeatherFinderImpl();
		int temperature = finder.find(city);
		return temperature;
	}
}
