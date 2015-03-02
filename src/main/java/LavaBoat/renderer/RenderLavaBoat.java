package LavaBoat.renderer;

import LavaBoat.Resources;
import LavaBoat.entity.EntityNKBoat;
import LavaBoat.model.ModelDoubleBoat;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBoat;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

/*
 * LavaBoat mod
 *
 * @author NightKosh
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
@SideOnly(Side.CLIENT)
public class RenderLavaBoat extends Render {

    /**
     * instance of ModelBoat for rendering
     */
    protected ModelBase modelBoat;
    private byte boatType;

    public RenderLavaBoat(RenderManager renderManager, byte boatType) {
        super(renderManager);
        this.shadowSize = 0.5F;
        this.boatType = boatType;
        this.modelBoat = getBoatModel();
    }

    /**
     * The render method used in RenderBoat that renders the boat model.
     */
    public void renderBoat(EntityNKBoat boat, double par2, double par4, double par6, float par8, float par9) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) par2, (float) par4, (float) par6);
        GL11.glRotatef(180 - par8, 0, 1, 0);
        float f2 = boat.getTimeSinceHit() - par9;
        float f3 = boat.getDamageTaken() - par9;

        if (f3 < 0) {
            f3 = 0;
        }

        if (f2 > 0) {
            GL11.glRotatef(MathHelper.sin(f2) * f2 * f3 / 10F * boat.getForwardDirection(), 1, 0, 0);
        }

        float f4 = 0.75F;
        GL11.glScalef(f4, f4, f4);
        GL11.glScalef(1 / f4, 1 / f4, 1 / f4);

        getTexture();
        GL11.glScalef(-1, -1, 1);

        if (boatType == 1 || boatType == 3) {
            GL11.glRotatef(180, 0, 1, 0);
        }
        this.modelBoat.render(boat, 0, 0, -0.1F, 0, 0, 0.0625F);
        GL11.glPopMatrix();
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method,
     * always casting down its argument and then handing it off to a worker
     * function which does the actual work. In all probabilty, the class Render
     * is generic (Render<T extends Entity) and this method has signature public
     * void doRender(T entity, double d, double d1, double d2, float f, float
     * f1). But JAD is pre 1.5 so doesn't do that.
     */
    @Override
    public void doRender(Entity entity, double par2, double par4, double par6, float par8, float par9) {
        this.renderBoat((EntityNKBoat) entity, par2, par4, par6, par8, par9);
    }

    private ModelBase getBoatModel() {
        if (boatType == 0 || boatType == 3) {
            return new ModelBoat();
        } else {
            return new ModelDoubleBoat();
        }
    }

    private void getTexture() {
        bindTexture(getEntityTexture(null));
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        switch (boatType) {
            case 1:
                return Resources.MODEL_DOUBLE_REINFORCED_BOAT;
            case 2:
                return Resources.MODEL_CARGO_REINFORCED_BOAT;
            case 3:
                return Resources.MODEL_LAVA_BOAT;
            case 4:
                return Resources.MODEL_DOUBLE_LAVA_BOAT;
            case 5:
                return Resources.MODEL_CARGO_LAVA_BOAT;
            case 0:
            default:
                return Resources.MODEL_REINFORCES_BOAT;
        }
    }
}
