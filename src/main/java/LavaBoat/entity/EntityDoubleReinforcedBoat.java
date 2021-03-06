package LavaBoat.entity;

import net.minecraft.block.material.Material;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

/*
 * LavaBoat mod
 *
 * @author NightKosh
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
public class EntityDoubleReinforcedBoat extends EntityDoubleBoat {

    public EntityDoubleReinforcedBoat(World world) {
        super(world);
    }

    public EntityDoubleReinforcedBoat(World world, double x, double y, double z) {
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
    protected EnumParticleTypes getParticles() {
        return EnumParticleTypes.WATER_SPLASH;
    }

    @Override
    protected Material getWaterMaterial() {
        return Material.water;
    }

    @Override
    protected double getYShift() {
        return -0.4;
    }

    @Override
    protected int getItemDamage() {
        return 1;
    }
}
