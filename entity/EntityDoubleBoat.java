package LavaBoat.entity;

import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.EnumEntitySize;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

/*
 * LavaBoat mod
 *
 * @author NightKosh
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
public abstract class EntityDoubleBoat extends EntityNKBoat {

    public EntityDoubleBoat(World world) {
        super(world);
        this.setSize(3.5F, 1.5F, 0.6F);
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
            this.riddenByEntity.setPosition(this.posX - xShift * 1.2, this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ - zShift * 1.2);

            if (this.riddenByPet != null) {
                this.riddenByPet.setPosition(this.posX + xShift * 0.4, this.posY + this.getMountedYOffset() + this.riddenByPet.getYOffset(), this.posZ + zShift * 0.4);
            } else {
                AxisAlignedBB petArea = AxisAlignedBB.getBoundingBox(this.boundingBox.minX - 7, this.boundingBox.minY - 7, this.boundingBox.minZ - 7,
                        this.boundingBox.maxX + 7, this.boundingBox.maxY + 7, this.boundingBox.maxZ + 7);

                if (!mountPet(petArea, EntityWolf.class)) {
                    mountPet(petArea, EntityOcelot.class);
                }
            }
        } else {
            if (this.riddenByPet != null) {
                unMountPet();
            }
        }
    }

    /*
     * Mount pet to boat
     */
    private boolean mountPet(AxisAlignedBB petArea, Class petClass) {
        List <EntityTameable> petsList = this.worldObj.getEntitiesWithinAABB(petClass, petArea);

        if (!petsList.isEmpty()) {
            Iterator <EntityTameable> it = petsList.iterator();
            EntityTameable pet;
            while (it.hasNext()) {
                pet = it.next();
                if (pet != null && pet.isTamed() && pet.getOwner() != null && pet.getOwner().equals(this.riddenByEntity) && pet.ridingEntity == null) {
                    pet.ridingEntity = this;
                    this.riddenByPet = pet;
                    pet.setSitting(true);
                    return true;
                }
            }
        }

        return false;
    }
}
