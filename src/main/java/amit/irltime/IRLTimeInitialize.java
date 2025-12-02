package amit.irltime;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

public class IRLTimeInitialize {
    static MinecraftClient client;
    static TextRenderer renderer;
    static int x;
    static int y;

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
            x = screenWidth - textWidth - margin;
            y = margin;

            assert client.player != null;
            Collection<StatusEffectInstance> effects = client.player.getStatusEffects();

            moveText(effects);
            drawContext.drawText(renderer, text, x, y, 0xFFFFFF, true);
        });
    }
    //if there's an effect on the player, time moves down
    private static void moveText(Collection<StatusEffectInstance> effects) {
        boolean hasTopRightEffects = effects.stream().anyMatch(effect ->
                effect.shouldShowIcon() && !isBottomHudEffect(effect)
        );

        if (hasTopRightEffects) {
            int offset = 20;
            x -= offset;
        }
    }
    //some effects don't appear in the top right
    private static boolean isBottomHudEffect(StatusEffectInstance effect) {
        var type = effect.getEffectType();
        return type == StatusEffects.BAD_OMEN
                || type == StatusEffects.TRIAL_OMEN
                || type == StatusEffects.RAID_OMEN
                || type == StatusEffects.WIND_CHARGED
                || type == StatusEffects.WEAVING
                || type == StatusEffects.OOZING;
    }


}
