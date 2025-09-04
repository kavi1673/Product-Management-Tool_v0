<<<<<<< HEAD
# Internal-product-management-tool
An internal tool to manage the product catalog of an eCommerce platform. The platform currently sells dresses and shoes. Every 6 months, it plans to expand into new categories (e.g., watches, smartphones, grooming products, accessories). Each category has its own unique attributes. 
=======
# Internal Product Management Tool

Tech: Java 17, Spring Boot 3, Spring Data JPA, H2, Validation, springdoc OpenAPI.

## How to run

Windows PowerShell:

```powershell
cd C:\Users\HP\Downloads\producttool\producttool
.\mvnw.cmd -DskipTests spring-boot:run
```

Then open:
- Swagger UI: http://localhost:8080/swagger-ui
- OpenAPI JSON: http://localhost:8080/api-docs
- H2 Console: http://localhost:8080/h2-console (JDBC: `jdbc:h2:file:./data/producttool`, user `sa`, empty password)
- Minimal UI: http://localhost:8080/index.html

Build a jar:
```powershell
cd C:\Users\HP\Downloads\producttool\producttool
.\mvnw.cmd -DskipTests package
java -jar target\producttool-0.0.1-SNAPSHOT.jar
```

If port 8080 is in use, use another port:
```powershell
java -jar target\producttool-0.0.1-SNAPSHOT.jar --server.port=8081
```

## Step 1: Database Design (ERD)

Mermaid ERD: `src/main/resources/static/diagrams/erd.mmd`

Justification:
- Categories define their own attributes (EAV pattern) using `CategoryAttribute`.
- `ProductAttributeValue` stores values using typed columns (`value_string`, `value_number`, etc.) for integrity and indexing while keeping flexibility.
- Uniqueness constraints: attribute name unique per category, one value per product-attribute pair.
- Scales to new categories and attributes without schema changes; add more `DataType`s if needed.

## Step 2: Class Design

Mermaid Class Diagram: `src/main/resources/static/diagrams/class.mmd`

- Services encapsulate validation and business rules.
- Controllers are thin, DTOs separate API shape from entities.

## Step 3: Implementation

APIs (see Swagger UI):
- Categories: create, get, update, delete; add/list attributes
- Products: create, get, update with dynamic attributes

Validation:
- Type-safe attribute values based on `DataType`
- Required flag enforced
- Allowed values for STRING attributes

Minimal UI:
- `index.html` provides quick manual testing for categories, attributes, and products.

## Export diagrams to PNG

If you have Node.js installed:
```bash
npm install -g @mermaid-js/mermaid-cli
# From repo root
mmdc -i src/main/resources/static/diagrams/erd.mmd -o docs/erd.png
mmdc -i src/main/resources/static/diagrams/class.mmd -o docs/class.png
```
The generated images will be under `docs/`.

## Record a short demo

- Start the app (on 8081 if needed) and open http://localhost:8081/index.html
- Use any screen recorder (e.g., Xbox Game Bar Win+G) to record:
  - Creating a category and attributes
  - Creating a product with attributes
  - Viewing the product
- Save as `docs/demo.mp4`.

## Push to GitHub

Initialize and push (replace YOUR_GITHUB_URL):
```powershell
cd C:\Users\HP\Downloads\producttool\producttool
git init
# Optional: install Git LFS for large media
# https://git-lfs.com
# git lfs install
# git lfs track "*.png" "*.mp4"

git add .
git commit -m "Product tool: schema, services, UI, diagrams, docs"
git branch -M main
git remote add origin YOUR_GITHUB_URL.git
git push -u origin main
``` 
>>>>>>> 8e685fd (Initial submission: backend, UI, diagrams, docs, demo)
