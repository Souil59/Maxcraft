package fr.maxcraft.server.economy.merchant;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import fr.maxcraft.utils.ItemStackCreator;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IMerchant;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.MerchantRecipe;
import net.minecraft.server.v1_8_R3.MerchantRecipeList;

public class Merchant implements IMerchant{
	
	private EntityPlayer player;
	private MerchantRecipeList recipe = new MerchantRecipeList();
	

	@Override
	public void a(MerchantRecipe arg0) {
		
	}

	@Override
	public void a_(EntityHuman arg0) {
		
	}

	@Override
	public void a_(ItemStack arg0) {
		
	}

	@Override
	public MerchantRecipeList getOffers(EntityHuman arg0) {
		return this.recipe;
	}

	@Override
	public IChatBaseComponent getScoreboardDisplayName() {
		return null;
	}

	@Override
	public EntityHuman v_() {
		return this.player;
	}

	public Merchant setRecipe(MerchantRecipeList recipe) {
		this.recipe = recipe;
		return this;
	}

	public Merchant setPlayer(Player player) {
		this.player = ((CraftPlayer)player).getHandle();
		return this;
	}

	public void open() {
		this.player.openTrade(this);
		
	}


	public void add(org.bukkit.inventory.ItemStack item, int i) {
		ItemStackCreator prix = new ItemStackCreator(Material.EMERALD,"Prix",Arrays.asList(""),i,0);
		MerchantRecipe r = new MerchantRecipe(CraftItemStack.asNMSCopy(prix),CraftItemStack.asNMSCopy(item));
		this.recipe.add(r);
	}

}
