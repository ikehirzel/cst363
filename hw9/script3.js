db.order_count.drop();

function orders_map() {
	var item_count = 0;

	for (const item of this.items)
		item_count += item.qty;

	emit(this.customer, {
		zip: null,
		count: item_count
	});
}

function customers_map() {
	emit(this.customerId, {
		zip: this.address.zip,
		count: 0
	});
}

var reduce = function(key, values) {
	var result = { zip: null, count: 0 };		

	for (const value of values)
	{
		if (result.zip == null && value.zip !== null)
			result.zip = value.zip;

		result.count += value.count;
	}

	return result;
}

db.customers.mapReduce(customers_map, reduce, {
	out: { reduce: "order_count" }
});

db.orders.mapReduce(orders_map, reduce, {
	out: { reduce: "order_count" }
});


var cursor = db.order_count.find();

while (cursor.hasNext())
	printjson(cursor.next().value);
