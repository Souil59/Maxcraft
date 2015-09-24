package fr.maxcraft.server.zone;

import java.util.UUID;

import fr.maxcraft.server.economy.Account;

public interface Owner extends Account{

	String getName();

	UUID getUuid();

	boolean isActive();

}
