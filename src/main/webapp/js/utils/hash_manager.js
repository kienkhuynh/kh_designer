/**
 * Hash change manager capturing/notifying all the hash changes
 * 
 * @author Kien Huynh
 */
define(["dojo/_base/declare", "dojo/_base/array", "dojo/_base/lang", "dojo/hash", "dojo/topic",
		 "./service_factory"],
		function(declare, array, lang, hash, topic, ServiceFactory)
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
	    		document.getElementById(hashName).style.display = "block";
	    		document.getElementById("loading").style.display == "none";
	    		
	    		ServiceFactory.instance().get_inventory_service().get_items();
	    		this._previousHashName = hashName;
	    	}
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