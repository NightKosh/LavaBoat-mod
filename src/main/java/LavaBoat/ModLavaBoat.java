package LavaBoat;

import LavaBoat.entity.EntityDoubleLavaBoat;
import LavaBoat.entity.EntityDoubleReinforcedBoat;
import LavaBoat.entity.EntityLavaBoat;
import LavaBoat.entity.EntityReinforcedBoat;
import LavaBoat.item.ItemLavaBoat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

/*
 * LavaBoat mod
 *
 * @author NightKosh
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
@Mod(modid = ModInfo.ID, name = ModInfo.NAME, version = ModInfo.VERSION)
public class ModLavaBoat {

    @Instance("LavaBoat")
    public static ModLavaBoat instance;
    @SidedProxy(clientSide = "LavaBoat.client.ClientProxy", serverSide = "LavaBoat.CommonProxy")
    public static CommonProxy proxy;
    public static Item lavaBoat;

    public ModLavaBoat() {
        instance = this;
    }

    @Mod.EventHandler
    public void load(FMLInitializationEvent event) {
        lavaBoat = new ItemLavaBoat();
        GameRegistry.registerItem(lavaBoat, "LBBoat");
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(lavaBoat, 0, Resources.reinforcedBoatModel);
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(lavaBoat, 1, Resources.doubleReinforcedBoatModel);
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(lavaBoat, 3, Resources.lavaBoatModel);
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(lavaBoat, 4, Resources.doubleLavaBoatModel);
        ModelBakery.addVariantName(lavaBoat, new String[]{"lavaboat:LBReinforcedBoat", "lavaboat:LBDoubleReinforcedBoat", "lavaboat:LBCargoReinforcedBoat",
                "lavaboat:LBLavaBoat", "lavaboat:LBDoubleLavaBoat", "lavaboat:LBCargoLavaBoat"});

        // recipe
        GameRegistry.addRecipe(new ItemStack(lavaBoat, 1, 0), "xyx", "xxx", 'x', Items.iron_ingot, 'y', Items.boat);
        GameRegistry.addRecipe(new ItemStack(lavaBoat, 1, 1), "xx", 'x', new ItemStack(lavaBoat, 1, 0));
        GameRegistry.addRecipe(new ItemStack(lavaBoat, 1, 3), "xyx", "xxx", 'x', Blocks.obsidian, 'y', new ItemStack(lavaBoat, 1, 0));
        GameRegistry.addRecipe(new ItemStack(lavaBoat, 1, 4), "xx", 'x', new ItemStack(lavaBoat, 1, 2));


        EntityRegistry.registerModEntity(EntityReinforcedBoat.class, "ReinforcedBoat", 0, this, 40, 1, true);

        EntityRegistry.registerModEntity(EntityDoubleReinforcedBoat.class, "DoubleReinforcedBoat", 1, this, 40, 1, true);

        EntityRegistry.registerModEntity(EntityLavaBoat.class, "LavaBoat", 3, this, 40, 1, true);

        EntityRegistry.registerModEntity(EntityDoubleLavaBoat.class, "DoubleLavaBoat", 4, this, 40, 1, true);

//        EntityRegistry.registerModEntity(EntityPetBoat.class, "PetBoat", 6, this, 40, 1, true);

        proxy.registerRenderers();
    }
}
