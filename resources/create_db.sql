-- Customer table stores customer full name/email/address/postal_code/gender
-- Only for demonstration purpose 

CREATE TABLE customer
(
    customer_id serial NOT NULL PRIMARY KEY,
    email varchar(128) NOT NULL,
    full_name varchar(128) NOT NULL,
    full_address varchar(128) NOT NULL,
    postal_code varchar(10) NOT NULL,
    gender integer,
    password varchar(128)
)
;
-- Item table stores information about a merchandise
-- Only for demonstration purpose 
CREATE TABLE item
(
    item_id serial NOT NULL PRIMARY KEY,
    name varchar(128) NOT NULL,
    search_keywords varchar(128) NOT NULL,
    sku_cpu varchar(32) NOT NULL
)
;
-- Mechandise inventories: available unit, mix_quantity before restocking, original price vs sale price
-- Only for demonstration purpose 
CREATE TABLE inventory
(
    inventory_id serial NOT NULL PRIMARY KEY,
    quantity integer,
    min_quantity integer,
    style_id integer NOT NULL,
    item_id integer NOT NULL,
    orignal_price double precision NOT NULL,
    sale_price double precision NOT NULL
)
;
-- Promotion codes and percentage
-- Only for demonstration purpose 
CREATE TABLE promotion
(
    promotion_id serial NOT NULL PRIMARY KEY,
    code varchar(32) NOT NULL,
    percentage double precision NOT NULL
)
;
-- Mechandise ordered item quantity
-- Only for demonstration purpose 
CREATE TABLE order_by_item
(
    item_order_id serial NOT NULL PRIMARY KEY,
    item_id integer NOT NULL,
    style_code integer NOT NULL,
    quantity integer,
    order_date timestamp without time zone NOT NULL,
    CONSTRAINT item_id_fkey FOREIGN KEY (item_id)
        REFERENCES item (item_id) MATCH SIMPLE
        ON DELETE NO ACTION
)
;
-- List of items order by a customer in one order
-- Only for demonstration purpose 
CREATE TABLE customer_order
(
    id serial NOT NULL PRIMARY KEY,
    customer_order_code varchar(32) NOT NULL,
    item_order_id integer NOT NULL,
    customer_id integer,
    CONSTRAINT customer_id_fkey FOREIGN KEY (customer_id)
        REFERENCES customer (customer_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT item_order_id FOREIGN KEY (item_order_id)
        REFERENCES order_by_item (item_order_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)
;
-- Customer purchase order
-- Only for demonstration purpose 
CREATE TABLE purchase_order
(
    purchase_id serial NOT NULL PRIMARY KEY,
    customer_order_code varchar(32) NOT NULL,
    sub_total double precision NOT NULL,
    total double precision NOT NULL,
    promotion_id integer,
    CONSTRAINT promotion_id FOREIGN KEY (promotion_id)
        REFERENCES promotion (promotion_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE SET NULL
)

	