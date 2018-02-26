/**
 * Handling all REST calls
 * 
 * @author Kien Huynh
 */
define(["dojo/_base/declare", "./base_service"],
		function(declare, BaseService)
{ 
	return declare("utils.InventoryService", BaseService, 
	{
		constructor: function() {
			this.inherited(arguments);
		},
		postCreate: function()
		{
			this.inherited(arguments);
		},
	    get_current: function(callback) {
	    	this._get(window.location.origin + "/resources/customerorders/current", callback);
	    },
	    add_order: function(inventoryId, callback) {
	    	this._post(window.location.origin + "/resources/customerorders/order/" + inventoryId, null, callback);
	    },
	    update_item_quantity(inventoryId, quantity, callback) {
	    	this._put(window.location.origin + "/resources/customerorders/order/" + inventoryId + "?quantity=" + quantity, null, callback);
	    }
	});
});