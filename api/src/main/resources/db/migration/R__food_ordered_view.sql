drop view if exists food_order_view;
drop view if exists food_ordered_view;
create or replace view food_ordered_view as
    select f.id as food_id, f.name as food_name, mo.day as order_day, f.service as food_service, sum(mo.quantity) as quantity
    from food f
    inner join meal_order mo on mo.soup_id = f.id or mo.dish_id = f.id or mo.dessert_id = f.id or mo.other_id = f.id
    group by f.id, f.name, mo.day, f.service;