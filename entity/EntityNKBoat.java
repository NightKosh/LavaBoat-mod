package LavaBoat.entity;

import LavaBoat.mod_LavaBoat;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

/*
 * LavaBoat mod
 *
 * @author NightKosh
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
public abstract class EntityNKBoat extends Entity {

    public static final double MAX_VELOCITY = 0.5;
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
        return -0.30000001192092896;
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

    /*
     * Spawn splashes
     */
    protected void spawnSplash(double d3) {
        if (d3 > 0.26249999999999996) {
            double d4 = Math.cos(this.rotationYaw * Math.PI / 180D);
            double d5 = Math.sin(this.rotationYaw * Math.PI / 180D);

            for (int j = 0; j < 1 + d3 * 60; ++j) {
                double d6 = this.rand.nextFloat() * 2 - 1;
                double d7 = (this.rand.nextInt(2) * 2 - 1) * 0.7;
                double splashX;
                double splashZ;

                if (this.rand.nextBoolean()) {
                    splashX = this.posX - d4 * d6 * 0.8 + d5 * d7;
                    splashZ = this.posZ - d5 * d6 * 0.8 - d4 * d7;
                } else {
                    splashX = this.posX + d4 + d5 * d6 * 0.7;
                    splashZ = this.posZ + d5 - d4 * d6 * 0.7;
                }
                this.worldObj.spawnParticle("splash", splashX, this.posY - 0.125, splashZ, this.motionX, this.motionY, this.motionZ);
            }
        }
    }
    

    /**
     * Called to update the entity's position/logic.
     */
    protected void onUpdate(Material material) {
        // decrease hit time
        if (this.getTimeSinceHit() > 0) {
            this.setTimeSinceHit(this.getTimeSinceHit() - 1);
        }
        // decrease taken damage
        if (this.getDamageTaken() > 0) {
            this.setDamageTaken(this.getDamageTaken() - 1);
        }

        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        
        double shiftY = 0;

        AxisAlignedBB axisalignedbb = AxisAlignedBB.getAABBPool().getAABB(this.boundingBox.minX, this.boundingBox.minY - 0.125, this.boundingBox.minZ,
                this.boundingBox.maxX, this.boundingBox.maxY - 0.125, this.boundingBox.maxZ);
        if (this.worldObj.isAABBInMaterial(axisalignedbb, material)) {
            shiftY = 1;
        }

        double motion = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

        spawnSplash(motion);

        if (this.worldObj.isRemote && this.field_70279_a) {
            if (this.boatPosRotationIncrements > 0) {
                double x = this.posX + (this.boatX - this.posX) / this.boatPosRotationIncrements;
                double y = this.posY + (this.boatY - this.posY) / this.boatPosRotationIncrements;
                double z = this.posZ + (this.boatZ - this.posZ) / this.boatPosRotationIncrements;
                
                double d10 = MathHelper.wrapAngleTo180_double(this.boatYaw - this.rotationYaw);
                this.rotationYaw = (float) (this.rotationYaw + d10 / this.boatPosRotationIncrements);
                this.rotationPitch = (float) (this.rotationPitch + (this.boatPitch - this.rotationPitch) / this.boatPosRotationIncrements);
                this.boatPosRotationIncrements--;
                this.setPosition(x, y, z);
                this.setRotation(this.rotationYaw, this.rotationPitch);
            } else {
                this.setPosition(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

                if (this.onGround) {
                    this.motionX *= 0.5;
                    this.motionY *= 0.5;
                    this.motionZ *= 0.5;
                }
            }
        } else {
            // шаталити
            if (shiftY < 1) {
                this.motionY += 0.03999999910593033D * (shiftY * 2 - 1);
            } else {
                if (this.motionY < 0) {
                    this.motionY /= 2D;
                }
                this.motionY += 0.007000000216066837;
            }

            if (this.riddenByEntity != null) {
                this.motionX += this.riddenByEntity.motionX * this.field_70276_b;
                this.motionZ += this.riddenByEntity.motionZ * this.field_70276_b;
            }

            double newMotion = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            if (newMotion > MAX_VELOCITY) {
                double d5 = MAX_VELOCITY / newMotion;
                this.motionX *= d5;
                this.motionZ *= d5;
                newMotion = MAX_VELOCITY;
            }

            if (newMotion > motion && this.field_70276_b < MAX_VELOCITY) {
                this.field_70276_b += (MAX_VELOCITY - this.field_70276_b) / 50D;//35D;

                if (this.field_70276_b > MAX_VELOCITY) {
                    this.field_70276_b = MAX_VELOCITY;
                }
            } else {
                this.field_70276_b -= (this.field_70276_b - 0.07) / 50D;//35D;

                if (this.field_70276_b < 0.07) {
                    this.field_70276_b = 0.07;
                }
            }

            if (this.onGround) {
                this.motionX *= 0.5;
                this.motionY *= 0.5;
                this.motionZ *= 0.5;
            }

            this.moveEntity(this.motionX, this.motionY, this.motionZ);

            if (this.isCollidedHorizontally && motion > 0.2) {
                if (!this.worldObj.isRemote && !this.isDead) {
                    //this.setDead();

                }
            }

            this.rotationPitch = 0;
            double rotation = this.rotationYaw;
            double deltaX = this.prevPosX - this.posX;
            double deltaZ = this.prevPosZ - this.posZ;

            if (deltaX * deltaX + deltaZ * deltaZ > 0.001) {
                rotation = Math.atan2(deltaZ, deltaX) * 180 / Math.PI;
            }

            double d12 = MathHelper.wrapAngleTo180_double(rotation - this.rotationYaw);

            if (d12 > 20) {
                d12 = 20;
            }

            if (d12 < -20) {
                d12 = -20;
            }

            this.rotationYaw = (float) (this.rotationYaw + d12);
            this.setRotation(this.rotationYaw, this.rotationPitch);

            // check for collisions with entities
            if (!this.worldObj.isRemote) {
                List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(0.20000000298023224, 0, 0.20000000298023224));
                int i;

                if (list != null && !list.isEmpty()) {
                    for (i = 0; i < list.size(); i++) {
                        Entity entity = (Entity) list.get(i);

                        if (entity != this.riddenByEntity && entity.canBePushed() && entity instanceof EntityReinforcedBoat) {
                            entity.applyEntityCollision(this);
                        }
                    }
                }

                // remove snow
                for (i = 0; i < 4; i++) {
                    int x = MathHelper.floor_double(this.posX + (i % 2 - 0.5) * 0.8);
                    int z = MathHelper.floor_double(this.posZ + (i / 2 - 0.5) * 0.8);

                    for (int j = 0; j < 2; j++) {
                        int y = MathHelper.floor_double(this.posY) + j;
                        int blockId = this.worldObj.getBlockId(x, y, z);

                        if (blockId == Block.snow.blockID) {
                            this.worldObj.setBlockToAir(x, y, z);
                        }
                    }
                }

                if (this.riddenByEntity != null && this.riddenByEntity.isDead) {
                    this.riddenByEntity = null;
                }
            }
        }
    }
}
