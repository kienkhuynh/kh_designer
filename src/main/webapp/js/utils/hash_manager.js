/**
 * Hash change manager capturing/notifying all the hash changes
 * 
 * @author Kien Huynh
 */
define(["dojo/_base/declare", "dojo/_base/array", "dojo/_base/lang", "dojo/hash", "dojo/topic",
		"dojo/dom-construct", "dojo/on", "./service_factory"],
		function(declare, array, lang, hash, topic, domConstruct, on, ServiceFactory)
{ 
	var HashMgr = declare("utils.HashMgr", null, 
	{
		constructor: function() {
			this.inherited(arguments);
			this._previousHashName = "home";
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
		},
	    _notify: function(hashName, data) {
	    	if (hashName != this._previousHashName) {
	    		document.getElementById("loading").style.display == "block";
	    		document.getElementById(this._previousHashName).style.display = "none";
	    		
	    		currentNode = document.getElementById(hashName);
	    		domConstruct.empty(currentNode);

	    		me = this;
	    		ServiceFactory.instance().get_inventory_service().get_items(hashName, function(data){
	    			document.getElementById("loading").style.display == "none";
	    			currentNode.style.display = "block";
	    			for (var i = 0; i < data.length; i++) {
	    				for (var j = 0; j < data[i].inventories.length; j++) {
	    					var inventory = data[i].inventories[j];
		    				var node = domConstruct.create("div");
		    				node.id = inventory.styleCode + "_item_" + data[i].itemId;
		    				node.className = "thumbnail";
		    				domConstruct.place(node, currentNode);
		    				
		    				var label = domConstruct.create("div");
		    				label.className = "label"
		    				label.innerHTML = data[i].name;
		    				domConstruct.place(label, node);
		    				
		    				var img = domConstruct.create("img");
		    				img.src = "/product/" + node.id + ".jpg";
		    				domConstruct.place(img, node);
		    				
		    				var onsale = inventory.salePrice != inventory.originalPrice;
		    				if (onsale) {
			    				var salePrice = domConstruct.create("div");
			    				salePrice.innerHTML = "$" + inventory.salePrice;
			    				salePrice.className = "salePrice";
			    				domConstruct.place(salePrice, node);
		    				}

		    				var price = domConstruct.create("div");
		    				price.innerHTML = "$" + inventory.originalPrice;
		    				price.className = onsale ? "strikethrough" : "originalPrice";
		    				domConstruct.place(price, node);
		    				
		    				if (inventory.quantity > 0) {
		    					var addToCart = domConstruct.create("div");
			    				addToCart.innerHTML = "Add To Cart";
			    				addToCart.className = "button";
			    				domConstruct.place(addToCart, node);
			    				on(addToCart, "click", me._addToCart);
		    				} else {
		    					var unavail = domConstruct.create("div");
		    					unavail.innerHTML = "Item is no longer available";
		    					domConstruct.place(unavail, node);
		    				}
	    				}
	    			}
	    		});
	    		this._previousHashName = hashName;
	    	}
	    },
	    _addToCart: function(event) {
	    	console.log(event);
	    }
	});
	
	HashMgr.instance = function() {
		if (!HashMgr._instance) {
			HashMgr._instance = new HashMgr();
			HashMgr._instance.startup();
		}
		return HashMgr._instance;
	};
	return HashMgr;
});