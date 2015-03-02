package LavaBoat.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
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
        this.setSize(3, 1.25F, 0.6F);

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
                System.out.println("interact");//TODO
                player.mountEntity(this);

                if (this.petSeat.riddenByEntity == null) {
                    System.out.println("mount");//TODO
                    petSeat.findAndMountPet();
                }
            } else if (this.petSeat.riddenByEntity != null) {
                System.out.println("unmount");//TODO
                petSeat.unmountPet();
            }
        }

        return true;
    }

    @Override
    protected boolean additionalCollisionChecks(Entity entity) {
        return entity != this.petSeat && entity != this.petSeat.riddenByEntity;
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate() {
        super.onUpdate();

//TODO
//        System.out.println("Boat position " + this.posX + "x" + this.posY + "x" + this.posZ);
//        System.out.println("Pet boat position " + this.petSeat.posX + "x" + this.petSeat.posY + "x" + this.petSeat.posZ);
//        if (this.petSeat.riddenByEntity != null)
//        System.out.println("Pet position " + this.petSeat.riddenByEntity.posX + "x" + this.petSeat.riddenByEntity.posY + "x" + this.petSeat.riddenByEntity.posZ);
//
//        System.out.println("---------------------");
    }
}
