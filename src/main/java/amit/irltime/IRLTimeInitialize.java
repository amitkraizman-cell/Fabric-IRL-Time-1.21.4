package amit.irltime;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class IRLTimeInitialize {
    static MinecraftClient client;
    static TextRenderer renderer;

    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void registerIRLTimeInitialize(){
        client = MinecraftClient.getInstance();
        renderer = client.textRenderer;
        IRLTime.LOGGER.info("Registering for " + IRLTime.MOD_ID);
        HudRenderCallback.EVENT.register((DrawContext drawContext, RenderTickCounter tickCounter) -> {
            TextRenderer renderer = MinecraftClient.getInstance().textRenderer;

            String text = LocalTime.now().format(TIME_FORMATTER);
            int screenWidth = client.getWindow().getScaledWidth();
            int textWidth = renderer.getWidth(text);
            int margin = 10;
            int x = screenWidth - textWidth - margin;
            int y = margin;

            // color = 0xRRGGBB, last boolean = shadow
            drawContext.drawText(renderer, text, x, y, 0xFFFFFF, true);
        });
    }


}
