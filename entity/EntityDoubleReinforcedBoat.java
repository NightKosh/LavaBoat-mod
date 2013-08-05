
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
public class EntityDoubleReinforcedBoat extends EntityDoubleBoat {
    
    public EntityDoubleReinforcedBoat(World world) {
        super(world);
    }

    public EntityDoubleReinforcedBoat(World world, double x, double y, double z) {
        this(world);
        this.setPosition(x + 0.5, y + this.yOffset + 1, z + 0.5);
        this.motionX = 0;
        this.motionY = 0;
        this.motionZ = 0;
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
        
        this.petSeat.setStartParams(x, y, z);
    }

    /**
     * Called when the entity is attacked.
     */
    @Override
    public boolean attackEntityFrom(DamageSource damageSource, int par2) {
        return attackEntityFrom(damageSource, 1, par2);
    }
    
    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate() {
        super.onUpdate();
        
        onUpdate(Material.water, -0.4, "splash");
    }
}
