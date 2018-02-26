/**
 * A common util to create different types of AJAX service.
 * 
 * @author Kien Huynh
 */
define(["dojo/_base/declare", "./item_inventory_service", "./shopping_cart_service", "./base_service"], 
		function(declare, InventoryService, ShoppingService, BaseService)
{
	var ServiceFactory = declare("util.ServiceFactory", null, 
	{
		constructor: function(type) {
			this.inherited(arguments);
		},
		get_inventory_service: function() {
			if (!this._inventory_service) {
				this._inventory_service = new InventoryService();
			}
			return this._inventory_service;
		},
		get_shopping_service: function() {
			if (!this._shopping_service) {
				this._shopping_service = new ShoppingService();
			}
			return this._shopping_service;
		},
		get_base_service: function() {
			if (!this._base_service) {
				this._base_service = new BaseService();
			}
			return this._base_service;
		}
	});
	
	ServiceFactory.instance = function() {
		if (!ServiceFactory._instance) {
			ServiceFactory._instance = new ServiceFactory(); 
		}
		return ServiceFactory._instance;
	};
	return ServiceFactory;
});