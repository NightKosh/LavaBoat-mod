package LavaBoat.client;

import LavaBoat.CommonProxy;
import LavaBoat.ModLavaBoat;
import LavaBoat.Resources;
import LavaBoat.entity.EntityDoubleLavaBoat;
import LavaBoat.entity.EntityDoubleReinforcedBoat;
import LavaBoat.entity.EntityLavaBoat;
import LavaBoat.entity.EntityReinforcedBoat;
import LavaBoat.renderer.RenderLavaBoat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

/*
 * LavaBoat mod
 *
 * @author NightKosh
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
public class ClientProxy extends CommonProxy {

    @Override
    public void registerRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(EntityReinforcedBoat.class, new RenderLavaBoat(Minecraft.getMinecraft().getRenderManager(), (byte) 0));
        RenderingRegistry.registerEntityRenderingHandler(EntityDoubleReinforcedBoat.class, new RenderLavaBoat(Minecraft.getMinecraft().getRenderManager(), (byte) 1));
        RenderingRegistry.registerEntityRenderingHandler(EntityLavaBoat.class, new RenderLavaBoat(Minecraft.getMinecraft().getRenderManager(), (byte) 3));
        RenderingRegistry.registerEntityRenderingHandler(EntityDoubleLavaBoat.class, new RenderLavaBoat(Minecraft.getMinecraft().getRenderManager(), (byte) 4));
    }

    public void registerModels() {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(ModLavaBoat.lavaBoat, 0, Resources.reinforcedBoatModel);
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(ModLavaBoat.lavaBoat, 1, Resources.doubleReinforcedBoatModel);
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(ModLavaBoat.lavaBoat, 3, Resources.lavaBoatModel);
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(ModLavaBoat.lavaBoat, 4, Resources.doubleLavaBoatModel);
        ModelBakery.addVariantName(ModLavaBoat.lavaBoat, new String[]{"lavaboat:LBReinforcedBoat", "lavaboat:LBDoubleReinforcedBoat", "lavaboat:LBCargoReinforcedBoat",
                "lavaboat:LBLavaBoat", "lavaboat:LBDoubleLavaBoat", "lavaboat:LBCargoLavaBoat"});
    }
}
