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
  - [Decorator Pattern](#decorator-pattern)
  - [Dependency Injection and Inversion of Control](#dependency-injection-and-inversion-of-control)
    - [Field Level Dependency Injection](#field-level-dependency-injection)
    - [Setter Injection](#setter-injection)
    - [Constructor Injection](#constructor-injection)
  - [Java EE Basics](#java-ee-basics)
      - [Advantages](#advantages)
      - [Layers and Patterns](#layers-and-patterns)
  - [Intercepting Filter](#intercepting-filter)
  - [Front Controller](#front-controller)
  - [MVC](#mvc)
  - [MVC Using Spring Boot](#mvc-using-spring-boot)
  - [Data Access Object DAO Pattern](#data-access-object-dao-pattern)
  - [Proxy Pattern](#proxy-pattern)
  - [Prototype Pattern](#prototype-pattern)
  - [Builder Pattern](#builder-pattern)
  - [Facade Pattern](#facade-pattern)
  - [MVC and Business Delegate](#mvc-and-business-delegate)
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

## Decorator Pattern

The Decorator Pattern is a behavioral pattern that adds additional functionality to an object dynamically at runtime. A decorator wraps an object with additional behavior without affecting other objects of the same type. One example from the java library is the io. In this case, both BufferedReader and FileReader implements the Reader interface.

```java
new BufferedReader(new FileReader(..));
```

In the following example, we have a Pizza component and the PlainPizza concrete component which implements the Pizza component and can be decorated. We can dynamically add toppings to the pizza at runtime using a PizzaDecorator decorator that will be extended by the concrete decorators VeggiePizzaDecorator and CheesePizzaDecorator. The PizzaDecorator class implements Pizza as well, and should have a reference to the Pizza itself on which we invoke the bake() method. The concrete decorator child classes has a constructor that calls the super PizzaDecorator class.

```java
public interface Pizza {
	public void bake();
}

public class PlainPizza implements Pizza {
	@Override
	public void bake() {
		System.out.println("Baking Plain Pizza");
	}
}

public class PizzaDecorator implements Pizza {
	private Pizza pizza;
	public PizzaDecorator(Pizza pizza) {
		this.pizza = pizza;
	}

	@Override
	public void bake() {
		pizza.bake();
	}
}

public class VeggiePizzaDecorator extends PizzaDecorator {
	public VeggiePizzaDecorator(Pizza pizza) {
		super(pizza);
	}

	public void bake() {
		super.bake();
		System.out.println("Adding Veggie Toppings");
	}
}

public class CheesePizzaDecorator extends PizzaDecorator {
	public CheesePizzaDecorator(Pizza pizza) {
		super(pizza);
	}

	public void bake() {
		super.bake();
		System.out.println("Adding Cheese Toppings");
	}
}
```

We can test by wrapping the decorator. We can wrap decorators on one another as well.

```java
public class PizzaShop {
	public static void main(String[] args) {
		Pizza pizza = new VeggiePizzaDecorator(new CheesePizzaDecorator(new PlainPizza()));
		pizza.bake();
	}
}
```

```
Baking Plain Pizza
Adding Cheese Toppings
Adding Veggie Toppings
```

## Dependency Injection and Inversion of Control

If we have two implementations of an interface, for example A AImpl and B BImpl. If AImpl needs an instance of B, or **HAS A** relationship with B, typically we would need to create an instance of BImpl and use it inside AImpl. This is called **Inversion of Control** wherein instead of us instantiating this, we can delegate the responsibility of object creation and injection into AImpl to Spring Framework. **Dependency Injection** is a mechanism of providing dependency to a class dynamically at runtime by creating an object of that type.

The manual way of doing dependency injection is the following: In our CustomerImpl class, we need to define a field for the CreditCardImpl.

```java
public interface CreditCard {
	public void makePayment();
}

public class CreditCardImpl implements CreditCard {
	@Override
	public void makePayment() {
		System.out.println("Payment Made");
	}
}

public interface Customer {
	public void pay();
}

public class CustomerImpl implements Customer {
	CreditCard creditCard = new CreditCardImpl();

	@Override
	public void pay() {
		creditCard.makePayment();
	}
}
```

### Field Level Dependency Injection

To invert the control of object creation to Spring, we can use autowiring to our creditCard field without having to instantiate it. Once marked with **@Autowired**, Spring will search for the CreditCard interface / CreditCardImpl class and create an object and inject it to CustomerImpl. Before Spring does so, we need to mark the CreditCardImpl class with **@Component** annotation which tells Spring that at runtime, the class can be created to be used in other classes. We also mark our Customerimpl with @Component since we will be using it in our test class later on.

```java
@Component
public class CreditCardImpl implements CreditCard {
	@Override
	public void makePayment() {
		System.out.println("Payment Made");
	}
}

@Component
public class CustomerImpl implements Customer {
	@Autowired
	CreditCard creditCard;

	@Override
	public void pay() {
		creditCard.makePayment();
	}
}
```

```java
@SpringBootTest
class IocApplicationTests {
	@Autowired
	Customer customer;

	@Test
	public void testPayment() {
		customer.pay();
	}
}
```

```
Payment Made
```

What happens is when the Spring Container launches, it will scan through our packages and subpackages for classes that are marked with @Component, create instances of those classes, and inject those to wherever we have the @Autowired annotation. In this case, it will first create the CreditCardImpl and inject it to CustomerImpl, and the Customer will be injected to the test class.

### Setter Injection

We can inject the CreditCard to the CustomerImpl class using setter injection.

```java
@Component
public class CustomerImpl implements Customer {
	private	CreditCard creditCard;

	@Override
	public void pay() {
		getCreditCard().makePayment();
	}

	public CreditCard getCreditCard() {
		return creditCard;
	}

	@Autowired
	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}
}
```

### Constructor Injection

To do constructor injection, we need to define a constructor method

```java
@Component
public class CustomerImpl implements Customer {
	private	CreditCard creditCard;

	@Autowired
	CustomerImpl(CreditCard creditCard) {
		this.creditCard = creditCard;
	}
}
```

## Java EE Basics

When we develop huge applications, they are divided into modules. Each module will be divided into Data Access Layer, Service Layer, Presentation Layer, Integration Layer. **Data Access Layer** is an independent layer that is responsible for performing CRUD oeprations against the data store. The services provided by the data access layer is used by the **Service Layer** where our business logic resides. These services in turn are used by the presentation layer and integration layer. The **Presentation Layer** is responsible for generating the UI. The **Integration Layer** allows our applications to acceess other applications and vice versa and is typically made of web services.

There are 10 interfaces and classes that fall across these layers. The Model Class represents our domain model. The **DAO** Interface and the **DAOImpl** is in the DAL. The **Service** Interface and **ServiceImpl** is in the Service Layer which will use the services provided by the DAO. The **Controller** class makes use of the services by the SL. **Utility Classes** are responsible for performing special operations across layers. The **Validator Class** are used to validate data. The **Service Provider** or Consumers, typically web services, are under the IL.

#### Advantages

1. Simplicity
2. Separation of Concerns
3. Maintainability

#### Layers and Patterns

**Presentation Layer**

> Intercepting Filter
> Front Controller
> MVC
> **Service Layer**
> Business Delegate
> Business Object
> **Data Access Layer**
> Data Access Object

## Intercepting Filter

When we create enterprise applications that receives request from clients, these requests are handled by Request Handlers. Sometimes we need to process these requests before reaching the target, examples are uncompressing, authenticating, decrypting, logging etc. Instead of directly handling to the target component, the Intercepting Filter will be handling the request and do processing before the request reaches the target.

In the example, we will be working with a Bad Browser Filter wherein our application only supports Chrome and should reject requests from other browsers. The filter will verify if the request comes from Chrome.

We start with creating the Target servlet class, and then the filter class that will filter the request before reaching the Target. If the user is using chrome, the request will reach the target, if not the request will go to badBrowser.jsp

```java
@WebServlet("/homeServlet")
public class HomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("username", "Doge");
		RequestDispatcher dispatcher = request.getRequestDispatcher("home.jsp");
		dispatcher.forward(request, response);
	}
}

@WebFilter("/*")
public class UserAgentFilter implements Filter {
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String userAgentHeader = ((HttpServletRequest) request).getHeader("User-Agent");
		if (userAgentHeader.contains("Chrome")) {
			// send to the target object
			chain.doFilter(request, response);
		} else {
			RequestDispatcher dispatcher = request.getRequestDispatcher("badBrowser.jsp");
			dispatcher.forward(request, response);
		}
	}
  ...
}
```

In the jsp page, we can read the data we have set using jsp tags

```jsp
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Home Page</title>
</head>
<body>
Welcome
<%request.getAttribute("username");%>
</body>
</html>
```

## Front Controller

When an end user accesses our web application, if there is no centralized request handling mechanism, each view will have its own processing logic and view navigation logic. The problem with this approach is duplicated business logic across views. The Front Controller pattern will handle the business processing and the view for us. When a request comes in, the request will go to the Front Controller, which invokes the appropriate business logic before navigating to the next view. The Front Controller will use the Command Pattern to make use of a CommandHelper to know which special processing has to be executed. Once it executes, the Front Controller will navigate to the next view using a Dispatcher component.

We start with creating the index.jsp.

```jsp
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Home Page</title>
</head>
<body>
	<a href="viewStudentDetails.do">View Student Details</a>
</body>
</html>
```

We will be using the Command Pattern. The application will be sending the student details back through the Command interface and ViewStudentCommand concrete command. Within this command, we create the StudentVo value object. A **Value Object** is a java bean that will carry the information across the Java EE layers. In this case, the StudentVo will be sent to the next view in the request object. We return the jsp page showStudentDetails.

```java
public interface Command {
	public String execute(HttpServletRequest request, HttpServletResponse response);
}

public class ViewStudentCommand implements Command {
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		StudentVo vo = new StudentVo(1, "Doge");
		request.setAttribute("studentDetails", vo);
		return "showStudentDetails";
	}
}

public class StudentVo {
	private int id;
	private String name;

	StudentVo(int id, String name) {
		this.id = id;
		this.name = name;
	}
```

We then proceed on writing the CommandHelper which returns a Command. Afterwards we can write the FrontController itself. We are dynamically retrieving the command by passing the requestURI to our CommandHelper which retrieves the command based on URI. The FrontController then executes the command by passing in the request and response. That command will perform the action of creating a StudentVo, set the details onto the request and return the next view showStudentDetails. Finally, we create the Dispatcher which will forward the user to the next view

```java
public class CommandHelper {
	public Command getCommand(String requestURI) {
		if (requestURI.contains("viewStudentDetails.do")) {
			return new ViewStudentCommand();
		}
		return null;
	}
}

@WebServlet("/*.do")
public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		CommandHelper commandHelper = new CommandHelper();
		Command command = CommandHelper.getCommand(requestURI);
		command.execute(request, response);

		response.getWriter().append("Served at: ").append(request.getContextPath());
	}
}

@WebServlet("*.do")
public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		CommandHelper commandHelper = new CommandHelper();
		Command command = commandHelper.getCommand(requestURI);
		String view = command.execute(request, response);
		Dispatcher dispatcher = new Dispatcher();
		dispatcher.dispatch(request, response, view);
	}
}

	private String mapPageToView(String view) {
		if (view.equals("showStudentDetails")) {
			return "viewStudentDetails.jsp";
		}
		return null;
	}
}
```

And the view for viewStudentDetails

```jsp
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Student Details</title>
</head>
<body>
	<jsp:useBean id="studentDetails"
		type="com.demiglace.patterns.frontcontroller.StudentVo"
		scope="request"></jsp:useBean>
	Student Id:
	<jsp:getProperty property="id" name="studentDetails" />
	Student Name:
	<jsp:getProperty property="name" name="studentDetails" />
</body>
</html>
```

## MVC

MVC is a design pattern that splits the web layer into Model, View and Controller. The **Model** represents the current state of the application and does most of the business logic, and can connect to the database if needed. The **View** is responsible for displaying the current model to the end user. The **Controller** acts as the glue between the Model and View, such as selecting the appropriate model to serve the appropriate view. The model is represented by a java class, View a JSP, and Controller a servlet.

We start with creating the view.

```html
<body>
	<h3>Enter two number:</h3>
	<form action="averageController" method="post">
		Number 1 : <input name="number1"/></br>
		Number 2 : <input name="number2"/></br>
		<input type="submit"/>
	</form>
</body>
```

Afterwards we create the Model which will represent the data and actions to be performed. And then the controller

```java
public class AverageModel {
	public int calculateAverage(int num1, int num2) {
		return (num1+num2)/2;
	}
}

@WebServlet("/averageController")
public class AverageController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int num1 = Integer.parseInt(request.getParameter("number1"));
		int num2 = Integer.parseInt(request.getParameter("number2"));

		AverageModel model = new AverageModel();
		int result = model.calculateAverage(num1, num2);
		request.setAttribute("result", result);

		RequestDispatcher requestDispatcher = request.getRequestDispatcher("result.jsp");
		requestDispatcher.forward(request, response);
	}
}
```

We then send the result to the next view

```jsp
<body>
<%
int result = (Integer) request.getAttribute("result");
out.println("Average of two numbers is: " + result);
%>
</body>
```

## MVC Using Spring Boot

With Spring Boot MVC, we can start with the Controller. We mark our controller class with **@Controller** annotation. We map helloMvc.jsp to /hello using the **@RequestMapping** annotation. Using a ModelMap parameter from Spring, we can send a model to the view.

```java
@Controller
public class HelloController {
	@RequestMapping("/hello")
	public String hello(ModelMap modelMap ) {
		modelMap.addAttribute("msg", "Doge");
		return "helloMvc";
	}
}
```

Afterwards we proceed on creating the view. We also need to configure the prefix and suffix for the View Resolver in application.properties. The context-path will make our application accessible at `http://localhost:8080/mvc/hello`

```jsp
<title>MVC Pattern</title>
</head>
<body>
	Hello MVC by ${msg}
</body>
```

```
spring.mvc.view.prefix=/WEB-INF/jsps/
spring.mvc.view.suffix=.jsp
server.servlet.context-path=/mvc
```

We also need to add the embedded jasper dependency which is needed to serve JSP pages from a JAR file since Spring Boot automatically runs on an embedded tomcat server.

```xml
		<dependency>
			<groupId>org.apache.tomcat.embed</groupId>
			<artifactId>tomcat-embed-jasper</artifactId>
			<scope>provided</scope>
		</dependency>
```

## Data Access Object DAO Pattern

The DAO Pattern is a Data Access Layer pattern that seprates the database related code. All the logic that has to do wtih database operations should be handled by the DAO. The DAO implementation will know how to connect and execute operations against a database.
We start by creating the EmployeeDAO interface and Employee model class. In the EmployeeDAOImpl, we can write out the implementation of the create() method. We also need to use the **@Repository** Spring annotation.

```java
public interface EmployeeDAO {
	public void create(Employee employee);
}

public class Employee {
	private int id;
	private String name;

@Repository
public class EmployeeDAOImpl implements EmployeeDAO {
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public void create(Employee employee) {
		String sql = "INSERT INTO employee values(?,?)";
		jdbcTemplate.update(sql, employee.getId(), employee.getName());
	}
}
```

To test, we first need to configure the data source information at application.properties and then write out the test class.

```
spring.datasource.url=jdbc:mysql://localhost:3006/mydb
spring.datasource.username=root
spring.datasource.password=1234
```

```java
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
```

## Proxy Pattern

We can implement lazy loading using the Virtual Proxy pattern. To do that, we first define a Customer interface and its implementation that will only fetch the Customer details. The CustomerProxyImpl will be the proxy class for CustomerImpl which will implement the same interface but will be responsible for loading the order details. CustomerImpl will have private fields for id and a list of orders, and will also contain getters and setter mehotds for these fields.

```java
public interface Customer {
	public int getId();
	public List<Order> getOrders();
}

public class Order {
	private int id;
	private String productName;
	private int quantity;

public class CustomerImpl implements Customer {
	private int id;
	private List<Order> orders;
```

At this point we can create the test class, which will only return the id for now and null for the orders.

```java
public class Test {
	public static void main(String[] args) {
		Customer customer = new CustomerImpl();
		System.out.println(customer.getId());
		System.out.println(customer.getOrders());
	}
}
```

We then proceed on writing the CustomerProxyImpl class. In the getOrders method, we are hardcoding a list of orders.

```java
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
```

In the test class, instead of using CustomerImpl, we can use CustomerProxyImpl instead. This time around, we won't be getting null for getOrders().

```java
public class Test {
	public static void main(String[] args) {
		Customer customer = new CustomerProxyImpl();
		System.out.println(customer.getId());
		System.out.println(customer.getOrders());
	}
}
```

## Prototype Pattern

Prototype is a creational design pattern that uses an existing object to create new objects. In this instance, we are working on a Gaming App that is accessed by multiple players. Instead of recreating the Gaming App when accessed, we can have a prototype object ready initialized with all the state and data. Spring supports both Prototype and Singleton. In this example, we have a Game class which implements the Cloneable interface.

The clone() method will return a copy of the object. The cloned object will have the same object reference to Membership as the first. This is doing a **shallow copy**.

```java
public class Game implements Cloneable {
	private int id;
	private String name;
	private Membership membership;

	@Override
	protected Game clone() throws CloneNotSupportedException {
		return (Game) super.clone();
	}

	@Override
	public String toString() {
		return "Game [id=" + id + ", name=" + name + ", membership=" + getMembership() + "]";
	}
  ...
}

public class Test {
	public static void main(String[] args) throws CloneNotSupportedException {
		Game game1 = new Game();
		game1.setId(1);
		game1.setName("Genshin Impact");
		game1.setMembership(new Membership());

		Game game2 = game1.clone();
		System.out.println(game1);
		System.out.println(game2);
	}
}
```

```
Game [id=1, name=Genshin Impact, membership=com.demiglace.patterns.prototype.Membership@6f2b958e]
Game [id=1, name=Genshin Impact, membership=com.demiglace.patterns.prototype.Membership@6f2b958e]
```

We can do a deep clone by instantiating the new Membership() inside the clone() method

```java
	@Override
	protected Game clone() throws CloneNotSupportedException {
		Game game = (Game) super.clone();
		game.setMembership(new Membership());
		return game;
	}
```

Or by creating a Copy Constructor:

```java
	Game(Game game) {
		this.id = game.id;
		this.name = game.name;
		this.membership = new Membership();
	}
```

And testing it:

```java
public class Test {
	public static void main(String[] args) throws CloneNotSupportedException {
		Game game1 = new Game();
		game1.setId(1);
		game1.setName("Genshin Impact");
		game1.setMembership(new Membership());

		Game game2 = game1.clone();
		System.out.println(game1);
		System.out.println(game2);

		Game game3 = new Game(game1);
		System.out.println(game3);
	}
}
```

```
Game [id=1, name=Genshin Impact, membership=com.demiglace.patterns.prototype.Membership@6f2b958e]
Game [id=1, name=Genshin Impact, membership=com.demiglace.patterns.prototype.Membership@5e91993f]
Game [id=1, name=Genshin Impact, membership=com.demiglace.patterns.prototype.Membership@1c4af82c]
```

## Builder Pattern

The following example highlights the problem the Builder pattern is trying to solve. If we instantiate an HttpClient object and we don't need some parameters, we need to pass in nulls. This results in unreadable code. The builder pattern helps us to create an object without all fields.

```java
public class HttpClient {
	private String method;
	private String url;
	private String userName;
	private String password;
	private String headers;
	private String body;

	public HttpClient(String method, String url, String userName, String password, String headers, String body) {
		super();
		this.method = method;
		this.url = url;
		this.userName = userName;
		this.password = password;
		this.headers = headers;
		this.body = body;
	}
}

public class Test {
	public static void main(String[] args) {
		HttpClient uglyHttpClient = new HttpClient("GET", "http://test.com", null, null, null, null);
	}
}
```

The builder pattern typically has a Builder interface or abstract class. The ConcreteBuilder will then implement that and provide various methods required to build an object. This makes it easy for the end client to create an instance of the object just by configuring the needed properties.

We will be creating a public static inner class, which will have a copy of the properties of the actual class. For some of the properties, we will have methods and each method will return an instance of the same class back that allows us to chain methods. The last method is the **build()** method which returns a new HttpClient object wherein we pass **this** as a parameter to the constructor.

```java
public class HttpClient {
	private String method;
	private String url;
	private String userName;
	private String password;
	private String headers;
	private String body;

	public HttpClient(HttpClientBuilder httpClientBuilder) {
		this.method = httpClientBuilder.method;
		this.url = httpClientBuilder.url;
		this.userName = httpClientBuilder.userName;
		this.password = httpClientBuilder.password;
		this.headers = httpClientBuilder.headers;
		this.body = httpClientBuilder.body;
	}

	public static class HttpClientBuilder {
		private String method;
		private String url;
		private String userName;
		private String password;
		private String headers;
		private String body;

		public HttpClientBuilder method(String method) {
			this.method = method;
			return this;
		}

		public HttpClientBuilder url(String url) {
			this.url = url;
			return this;
		}

		public HttpClientBuilder secure(String userName, String password) {
			this.userName = userName;
			this.password = password;
			return this;
		}

		public HttpClientBuilder headers(String headers) {
			this.headers = headers;
			return this;
		}

		public HttpClientBuilder body(String body) {
			this.body = body;
			return this;
		}

		public HttpClient build() {
			return new HttpClient(this);
		}
	}
}
```

The following test shows how we can build an HttpClient object using the builder.

```java
public class Test {
	public static void main(String[] args) {
		HttpClientBuilder builder = new HttpClient.HttpClientBuilder();
		HttpClient client = builder.method("POST").url("http://test.com").body("{}").build();
		System.out.println(client);
	}
}
```

```
HttpClient [method=POST, url=http://test.com, userName=null, password=null, headers=null, body={}]
```

## Facade Pattern

The Facade Pattern makes a complex system easy to use for the client application. In the following order example, we have classes for CheckStock, PlaceOrder, ShipOrder. The client has to use these 3 classes and needs to be aware of the method signatures. The Facade class will be the one handling everything instead. The following code shows the problem we are trying to solve. The client class in this case has to write out all the logic for instantiating the class.

```java
public class OrderProcessor {
	public boolean checkStock(String name) {
		System.out.println("Checking stock");
		return true;
	}

	public String placeOrder(String name, int quantity) {
		System.out.println("Order placed");
		return "abc123";
	}

	public void shipOrder(String orderId) {
		System.out.println("Order shipped");
	}
}

public class Test {
	public static void main(String[] args) {
		OrderProcessor processor = new OrderProcessor();
		if (processor.checkStock("Macbook")) {
			String orderId = processor.placeOrder("Macbook", 3);
			processor.shipOrder(orderId);
		};
	}
}
```

We can instead build a Facade class that will contain all the logic of the client class. This results in a much cleaner client Test class.

```java
public class OrderFacade {
	private OrderProcessor processor = new OrderProcessor();

	public void processOrder(String name, int quantity) {
		if (processor .checkStock(name)) {
			String orderId = processor .placeOrder(name, quantity);
			processor .shipOrder(orderId);
		}
		;
	}
}

public class Test {
	public static void main(String[] args) {
		OrderFacade facade = new OrderFacade();
		facade.processOrder("Macbook", 3);
	}
}
```

The limitation of using Facade is everytime the methods in our OrderProcessor class changes, we need to update our processOrder method as well.

## MVC and Business Delegate
