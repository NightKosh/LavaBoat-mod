package LavaBoat.entity;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
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
public class EntityLavaBoat extends EntityNKBoat {

    public EntityLavaBoat(World world) {
        super(world);
        this.isImmuneToFire = true;
    }

    public EntityLavaBoat(World world, double x, double y, double z) {
        this(world);
        this.setPosition(x, y + this.yOffset, z);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
    }

    /**
     * Returns the Y offset from the entity's position for any entity riding
     * this one.
     */
    @Override
    public double getMountedYOffset() {
        return this.height * 0.0D + 0.20000001192092896D;
    }

    /**
     * Called when the entity is attacked.
     */
    @Override
    public boolean attackEntityFrom(DamageSource damageSource, int par2) {
        return attackEntityFrom(damageSource, 2, par2);
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate() {
        super.onUpdate();

        if (this.getTimeSinceHit() > 0) {
            this.setTimeSinceHit(this.getTimeSinceHit() - 1);
        }

        if (this.getDamageTaken() > 0) {
            this.setDamageTaken(this.getDamageTaken() - 1);
        }

        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        byte b0 = 5;
        double d0 = 0.0D;

        for (int i = 0; i < b0; ++i) {
            double d1 = this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) * i / b0 - 1.1;
            double d2 = this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) * (i + 1) / b0 - 1.1;
            AxisAlignedBB axisalignedbb = AxisAlignedBB.getAABBPool().getAABB(this.boundingBox.minX, d1, this.boundingBox.minZ, this.boundingBox.maxX, d2, this.boundingBox.maxZ);

            if (this.worldObj.isAABBInMaterial(axisalignedbb, Material.lava)) {
                d0 += 1.0D / b0;
            }
        }

        double d3 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        double d4;
        double d5;

        if (d3 > 0.26249999999999996D) {
            d4 = Math.cos((double) this.rotationYaw * Math.PI / 180.0D);
            d5 = Math.sin((double) this.rotationYaw * Math.PI / 180.0D);

            for (int j = 0; j < 1 + d3 * 60; ++j) {
                double d6 = this.rand.nextFloat() * 2.0F - 1.0F;
                double d7 = (this.rand.nextInt(2) * 2 - 1) * 0.7D;
                double d8;
                double d9;

                if (this.rand.nextBoolean()) {
                    d8 = this.posX - d4 * d6 * 0.8D + d5 * d7;
                    d9 = this.posZ - d5 * d6 * 0.8D - d4 * d7;
                    this.worldObj.spawnParticle("splash", d8, this.posY - 0.125D, d9, this.motionX, this.motionY, this.motionZ);
                } else {
                    d8 = this.posX + d4 + d5 * d6 * 0.7D;
                    d9 = this.posZ + d5 - d4 * d6 * 0.7D;
                    this.worldObj.spawnParticle("splash", d8, this.posY - 0.125D, d9, this.motionX, this.motionY, this.motionZ);
                }
            }
        }

        double d10;
        double d11;

