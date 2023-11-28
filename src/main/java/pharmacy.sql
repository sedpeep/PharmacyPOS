CREATE DATABASE PharmacyPOS;
Use PharmacyPOS;
CREATE TABLE Users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    role ENUM('Manager','Sales Assistant')
);
CREATE TABLE Categories (
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    category_name VARCHAR(100) NOT NULL
);
CREATE TABLE Products (
    product_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    quantity INT DEFAULT 0,
    category_id INT,
    expiration_date date not null ,
    FOREIGN KEY (category_id) REFERENCES Categories(category_id)
);
CREATE TABLE Orders (
    order_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    total DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);
CREATE TABLE OrderDetails (
    order_detail_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT,
    product_id INT,
    quantity INT,
    price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES Orders(order_id),
    FOREIGN KEY (product_id) REFERENCES Products(product_id)
);
INSERT INTO Users (username, password, role) VALUES 
('manager', 'password123', 'Manager'),
('assistant1', 'password123', 'Sales Assistant');

INSERT INTO Categories (category_name) VALUES ('Pain Relief'), ('Ointments'), ('Cough and Cold');

INSERT INTO Products (name, description, price, quantity, category_id,expiration_date) VALUES
('Aspirin', 'Pain reliever and fever reducer', 5.99, 100, 1,'2023-11-18'),
('Ibuprofen', 'Nonsteroidal anti-inflammatory drug', 8.99, 200, 1,'2023-12-21'),
('Antiseptic Cream', 'Topical ointment for minor cuts and abrasions', 4.49, 50, 2,'2023-12-11'),
('Cough Syrup', 'Relieves cough due to minor throat and bronchial irritation', 6.49, 75, 3,'2024-1-1');

INSERT INTO Orders (user_id, total) VALUES (2, 25.47);
INSERT INTO OrderDetails (order_id, product_id, quantity, price) VALUES 
(1, 1, 2, 11.98);

INSERT INTO Categories (category_name) VALUES
                                           ('Vitamins'),
                                           ('Skin Care'),
                                           ('Digestive Health'),
                                           ('First Aid'),('Allergy Relief'),
                                           ('Hair Care'),
                                           ('Oral Care'),
                                           ('Eye Care');


INSERT INTO Products (name, description, price, quantity, category_id, expiration_date) VALUES
                                                                                            ('Paracetamol', 'Used to treat pain and fever', 3.99, 150, 1, '2024-05-15'),
                                                                                            ('Vitamin C', 'Essential vitamin for health', 12.99, 200, 4, '2024-07-20'),
                                                                                            ('Aloe Vera Gel', 'Used for healing and softening the skin', 7.50, 80, 5, '2023-12-31'),
                                                                                            ('Digestive Enzymes', 'Helps in digestion', 15.49, 60, 6, '2024-06-30'),
                                                                                            ('Band-Aids', 'Adhesive bandages for minor cuts', 2.99, 120, 7, '2025-01-01'),
                                                                                            ('Antibiotic Ointment', 'Prevents infection in minor cuts', 5.99, 90, 7, '2024-08-25'),
                                                                                            ('Acetaminophen', 'Mild pain reliever and fever reducer', 4.99, 200, 1, '2024-03-10'),
                                                                                            ('Hydrocortisone Cream', 'Treats skin irritation and rashes', 6.45, 50, 5, '2024-04-22'),
                                                                                            ('Probiotics', 'Supports digestive and gut health', 18.99, 70, 6, '2024-09-15'),
                                                                                            ('Multivitamins', 'Various vitamins and minerals for daily health', 19.99, 100, 4, '2024-11-05'),('Loratadine', 'Allergy relief medication', 10.99, 120, 8, '2024-12-31'),
                                                                                            ('Cetirizine', 'Treats symptoms such as itching, swelling, and rashes', 9.49, 100, 8, '2025-01-20'),
                                                                                            ('Shampoo', 'Nourishing hair care shampoo', 6.99, 150, 9, '2024-08-15'),
                                                                                            ('Conditioner', 'Hair conditioner for dry hair', 7.50, 150, 9, '2024-09-10'),
                                                                                            ('Toothpaste', 'Fluoride toothpaste for cavity protection', 3.99, 200, 10, '2024-07-25'),
                                                                                            ('Mouthwash', 'Antibacterial mouthwash for oral hygiene', 4.99, 180, 10, '2024-06-30'),
                                                                                            ('Eye Drops', 'Relieves eye irritation and dryness', 8.99, 90, 11, '2024-05-01');


