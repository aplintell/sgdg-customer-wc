SELECT t1.user_id as id, concat(t1.last_name, ' ' , t1.first_name) as name, 
	t1.status, concat(',',group_concat(permission_id),',') as permissions
FROM user t1 
	JOIN role_permission t2 USING(role_id)
WHERE t1.user_id = ?
group by user_id

