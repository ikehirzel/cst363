// 1 & 2
db.createCollection("patients");
db.patients.insert([
	{
		name: "John",
		ssn: "000-00-0000",
		age: 10,
		address: "123 Street Ave"
	},
	{
		name: "Alice",
		ssn: "000-00-0001",
		age: 20,
		address: "124 Street Ave"
	},
	{
		name: "Sarah",
		ssn: "000-00-0002",
		age: 30,
		address: "125 Street Ave",
		prescriptions: [
			{ id: "RX743009", tradename : "Hydrochlorothiazide" },
			{ id : "RX656003", tradename : "LEVAQUIN", formula : "levofloxacin" }
		]
	}
]);


// 3
var cursor = db.patients.find();

while (cursor.hasNext())
{
	printjson(cursor.next());
}

// 4
db.patients.findOne({ age: 20 });

// 5
db.patients.find({ age: { $lt: 25 } });

// 6
db.patients.drop();
