package LavaBoat.client;

import LavaBoat.CommonProxy;
import LavaBoat.entity.EntityLavaBoat;
import LavaBoat.renderer.RenderLavaBoat;
import cpw.mods.fml.client.registry.RenderingRegistry;

/*
 * LavaBoat mod
 *
 * @author NightKosh
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
public class ClientProxy extends CommonProxy {

    public void registerRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(EntityLavaBoat.class, new RenderLavaBoat());
    }
}