        if (this.worldObj.isRemote && this.field_70279_a) {
            if (this.boatPosRotationIncrements > 0) {
                d4 = this.posX + (this.boatX - this.posX) / this.boatPosRotationIncrements;
                d5 = this.posY + (this.boatY - this.posY) / this.boatPosRotationIncrements;
                d11 = this.posZ + (this.boatZ - this.posZ) / this.boatPosRotationIncrements;
                d10 = MathHelper.wrapAngleTo180_double(this.boatYaw - this.rotationYaw);
                this.rotationYaw = (float) (this.rotationYaw + d10 / this.boatPosRotationIncrements);
                this.rotationPitch = (float) (this.rotationPitch + (this.boatPitch - this.rotationPitch) / this.boatPosRotationIncrements);
                --this.boatPosRotationIncrements;
                this.setPosition(d4, d5, d11);
                this.setRotation(this.rotationYaw, this.rotationPitch);
            } else {
                d4 = this.posX + this.motionX;
                d5 = this.posY + this.motionY;
                d11 = this.posZ + this.motionZ;
                this.setPosition(d4, d5, d11);

                if (this.onGround) {
                    this.motionX *= 0.5D;
                    this.motionY *= 0.5D;
                    this.motionZ *= 0.5D;
                }

                this.motionX *= 0.9900000095367432D;
                this.motionY *= 0.949999988079071D;
                this.motionZ *= 0.9900000095367432D;
            }
        } else {
            if (d0 < 1.0D) {
                d4 = d0 * 2.0D - 1.0D;
                this.motionY += 0.03999999910593033D * d4;
            } else {
                if (this.motionY < 0.0D) {
                    this.motionY /= 2.0D;
                }

                this.motionY += 0.007000000216066837D;
            }

            if (this.riddenByEntity != null) {
                this.motionX += this.riddenByEntity.motionX * this.field_70276_b;
                this.motionZ += this.riddenByEntity.motionZ * this.field_70276_b;
            }

            d4 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

            if (d4 > 0.35D) {
                d5 = 0.35D / d4;
                this.motionX *= d5;
                this.motionZ *= d5;
                d4 = 0.35D;
            }

            if (d4 > d3 && this.field_70276_b < 0.35D) {
                this.field_70276_b += (0.35D - this.field_70276_b) / 35.0D;

                if (this.field_70276_b > 0.35D) {
                    this.field_70276_b = 0.35D;
                }
            } else {
                this.field_70276_b -= (this.field_70276_b - 0.07D) / 35.0D;

                if (this.field_70276_b < 0.07D) {
                    this.field_70276_b = 0.07D;
                }
            }

            if (this.onGround) {
                this.motionX *= 0.5D;
                this.motionY *= 0.5D;
                this.motionZ *= 0.5D;
            }

            this.moveEntity(this.motionX, this.motionY, this.motionZ);

            if (this.isCollidedHorizontally && d3 > 0.2D) {
            } else {
                this.motionX *= 0.9900000095367432D;
                this.motionY *= 0.949999988079071D;
                this.motionZ *= 0.9900000095367432D;
            }

            this.rotationPitch = 0.0F;
            d5 = this.rotationYaw;
            d11 = this.prevPosX - this.posX;
            d10 = this.prevPosZ - this.posZ;

            if (d11 * d11 + d10 * d10 > 0.001D) {
                d5 = Math.atan2(d10, d11) * 180.0D / Math.PI;
            }

            double d12 = MathHelper.wrapAngleTo180_double(d5 - this.rotationYaw);

            if (d12 > 20.0D) {
                d12 = 20.0D;
            }

            if (d12 < -20.0D) {
                d12 = -20.0D;
            }

            this.rotationYaw = (float) (this.rotationYaw + d12);
            this.setRotation(this.rotationYaw, this.rotationPitch);

            if (!this.worldObj.isRemote) {
                List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));
                int l;

                if (list != null && !list.isEmpty()) {
                    for (l = 0; l < list.size(); ++l) {
                        Entity entity = (Entity) list.get(l);

                        if (entity != this.riddenByEntity && entity.canBePushed() && entity instanceof EntityLavaBoat) {
                            entity.applyEntityCollision(this);
                        }
                    }
                }

                for (l = 0; l < 4; ++l) {
                    int i1 = MathHelper.floor_double(this.posX + (l % 2 - 0.5D) * 0.8D);
                    int j1 = MathHelper.floor_double(this.posZ + (l / 2 - 0.5D) * 0.8D);

                    for (int k1 = 0; k1 < 2; ++k1) {
                        int l1 = MathHelper.floor_double(this.posY) + k1;
                        int i2 = this.worldObj.getBlockId(i1, l1, j1);

                        if (i2 == Block.snow.blockID) {
                            this.worldObj.setBlockToAir(i1, l1, j1);
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
