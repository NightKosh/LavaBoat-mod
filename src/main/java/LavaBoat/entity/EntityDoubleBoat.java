package LavaBoat.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;

/*
 * LavaBoat mod
 *
 * @author NightKosh
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
public abstract class EntityDoubleBoat extends EntityNKBoat {

    protected EntityLivingBase mob;

    public EntityDoubleBoat(World world) {
        super(world);
        this.setSize(3, 1.25F, 0.6F);
    }

    /**
     * Sets the width and height of the entity. Args: width, height
     */
    protected void setSize(float length, float width, float height) {
        if (width != this.width || height != this.height) {
            this.width = width;
            this.length = length;
            this.height = height;
            this.setEntityBoundingBox(new AxisAlignedBB(this.getEntityBoundingBox().minX, this.getEntityBoundingBox().minY, this.getEntityBoundingBox().minZ,
                    this.getEntityBoundingBox().minX + this.width, this.getEntityBoundingBox().minY + this.height, this.getEntityBoundingBox().minZ + this.length));
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

            if (mob != null) {
                this.mob.setPosition(this.posX + xShift * 0.6, this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ + zShift * 0.6);
            }
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
                player.mountEntity(this);

                if (this.mob == null) {
                    if (!mountLeashedMob(player)) {
                        findAndMountPet();
                    }
                }
            }
        }

        return true;
    }

    @Override
    protected boolean additionalCollisionChecks(Entity entity) {
        return entity != mob;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (this.riddenByEntity == null) {
            unMountMob();
        }
    }

    protected void findAndMountPet() {
        AxisAlignedBB mountArea = AxisAlignedBB.fromBounds(this.getEntityBoundingBox().minX - 7, this.getEntityBoundingBox().minY - 7, this.getEntityBoundingBox().minZ - 7,
                this.getEntityBoundingBox().maxX + 7, this.getEntityBoundingBox().maxY + 7, this.getEntityBoundingBox().maxZ + 7);

        if (!mountTamedPet(mountArea, EntityWolf.class)) {
            mountTamedPet(mountArea, EntityOcelot.class);
        }
    }

    public boolean mountLeashedMob(EntityPlayer player) {
        double d = 7;
        List list = this.worldObj.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB(this.posX - d, this.posY - d, this.posZ - d, this.posX + d, this.posY + d, this.posZ + d));
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            EntityLiving mob = (EntityLiving) iterator.next();
            if (mob.getLeashed() && mob.getLeashedToEntity() == player) {
                mob.clearLeashed(true, false);
                mountMob(mob);

                byte emptySlot = EntityNKBoat.getPlayerEmptySlot(player.inventory.mainInventory);
                if (emptySlot != -1) {
                    player.inventory.setInventorySlotContents(emptySlot, new ItemStack(Items.lead, 1));
                } else {
                    this.entityDropItem(new ItemStack(Items.lead, 1), 0);
                }
                return true;
            }
        }
        return false;
    }

    public void unMountMob() {
        if (this.mob != null) {
            if (this.mob instanceof EntityTameable) {
                ((EntityTameable) this.mob).setSitting(false);
            }
            this.mob = null;
        }
    }

    /*
     * Mount pet to boat
     */
    private boolean mountTamedPet(AxisAlignedBB petArea, Class petClass) {
        List<EntityTameable> petsList = this.worldObj.getEntitiesWithinAABB(petClass, petArea);

        if (!petsList.isEmpty()) {
            Iterator<EntityTameable> it = petsList.iterator();
            EntityTameable pet;
            while (it.hasNext()) {
                pet = it.next();
                if (pet != null && pet.isTamed() && pet.getOwner() != null && pet.getOwner().equals(this.riddenByEntity) && pet.ridingEntity == null) {
                    pet.setSitting(true);
                    mountMob(pet);

                    return true;
                }
            }
        }

        return false;
    }

    protected void mountMob(EntityLiving mob) {
        this.mob = mob;
        updateRiderPosition();
    }
}
