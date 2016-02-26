package fr.maxcraft.player.permissions.groups;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;

import fr.maxcraft.player.permissions.Group;

public class Citoyen implements Group{

	private boolean isStaff = false;
    private Group usualGroup = this;

	@Override
	public List<String> getPermissions() {
		return Arrays.asList("maxcraft.base");
	}

	@Override
	public String getPrefix() {
		return "";
	}

	@Override
	public ChatColor getColor() {
		return ChatColor.AQUA;
	}

	public boolean isStaff() {
		return isStaff;
	}

	public void setIsStaff(boolean isStaff) {
		this.isStaff = isStaff;
	}

    public Group getUsualGroup() {
        return usualGroup;
    }

    public void setUsualGroup(Group usualGroup) {
        this.usualGroup = usualGroup;
    }
}
