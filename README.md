## Product Tool

Internal product management tool for eCommerce, built with Spring Boot 3, JPA, and H2/MySQL. It provides REST APIs to manage categories, their dynamic attributes, and products with typed attribute values.

### Tech stack
- **Java**: 17
- **Spring Boot**: 3.5.4 (Web, Data JPA, Validation)
- **API Docs**: springdoc-openapi with Swagger UI

### Requirements
- Java 17+
- Maven (or use the included Maven Wrapper)

### Quick start (Windows PowerShell)
```powershell
# From the project root
./mvnw.cmd clean spring-boot:run
```

App starts on `http://localhost:8080`.

### Useful URLs
- Swagger UI: `http://localhost:8080/swagger-ui`
- OpenAPI JSON: `http://localhost:8080/api-docs`


### Build a jar
```powershell
./mvnw.cmd clean package -DskipTests
java -jar target/producttool-0.0.1-SNAPSHOT.jar
```

### Configuration
Default configuration is in `src/main/resources/application.properties`:
- `spring.datasource.url=jdbc:h2:file:./data/producttool` (file persisted under `data/`)
- `spring.jpa.hibernate.ddl-auto=update`
- `springdoc.swagger-ui.path=/swagger-ui`

To switch to MySQL, set the following (example):
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/producttool?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

### Domain overview
- **Category**: has a name/description and a set of **Category Attributes**.
- **Category Attribute**: defines name, data type (`STRING`, `NUMBER`, `BOOLEAN`, `DATE`), required flag, and optional allowed values (CSV).
- **Product**: belongs to one category and contains typed attribute values according to that category’s attributes.

### REST API

Base path: `/api`

#### Categories
- `POST /api/categories` — create category
- `GET /api/categories/{id}` — get category with attributes
- `PATCH /api/categories/{id}` — update category
- `DELETE /api/categories/{id}` — delete category
- `POST /api/categories/{id}/attributes` — add attribute to category
- `GET /api/categories/{id}/attributes` — list attributes of category

Example: create category
```json
{
  "name": "Electronics",
  "description": "Devices and gadgets"
}
```

Example: add attribute to category
```json
{
  "name": "Color",
  "dataType": "STRING",
  "required": true,
  "allowedValuesCsv": "Black,White,Red"
}
```

#### Products
- `POST /api/products` — create product
- `GET /api/products/{id}` — get product
- `PATCH /api/products/{id}` — update product

Example: create product
```json
{
  "sku": "IPHONE15-BLK-128",
  "title": "iPhone 15",
  "description": "128GB, Black",
  "categoryId": 1,
  "price": 999.99,
  "active": true,
  "attributes": [
    { "categoryAttributeId": 10, "value": "Black" },
    { "categoryAttributeId": 11, "value": "128" },
    { "categoryAttributeId": 12, "value": "true" }
  ]
}
```

### Db Diagram 

![db diagram](https://github.com/user-attachments/assets/6db57c2a-4439-48c8-ad54-2e797988b004)



### Class Diagram

![classdiagram drawio](https://github.com/user-attachments/assets/e2f22cc9-969a-42b3-a68d-3f1ec40a7271)





https://github.com/user-attachments/assets/57dcee19-50a6-45c4-87fe-e1bf93d05138




