package com.demiglace.patterns.abstractfactory;

public class Test {
	public static void main(String[] args) {
		DaoAbstractFactory daf1 = DaoFactoryProducer.produce("xml");
		Dao dao1 = daf1.createDao("emp");
		dao1.save(); // Saving Employee to XML
		
		DaoAbstractFactory daf2 = DaoFactoryProducer.produce("db");
		Dao dao2 = daf2.createDao("emp");
		dao2.save(); // Saving Employee to DB
	}
}
