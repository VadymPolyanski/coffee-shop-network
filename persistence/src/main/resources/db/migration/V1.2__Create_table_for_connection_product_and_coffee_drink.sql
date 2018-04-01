-- Create a join table for coffee drinks that consist of products.
DROP TABLE IF EXISTS coffee_drinks_products;

CREATE TABLE coffee_drinks_products (
  coffee_drink_name  VARCHAR(255) REFERENCES coffee_drinks(name)
                    ON UPDATE CASCADE
                    ON DELETE CASCADE,
  product_name    VARCHAR(255) REFERENCES products(name)
                    ON UPDATE CASCADE
                    ON DELETE CASCADE,
  PRIMARY KEY (coffee_drink_name, product_name)
);
