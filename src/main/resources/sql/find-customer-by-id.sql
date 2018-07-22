SELECT  
	t1.customer_id, 
	t1.login_id, 
	t1.status , 
	t2.last_name, 
	t2.first_name, 
	t2.email
FROM 
	customer t1 
	JOIN customer_info t2 USING(customer_id) 
WHERE 
	t1.customer_id = ? 