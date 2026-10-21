# Video Script (English) – Product Management System

**Target length:** about 5 minutes (≈ 650 spoken words).
**Tip:** speak slowly. Lines in *[SHOW: …]* tell you what to display; only the text in quotes is spoken.
**Before recording:** start MySQL, run `database/script.sql`, and leave the `productos` table empty so the demo starts clean.

---

## 1. Introduction (≈ 0:00 – 0:30)

*[SHOW: Main application window, empty form]*

"Hello, my name is [YOUR NAME], and I am a student at Universidad Manuela Beltrán.
In this video I will present my project, the Product Management System.
It is a desktop application made in Java that saves product information in a MySQL database."

## 2. The problem (≈ 0:30 – 0:55)

*[SHOW: Main application window]*

"Small stores need to keep a list of their products: the code, the name, the price and how many units they have.
If this information only lives in memory, it is lost when the program closes.
This application solves that problem. It stores every product in a database, so the data is still there the next time we open the program."

## 3. Technologies (≈ 0:55 – 1:20)

*[SHOW: pom.xml open in the IDE, highlight the mysql-connector-j dependency]*

"I used Java for the code, Swing for the graphical interface, and MySQL as the database.
To connect Java with MySQL, I used JDBC with the official MySQL driver, called Connector J.
Maven manages this dependency. Here in the pom file you can see it."

## 4. Project structure (≈ 1:20 – 2:00)

*[SHOW: Project tree in the IDE, expand all packages]*

"The project follows a simple layered design.
In the model package, the Producto class represents one product with its six attributes.
In the conexion package, the ConexionBD class opens the connection to MySQL. All the connection settings, like the user, the password and the port, are in this one class.
In the dao package, the ProductoDAO class has all the SQL queries. DAO means Data Access Object. It keeps the database code separate from the interface.
In the vista package, the ProductoFrame class is the window. It reads the user input, validates it, and calls the DAO.
Finally, the Main class starts the application."

## 5. Database (≈ 2:00 – 2:25)

*[SHOW: database/script.sql, then the table structure in MySQL Workbench]*

"The database is called sistema underscore productos, and it has one table called productos.
The table has an id that increases automatically, a code, a name, a description, a price and a quantity.
The code is unique, so two products cannot have the same code."

## 6. JDBC connection (≈ 2:25 – 3:00)

*[SHOW: ConexionBD.java, then the registrar method in ProductoDAO.java]*

"Here is how the connection works. The DriverManager class uses the URL, the user and the password to create a Connection.
In the DAO, every query uses a PreparedStatement. The values go into question-mark parameters. This protects the application from SQL injection.
I also use try-with-resources, so the connection, the statement and the result set are always closed automatically, even if there is an error."

## 7. Demo – Create (≈ 3:00 – 3:25)

*[SHOW: Application. Type P001, Arroz 500 g, Bolsa, 2500, 40 → click Registrar]*

"Now let me show the application. First, I register a new product. I type the code, the name, the description, the price and the quantity, and I click Registrar. The message says the product was saved, and it appears in the table."

*[SHOW: Type a negative price, e.g. -100 → click Registrar]*

"The program also validates the data. If I write a negative price, it shows a clear message and does not save anything. The same happens with empty fields or a quantity with letters."

## 8. Demo – Read and Search (≈ 3:25 – 3:55)

*[SHOW: Register P002 Aceite quickly. Click "Consultar / Actualizar tabla". Then type P002 in Código → Buscar. Then Limpiar, type "arroz" in Nombre → Buscar]*

"The Consultar button loads all the products from the database into the table.
I can also search. If I write a code, it searches by code. If the code is empty, it searches by name. Here I search for the word arroz, and it finds the matching product."

## 9. Demo – Update (≈ 3:55 – 4:20)

*[SHOW: Click Consultar, select P001 in the table, change price to 2700 and quantity to 35 → Actualizar]*

"To update a product, I select it in the table, and its data is loaded into the form. I change the price and the quantity, and I click Actualizar. The table now shows the new values.
If I try to update a code that does not exist, the program tells me that the product was not found."

## 10. Demo – Delete (≈ 4:20 – 4:45)

*[SHOW: Select P002 → Eliminar → click No; then Eliminar again → click Yes]*

"To delete a product, I use its code and click Eliminar. The program asks for confirmation first. If I choose No, nothing is deleted. If I choose Yes, the product is removed from the database."

## 11. Persistence (≈ 4:45 – 5:15)

*[SHOW: Close the application. Run it again. Then show SELECT * FROM productos; in MySQL]*

"Now the most important part: persistence. I close the application completely and open it again.
As you can see, the products are still here, because they are saved in MySQL and not only in memory. I can also see the same data directly in MySQL with a select query."

## 12. Conclusion (≈ 5:15 – 5:40)

*[SHOW: Main application window with data]*

"In conclusion, this project shows how to build a complete CRUD application in Java: create, read, update and delete.
I learned how to use JDBC, PreparedStatement and the DAO pattern to keep the code organized and safe.
The source code is available on GitHub. Thank you for watching."
