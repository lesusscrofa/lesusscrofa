drop view if exists order_with_price_view;
create or replace view order_with_price_view as
select 	fp.id as id,
        '' as name,
        o.client_id as client_id,
        o.day as day,
		o.formula as formula,
		fp.price as price,
		fp.vat as vat,
		o.quantity as quantity,
		'pc.' as unit
from meal_order o
join formula_price fp on fp.formula = o.formula and fp.start_date = (SELECT MAX(fpm.start_date) AS min_start_date
    FROM formula_price fpm
    WHERE fpm.start_date <= o.day and fpm.formula = o.formula
    GROUP BY fpm.formula)
where o.formula <> 'OTHER'
union
select 	f.id as id,
          f.name as name,
          o.client_id as client_id,
          o.day as day,
		o.formula as formula,
		f.price as price,
		f.vat as vat,
		o.quantity as quantity ,
		f.unit as unit
from meal_order o
    join food f on f.id = o.other_id
where o.formula = 'OTHER';