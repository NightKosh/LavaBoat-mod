package LavaBoat.entity;

import LavaBoat.ModLavaBoat;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumEntitySize;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

/*
 * LavaBoat mod
 *
 * @author NightKosh
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
public abstract class EntityDoubleBoat extends EntityNKBoat {

    protected EntityPetBoat petSeat;

    public EntityDoubleBoat(World world) {
        super(world);
        this.setSize(3F, 1.25F, 0.6F);

        this.petSeat = new EntityPetBoat(world, this);
        worldObj.spawnEntityInWorld(this.petSeat);
    }

    /**
     * Sets the width and height of the entity. Args: width, height
     */
    protected void setSize(float length, float width, float height) {
        if (width != this.width || height != this.height) {
            this.width = width;
            this.length = length;
            this.height = height;
            this.boundingBox.maxX = this.boundingBox.minX + this.width;
            this.boundingBox.maxZ = this.boundingBox.minZ + this.length;
            this.boundingBox.maxY = this.boundingBox.minY + this.height;
        }

        float f2 = width % 2.0F;

        if (f2 < 0.375) {
            this.myEntitySize = EnumEntitySize.SIZE_1;
        } else if (f2 < 0.75) {
            this.myEntitySize = EnumEntitySize.SIZE_2;
        } else if (f2 < 1) {
            this.myEntitySize = EnumEntitySize.SIZE_3;
        } else if (f2 < 1.375) {
            this.myEntitySize = EnumEntitySize.SIZE_4;
        } else if (f2 < 1.75) {
            this.myEntitySize = EnumEntitySize.SIZE_5;
        } else {
            this.myEntitySize = EnumEntitySize.SIZE_6;
        }
    }

    /**
     * Returns the Y offset from the entity's position for any entity riding
     * this one.
     */
    @Override
    public double getMountedYOffset() {
        return 0.3;
    }

    @Override
    public void updateRiderPosition() {
        if (this.riddenByEntity != null) {
            double xShift = Math.cos(this.rotationYaw * Math.PI / 180D);
            double zShift = Math.sin(this.rotationYaw * Math.PI / 180D);
            this.riddenByEntity.setPosition(this.posX - xShift * 0.5, this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ - zShift * 0.5);
        }
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow,
     * gets into the saddle on a pig.
     */
    @Override
    public boolean interactFirst(EntityPlayer player) {
        if (!this.worldObj.isRemote) {
            if (this.riddenByEntity == null) {
                System.out.println("interact");
                player.mountEntity(this);

                if (this.petSeat.riddenByEntity == null) {
                    System.out.println("mount");
                    petSeat.findAndMountPet();
                }
            } else if (this.petSeat.riddenByEntity != null) {
                
                    System.out.println("unmount");
                petSeat.unmountPet();
            }
        }

        return true;
    }

    /**
     * Applies a velocity to each of the entities pushing them away from each
     * other. Args: entity
     */
    @Override
    public void applyEntityCollision(Entity entity) {
        if (!this.worldObj.isRemote) {
            if (entity != this.riddenByEntity && entity != this.petSeat && entity != this.petSeat.riddenByEntity || this.petSeat == null) {
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

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    protected void onUpdate(Material material, double yShift, String particles) {
        super.onUpdate(material, yShift, particles);


        //System.out.println("Boat position " + this.posX + "x" + this.posY + "x" + this.posZ);
        //System.out.println("Pet boat position " + this.petSeat.posX + "x" + this.petSeat.posY + "x" + this.petSeat.posZ);
        //if (this.petSeat.riddenByEntity != null)
        //System.out.println("Pet position " + this.petSeat.riddenByEntity.posX + "x" + this.petSeat.riddenByEntity.posY + "x" + this.petSeat.riddenByEntity.posZ);

        //System.out.println("---------------------");
    }

    /**
     * Called when the entity is attacked.
     */
    @Override
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
}
