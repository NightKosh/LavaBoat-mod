
package LavaBoat;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;

/*
 * LavaBoat mod
 *
 * @author NightKosh
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
public class Resources {
    
    private Resources() {
        
    }
    
    private static final String MOD_ID = ModInfo.ID.toLowerCase();
    private static final String MODEL_LOCATION = MOD_ID + ":textures/models/";
    
    // items
    public static final String REINFORCED_BOAT        = MOD_ID + ":LBReinforcedBoat";
    public static final String DOUBLE_REINFORCED_BOAT = MOD_ID + ":LBDoubleReinforcedBoat";
    public static final String CARGO_REINFORCED_BOAT  = MOD_ID + ":LBCargoReinforcedBoat";
    public static final String LAVA_BOAT              = MOD_ID + ":LBLavaBoat";
    public static final String DOUBLE_LAVA_BOAT       = MOD_ID + ":LBDoubleLavaBoat";
    public static final String CARGO_LAVA_BOAT        = MOD_ID + ":LBCargoLavaBoat";
    
    // models
    public static final ResourceLocation MODEL_REINFORCES_BOAT        = new ResourceLocation(MODEL_LOCATION + "ReinforcedBoat.png");
    public static final ResourceLocation MODEL_DOUBLE_REINFORCED_BOAT = new ResourceLocation(MODEL_LOCATION + "ReinforcedDoubleBoat.png");
    public static final ResourceLocation MODEL_CARGO_REINFORCED_BOAT  = new ResourceLocation(MODEL_LOCATION + "ReinforcedDoubleBoat.png");
    public static final ResourceLocation MODEL_LAVA_BOAT              = new ResourceLocation(MODEL_LOCATION + "LavaBoat.png");
    public static final ResourceLocation MODEL_DOUBLE_LAVA_BOAT       = new ResourceLocation(MODEL_LOCATION + "DoubleLavaBoat.png");
    public static final ResourceLocation MODEL_CARGO_LAVA_BOAT        = new ResourceLocation(MODEL_LOCATION + "DoubleLavaBoat.png");


    public static final ModelResourceLocation reinforcedBoatModel = new ModelResourceLocation(REINFORCED_BOAT, "inventory");
    public static final ModelResourceLocation doubleReinforcedBoatModel = new ModelResourceLocation(DOUBLE_REINFORCED_BOAT, "inventory");
    public static final ModelResourceLocation cargoReinforcedBoatModel = new ModelResourceLocation(CARGO_REINFORCED_BOAT, "inventory");
    public static final ModelResourceLocation lavaBoatModel = new ModelResourceLocation(LAVA_BOAT, "inventory");
    public static final ModelResourceLocation doubleLavaBoatModel = new ModelResourceLocation(DOUBLE_LAVA_BOAT, "inventory");
    public static final ModelResourceLocation cargoLavaBoatModel = new ModelResourceLocation(CARGO_LAVA_BOAT, "inventory");
}
