SELECT  
	t2.customer_id,	t2.login_id, t2.status , t1.last_name, t1.first_name, t1.email 
FROM 
	customer_info t1 
	JOIN customer t2 USING(customer_id)
WHERE
	? = '%%' OR t2.login_id like ?
    AND ? = '%%' OR t1.first_name like ?
    AND ? = '%%' OR t1.last_name like ?
    AND ? = '%%' OR t1.email like ?
LIMIT 
	?,?