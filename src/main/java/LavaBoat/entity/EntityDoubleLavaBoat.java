package LavaBoat.entity;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

/*
 * LavaBoat mod
 *
 * @author NightKosh
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
public class EntityDoubleLavaBoat extends EntityDoubleBoat {

    public EntityDoubleLavaBoat(World world) {
        super(world);
        this.isImmuneToFire = true;
    }

    public EntityDoubleLavaBoat(World world, double x, double y, double z) {
        this(world);
        this.setPosition(x + 0.5, y + this.getYOffset() + 1, z + 0.5);
        this.motionX = 0;
        this.motionY = 0;
        this.motionZ = 0;
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (this.mob != null) {
            addFireProtection(mob);
        }
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer player) {
        if (this.riddenByEntity != null && this.riddenByEntity == player) {
            addFireProtection(player);
        }
        super.onCollideWithPlayer(player);
    }

    protected void addFireProtection(EntityLivingBase mob) {
        PotionEffect effect = new PotionEffect(Potion.fireResistance.getId(), 300);
        mob.addPotionEffect(effect);
    }

    /**
     * Returns the Y offset from the entity's position for any entity riding
     * this one.
     */
    @Override
    public double getMountedYOffset() {
        return 0.75;
    }

    @Override
    protected EnumParticleTypes getParticles() {
        return EnumParticleTypes.LAVA;
    }

    @Override
    protected Material getWaterMaterial() {
        return Material.lava;
    }

    @Override
    protected double getYShift() {
        return -0.4;
    }

    @Override
    protected int getItemDamage() {
        return 4;
    }
}
