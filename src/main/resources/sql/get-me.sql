SELECT t1.customer_id as id, concat(t2.last_name, ' ' , t2.first_name) as name, t1.status
FROM customer t1 
	JOIN customer_info t2 USING(customer_id)
WHERE t1.customer_id = ?
