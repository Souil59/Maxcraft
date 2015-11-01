package fr.maxcraft.player.magicraft;

import java.lang.reflect.Field;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.DataWatcher;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;


import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class ManaDisplay {

	public static void sendPacket(Player player, Mage mage){
		EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
		entityPlayer.playerConnection.sendPacket(getMobPacket(player,mage));
		entityPlayer.playerConnection.sendPacket(destroy());
	}
		@SuppressWarnings("deprecation")
		public static PacketPlayOutSpawnEntityLiving getMobPacket(Player p, Mage mage){
			Location loc = p.getLocation().add(p.getLocation().getDirection().multiply(2));
			PacketPlayOutSpawnEntityLiving mobPacket = new PacketPlayOutSpawnEntityLiving();
			try {
				Field a = mobPacket.getClass().getDeclaredField("a");
		        a.setAccessible(true);
				a.set(mobPacket, (int) 63);
				
				Field b = mobPacket.getClass().getDeclaredField("b");
		        b.setAccessible(true);
				b.set(mobPacket, (byte) EntityType.WITHER.getTypeId());
				
				Field c = mobPacket.getClass().getDeclaredField("c");
		        c.setAccessible(true);
				c.set(mobPacket, (int) Math.floor(loc.getBlockX() * 32.0D));
				
				Field d = mobPacket.getClass().getDeclaredField("d");
		        d.setAccessible(true);
				d.set(mobPacket, (int) Math.floor(loc.getBlockY() * 32.0D));
				
				Field e = mobPacket.getClass().getDeclaredField("e");
		        e.setAccessible(true);
				e.set(mobPacket, (int) Math.floor(loc.getBlockZ() * 32.0D));
				
				Field f = mobPacket.getClass().getDeclaredField("f");
		        f.setAccessible(true);
				f.set(mobPacket, (byte) 0);
				
				Field g = mobPacket.getClass().getDeclaredField("g");
		        g.setAccessible(true);
				g.set(mobPacket, (byte) 0);
				
				Field h = mobPacket.getClass().getDeclaredField("h");
		        h.setAccessible(true);
				h.set(mobPacket, (byte) 0);
				
				Field i = mobPacket.getClass().getDeclaredField("i");
		        i.setAccessible(true);
				i.set(mobPacket, (byte) 0);
				
				Field j = mobPacket.getClass().getDeclaredField("j");
		        j.setAccessible(true);
				j.set(mobPacket, (byte) 0);
				
				Field k = mobPacket.getClass().getDeclaredField("k");
		        k.setAccessible(true);
				k.set(mobPacket, (byte) 0);
				
				Field l = mobPacket.getClass().getDeclaredField("l");
				l.setAccessible(true);
				l.set(mobPacket, getWatcher(mage));
			
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			return mobPacket;
		}
		public static DataWatcher getWatcher(Mage mage){
		
			DataWatcher watcher = new DataWatcher(null);
			watcher.a(0, (byte) 0x20);
			watcher.a(2, ChatColor.DARK_PURPLE+"Mana :");
			watcher.a(3, (byte) 1);
			watcher.a(6, (float)mage.getMana()/mage.getMaxMana()*300);
			watcher.a(20, (int) 1000);
			return watcher;
		}
		
		public static PacketPlayOutEntityDestroy destroy(){
			return new PacketPlayOutEntityDestroy(new int[] {63});
		}
}
