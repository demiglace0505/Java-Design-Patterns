# Java Design Patterns

- [Java Design Patterns](#java-design-patterns)
  - [Basics](#basics)
    - [Pattern](#pattern)
    - [Pattern Catalogs](#pattern-catalogs)
      - [GOF Pattern Catalog](#gof-pattern-catalog)
      - [JEE Pattern Catalog](#jee-pattern-catalog)
  - [Singleton Pattern](#singleton-pattern)
      - [Eager Initialization](#eager-initialization)
      - [Multithreading](#multithreading)
      - [Serialization and Deserialization](#serialization-and-deserialization)
  - [Factory](#factory)
  - [Abstract Factory](#abstract-factory)
  - [Template Method](#template-method)
  - [Adapter Pattern](#adapter-pattern)
  - [Flyweight Pattern](#flyweight-pattern)
  - [Command Pattern](#command-pattern)
-

## Basics

### Pattern

A design pattern helps us identify recurring problems and provides ready to use solutions. Design patterns help us capture design experience for a problem. It promotes reusing a solution without having to reinvent the wheel. It also helps define our application structure. Finally, it provides a common design language among developers.

### Pattern Catalogs

#### GOF Pattern Catalog

It is subdivided into Creational Patterns, Structural Patterns, Behavioral Patterns. **Creational Patterns** provides guidelines to instantiate an object. **Structural Patterns** provide a manner to define relationships between classes. **Behavioral Patterns** define how communication should happen between classes and objects.

#### JEE Pattern Catalog

Every java application is organized into multiple logical layers. The **Data Access layer** is for performing database operations. **Services layer** is where the business logic goes. **Presentation layer** presents the application to the client. **Integration layer** is responsible for communication with other applications. Each layer uses services provided by the other layers.

## Singleton Pattern

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

## Template Method

The template method is a behavioral pattern that provides a base template method that should be used by child clients. The child classes can override certain methods, but they should use the template method. In the following example, we have the DataRenderer class that has the methods readData() processData() and render(). We want to render the data in the same way no matter which format the data comes in. The child classes will be able to override the readData() and processData() methods, but the render() method will be provided as the base template.

The readData() and processData() will be abstract methods that need to be overriden by the child classes. The render() method will know how to invoke the other methods. Afterwards, we create the child classes that provides the implementation for readData() and processData().

```java
public abstract class DataRenderer {
	public void render() {
		String data = readData();
		String processedData = processData(data);
		System.out.println(processedData);
	}

	public abstract String readData();

	public abstract String processData(String data);
}

public class XMLDataRenderer extends DataRenderer {
	@Override
	public String readData() {
		return "XML Data";
	}

	@Override
	public String processData(String data) {
		return "Processed Data " + data;
	}
}

public class CSVDataRenderer extends DataRenderer {
	@Override
	public String readData() {
		return "CSV Data";
	}

	@Override
	public String processData(String data) {
		return "Processed Data " + data;
	}
}
```

We can test the above using the following test class

```java
public class Test {
	public static void main(String[] args) {
		DataRenderer renderer = new XMLDataRenderer();
		renderer.render();

		DataRenderer renderer2 = new CSVDataRenderer();
		renderer2.render();
	}
}
```

```
Processed Data XML Data
Processed Data CSV Data
```

## Adapter Pattern

The adapter pattern is used when two objects are using each other. In the example below, we have the WeatherFinderImpl class which has the method find(city). The WeatherUI class on the other hand needs to invoke the WeatherFinder class, but it only has the zip code of the city. This is where WeatherAdapter comes into play with looking up the city according to the zip code, and then invoke the WeatherFinder class and take the results back to WeatherUI.

```java
public interface WeatherFinder {
	int find(String city);
}

public class WeatherFinderImpl implements WeatherFinder {
	@Override
	public int find(String city) {
		return 32;
	}
}

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

public class WeatherUI {
	public void showTemperature(int zipcode) {
		WeatherAdapter adapter = new WeatherAdapter();
		System.out.println(adapter.findTemperature(zipcode));
	}

	public static void main(String[] args) {
		WeatherUI ui = new WeatherUI();
		ui.showTemperature(19406);
	}
}
```

## Flyweight Pattern

The Flyweight pattern is a structural design pattern. Instead of creating a large number of similar objects, we can reuse some objects to save memory. In the example, we will be working on a paint app that allows users to draw different shapes. The Shape interface with a draw() method will be overriden by different classes. If we need to create circles and rectangles, we will need to set the radius, length etc. for each shape we want to create. Using the flyweight pattern, we can create a single Circle and Rectangle instead.

The PaintApp class contains a method that takes in how many number of shapes the user wants.

```java
public interface Shape {
	public void draw();
}

public class Circle implements Shape {
	private String label;
	private int radius;
	private String fillColor;
	private String lineColor;

	public Circle() {
		label ="Circle";
	}

	@Override
	public void draw() {
		System.out.println("Drawing: " + label + radius + fillColor + lineColor);
	}
}

public class Rectangle implements Shape {
	private String label;
	private int length;
	private int breadth;
	private String fillStyle;

	public Rectangle() {
		label = "rectangle";
	}

	@Override
	public void draw() {
		System.out.println("Drawing: " + label + length + breadth + fillStyle);
	}
}
```

The following PaintApp class does not implement flyweight pattern yet. Everytime we are creating a Circle or Rectangle, a new object is being instantiated. This consumes memory.

```java
public class PaintApp {
	public void render(int numberOfShapes) {
		Shape[] shapes = new Shape[numberOfShapes + 1];

		for (int i = 1; i <= numberOfShapes; i++) {
			if (i%2==0) {
				shapes[i] = new Circle();
				((Circle) shapes[i]).setRadius(i);
				((Circle) shapes[i]).setLineColor("red");
				((Circle) shapes[i]).setFillColor("white");
				shapes[i].draw();
			} else {
				shapes[i] = new Rectangle();
				((Rectangle) shapes[i]).setLength(i);
				((Rectangle) shapes[i]).setBreadth(i+i);
				((Rectangle) shapes[i]).setFillStyle("dotted");
				shapes[i].draw();
			}
		}
	}
}

public class Test {
	public static void main(String[] args) {
		PaintApp app = new PaintApp();
		app.render(10);
	}
}
```

To implement the flyweight pattern, we can follow steps:

1. Separate the extrinsic state
2. Pass state as parameters
3. Create a factory class

We refactor the Circle and Rectangle class to extract the extrinsic state. All fields except label are extrinsic in this case. We will pass these as parameters to the draw() method of our interface, which we change to an abstract class at this point.

```java
public abstract class Shape {
	public void draw(int radius, String fillColor, String lineColor) {

	}

	public void draw(int length, int breadth, String fillStyle) {

	}
}

public class Circle extends Shape {
	private String label;

	public Circle() {
		label ="Circle";
	}

	@Override
	public void draw(int radius, String fillColor, String lineColor) {
		System.out.println("Drawing: " + label + radius + fillColor + lineColor);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}

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
```

Afterwards we create the ShapeFactory factory class that will cache objects using a hashmap allowing us to reuse objects. The factory checks if a shape is already present in the hashmap, if not, it will instantiate and cache it. We then implement the factory class to our PaintApp.

```java
public class ShapeFactory {
	private static Map<String, Shape> shapes = new HashMap<>();

	public static Shape getShape(String type) {
		Shape shape = null;
		if (shapes.get(type) != null) {
			shape = shapes.get(type);
		} else {
			if (type.equals("circle")) {
				shape = new Circle();
			} else if (type.equals("rectangle")) {
				shape = new Rectangle();
			}
			shapes.put(type, shape);
		}
		return shape;
	}
}

public class PaintApp {
	public void render(int numberOfShapes) {
		Shape shape = null;

		for (int i = 1; i <= numberOfShapes; i++) {
			if (i%2==0) {
				shape = ShapeFactory.getShape("circle");
				shape.draw(i, "red", "white");
			} else {
				shape = ShapeFactory.getShape("rectangle");
				shape.draw(i, i+i, "dotted");
			}
		}
	}
}
```

## Command Pattern

The Command design pattern is a behavioral pattern from the GOF. It is used to encapsulate a request as an object and pass it into an invoker. The invoker does not know how to service the request from the client, and will take the command to the receiver who knows how to perform the action. The advantage of the Command Pattern is that the invoker is decoupled from the receiver. The 5 actors in a Command design pattern are:

1. Command
2. Client
3. Invoker
4. ConcreteCommand
5. Receiver

In the following example, we have a Person class that uses the Television using a RemoteControl. The Person is the client who wants to execute the on() and off() command of the Television. The RemoteControl is the invoker the Command classes OnCommand and OffCommand will implement the interface Command with an execute() method. The Person serves as the client, Television is the receiver, RemoteControl as the invoker, Command is the command while OnCommand and OffCommand are the concrete commands.

We first start with creating the receiver Television, which knows how to perform the on() and off() actions. We then proceed with writing the command Command interface, which will be implemented by the concrete commands. To pass the Television to our concrete command, we need to define a constructor. The final class is the invoker RemoteController which has a private field for a Command including a getter and setter method.

```java
public class Television {
	public void on() {
		System.out.println("Television switched on");
	}

	public void off() {
		System.out.println("Television switched off");
	}
}

public interface Command {
	public void execute();
}


public class OnCommand implements Command{
	Television television;

	public OnCommand(Television television) {
		this.television = television;
	}

	@Override
	public void execute() {
		television.on();
	}
}

public class OffCommand implements Command{
	Television television;

	public OffCommand(Television television) {
		this.television = television;
	}

	@Override
	public void execute() {
		television.off();
	}
}

public class RemoteControl {
	private Command command;

	public void pressButton() {
		command.execute();
	}

  public Command getCommand() {
		return command;
	}

	public void setCommand(Command command) {
		this.command = command;
	}
}
```

To test, we need to create the client Person class. We create an instance of the receiver Television and invoker RemoteControl. We then instantiate the concrete command wherein we pass the receiver Television to it. Afterwards, we pass this concrete command to the invoker RemoteControl

```java
public class Person {
	public static void main(String[] args) {
		Television television = new Television();
		RemoteControl remoteControl = new RemoteControl();

		OnCommand on = new OnCommand(television);
		remoteControl.setCommand(on);
		remoteControl.pressButton();

		OffCommand off = new OffCommand(television);
		remoteControl.setCommand(off);
		remoteControl.pressButton();
	}
}
```

```
Television switched on
Television switched off
```
