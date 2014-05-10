package LavaBoat.entity;

import LavaBoat.ModLavaBoat;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

/*
 * LavaBoat mod
 *
 * @author NightKosh
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
public abstract class EntityNKBoat extends EntityBoat {

    public static final double MAX_VELOCITY = 0.5;
    protected float length;
    protected boolean isBoatEmpty;
    protected double speedMultiplier;
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
        this.speedMultiplier = 0.1D;
    }

    /**
     * Returns the Y offset from the entity's position for any entity riding
     * this one.
     */
    @Override
    public double getMountedYOffset() {
        return 0.1;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource damageSource, int itemDamage, float par2) {
        if (this.isEntityInvulnerable()) {
            return false;
        } else if (!this.worldObj.isRemote && !this.isDead) {
            if (damageSource.getEntity() instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) damageSource.getEntity();
                byte emptySlot = getPlayerEmptySlot(player.inventory.mainInventory);
                if (emptySlot != -1) {
                    player.inventory.setInventorySlotContents(emptySlot, new ItemStack(ModLavaBoat.lavaBoat, 1, itemDamage));
                    this.setDead();
                }
            } else {
                this.setForwardDirection(-this.getForwardDirection());
                this.setTimeSinceHit(10);
                this.setDamageTaken((int) (this.getDamageTaken() + par2 * 10));
                this.setBeenAttacked();

                if (this.getDamageTaken() > 200) {
                    this.entityDropItem(new ItemStack(ModLavaBoat.lavaBoat, 1, itemDamage), 0);
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
    protected static byte getPlayerEmptySlot(ItemStack[] items) {
        for (byte i = 0; i < items.length; i++) {
            if (items[i] == null) {
                return i;
            }
        }
        return (byte) -1;
    }

    /**
     * Sets the position and rotation. Only difference from the other one is no
     * bounding on the rotation. Args: posX, posY, posZ, yaw, pitch
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void setPositionAndRotation2(double posX, double posY, double posZ, float yaw, float pitch, int par9) {
        if (this.isBoatEmpty) {
            this.boatPosRotationIncrements = par9 + 15;
        } else {
            double deltaX = posX - this.posX;
            double deltaY = posY - this.posY;
            double deltaZ = posZ - this.posZ;
            double delta = deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ;

            if (delta <= 1) {
                return;
            }

            this.boatPosRotationIncrements = 12;
        }

        this.boatX = posX;
        this.boatY = posY;
        this.boatZ = posZ;
        this.boatYaw = yaw;
        this.boatPitch = pitch;
        this.motionX = this.velocityX;
        this.motionY = this.velocityY;
        this.motionZ = this.velocityZ;
    }

    /**
     * Applies a velocity to each of the entities pushing them away from each other. Args: entity
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

    protected void spawnSplash(double d3, String particles) {
        if (d3 > 0.25) {
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
                this.worldObj.spawnParticle(particles, splashX, this.posY - 0.125, splashZ, this.motionX, this.motionY, this.motionZ);
            }
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate() {
        this.onEntityUpdate();
    }

    /**
     * Called to update the entity's position/logic.
     */
    protected void onUpdate(Material material, double yShift, String particles) {
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
        AxisAlignedBB axisalignedbb = AxisAlignedBB.getAABBPool().getAABB(this.boundingBox.minX, this.boundingBox.minY - yShift, this.boundingBox.minZ,
                this.boundingBox.maxX, this.boundingBox.minY - yShift, this.boundingBox.maxZ);
        if (this.worldObj.isAABBInMaterial(axisalignedbb, material)) {
            shiftY = 1;
        }

        double motion = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        spawnSplash(motion, particles);

        if (this.worldObj.isRemote && this.isBoatEmpty) {
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
                this.motionY += 0.04D * (shiftY * 2 - 1);
            } else {
                if (this.motionY < 0) {
                    this.motionY /= 2D;
                }
                this.motionY += 0.007;
            }

            if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityLivingBase) {
                double d4 = (double) ((EntityLivingBase) this.riddenByEntity).moveForward;

                if (d4 > 0) {
                    double d5 = -Math.sin((double) (this.riddenByEntity.rotationYaw * (float) Math.PI / 180F));
                    double d11 = Math.cos((double) (this.riddenByEntity.rotationYaw * (float) Math.PI / 180F));
                    this.motionX += d5 * this.speedMultiplier * 0.05D;
                    this.motionZ += d11 * this.speedMultiplier * 0.05D;
                }
            }

            double newMotion = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            if (newMotion > MAX_VELOCITY) {
                double d5 = MAX_VELOCITY / newMotion;
                this.motionX *= d5;
                this.motionZ *= d5;
                newMotion = MAX_VELOCITY;
            }

            if (newMotion > motion && this.speedMultiplier < MAX_VELOCITY) {
                this.speedMultiplier += (MAX_VELOCITY - this.speedMultiplier) / 50D;//35D;

                if (this.speedMultiplier > MAX_VELOCITY) {
                    this.speedMultiplier = MAX_VELOCITY;
                }
            } else {
                this.speedMultiplier -= (this.speedMultiplier - 0.1) / 50D;//35D;

                if (this.speedMultiplier < 0.1) {
                    this.speedMultiplier = 0.1;
                }
            }

            if (this.onGround) {
                this.motionX *= 0.5;
                this.motionY *= 0.5;
                this.motionZ *= 0.5;
            }

            this.moveEntity(this.motionX, this.motionY, this.motionZ);

            if (this.isCollidedHorizontally && motion > 0.2) {
                //if (!this.worldObj.isRemote && !this.isDead) {
                //this.setDead();
                //}
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
                List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(0.2, 0, 0.2));
                int i;

                if (list != null && !list.isEmpty()) {
                    for (i = 0; i < list.size(); i++) {
                        Entity entity = (Entity) list.get(i);

                        if (entity != this.riddenByEntity && entity.canBePushed() && entity instanceof EntityNKBoat) {
                            entity.applyEntityCollision(this);
                        }
                    }
                }

                // remove waterlily
                if (material == Material.water) {
                    int y = MathHelper.floor_double(this.posY);

                    int minX = MathHelper.floor_double(this.boundingBox.minX - 0.2);
                    int minZ = MathHelper.floor_double(this.boundingBox.minZ - 0.2);
                    int maxX = MathHelper.floor_double(this.boundingBox.maxX + 0.2);
                    int maxZ = MathHelper.floor_double(this.boundingBox.maxZ + 0.2);

                    for (int x = minX; x <= maxX; x++) {
                        for (int z = minZ; z <= maxZ; z++) {
                            Block block = this.worldObj.getBlock(x, y, z);

                            if (block.equals(Blocks.waterlily)) {
                                Blocks.waterlily.dropBlockAsItem(this.worldObj, x, y, z, 0, 0);
                                this.worldObj.setBlock(x, y, z, Blocks.air, 0, 2);
                            }
                        }
                    }

                }

                if (this.riddenByEntity != null && this.riddenByEntity.isDead) {
                    this.riddenByEntity = null;
                }
            }
        }
    }

    /**
     * Return whether this entity should be rendered as on fire.
     */
    @Override
    public boolean canRenderOnFire() {
        return false;
    }
}
