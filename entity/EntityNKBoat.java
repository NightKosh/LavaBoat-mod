package LavaBoat.entity;

import LavaBoat.mod_LavaBoat;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

/*
 * LavaBoat mod
 *
 * @author NightKosh
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
public abstract class EntityNKBoat extends Entity {

    protected boolean field_70279_a;
    protected double field_70276_b;
    protected int boatPosRotationIncrements;
    protected double boatX;
    protected double boatY;
    protected double boatZ;
    protected double boatYaw;
    protected double boatPitch;
    @SideOnly(Side.CLIENT)
    protected double velocityX;
    @SideOnly(Side.CLIENT)
    protected double velocityY;
    @SideOnly(Side.CLIENT)
    protected double velocityZ;

    public EntityNKBoat(World world) {
        super(world);
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they
     * walk on. used for spiders and wolves to prevent them from trampling crops
     */
    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    protected void entityInit() {
        this.dataWatcher.addObject(17, new Integer(0));
        this.dataWatcher.addObject(18, new Integer(1));
        this.dataWatcher.addObject(19, new Integer(0));
    }

    /**
     * Returns a boundingBox used to collide the entity with other entities and
     * blocks. This enables the entity to be pushable on contact, like boats or
     * minecarts.
     */
    @Override
    public AxisAlignedBB getCollisionBox(Entity entity) {
        return entity.boundingBox;
    }

    /**
     * returns the bounding box for this entity
     */
    @Override
    public AxisAlignedBB getBoundingBox() {
        return this.boundingBox;
    }

    /**
     * Returns true if this entity should push and be pushed by other entities
     * when colliding.
     */
    @Override
    public boolean canBePushed() {
        return true;
    }

    /**
     * Returns the Y offset from the entity's position for any entity riding
     * this one.
     */
    @Override
    public double getMountedYOffset() {
        return this.height * 0.0D - 0.30000001192092896D;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource damageSource, int itemDamage, int par2) {
        if (this.isEntityInvulnerable()) {
            return false;
        } else if (!this.worldObj.isRemote && !this.isDead) {
            if (damageSource.getEntity() instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) damageSource.getEntity();
                byte emptySlot = getPlayerEmptySlot(player.inventory.mainInventory);
                if (emptySlot != -1) {
                    player.inventory.setInventorySlotContents(emptySlot, new ItemStack(mod_LavaBoat.lavaBoat, 1, itemDamage));
                    this.setDead();
                }
            } else {
                this.setForwardDirection(-this.getForwardDirection());
                this.setTimeSinceHit(10);
                this.setDamageTaken(this.getDamageTaken() + par2 * 10);
                this.setBeenAttacked();

                if (this.getDamageTaken() > 200) {
                    if (this.riddenByEntity != null) {
                        this.riddenByEntity.mountEntity(this);
                    }

                    this.entityDropItem(new ItemStack(mod_LavaBoat.lavaBoat, 1, itemDamage), 0);

                    this.setDead();
                }
            }

            return true;
        } else {
            return true;
        }
    }

    /*
     * return empty slot number
     */
    private static byte getPlayerEmptySlot(ItemStack[] items) {
        for (byte i = 0; i < items.length; i++) {
            if (items[i] == null) {
                return i;
            }
        }
        return (byte) -1;
    }

    /**
     * Setups the entity to do the hurt animation. Only used by packets in
     * multiplayer.
     */
    @SideOnly(Side.CLIENT)
    @Override
    public void performHurtAnimation() {
        this.setForwardDirection(-this.getForwardDirection());
        this.setTimeSinceHit(10);
        this.setDamageTaken(this.getDamageTaken() * 11);
    }

    /**
     * Returns true if other Entities should be prevented from moving through
     * this Entity.
     */
    @Override
    public boolean canBeCollidedWith() {
        return !this.isDead;
    }

    /**
     * Sets the position and rotation. Only difference from the other one is no
     * bounding on the rotation. Args: posX, posY, posZ, yaw, pitch
     */
    @SideOnly(Side.CLIENT)
    @Override
    public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9) {
        if (this.field_70279_a) {
            this.boatPosRotationIncrements = par9 + 5;
        } else {
            double d3 = par1 - this.posX;
            double d4 = par3 - this.posY;
            double d5 = par5 - this.posZ;
            double d6 = d3 * d3 + d4 * d4 + d5 * d5;

            if (d6 <= 1.0D) {
                return;
            }

            this.boatPosRotationIncrements = 3;
        }

        this.boatX = par1;
        this.boatY = par3;
        this.boatZ = par5;
        this.boatYaw = (double) par7;
        this.boatPitch = (double) par8;
        this.motionX = this.velocityX;
        this.motionY = this.velocityY;
        this.motionZ = this.velocityZ;
    }

    /**
     * Sets the velocity to the args. Args: x, y, z
     */
    @SideOnly(Side.CLIENT)
    @Override
    public void setVelocity(double x, double y, double z) {
        this.velocityX = this.motionX = x;
        this.velocityY = this.motionY = y;
        this.velocityZ = this.motionZ = z;
    }

    @Override
    public void updateRiderPosition() {
        if (this.riddenByEntity != null) {
            double d0 = Math.cos((double) this.rotationYaw * Math.PI / 180.0D) * 0.4D;
            double d1 = Math.sin((double) this.rotationYaw * Math.PI / 180.0D) * 0.4D;
            this.riddenByEntity.setPosition(this.posX + d0, this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ + d1);
        }
    }

    /**
     * Protected helper method to write subclass entity data to NBT.
     */
    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt) {
    }

    /**
     * Protected helper method to read subclass entity data from NBT.
     */
    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt) {
    }

    /**
     * Return shadow size
     *
     * @return float Shadow size
     */
    @SideOnly(Side.CLIENT)
    @Override
    public float getShadowSize() {
        return 0.0F;
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow,
     * gets into the saddle on a pig.
     */
    @Override
    public boolean interact(EntityPlayer player) {
        if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayer && this.riddenByEntity != player) {
        } else if (!this.worldObj.isRemote) {
            player.mountEntity(this);
        }

        return true;
    }

    /**
     * Sets the damage taken from the last hit.
     */
    public void setDamageTaken(int par1) {
        this.dataWatcher.updateObject(19, Integer.valueOf(par1));
    }

    /**
     * Gets the damage taken from the last hit.
     */
    public int getDamageTaken() {
        return this.dataWatcher.getWatchableObjectInt(19);
    }

    /**
     * Sets the time to count down from since the last time entity was hit.
     */
    public void setTimeSinceHit(int par1) {
        this.dataWatcher.updateObject(17, Integer.valueOf(par1));
    }

    /**
     * Gets the time since the last hit.
     */
    public int getTimeSinceHit() {
        return this.dataWatcher.getWatchableObjectInt(17);
    }

    /**
     * Sets the forward direction of the entity.
     */
    public void setForwardDirection(int par1) {
        this.dataWatcher.updateObject(18, Integer.valueOf(par1));
    }

    /**
     * Gets the forward direction of the entity.
     */
    public int getForwardDirection() {
        return this.dataWatcher.getWatchableObjectInt(18);
    }

    @SideOnly(Side.CLIENT)
    public void func_70270_d(boolean par1) {
        this.field_70279_a = par1;
    }

    /**
     * Applies a velocity to each of the entities pushing them away from each
     * other. Args: entity
     */
    @Override
    public void applyEntityCollision(Entity entity) {
        if (!this.worldObj.isRemote) {
            if (entity != this.riddenByEntity) {
                if (this.riddenByEntity != null) {
                    if (this.motionX * this.motionX + this.motionZ * this.motionZ > 0.01D) {
                        entity.motionX = this.motionX * 6;
                        entity.motionZ = this.motionX * 6;
                        entity.motionY = 0.5;
                        if (Math.abs(this.motionX) > Math.abs(this.motionZ)) {
                            entity.motionZ += new Random().nextFloat() - 0.5;
                        } else {
                            entity.motionX += new Random().nextFloat() - 0.5;
                        }
                    }
                } else {
                    if (entity instanceof EntityLiving && !(entity instanceof EntityPlayer) && this.motionX * this.motionX + this.motionZ * this.motionZ > 0.01D) {
                        entity.mountEntity(this);
                    } else {
                        super.applyEntityCollision(entity);
                    }
                }
            }
        }
    }
}
