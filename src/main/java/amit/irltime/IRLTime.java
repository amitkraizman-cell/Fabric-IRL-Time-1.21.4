package amit.irltime;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IRLTime implements ModInitializer {
	public static final String MOD_ID = "irltime";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);




    @Override
	public void onInitialize() {
        IRLTimeInitialize.registerIRLTimeInitialize();
    }
}