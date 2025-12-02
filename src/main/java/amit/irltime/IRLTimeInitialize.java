package amit.irltime;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class IRLTimeInitialize {

    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void registerIRLTimeInitialize(){
        IRLTime.LOGGER.info("Registering for " + IRLTime.MOD_ID);
        HudRenderCallback.EVENT.register((DrawContext drawContext, RenderTickCounter tickCounter) -> {
            TextRenderer renderer = MinecraftClient.getInstance().textRenderer;

            String text = LocalTime.now().format(TIME_FORMATTER);
            int x = 380;
            int y = 10;

            // color = 0xRRGGBB, last boolean = shadow
            drawContext.drawText(renderer, text, x, y, 0xFFFFFF, true);
        });
    }


}
