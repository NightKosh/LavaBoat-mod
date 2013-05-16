package LavaBoat.renderer;

import LavaBoat.entity.EntityLavaBoat;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBoat;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

/*
 * LavaBoat mod
 *
 * @author NightKosh
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
@SideOnly(Side.CLIENT)
public class RenderLavaBoat extends Render {

    /** instance of ModelBoat for rendering */
    protected ModelBase modelBoat;

    public RenderLavaBoat() {
        this.shadowSize = 0.5F;
        this.modelBoat = new ModelBoat();
    }

    /**
     * The render method used in RenderBoat that renders the boat model.
     */
    public void renderBoat(EntityLavaBoat boat, double par2, double par4, double par6, float par8, float par9) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) par2, (float) par4, (float) par6);
        GL11.glRotatef(180.0F - par8, 0.0F, 1.0F, 0.0F);
        float f2 = (float) boat.getTimeSinceHit() - par9;
        float f3 = (float) boat.getDamageTaken() - par9;

        if (f3 < 0.0F) {
            f3 = 0.0F;
        }

        if (f2 > 0.0F) {
            GL11.glRotatef(MathHelper.sin(f2) * f2 * f3 / 10.0F * (float) boat.getForwardDirection(), 1.0F, 0.0F, 0.0F);
        }

        float f4 = 0.75F;
        GL11.glScalef(f4, f4, f4);
        GL11.glScalef(1.0F / f4, 1.0F / f4, 1.0F / f4);
        
        this.loadTexture("/mods/LavaBoat/textures/LavaBoat.png");
        GL11.glScalef(-1.0F, -1.0F, 1.0F);
        this.modelBoat.render(boat, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity entity, double par2, double par4, double par6, float par8, float par9) {
        this.renderBoat((EntityLavaBoat) entity, par2, par4, par6, par8, par9);
    }
}