INSERT INTO Products (name, description, price, quantity, category_id, expiration_date) VALUES
                                                                                            ('Azomax', 'Used to treat seasonal sinus', 3.99, 5, 3, '2024-05-15'),
                                                                                            ('Leflox', 'Antibiotic used to treat astma', 12.99, 8, 3, '2024-07-20');


INSERT INTO Orders (user_id, total) VALUES
                                        (2, 45.90),
                                        (2, 60.25),
                                        (2, 15.50);

INSERT INTO OrderDetails (order_id, product_id, quantity, price) VALUES
                                                                     (1, 1, 1, 20.95),
                                                                     (1, 3, 2, 24.95);


INSERT INTO OrderDetails (order_id, product_id, quantity, price) VALUES
                                                                     (2, 2, 1, 30.25),
                                                                     (2, 4, 1, 30.00);

INSERT INTO OrderDetails (order_id, product_id, quantity, price) VALUES
    (3, 1, 1, 15.50);

INSERT INTO Orders (user_id, total) VALUES
                                        (2, 35.75),
                                        (2, 20.00),
                                        (2, 55.30),
                                        (2, 10.00);

INSERT INTO OrderDetails (order_id, product_id, quantity, price) VALUES
                                                                     (4, 5, 1, 15.75),
                                                                     (4, 2, 2, 20.00);


INSERT INTO OrderDetails (order_id, product_id, quantity, price) VALUES
    (5, 3, 1, 55.30);


INSERT INTO OrderDetails (order_id, product_id, quantity, price) VALUES
    (6, 4, 3, 10.00);


INSERT INTO OrderDetails (order_id, product_id, quantity, price) VALUES
                                                                     (7, 1, 2, 5.00),
                                                                     (7, 5, 1, 5.00);


ALTER TABLE Orders
    CHANGE COLUMN `timestamp` `timestamp` DATETIME NOT NULL;

INSERT INTO Orders (user_id, total, timestamp) VALUES
                                                   (1, 100.00, '2023-01-01 10:00:00'),
                                                   (1, 150.00, '2023-01-08 10:00:00'),
                                                   (1, 200.00, '2023-01-15 10:00:00'),
                                                   (1, 250.00, '2023-01-22 10:00:00'),
                                                   (1, 300.00, '2023-02-01 10:00:00'),
                                                   (1, 350.00, '2023-02-08 10:00:00'),
                                                   (1, 400.00, '2023-03-01 10:00:00'),
                                                   (1, 450.00, '2023-03-08 10:00:00'),
                                                   (1, 500.00, '2023-04-01 10:00:00'),
                                                   (1, 550.00, '2023-04-08 10:00:00');


INSERT INTO OrderDetails (order_id, product_id, quantity, price) VALUES
                                                                     (1, 1, 10, 10.00),
                                                                     (2, 1, 15, 10.00),
                                                                     (3, 1, 20, 10.00),
                                                                     (4, 1, 25, 10.00),
                                                                     (5, 1, 30, 10.00),
                                                                     (6, 1, 35, 10.00),
                                                                     (7, 1, 40, 10.00),
                                                                     (8, 1, 45, 10.00),
                                                                     (9, 1, 50, 10.00),
                                                                     (10, 1, 55, 10.00);

