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
	    get_items: function(searchKey) {
	    	
	    }
	});
});