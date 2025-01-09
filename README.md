# E-commerce Application

This is a full-fledged e-commerce web application built using Spring Boot, Thymeleaf, and Bootstrap. The platform allows users to browse products, add them to their cart, and manage orders seamlessly.

## Project Description

- **E-commerce Platform (Spring Boot, Thymeleaf, Data JPA, Hibernate):**
  - Designed and developed a comprehensive e-commerce platform offering a dynamic product catalog and user-friendly interface.
  - Integrated payment processing, warehouse management, and personalized recommendations.
  - Utilized efficient database handling and a chatbot for real-time assistance.

## Features
- User-friendly interface for browsing products.
- Add items to the cart and proceed to checkout.
- Manage user accounts and orders.
- Admin panel for managing products and inventory.
- Integrated payment processing.
- Real-time chatbot for assistance.
- Warehouse management system.

## Technologies Used
- **Backend**: Spring Boot
- **Frontend**: Thymeleaf, Bootstrap
- **Database**: MySQL
- **ORM**: Hibernate, Spring Data JPA

## Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/Nayankumardhome/E-commerce-Application.git
2. Open the project in your favorite IDE (e.g., IntelliJ IDEA or Eclipse).
3. Configure the database connection in application.properties:
```properties:
  spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db
  spring.datasource.username=your_username
  spring.datasource.password=your_password

  spring.mail.username=your_username
  spring.mail.password=your_password_token
```
4. Build and run the application
  ```bash
  mvn clean install
  mvn spring-boot:run
```
5. Access the application in your browser at http://localhost:8080.
