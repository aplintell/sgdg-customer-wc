SELECT  
	t1.customer_id, 
	t1.login_id, 
	t1.status, 
	t2.last_name, 
	t2.first_name, 
	t2.email,
	t2.gender,
	t2.dob,
	t2.address,
	t2.avatar,
	t2.front_id_card,
	t2.back_id_card,
	t2.point,
	t2.customer_type,
	t2.phone
FROM 
	customer t1 
	JOIN customer_info t2 USING(customer_id) 
WHERE 
	t1.customer_id = ? 