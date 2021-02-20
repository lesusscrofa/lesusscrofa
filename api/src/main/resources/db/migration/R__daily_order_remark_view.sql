drop view if exists daily_order_remark_view;
create or replace view daily_order_remark_view as
select 	mo.client_id    as client_id,
          c.first_name    as client_first_name,
          c.last_name     as client_last_name,
          mo.day          as day,
		cr.message      as client_message,
        dr.message      as daily_message
from meal_order mo
    inner join client c on c.id = mo.client_id
    left outer join remark cr on cr.client_id = mo.client_id and cr.day is null
    left outer join remark dr on dr.client_id = mo.client_id and dr.day = mo.day
where (cr.message is not null or dr.message is not null)
group by mo.client_id, c.first_name, c.last_name, mo.day, cr.message, dr.message;