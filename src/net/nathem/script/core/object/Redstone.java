package net.nathem.script.core.object;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import net.nathem.script.core.Option;
import net.nathem.script.core.NathemWorld;
import net.nathem.script.core.sign.Sign;
import net.nathem.script.enums.ObjectType;

public class Redstone extends Block {

	public Redstone() {
		super();
		
	}

	public Redstone(Sign sign, NathemWorld nathemWorld) {
		super(sign, nathemWorld);
		this.material = new ItemStack(Material.REDSTONE_BLOCK);
	}

	@Override
	public HashMap<String, Option> getOptionsList() {
		HashMap<String, Option> options = super.getOptionsList();
		options.remove("block");
		return options;
	}

	@Override
	public ObjectType getObjectType() {
		return ObjectType.REDSTONE;
	}
	
	

}
