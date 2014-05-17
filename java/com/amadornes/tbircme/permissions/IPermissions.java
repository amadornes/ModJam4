package com.amadornes.tbircme.permissions;

public interface IPermissions<U extends IUser> {

	public boolean canRunCommand(U user);
	
}
