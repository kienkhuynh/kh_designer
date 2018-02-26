insert into customer (email, full_name, password) 
	values('kien.huynh@gmail.com', 'Kien Huynh', 'zhr4LA==');
	
	
insert into item (name, search_keywords)
	values('T-shirt', '|men|t-shirt|');
insert into item (name, search_keywords)
	values('T-shirt', '|woman|women|t-shirt|');
insert into item (name, search_keywords)
	values('T-shirt', '|kids|kid|t-shirt|');

insert into inventory(quantity,style_code,item_id,original_price,sale_price)
    values(1000,  'men-small-white',3,30,30);
insert into inventory(quantity,style_code,item_id,original_price,sale_price)
    values(1000, 'men-medium-pink',3,30,30);
insert into inventory(quantity,style_code,item_id,original_price,sale_price)
    values(1000, 'men-large-black',3,30,30);

insert into inventory(quantity,style_code,item_id,original_price,sale_price)
    values(1000, 'women-small-blue',3,40,30);
insert into inventory(quantity,style_code,item_id,original_price,sale_price)
    values(1000, 'women-small-white',3,40,40);
insert into inventory(quantity,style_code,item_id,original_price,sale_price)
    values(1000, 'women-medium-red',3,30,30);
insert into inventory(quantity,style_code,item_id,original_price,sale_price)
    values(1000, 'women-large-navy',3,30,30);

insert into inventory(quantity,style_code,item_id,original_price,sale_price)
    values(1000, 'kids-small-blue',3,30,15);
insert into inventory(quantity,style_code,item_id,original_price,sale_price)
    values(1000, 'kids-small-red',3,30,15);
insert into inventory(quantity,style_code,item_id,original_price,sale_price)
    values(1000, 'kids-small-yellow',3,30,15);

 
