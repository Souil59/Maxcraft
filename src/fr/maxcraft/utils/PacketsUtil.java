package fr.maxcraft.utils;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PacketsUtil {
	//http://wiki.vg/Protocol
/*	
	static HashMap<Player,InputSign> input = new HashMap<Player,InputSign>();
	
	public PacketsUtil(ProtocolManager protocolManager) {

		protocolManager.addPacketListener(
				  new PacketAdapter(Main.getPlugin(), 
				          PacketType.Play.Client.UPDATE_SIGN) {
				    @Override
				    public void onPacketReceiving(PacketEvent event) {
				    	if (input.containsKey(event.getPlayer()))
				    		if (event.getPacketType() == PacketType.Play.Client.UPDATE_SIGN) {
						    	ArrayList<String> ls = new ArrayList<String>();
				    			for (WrappedChatComponent m : event.getPacket().getChatComponentArrays().read(0)){
									ls.add(m.getJson().substring(1,m.getJson().length()-1));
				    			}
				    			input.get(event.getPlayer()).input(ls,event.getPlayer());
				    		}
				    	}
				});
		
	}
	*/
	public static void sendActionBar(Player player, String message){
		message = ChatColor.translateAlternateColorCodes('&', message);
        IChatBaseComponent cbc = ChatSerializer.a("{\"text\": \"" + message + "\"}");
        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc,(byte) 2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(ppoc);
    }
	/*
	@SuppressWarnings("deprecation")
	public static void openSignGUI(Player player, InputSign is,String s1,String s2,String s3,String s4){
		input.put(player, is);
        CraftPlayer p = (CraftPlayer) player;
        player.sendBlockChange(p.getWorld().getBlockAt(0,0,0).getLocation(), Material.WALL_SIGN, (byte) 0);
		IChatBaseComponent[] i = {ChatSerializer.a(s1),ChatSerializer.a(s2),ChatSerializer.a(s3),ChatSerializer.a(s4)};
		PacketPlayOutUpdateSign ppous = new PacketPlayOutUpdateSign(((CraftWorld) player.getWorld()).getHandle(), new BlockPosition(0,0,0),i);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(ppous);
        PacketPlayOutOpenSignEditor ppoose = new PacketPlayOutOpenSignEditor(new BlockPosition(0,0,0));
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(ppoose);
        
	}
	*/
}
