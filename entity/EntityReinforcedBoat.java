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
public class EntityReinforcedBoat extends EntityNKBoat {

    public EntityReinforcedBoat(World world) {
        super(world);
    }

    public EntityReinforcedBoat(World world, double x, double y, double z) {
        this(world);
        this.setPosition(x + 0.5, y + this.yOffset + 1, z + 0.5);
        this.motionX = 0;
        this.motionY = 0;
        this.motionZ = 0;
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
    }

    /**
     * Called when the entity is attacked.
     */
    @Override
    public boolean attackEntityFrom(DamageSource damageSource, int par2) {
        return attackEntityFrom(damageSource, 0, par2);
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate() {
        super.onUpdate();
        
        onUpdate(Material.water, -0.05, "splash");
    }
}
