package LavaBoat.item;

import LavaBoat.entity.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/*
 * LavaBoat mod
 *
 * @author NightKosh
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
public class ItemLavaBoat extends Item {

    public static final String[] NAMES = new String[]{
            "reinforced_boat", "large_reinforced_boat", "cargo_reinforced_boat",
            "lava_boat", "large_lava_boat", "cargo_lava_boat"
    };

    public ItemLavaBoat() {
        super();
        this.maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.tabTransport);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    /**
     * Called whenever this item is equipped and the right mouse button is
     * pressed. Args: itemStack, world, entityPlayer
     */
    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        float f1 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch);
        float f2 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw);

        double xPosition = player.prevPosX + player.posX - player.prevPosX;
        double yPosition = player.prevPosY + player.posY - player.prevPosY + 1.62 - player.getYOffset();
        double zPosition = player.prevPosZ + player.posZ - player.prevPosZ;
        Vec3 positionVec = new Vec3(xPosition, yPosition, zPosition);

        float f3 = MathHelper.cos(-f2 * 0.017453292F - (float) Math.PI);
        float f4 = MathHelper.sin(-f2 * 0.017453292F - (float) Math.PI);
        float f5 = -MathHelper.cos(-f1 * 0.017453292F);
        float f6 = MathHelper.sin(-f1 * 0.017453292F);
        float f7 = f4 * f5;
        float f8 = f3 * f5;
        double d3 = 5;
        Vec3 vec31 = positionVec.addVector(f7 * d3, f6 * d3, f8 * d3);
        MovingObjectPosition objectPosition = world.rayTraceBlocks(positionVec, vec31, true);

        if (objectPosition == null) {
            return itemStack;
        } else {
            Vec3 vec32 = player.getLook(1);
            boolean flag = false;
            float f9 = 1;
            List list = world.getEntitiesWithinAABBExcludingEntity(player, player.getEntityBoundingBox().addCoord(vec32.xCoord * d3, vec32.yCoord * d3, vec32.zCoord * d3).expand(f9, f9, f9));

            for (int i = 0; i < list.size(); i++) {
                Entity entity = (Entity) list.get(i);

                if (entity.canBeCollidedWith()) {
                    float collisionSize = entity.getCollisionBorderSize();
                    AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox().expand(collisionSize, collisionSize, collisionSize);

                    if (axisalignedbb.isVecInside(positionVec)) {
                        flag = true;
                    }
                }
            }

            if (flag) {
                return itemStack;
            } else {
                if (objectPosition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                    int x = objectPosition.getBlockPos().getX();
                    int y = objectPosition.getBlockPos().getY();
                    int z = objectPosition.getBlockPos().getZ();

                    if (world.getBlockState(new BlockPos(x, y, z)).getBlock().equals(Blocks.snow_layer)) {
                        --y;
                    }

                    EntityNKBoat NKBoat;
                    switch (itemStack.getItemDamage()) {
                        case 1:
                            NKBoat = new EntityDoubleReinforcedBoat(world, x, y, z);
                            break;
                        case 3:
                            NKBoat = new EntityLavaBoat(world, x, y, z);
                            break;
                        case 4:
                            NKBoat = new EntityDoubleLavaBoat(world, x, y, z);
                            break;
                        case 0:
                        default:
                            NKBoat = new EntityReinforcedBoat(world, x, y, z);
                            break;
                    }

                    NKBoat.rotationYaw = (MathHelper.floor_double(player.rotationYaw * 4 / 360F + 0.5) & 3 - 1) * 90;

                    if (!world.getCollidingBoundingBoxes(NKBoat, NKBoat.getEntityBoundingBox().expand(-0.1, -0.1, -0.1)).isEmpty()) {
                        return itemStack;
                    }

                    if (!world.isRemote) {
                        world.spawnEntityInWorld(NKBoat);
                    }

                    if (!player.capabilities.isCreativeMode) {
                        --itemStack.stackSize;
                    }
                }

                return itemStack;
            }
        }
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye
     * returns 16 items)
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < NAMES.length; i++) {
            if (i != 2 && i != 5) //TODO
                list.add(new ItemStack(item, 1, i));
        }
    }

    /**
     * Returns the metadata of the block which this Item (ItemBlock) can place
     */
    @Override
    public int getMetadata(int metadata) {
        return metadata;
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an
     * ItemStack so different stacks can have different names based on their
     * damage or NBT.
     */
    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int i = stack.getItemDamage();
        if (i < 0 || i >= NAMES.length) {
            i = 0;
        }

        return "item." + NAMES[i];
    }
}
