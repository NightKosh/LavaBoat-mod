package LavaBoat.item;

import LavaBoat.entity.EntityLavaBoat;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

/*
 * LavaBoat mod
 *
 * @author NightKosh
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
public class ItemLavaBoat extends Item {

    public ItemLavaBoat(int par1) {
        super(par1);
        this.maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.tabTransport);
    }

    @Override
    public void registerIcons(IconRegister register) {
        this.itemIcon = register.registerIcon("LavaBoat:lavaboat");
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        float f = 1.0F;
        float f1 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * f;
        float f2 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * f;
        double d0 = player.prevPosX + (player.posX - player.prevPosX) * (double) f;
        double d1 = player.prevPosY + (player.posY - player.prevPosY) * (double) f + 1.62D - (double) player.yOffset;
        double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * (double) f;
        Vec3 vec3 = world.getWorldVec3Pool().getVecFromPool(d0, d1, d2);
        float f3 = MathHelper.cos(-f2 * 0.017453292F - (float) Math.PI);
        float f4 = MathHelper.sin(-f2 * 0.017453292F - (float) Math.PI);
        float f5 = -MathHelper.cos(-f1 * 0.017453292F);
        float f6 = MathHelper.sin(-f1 * 0.017453292F);
        float f7 = f4 * f5;
        float f8 = f3 * f5;
        double d3 = 5.0D;
        Vec3 vec31 = vec3.addVector((double) f7 * d3, (double) f6 * d3, (double) f8 * d3);
        MovingObjectPosition movingobjectposition = world.rayTraceBlocks_do(vec3, vec31, true);

        if (movingobjectposition == null) {
            return itemStack;
        } else {
            Vec3 vec32 = player.getLook(f);
            boolean flag = false;
            float f9 = 1.0F;
            List list = world.getEntitiesWithinAABBExcludingEntity(player, player.boundingBox.addCoord(vec32.xCoord * d3, vec32.yCoord * d3, vec32.zCoord * d3).expand((double) f9, (double) f9, (double) f9));
            int i;

            for (i = 0; i < list.size(); ++i) {
                Entity entity = (Entity) list.get(i);

                if (entity.canBeCollidedWith()) {
                    float f10 = entity.getCollisionBorderSize();
                    AxisAlignedBB axisalignedbb = entity.boundingBox.expand((double) f10, (double) f10, (double) f10);

                    if (axisalignedbb.isVecInside(vec3)) {
                        flag = true;
                    }
                }
            }

            if (flag) {
                return itemStack;
            } else {
                if (movingobjectposition.typeOfHit == EnumMovingObjectType.TILE) {
                    i = movingobjectposition.blockX;
                    int j = movingobjectposition.blockY;
                    int k = movingobjectposition.blockZ;

                    if (world.getBlockId(i, j, k) == Block.snow.blockID) {
                        --j;
                    }

                    EntityLavaBoat boat = new EntityLavaBoat(world, (double) ((float) i + 0.5F), (double) ((float) j + 1.0F), (double) ((float) k + 0.5F));
                    boat.rotationYaw = (float) (((MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) - 1) * 90);

                    if (!world.getCollidingBoundingBoxes(boat, boat.boundingBox.expand(-0.1D, -0.1D, -0.1D)).isEmpty()) {
                        return itemStack;
                    }

                    if (!world.isRemote) {
                        world.spawnEntityInWorld(boat);
                    }

                    if (!player.capabilities.isCreativeMode) {
                        --itemStack.stackSize;
                    }
                }

                return itemStack;
            }
        }
    }
}
