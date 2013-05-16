package LavaBoat;

import LavaBoat.entity.EntityLavaBoat;
import LavaBoat.item.ItemLavaBoat;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;

/*
 * LavaBoat mod
 *
 * @author NightKosh
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
@Mod(modid = "LavaBoat", name = "LavaBoat", version = "1.1.0")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class mod_LavaBoat {

    @Instance("LavaBoat")
    public static mod_LavaBoat instance;
    
    @SidedProxy(clientSide = "LavaBoat.client.ClientProxy", serverSide = "LavaBoat.CommonProxy")
    public static CommonProxy proxy;
    
    // lava boat
    public static int lavaBoatId;
    public static Item lavaBoat;
    public static boolean canSwimInWater;

    public mod_LavaBoat() {
        instance = this;
    }

    @PreInit
    public void preInit(FMLPreInitializationEvent event) {
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        
        config.load();
        
        lavaBoatId = config.getItem("LavaBoat", 9000 - 256).getInt();
        canSwimInWater = config.get(Configuration.CATEGORY_GENERAL, "CanSwimInWater", false).getBoolean(false);
        
        config.save();
    }

    @Init
    public void load(FMLInitializationEvent event) {

        // create chisel
        lavaBoat = new ItemLavaBoat(lavaBoatId);
        LanguageRegistry.addName(lavaBoat, "Lava boat");

        // recipe
        GameRegistry.addRecipe(new ItemStack(lavaBoat), "xyx", "xxx", 'x', Block.obsidian, 'y', Item.minecartEmpty);

        EntityRegistry.registerGlobalEntityID(EntityLavaBoat.class, "LavaBoat", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerModEntity(EntityLavaBoat.class, "LavaBoat", 1, this, 40, 1, true);
        LanguageRegistry.instance().addStringLocalization("entity.LavaBoat.name", "LavaBoat");

        proxy.registerRenderers();
    }
}
