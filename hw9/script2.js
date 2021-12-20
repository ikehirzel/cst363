db.customers.mapReduce(
	function() { emit(this.address.zip, 1); },
	function(key, values) { return values.length; },
	{
		query: { "address.zip": /^9/ },
		out: "customer_count"
	}
);

var cursor = db.customer_count.find();

while (cursor.hasNext())
	printjson(cursor.next());
