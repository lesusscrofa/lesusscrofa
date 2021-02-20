drop view if exists client_with_delivery_detail_view;
create or replace view client_with_delivery_detail_view as
    select c.*, dm.id as delivery_man_id, dm.first_name as delivery_man_first_name, dm.last_name as delivery_man_last_name, dz.name as delivery_zone_name
    from client c
    inner join delivery_zone dz on dz.id = c.delivery_zone_id
    inner join delivery_man dm on dz.delivery_man_id = dm.id
    ;
