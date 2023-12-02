# Pharmacy Management System

This Pharmacy Management System is a Java application designed for managing pharmacy operations such as product search, adding products to a cart, and processing orders. It provides a user-friendly interface for sales assistants to perform their tasks efficiently.

## Prerequisites

Before you can run this application, ensure you have the following software installed:

1. **Java Development Kit (JDK)**: You'll need Java to run the application. Download and install it from [Oracle's website](https://www.oracle.com/java/technologies/javase-downloads.html) or use an OpenJDK distribution.

2. **IntelliJ IDEA**: You can download IntelliJ IDEA, a popular Java IDE, from [JetBrains](https://www.jetbrains.com/idea/download/). It's a convenient choice for developing and running Java applications.

3. **MySQL**: Install MySQL as your database server. You can download it from the [official MySQL website](https://dev.mysql.com/downloads/mysql/).

## Getting Started

Follow these steps to set up and run the Pharmacy Management System:

1. **Clone the Repository**: Clone this GitHub repository to your local machine using Git or by downloading the ZIP archive.

   ```shell
   git clone https://github.com/yourusername/pharmacy-management-system.git
**1. Configure Database Connection:**
      Open the Constants.java file located in the GUI package.
Update the DB_USERNAME and DB_PASSWORD variables with your MySQL username and password.

**2. Set Up Database:**
      In IntelliJ IDEA, open the Database tool window (View -> Tool Windows -> Database).
Click the "+" icon and select "Data Source" -> "MySQL."
Enter your MySQL username and password, and configure the connection settings.

**3. Import Database Schema:**
    Open the SQL file provided in the project (e.g., pharmacy_management_system.sql) in a MySQL database client.
Execute the SQL script to create the necessary tables and seed data.

**4. Run the Application:**
    Open the HomeScreen.java file located in the project.
Right-click within the file and select "Run HomeScreen.main()" to start the application.
5. Using the Application
    Once you've successfully run the application, you'll see the Home Screen. Use this screen to navigate and perform various tasks within the Pharmacy Management System.

Sales Assistant Dashboard: Access the main dashboard for sales assistants. Here, you can search for products, add them to a cart, and process orders.

**Other Features:** Explore other features and functionalities provided by the application.

Troubleshooting
If you encounter any issues or have questions about using this Pharmacy Management System, please don't hesitate to reach out to the project maintainers or consult the project's documentation.

Contributions
Contributions to this project are welcome! Feel free to submit bug reports, feature requests, or even pull requests if you'd like to contribute code improvements.

Enjoy using the Pharmacy Management System!
