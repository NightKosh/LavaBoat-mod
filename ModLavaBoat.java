package LavaBoat;

import LavaBoat.entity.EntityDoubleLavaBoat;
import LavaBoat.entity.EntityDoubleReinforcedBoat;
import LavaBoat.entity.EntityLavaBoat;
import LavaBoat.entity.EntityPetBoat;
import LavaBoat.entity.EntityReinforcedBoat;
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
@Mod(modid = ModInfo.ID, name = ModInfo.NAME, version = ModInfo.VERSION)
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class ModLavaBoat {

    @Instance("LavaBoat")
    public static ModLavaBoat instance;
    @SidedProxy(clientSide = "LavaBoat.client.ClientProxy", serverSide = "LavaBoat.CommonProxy")
    public static CommonProxy proxy;
    // lava boat
    public static int lavaBoatId;
    public static Item lavaBoat;

    public ModLavaBoat() {
        instance = this;
    }

    @PreInit
    public void preInit(FMLPreInitializationEvent event) {
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());

        config.load();

        lavaBoatId = config.getItem("LavaBoat", 9000 - 256).getInt();

        config.save();
    }

    @Init
    public void load(FMLInitializationEvent event) {
        lavaBoat = new ItemLavaBoat(lavaBoatId);
        GameRegistry.registerItem(lavaBoat, "Lava boat");
        for (byte i = 0; i < ItemLavaBoat.NAMES.length; i++) {
            if (i == 0 || i == 3) { //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                ItemStack boatStack = new ItemStack(lavaBoat, 1, i);
                LanguageRegistry.addName(boatStack, ItemLavaBoat.NAMES[i]);
            }
        }

        // recipe
        GameRegistry.addRecipe(new ItemStack(lavaBoat, 1, 0), "xyx", "xxx", 'x', Item.ingotIron, 'y', Item.boat);
        //GameRegistry.addRecipe(new ItemStack(lavaBoat, 1, 1), "xx", 'x', new ItemStack(lavaBoat, 1, 0));
        GameRegistry.addRecipe(new ItemStack(lavaBoat, 1, 2), "xyx", "xxx", 'x', Block.obsidian, 'y', new ItemStack(lavaBoat, 1, 0));
        //GameRegistry.addRecipe(new ItemStack(lavaBoat, 1, 3), "xx", 'x', new ItemStack(lavaBoat, 1, 2));


        EntityRegistry.registerGlobalEntityID(EntityReinforcedBoat.class, "ReinforcedBoat", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerModEntity(EntityReinforcedBoat.class, "ReinforcedBoat", 0, this, 40, 1, true);
        LanguageRegistry.instance().addStringLocalization("entity.ReinforcedBoat.name", "Reinforced boat");

        EntityRegistry.registerGlobalEntityID(EntityDoubleReinforcedBoat.class, "DoubleReinforcedBoat", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerModEntity(EntityDoubleReinforcedBoat.class, "DoubleReinforcedBoat", 1, this, 40, 1, true);
        LanguageRegistry.instance().addStringLocalization("entity.DoubleReinforcedBoat.name", "Reinforced double boat");

        EntityRegistry.registerGlobalEntityID(EntityLavaBoat.class, "LavaBoat", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerModEntity(EntityLavaBoat.class, "LavaBoat", 3, this, 40, 1, true);
        LanguageRegistry.instance().addStringLocalization("entity.LavaBoat.name", "Lava boat");

        EntityRegistry.registerGlobalEntityID(EntityDoubleLavaBoat.class, "DoubleLavaBoat", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerModEntity(EntityDoubleLavaBoat.class, "DoubleLavaBoat", 4, this, 40, 1, true);
        LanguageRegistry.instance().addStringLocalization("entity.DoubleLavaBoat.name", "Double lava boat");

        EntityRegistry.registerGlobalEntityID(EntityPetBoat.class, "PetBoat", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerModEntity(EntityPetBoat.class, "PetBoat", 6, this, 40, 1, true);
        LanguageRegistry.instance().addStringLocalization("entity.PetBoat.name", "Pet boat");

        proxy.registerRenderers();
    }
}
