package fr.maxcraft.server.customentities;

import fr.maxcraft.server.customentities.custompathfinder.PathfinderGoalLookAtPlayer;
import fr.maxcraft.server.customentities.custompathfinder.PathfinderGoalWalkToLoc;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

/**
 * Created by Crevebedaine on 26/01/2016.
 */
public class EntityNPC  extends EntityAgeable implements NPC {

    public EntityNPC(World world) {
        super(world);
        NBTTagCompound tag = new NBTTagCompound();
        this.c(tag);
        tag.setBoolean("Invulnerable", true);
        this.f(tag);
        this.goalSelector.a(0, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 5.0F, 1.0F));
    }

    public void setDestination(Location l){
        this.goalSelector.a(5,new PathfinderGoalWalkToLoc(this,l,1));
    }

    public static EntityNPC spawn(Location l){
        EntityNPC e = new EntityNPC(((CraftWorld)l.getWorld()).getHandle());
        EntityTypes.spawnEntity(e,l);
        return e;

    }

    @Override
    public EntityAgeable createChild(EntityAgeable entityAgeable) {
        EntityVillager entityvillager = new EntityVillager(this.world);
        entityvillager.prepare(this.world.E(new BlockPosition(entityvillager)), (GroupDataEntity)null);
        return entityvillager;
    }
}
