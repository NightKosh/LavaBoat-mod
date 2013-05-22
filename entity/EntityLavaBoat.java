package LavaBoat.entity;

import net.minecraft.block.material.Material;
import net.minecraft.util.DamageSource;
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
        this.motionX = 0;
        this.motionY = 0;
        this.motionZ = 0;
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
        return 0.20000001192092896;
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
        
        onUpdate(Material.lava);
    }
}
