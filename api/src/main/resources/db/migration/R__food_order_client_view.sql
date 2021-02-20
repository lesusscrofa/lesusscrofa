drop view if exists food_order_client_view;
create or replace view food_order_client_view as
    select
        mo.client_id as client_id,
        f.id as food_id, f.name as food_name, mo.day as order_day, f.service as food_service, sum(mo.quantity) as quantity
    from food f
             inner join meal_order mo on mo.soup_id = f.id or mo.dish_id = f.id or mo.dessert_id = f.id
    group by mo.client_id, mo.day, f.id, f.name, f.service;