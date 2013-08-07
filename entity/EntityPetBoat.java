package LavaBoat.entity;

import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityWolf;
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
public class EntityPetBoat extends Entity {

    protected Entity boat;

    public EntityPetBoat(World world) {
        super(world);
        this.setSize(0, 0);
        //this.setSize(1.5F, 0.6F);
        preventEntitySpawning = true;
        this.isImmuneToFire = true;
    }

    public EntityPetBoat(World world, EntityDoubleBoat boat) {
        this(world);
        this.boat = boat;
    }

    protected void setStartParams(double x, double y, double z) {        
        this.setPosition(x, y + this.yOffset, z);
        this.motionX = 0;
        this.motionY = 0;
        this.motionZ = 0;
        
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
        updatePosition();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        
        if (this.riddenByEntity != null && this.riddenByEntity.isDead) {
            this.riddenByEntity = null;
        }
        
        if (boat == null || boat.isDead) {
            if (this.riddenByEntity != null) {
                unmountPet();
            }
            setDead();
        } else {
            updatePosition();
        }
    }

    private void updatePosition() {
        prevRotationYaw = boat.prevRotationYaw;
        prevRotationPitch = boat.prevRotationPitch;
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        
        this.setRotation(this.boat.rotationYaw, this.boat.rotationPitch);
        this.setPosition(this.boat.posX, this.boat.posY, this.boat.posZ);
    }

    /**
     * Returns the Y offset from the entity's position for any entity riding
     * this one.
     */
    @Override
    public double getMountedYOffset() {
        return this.boat.getMountedYOffset();
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    protected void entityInit() {
    }

    @Override
    public AxisAlignedBB getCollisionBox(Entity entity) {
        return entity.boundingBox;
    }

    @Override
    public AxisAlignedBB getBoundingBox() {
        return boundingBox;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public float getShadowSize() {
        return 0.0F;
    }

    @Override
    public boolean attackEntityFrom(DamageSource damagesource, float i) {
        return false;
    }

    @Override
    public void updateRiderPosition() {
        if (riddenByEntity != null) {
            double xShift = Math.cos(this.rotationYaw * Math.PI / 180D);
            double zShift = Math.sin(this.rotationYaw * Math.PI / 180D);
            this.riddenByEntity.setPosition(this.posX + xShift * 0.6, this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ + zShift * 0.6);
        }
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
    }

    /**
     * Applies a velocity to each of the entities pushing them away from each
     * other. Args: entity
     */
    @Override
    public void applyEntityCollision(Entity entity) {
    }

    protected void findAndMountPet() {
        AxisAlignedBB mountArea = AxisAlignedBB.getBoundingBox(this.boat.boundingBox.minX - 7, this.boat.boundingBox.minY - 7, this.boat.boundingBox.minZ - 7,
                this.boat.boundingBox.maxX + 7, this.boat.boundingBox.maxY + 7, this.boat.boundingBox.maxZ + 7);

        if (!mountPet(mountArea, EntityWolf.class)) {
            this.mountPet(mountArea, EntityOcelot.class);
        }
    }
    /*
     * Mount pet to boat
     */

    private boolean mountPet(AxisAlignedBB petArea, Class petClass) {
        List<EntityTameable> petsList = this.worldObj.getEntitiesWithinAABB(petClass, petArea);

        if (!petsList.isEmpty()) {
            Iterator<EntityTameable> it = petsList.iterator();
            EntityTameable pet;
            while (it.hasNext()) {
                pet = it.next();
                if (pet != null && pet.isTamed() && pet.getOwner() != null && pet.getOwner().equals(this.boat.riddenByEntity) && pet.ridingEntity == null) {
                    System.out.println("Mount pet " + pet.getEntityName());
                    pet.setSitting(true);
                    pet.mountEntity(this);

                    return true;
                }
            }
        }

        return false;
    }
    
    protected void unmountPet() {
        ((EntityTameable) this.riddenByEntity).setSitting(false);
        //this.riddenByEntity.unmountEntity(this);
    }
}
