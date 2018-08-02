SELECT  
	COUNT(*)
FROM 
	customer_info t1 
	JOIN customer t2 USING(customer_id)
WHERE
	(? = '%%' OR t2.login_id like ?)
    AND (? = '%%' OR t1.first_name like ?)
    AND (? = '%%' OR t1.last_name like ?)
    AND (? = '%%' OR t1.email like ?)
    AND (t2.is_del = 0)