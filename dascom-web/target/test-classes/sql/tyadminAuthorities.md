findAuthMethodList
===
	select auth.auth_method from tyadmin_authorities auth, tyadmin_group_auths gpauth where auth.id = gpauth.auth_id 
	and gpauth.group_id = #groupId# and auth.servicekey = #servicekey#