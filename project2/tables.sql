create table doctor
(
	ssn varchar not null,
	name varchar not null,
	specialty varchar,

	primary key(ssn)
);

create table patient
(
	ssn varchar not null,
	primary_physician_ssn varchar,
	name varchar not null,
	dob varchar not null,
	address varchar,

	foreign key(primary_physician_ssn) references doctor(ssn),

	primary key(ssn)
);

create table pharmacy
(
	name varchar not null,
	address varchar not null,
	phone_number varchar not null,

	primary key(name)
);

create table drug_company
(
	name varchar not null,

	primary key(name)
);

create table supervisor
(
	email varchar not null,
	phone_number varchar not null unique,

	primary key(email)
);

create table drug
(
	trade_name varchar not null,
	drug_company_name varchar not null,
	formula varchar not null,

	foreign key(drug_company_name) references drug_company(name),

	primary key(trade_name)
);

create table prescription
(
	id int not null,
	doctor_ssn varchar not null,
	patient_ssn varchar not null,
	drug_trade_name varchar not null,
	date varchar not null,
	quantity int not null,

	foreign key(doctor_ssn) references doctor(ssn),
	foreign key(patient_ssn) references patient(ssn),
	foreign key(drug_trade_name) references drug(trade_name),

	primary key(id)
);

create table prescription_fill
(
	prescription_id int not null,
	pharmacy_name varchar not null,
	date varchar not null,

	foreign key(prescription_id) references prescription(id),
	foreign key(pharmacy_name) references pharmacy(name),

	primary key(prescription_id)
);

create table drug_sale
(
	drug_trade_name varchar not null,
	pharmacy_name varchar not null,
	price real not null,

	foreign key(drug_trade_name) references drug(trade_name),
	foreign key(pharmacy_name) references pharmacy(name),

	primary key(drug_trade_name, pharmacy_name)
);

create table contract
(
	drug_company_name varchar not null,
	pharmacy_name varchar not null,
	supervisor_email varchar not null,
	text varchar not null,
	start_date varchar not null,
	end_date varchar not null,

	foreign key(drug_company_name) references drug_company(name),
	foreign key(pharmacy_name) references pharmacy(name),
	foreign key(supervisor_email) references supervisor(email),

	primary key(drug_company_name, pharmacy_name)
);

-- patient prescription count
select name, count(*)
from patient
left join prescription on prescription.patient_ssn = patient.ssn
group by patient.name
order by name;

-- top 10 doctors with most patients
select doctor.name, count(*)
from doctor
left join patient on patient.primary_physician_ssn = doctor.ssn
group by doctor.name
order by count(*) desc
limit 10;

-- unfilled prescriptions
select * from prescription
where not exists(select * from prescription_fill where prescription_fill.prescription_id = id);

-- pharmacies with least contracts
select name, count(*)
from pharmacy
left join contract on contract.pharmacy_name = pharmacy.name
group by pharmacy.name
order by count(*) asc;

-- 5 most prescribed drug
select trade_name, count(*)
from drug
left join prescription on prescription.drug_trade_name = drug.trade_name
group by trade_name
order by count(*) desc
limit 5;
