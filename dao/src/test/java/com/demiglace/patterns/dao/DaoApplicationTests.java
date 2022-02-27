package com.demiglace.patterns.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DaoApplicationTests {
	@Autowired
	EmployeeDAO dao;
	
	@Test
	void testCreate() {
		Employee employee = new Employee();
		employee.setId(123);
		employee.setName("Doge");
		dao.create(employee);
	}
}
