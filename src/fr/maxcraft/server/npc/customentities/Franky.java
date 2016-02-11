package fr.maxcraft.server.npc.customentities;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;

/**
 * Created by Crevebedaine on 26/01/2016.
 */
public class Franky extends  EntityWolf{

    public Franky(net.minecraft.server.v1_8_R3.World w) {
        super(w);
        this.persistent = true;
        NBTTagCompound tag = new NBTTagCompound();
        this.c(tag);
        tag.setBoolean("Invulnerable", true);
        this.f(tag);
        this.setCustomNameVisible(true);
        this.setCustomName(ChatColor.GREEN+"Franky");
        this.goalSelector = new PathfinderGoalSelector(w != null && w.methodProfiler != null?w.methodProfiler:null);
        this.goalSelector.a(2, new PathfinderGoalFollowOwner(this, 1.5D, 5.0F, 2.0F));
        this.goalSelector.a(3, new PathfinderGoalBreed(this, 1.0D));
        this.goalSelector.a(4, new PathfinderGoalRandomStroll(this, 1.0D));
        this.goalSelector.a(5, new net.minecraft.server.v1_8_R3.PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        this.goalSelector.a(6, new PathfinderGoalRandomLookaround(this));
        this.goalSelector.a(1, new PathfinderGoalTempt(this, 1.2D, Items.CARROT, false));
    }

    public static Franky spawn(Player p){
        Franky e = new Franky(((CraftWorld) p.getWorld()).getHandle());
        e.setOwnerUUID(p.getUniqueId()+"");
        EntityTypes.spawnEntity(e,p.getLocation());
        return e;

    }

    @Override
    public boolean isInvulnerable(DamageSource damagesource) {
        return damagesource != DamageSource.OUT_OF_WORLD;
    }

    //ZQSD controle
    @Override
    public void g(float sideMot, float forMot) {
        if(this.passenger != null && this.passenger.equals(this.getOwner())) {
            this.lastYaw = this.yaw = this.passenger.yaw;
            this.pitch = this.passenger.pitch * 0.5F;
            this.setYawPitch(this.yaw, this.pitch);
            this.aI = this.aG = this.yaw;
            sideMot = ((EntityLiving)this.passenger).aZ * 0.5F;
            forMot = ((EntityLiving)this.passenger).ba;
            if(forMot <= 0.0F) {
                forMot *= 0.25F;
            }

            this.S = 1.0F;
            this.aM = this.bI() * 0.1F;
            if(!this.world.isClientSide) {
                this.k((float)this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue());
                super.g(sideMot, forMot);
            }


            this.ay = this.az;
            double d0 = this.locX - this.lastX;
            double d1 = this.locZ - this.lastZ;
            float f4 = MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0F;
            if(f4 > 1.0F) {
                f4 = 1.0F;
            }

            this.az += (f4 - this.az) * 0.4F;
            this.aA += this.az;
        } else {
            super.g(sideMot, forMot);
        }


    }
    protected void initAttributes() {
        super.initAttributes();
        this.getAttributeInstance(GenericAttributes.maxHealth).setValue(200.0D);
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.25D);
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setBoolean("Saddle", this.hasSaddle());
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.setSaddle(nbttagcompound.getBoolean("Saddle"));
    }

    protected String z() {
        return "mob.pig.say";
    }

    protected String bo() {
        return "mob.pig.say";
    }

    protected String bp() {
        return "mob.pig.death";
    }

    protected void a(BlockPosition blockposition, Block block) {
        this.makeSound("mob.pig.step", 0.15F, 1.0F);
    }

    public boolean a(EntityHuman entityhuman) {
        ItemStack itemstack = entityhuman.inventory.getItemInHand();
        if (itemstack==null) {
            entityhuman.mount(this);
            return true;
        }
        if (itemstack.getItem()==Items.SADDLE) {
            this.setSaddle(!hasSaddle());
            return true;
        }
        if(!this.hasSaddle() || this.world.isClientSide || this.passenger != null && this.passenger != entityhuman) {
            return false;
        } else {
            entityhuman.mount(this);
            return true;
        }
    }

    public boolean hasSaddle() {
        return (this.datawatcher.getByte(16) & 1) != 0;
    }

    public void setSaddle(boolean flag) {
        if(flag) {
            this.datawatcher.watch(16, Byte.valueOf((byte)1));
        } else {
            this.datawatcher.watch(16, Byte.valueOf((byte)0));
        }

    }

    public boolean d(ItemStack itemstack) {
        return itemstack != null && itemstack.getItem() == Items.CARROT;
    }

    public EntityAgeable createChild(EntityAgeable entityageable) {
        return this.b(entityageable);
    }

    public boolean isSitting() {
        return false;
    }
    public boolean isTamed() {
        return true;
    }
}
