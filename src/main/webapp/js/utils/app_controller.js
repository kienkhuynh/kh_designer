/**
 * Hash change manager capturing/notifying all the hash changes
 * 
 * @author Kien Huynh
 */
define(["dojo/_base/declare", "dojo/_base/array", "dojo/_base/lang", "dojo/hash", "dojo/topic",
		"dojo/dom-construct", "dojo/on", "dojo/_base/lang", "./service_factory"],
		function(declare, array, lang, hash, topic, domConstruct, on, lang, ServiceFactory)
{ 
	var AppController = declare("utils.AppController", null, 
	{
		constructor: function() {
			this.inherited(arguments);
			this._previousHashName = "home";
			this._shopping_card_items = document.getElementById("numberOfItems")
		},
		postCreate: function()
		{
			this.inherited(arguments);
		},
		startup: function()
		{
			this.inherited(arguments);
			
			var self = this;
			topic.subscribe("/dojo/hashchange", function(hashName){
				self._notify(hashName)
			});
			self._notify(this._get_hash());
		},
		_get_hash: function() {
			var hash = window.location.hash.replace("#","");
			
			if (hash != "") {
				var parameterIndex = hash.indexOf("?");
				if (parameterIndex > 0) {
					hash = hash.substr(0, parameterIndex);
				} else if (parameterIndex == 0) {
					hash = null;
				}
			}
			if (!!hash && hash != "") {
				hash = hash.toLowerCase();
			}
			return hash;
		},
	    _notify: function(hashName, forceLoad) {
	    	var self = this;
	    	ServiceFactory.instance().get_shopping_service().get_cart_item_count(function(data){
	    		self._shopping_card_items.innerHTML = "(" + data + " items)";
	    	});
	    	if (hashName != this._previousHashName || forceLoad == true) {
	    		if (hashName == "home") {
	    			document.getElementById(this._previousHashName).style.display = "none";
	        		currentNode = document.getElementById(hashName).style.display = "block";
	    		} else if (hashName == "shoppingcart") {
	    			this._build_shopping_cart(hashName);
	    		} else if (hashName == "signin") {
	    			this._build_sigin_page(hashName);
	    		} else {
	    			this._build_product_page(hashName);
	    		}
	    		this._previousHashName = hashName;
	    	}
	    },
	    _build_shopping_cart: function(hashName) {
	    	document.getElementById("loading").style.display == "block";
    		document.getElementById(this._previousHashName).style.display = "none";
    		
    		currentNode = document.getElementById(hashName);
    		domConstruct.empty(currentNode);
    		var self = this;
	    	ServiceFactory.instance().get_shopping_service().get_current(function(data){
    			document.getElementById("loading").style.display == "none";
    			currentNode.style.display = "block";

    			if (data && data.orderByItems) {
        			for (var i = 0; i < data.orderByItems.length; i++) {
        				var orderItem = data.orderByItems[i];
        				var inventory = orderItem.inventory;
        				var itemContainer = self._new_node(currentNode, null, "itemContainer", inventory.inventoryId);
        				self._new_node(itemContainer, (i + 1) + "");
        				self._new_img(itemContainer, "/product/" + inventory.styleCode + ".jpg");
        				self._new_node(itemContainer, "Quantity: ", null, null);
        				var quantity = self._new_node(itemContainer, null, null, "quantity_" + inventory.inventoryId, "input");
        				quantity.value = orderItem.quantity;
        				var update = self._new_node(itemContainer, "Update", "button");
        				on(update, "click", lang.hitch(self, "_update_item_quantity"));
        			}
    			} else {
    				self._new_node(currentNode, "Cart is currently empty", "emptyCart");
    			}
	    	});
	    },
	    _build_sigin_page: function(hashName) {
    		document.getElementById(this._previousHashName).style.display = "none";
    		currentNode = document.getElementById(hashName);
    		domConstruct.empty(currentNode);
    		currentNode.style.display = "block";
    		
    		this._new_node(currentNode, "Not yet implemented");
	    },
	    _build_product_page: function(hashName) {
	    	document.getElementById("loading").style.display == "block";
    		document.getElementById(this._previousHashName).style.display = "none";
    		
    		currentNode = document.getElementById(hashName);
    		domConstruct.empty(currentNode);

    		var self = this;
    		ServiceFactory.instance().get_inventory_service().get_items(hashName, function(data){
    			document.getElementById("loading").style.display == "none";
    			currentNode.style.display = "block";
    			for (var i = 0; i < data.length; i++) {
    				for (var j = 0; j < data[i].inventories.length; j++) {
    					var inventory = data[i].inventories[j];

    					var node = self._new_node(currentNode, null, "thumbnail", inventory.styleCode);
	    				node.setAttribute("inventory_id", inventory.inventoryId);
	    				
	    				self._new_node(node, data[i].name, "label")
	    				self._new_img(node, "/product/" + node.id + ".jpg", null);

	    				var onsale = inventory.salePrice != inventory.originalPrice;
	    				if (onsale) {
		    				self._new_node(node, "$" + inventory.salePrice, "salePrice");
	    				}

	    				self._new_node(node, "$" + inventory.originalPrice, onsale ? "strikethrough" : "originalPrice");
	    				
	    				if (inventory.quantity > 0) {
	    					var addToCart = self._new_node(node, "Add To Cart", "button");
		    				on(addToCart, "click", lang.hitch(self, "_add_to_cart"));
	    				} else {
	    					self._new_node(node, "Item is no longer available");
	    				}
    				}
    			}
    		});
	    },
	    _update_item_quantity: function(event) {
	    	var inventoryId = event.target.parentElement.id;
	    	var input = document.getElementById("quantity_" + inventoryId);
	    	var self = this;
	    	ServiceFactory.instance().get_shopping_service().update_item_quantity(inventoryId, input.value, function() {
	    		if (input.value == 0) {
		    		self._notify(self._get_hash(), true);
	    		}
	    	});
	    },
	    _add_to_cart: function(event) {
	    	var inventoryId = event.target.parentElement.getAttribute("inventory_id")
	    	var self = this;
	    	ServiceFactory.instance().get_shopping_service().add_order(inventoryId, function(data) {
	    		self._shopping_card_items.innerHTML = "(" + data.orderByItems.length + " items)";
	    	});
	    },
	    _new_node: function(parent, text, className, id, tag) {
	    	var node = (!tag) ? domConstruct.create("div") : domConstruct.create(tag);
	    	if (text) node.innerHTML = text;
	    	if (id) node.id = id;
	    	if (className) node.className = className;
	    	domConstruct.place(node, parent);
	    	return node;
	    },
	    _new_img: function(parent, src, className) {
	    	var img = domConstruct.create("img");
			img.src = src;
			if (className) img.className = className;
			domConstruct.place(img, parent);
	    }
	});
	
	AppController.instance = function() {
		if (!AppController._instance) {
			AppController._instance = new AppController();
			AppController._instance.startup();
		}
		return AppController._instance;
	};
	return AppController;
});