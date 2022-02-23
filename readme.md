# Java Design Patterns

## Basics

### Pattern

A design pattern helps us identify recurring problems and provides ready to use solutions. Design patterns help us capture design experience for a problem. It promotes reusing a solution without having to reinvent the wheel. It also helps define our application structure. Finally, it provides a common design language among developers.

### Pattern Catalogs

#### GOF Pattern Catalog

It is subdivided into Creational Patterns, Structural Patterns, Behavioral Patterns. **Creational Patterns** provides guidelines to instantiate an object. **Structural Patterns** provide a manner to define relationships between classes. **Behavioral Patterns** define how communication should happen between classes and objects.

#### JEE Pattern Catalog

Every java application is organized into multiple logical layers. The **Data Access layer** is for performing database operations. **Services layer** is where the business logic goes. **Presentation layer** presents the application to the client. **Integration layer** is responsible for communication with other applications. Each layer uses services provided by the other layers.

## Singleton

Object creation pattern that allows our application to create **ONE AND ONLY ONE** instance of a class. Example of a singleton object is a logger and the data source class. To implement the Singleton pattern, we follow the following steps:

1. declare the constructor of the class as private
2. declare a static method which all other classes can use to create an object of the singleton class
3. declare a static member of the same class type in the class

```java
public class DateUtil {
	//step 3
	private static DateUtil instance;

	// step 1
	private DateUtil() {

	}

	// step 2
	public static DateUtil getInstance() {
		if (instance == null) {
			instance = new DateUtil();
		}
		return instance;
	}
}
```

When we declared the constructor as private, no other class will be able to instantiate that class. When we are instantiating the static member, we only instantiate if it is null initially. Running the following test will show that the two objects point to the same reference.

```java
public class Test {
	public static void main(String[] args) {
		DateUtil dateUtil1 = DateUtil.getInstance();
		DateUtil dateUtil2 = DateUtil.getInstance();
		System.out.println(dateUtil1==dateUtil2);
	}
}
```

#### Eager Initialization

In the prior example, we are only creating the instance when the getInstance() is invoked. To achieve eager initialization, we can create an instance at the static variable declaration. Another way is by using static blocks. Static blocks are executed when the class is loaded into memory.

```java
public class DateUtil {
	private static DateUtil instance = new DateUtil();

	private DateUtil() {

	}

	public static DateUtil getInstance() {
		return instance;
	}
}

public class DateUtil {
	private static DateUtil instance;

	static {
		instance = new DateUtil();
	}

	private DateUtil() {

	}

	public static DateUtil getInstance() {
		return instance;
	}
}
```

#### Multithreading

To make our singleton thread-safe, we can add a synchronized block with a lock on the DateUtil class. However, this is expensive on resources so we add another if block to check if instance is null.

```java
	public static DateUtil getInstance() {
		if (instance == null) {
			synchronized (DateUtil.class) {
				if (instance == null) {
					instance = new DateUtil();
				}
			}
		}
		return instance;
	}
```

#### Serialization and Deserialization

Once we serialize the object and deserialize it again, we will be getting different objects. We can handle this problem by implementing the readResolve() method in our singleton class, which ObjectInputStream will invoke internally once it finishes reading the object from a file.

```java
public class DateUtil implements Serializable {
  ...
	protected Object readResolve() {
		return instance;
	}
}

public class Test {
	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
		DateUtil dateUtil1 = DateUtil.getInstance();
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(
				"C:\\Users\\ChristianCruz\\Documents\\Christian\\projects\\Java-Design-Patterns\\singleton\\dateUtil.ser")));
		oos.writeObject(dateUtil1);

		DateUtil dateUtil2 = null;
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(
				"C:\\Users\\ChristianCruz\\Documents\\Christian\\projects\\Java-Design-Patterns\\singleton\\dateUtil.ser")));
		dateUtil2 = (DateUtil) ois.readObject();

		oos.close();
		ois.close();

		System.out.println(dateUtil1 == dateUtil2);
	}
}
```

We also need to ensure that our singleton class is not cloneable. We do this by implementing the **Cloneable** interface and overriding the clone() method and throwing CloneNotSupportedException().

```java
	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
```

## Factory

The Factory pattern is a creational pattern that abstracts the object creation process. An example is a jdbc driver. To use the factory pattern, we follow the following steps:

1. Determine the types of classes that the factory can manufacture
2. Come up with a base interface that will be implemented by the classes
3. Create the factory class with a static method for creating objects

```java
public interface Pizza {
	void prepare();
	void bake();
	void cut();
}

public class CheesePizza implements Pizza {
	...
}


public class PizzaFactory {
	public static Pizza createPizza(String type) {
		Pizza p = null;
		if (type.equals("cheese")) {
			p = new CheesePizza();
		} else if (type.equals("chicken")) {
			p = new ChickenPizza();
		} else if (type.equals("veggie")) {
			p = new VeggiePizza();
		}

		return p;
	}
}

public class PizzaStore {
	public Pizza orderPizza(String type) {
		Pizza p = PizzaFactory.createPizza(type);
		p.prepare();
		p.bake();
		p.cut();
		return p;
	}
}
```

To test:

```java
	public static void main(String[] args) {
		PizzaStore ps = new PizzaStore();
		ps.orderPizza("veggie");
	}
```

## Abstract Factory

An abstract factory is a factory of factories. It hides the creation of the factory itself. Example of this is the JAXP api. First we need to identify the different classes in our application and create an abstract factory plus classes that will extend this abstract factory class.

```java
public class DBDeptDao implements Dao {

}

public class DBEmpDao implements Dao {

}

public class XMLEmpDao implements Dao {

}

public class XMLDeptDao implements Dao {

}

public abstract class DaoAbstractFactory {
	public abstract Dao createDao(String type);
}

public class XMLDaoFactory extends DaoAbstractFactory {
	@Override
	public Dao createDao(String type) {
		Dao dao = null;
		if (type.equals("emp")) {
			dao = new XMLEmpDao();
		} else if (type.equals("dept")) {
			dao = new XMLDeptDao();
		}
		return dao;
	}
}

public class DBDaoFactory extends DaoAbstractFactory {
	@Override
	public Dao createDao(String type) {
		Dao dao = null;
		if (type.equals("emp")) {
			dao = new DBEmpDao();
		} else if (type.equals("dept")) {
			dao = new DBDeptDao();
		}
		return dao;
	}
}
```

We finally write out the factory of factories and our test class

```java
public class DaoFactoryProducer {
	public static DaoAbstractFactory produce(String factoryType) {
		DaoAbstractFactory daf = null;

		if (factoryType.equals("xml")) {
			daf = new XMLDaoFactory();
		} else if (factoryType.equals("db")) {
			daf = new DBDaoFactory();
		}
		return daf;
	}
}

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
```
