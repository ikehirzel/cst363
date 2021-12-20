function map() {
	var count = 0;

	for (const item of this.items)
		count += item.qty;

	emit(1, count);
}

function reduce(key, values) {
	var avg = Array.sum(values) / values.length;

	return avg;
}

db.orders.mapReduce(map, reduce, { out: "avg_order_qty" });
var document = db.avg_order_qty.findOne();

print(document.value);
