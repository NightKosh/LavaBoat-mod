package LavaBoat.client;

import LavaBoat.CommonProxy;
import LavaBoat.entity.EntityLavaBoat;
import LavaBoat.renderer.RenderLavaBoat;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {

    public void registerRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(EntityLavaBoat.class, new RenderLavaBoat());
    }
}
