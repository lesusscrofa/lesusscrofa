CREATE TABLE IF NOT EXISTS client (
	id serial primary key not null,
	first_name text not null,
	last_name text not null,
	delivery_street text,
    delivery_zip_code integer,
    delivery_city text,
    delivery_zone_id integer not null,
	delivery_phone text,
	billing_street text,
    billing_zip_code integer,
    billing_city text,
    billing_phone text,
    delivery_position integer,
    reduction integer,
    delivery_preference_take_away boolean not null,

    unique(delivery_position) DEFERRABLE INITIALLY DEFERRED
);

CREATE TABLE IF NOT EXISTS food (
	id serial primary key not null,
	name text not null,
	service text not null,
	start_date date,
	end_date date,
	price real,
	vat integer,
    unit varchar(10)
);

CREATE TABLE IF NOT EXISTS meal_order (
	id serial primary key not null,
	client_id integer not null,

	soup_id integer,
    dish_id integer,
    dessert_id integer,
    other_id integer,

    formula varchar not null,
	day date not null,
	quantity integer not null,

	delivery boolean not null,

	unique(client_id, day, soup_id, dish_id, dessert_id, other_id)
);

ALTER TABLE meal_order
    add constraint fk_meal_order_client_id foreign key (client_id) REFERENCES client (id);

ALTER TABLE meal_order
    add constraint fk_meal_order_soup_id foreign key (soup_id) REFERENCES food (id);

ALTER TABLE meal_order
    add constraint fk_meal_order_dish_id foreign key (dish_id) REFERENCES food (id);

ALTER TABLE meal_order
    add constraint fk_meal_order_dessert_id foreign key (dessert_id) REFERENCES food (id);

ALTER TABLE meal_order
    add constraint fk_meal_order_other_id foreign key (other_id) REFERENCES food (id);

CREATE TABLE IF NOT EXISTS formula_price (
    id serial primary key,
    start_date date not null,
    formula varchar not null,
    price real not null,
    vat integer not null,
    unique(start_date, formula)
);

CREATE TABLE IF NOT EXISTS delivery_man (
    id serial primary key,
    first_name text not null,
    last_name text not null
);

CREATE TABLE IF NOT EXISTS delivery_zone (
    id serial primary key not null,
    name text not null,
    delivery_man_id integer not null
);

ALTER TABLE delivery_zone
    add constraint fk_delivery_zone_man_id foreign key (delivery_man_id) REFERENCES delivery_man (id);

ALTER TABLE client
    add constraint fk_client_delivery_zone_id foreign key (delivery_zone_id) REFERENCES delivery_zone (id);

CREATE TABLE remark (
    id serial primary key not null,
    client_id integer not null,
    day date,
    message text not null,
    unique(client_id, day)
);

ALTER TABLE remark
    add constraint fk_remark_client_id foreign key (client_id) REFERENCES client (id);
