-- Customer table stores customer full name/email/address/postal_code/gender
-- Only for demonstration purpose 

CREATE TABLE customer
(
    customer_id serial NOT NULL PRIMARY KEY,
    email varchar(128) NOT NULL,
    full_name varchar(128) NOT NULL,
    password varchar(128),
    CONSTRAINT unique_email UNIQUE (email)
)
;
-- Item table stores information about a merchandise
-- Only for demonstration purpose 
CREATE TABLE item
(
    item_id serial NOT NULL PRIMARY KEY,
    name varchar(128) NOT NULL,
    search_keywords varchar(128) NOT NULL
)
;
-- Mechandise inventories: available unit, mix_quantity before restocking, original price vs sale price
-- Only for demonstration purpose 
CREATE TABLE inventory
(
    inventory_id serial NOT NULL PRIMARY KEY,
    quantity integer,
    style_code varchar(32) NOT NULL,
    item_id integer NOT NULL,
    original_price double precision NOT NULL,
    sale_price double precision NOT NULL,
    CONSTRAINT item_id_fkey FOREIGN KEY (item_id)
        REFERENCES public.item (item_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

;
-- List of items order by a customer in one order
-- Only for demonstration purpose 
CREATE TABLE customer_order
(
    customer_order_id serial NOT NULL PRIMARY KEY,
    customer_id integer NOT NULL,
    process_status boolean,
    CONSTRAINT customer_id_fkey FOREIGN KEY (customer_id)
        REFERENCES customer (customer_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
) 
;

-- Mechandise ordered item quantity
-- Only for demonstration purpose 
CREATE TABLE order_by_item
(
    item_order_id serial NOT NULL PRIMARY KEY,
    inventory_id integer NOT NULL,
    customer_order_id integer NOT NULL,
    quantity integer,
    CONSTRAINT inventory_id_fkey FOREIGN KEY (inventory_id)
        REFERENCES public.inventory (inventory_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
     CONSTRAINT customer_order_id_fkey FOREIGN KEY (customer_order_id)
        REFERENCES customer_order (customer_order_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
	