/**
 * A common util to create different types of AJAX service.
 * 
 * @author Kien Huynh
 */
define(["dojo/_base/declare", "./item_inventory_service"], 
		function(declare, InventoryService)
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