INSERT INTO Orders (user_id, total, timestamp) VALUES
                                                   (1, 120.00, '2023-01-15 10:00:00'), -- January
                                                   (1, 110.00, '2023-02-20 10:00:00'), -- February
                                                   (1, 130.00, '2023-03-18 10:00:00'), -- March
                                                   (1, 140.00, '2023-04-10 10:00:00'), -- April
                                                   (1, 200.00, '2023-05-12 10:00:00'), -- May
                                                   (1, 160.00, '2023-06-21 10:00:00'), -- June
                                                   (1, 180.00, '2023-07-19 10:00:00'), -- July
                                                   (1, 220.00, '2023-08-17 10:00:00'), -- August
                                                   (1, 210.00, '2023-09-15 10:00:00'), -- September
                                                   (1, 190.00, '2023-10-20 10:00:00'), -- October
                                                   (1, 230.00, '2023-11-18 10:00:00'), -- November
                                                   (1, 250.00, '2023-12-10 10:00:00'); -- December


INSERT INTO OrderDetails (order_id, product_id, quantity, price) VALUES
                                                                     (11, 1, 12, 10.00),
                                                                     (12, 1, 11, 10.00),
                                                                     (13, 1, 13, 10.00),
                                                                     (14, 1, 14, 10.00),
                                                                     (15, 1, 20, 10.00),
                                                                     (16, 1, 16, 10.00),
                                                                     (17, 1, 18, 10.00),
                                                                     (18, 1, 22, 10.00),
                                                                     (19, 1, 21, 10.00),
                                                                     (20, 1, 19, 10.00),
                                                                     (21, 1, 23, 10.00),
                                                                     (22, 1, 25, 10.00);


INSERT INTO Orders (user_id, total, timestamp) VALUES
                                                   (1, 1000, '2021-05-15 10:00:00'),
                                                   (1, 1200, '2021-06-20 11:00:00'),
                                                   (1, 1300, '2021-07-25 12:00:00'),
                                                   (2, 1100, '2022-05-15 10:00:00'),
                                                   (2, 1400, '2022-06-20 11:00:00'),
                                                   (2, 1500, '2022-07-25 12:00:00'),
                                                   (2, 1600, '2020-05-15 10:00:00'),
                                                   (2, 1700, '2020-06-20 11:00:00'),
                                                   (2, 1800, '2020-07-25 12:00:00');


INSERT INTO OrderDetails (order_id, product_id, quantity, price) VALUES
                                                                     (1, 1, 5, 200),
                                                                     (1, 2, 3, 100),
                                                                     (1, 3, 2, 50),
                                                                     (2, 1, 6, 200),
                                                                     (2, 2, 4, 100),
                                                                     (2, 3, 2, 50),
                                                                     (3, 1, 7, 200),
                                                                     (3, 2, 3, 100),
                                                                     (3, 3, 1, 50),
                                                                     (4, 1, 8, 200),
                                                                     (4, 2, 3, 100),
                                                                     (4, 3, 2, 50),
                                                                     (5, 1, 6, 200),
                                                                     (5, 2, 5, 100),
                                                                     (5, 3, 4, 50),
                                                                     (6, 1, 4, 200),
                                                                     (6, 2, 6, 100);





ALTER TABLE Products
    ADD CONSTRAINT fk_products_categories
        FOREIGN KEY (category_id)
            REFERENCES Categories(category_id)
            ON DELETE SET NULL
            ON UPDATE CASCADE;
ALTER TABLE Orders
    ADD CONSTRAINT fk_orders_users
        FOREIGN KEY (user_id)
            REFERENCES Users(user_id)
            ON DELETE CASCADE
            ON UPDATE CASCADE;

ALTER TABLE OrderDetails
    ADD CONSTRAINT fk_orderdetails_orders
        FOREIGN KEY (order_id)
            REFERENCES Orders(order_id)
            ON DELETE CASCADE
            ON UPDATE CASCADE;

ALTER TABLE OrderDetails
    ADD CONSTRAINT fk_orderdetails_products
        FOREIGN KEY (product_id)
            REFERENCES Products(product_id)
            ON DELETE CASCADE
            ON UPDATE CASCADE;
ALTER TABLE Orders MODIFY COLUMN `timestamp` TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
