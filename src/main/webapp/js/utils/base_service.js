define(["dojo/_base/declare","dojo/request/xhr", "dojo/_base/array"], 
		function(declare, xhr, array)
{
	return declare("util.BaseService", null, 
	{
		constructor: function() {
		},
		postCreate: function()
		{
		}, 
		query: function(url, queryParameters, queryValues, callback) {
			array.forEach(queryParameters, function(name, i) {
				if (i == 0) {
					url = url + "?";
				}
				url = url + name + "=" + queryValues[i] + "&";
			});
			this._get(url, callback);
		},
		preventCache: function(url) {
			if (dojo.isIE > 0) {
				var index = url.indexOf("?");
				if (index < 0) {
					return url + "?preventCache=" + new Date().getTime();
				} else if (index < url.length) {
					return url + "&preventCache=" + new Date().getTime();
				} else {
				    return url + "preventCache=" + new Date().getTime();
				}
			} else {
				return url;
			}
		},
		_get: function (url, callback) {
			this._base(this.preventCache(url), null, callback, "GET");
		},
		_post: function (url, data, callback) {
			this._base(url, data, callback, "POST");
		},
        _put: function (url, data, callback) {
            this._base(url, data, callback, "PUT");
        },
		_delete: function (url, data, callback) {
			this._base(url, data, callback, "DELETE");
		},
		_base: function (url, data, callback, method) {
			xhr(url, {
				method: method,
				sync: false,
				handleAs: "json",
				headers: {"Content-Type": "application/json"},
				data: data
			}).then(function(data) {
				if (!!callback) {
				    if (data && data.redirectUrl) {
				        window.location = data.redirectUrl;
				    }
					callback(data, null, url);
				}
			}, function(error){
				if (!!callback) {
					callback(null, error, url);
				}
			}, function(evt){
			    // Handle a progress event from the request if the
			    // browser supports XHR2
				console.log(evt);
			});
		}
	});
});