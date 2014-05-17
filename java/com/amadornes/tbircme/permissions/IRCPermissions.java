package com.amadornes.tbircme.permissions;

public class IRCPermissions implements IPermissions<User> {

	@Override
	public boolean canRunCommand(User user) {
		return false;
	}

}
