package fr.maxcraft.server.customentities.customplayer;

import com.mojang.authlib.GameProfile;
import fr.maxcraft.server.customentities.EntityTypes;
import net.minecraft.server.v1_8_R3.EntityHuman;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

import java.util.UUID;

/**
 * Created by Crevebedaine on 25/01/2016.
 */
public class CustomPlayer extends EntityHuman{

    public CustomPlayer(World world) {
        super(((CraftWorld)world).getHandle(),new GameProfile(UUID.randomUUID(),"test"));
    }

    @Override
    public boolean isSpectator() {
        return false;
    }
    public static CustomPlayer spawn(Location l){
        CustomPlayer e = new CustomPlayer(l.getWorld());
        EntityTypes.spawnEntity(e,l);
        return e;
    }
}